(ns luminus-clojure-test.core
  (:require [reagent.core :as r]
            [reagent.session :as session]
            [secretary.core :as secretary :include-macros true]
            [goog.events :as events]
            [goog.history.EventType :as HistoryEventType]
            [markdown.core :refer [md->html]]
            [luminus-clojure-test.ajax :refer [load-interceptors!]]
            [ajax.core :refer [GET POST]])
  (:import goog.History))

(defn nav-link [uri title page collapsed?]
  [:li.nav-item
   {:class (when (= page (session/get :page)) "active")}
   [:a.nav-link
    {:href uri
     :on-click #(reset! collapsed? true)} title]])

(defn navbar []
  (let [collapsed? (r/atom true)]
    (fn []
      [:nav.navbar.navbar-light.bg-faded
       [:button.navbar-toggler.hidden-sm-up
        {:on-click #(swap! collapsed? not)} "☰"]
       [:div.collapse.navbar-toggleable-xs
        (when-not @collapsed? {:class "in"})
        [:a.navbar-brand {:href "#/"} "luminus-clojure-test"]
        [:ul.nav.navbar-nav
         [nav-link "#/" "Home" :home collapsed?]
         [nav-link "#/about" "About" :about collapsed?]]]])))

(defn handler [response]
  (.alert js/window (str response))
  (.log js/console (str response)))
(defn error-handler [{:keys [status status-text]}]
   (.log js/console
     (str "something bad happened: " status " " status-text)))

(def state (r/atom "good"))
(defn input-text [label-text]
  [:div
    [:div
      [:label label-text]
      [:input {:type "text"
               :value @state
               :on-change #(reset! state (-> % .-target .-value))}]]
    [:div (str "and change here: " @state)]])

(defn btn-test []
  [:button {:on-click #(GET "/hello" {:handler handler})} "ajax click"])

(defn btn-api
  []
  [:button
    {:on-click #((.log js/console "test api")
                 (GET "/api/times" {:handler handler}))}
    "api times"])

(defn about-page []
  [:div.container
   [:div.row
    [:div.col-md-12
     (input-text "test input: ")
     "this is the story of luminus-clojure-test... work in progress"]
    [:div.col-md-12 (btn-test)]]])

(defn not-found []
  [:div.container [:h1 "404: Page doesn't exist"]])

(defn home-page []
  [:div.container
   [:div.jumbotron
    [:h1 "Welcome to luminus-clojure-test"]
    [:p "Time to start building your site!"]
    [:p [:a.btn.btn-primary.btn-lg {:href "http://luminusweb.net"} "Learn more »"]]]
   [:div.row
    [:div.col-md-12
     [:h2 "Welcome to ClojureScript"]]]
   (when-let [docs (session/get :docs)]
     [:div.row
      [:div.col-md-12
       [:div {:dangerouslySetInnerHTML
              {:__html (md->html docs)}}]]])])

(def pages
  {:home #'home-page
   :error #'not-found
   :about #'about-page})

(defn page []
  [(pages (session/get :page))])

;; -------------------------
;; Routes
(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (session/put! :page :home))

(secretary/defroute "/about" []
  (session/put! :page :about))

(secretary/defroute "*" []
  (session/put! :page :error))

;; -------------------------
;; History
;; must be called after routes have been defined
(defn hook-browser-navigation! []
  (doto (History.)
        (events/listen
          HistoryEventType/NAVIGATE
          (fn [event]
              (secretary/dispatch! (.-token event))))
        (.setEnabled true)))

;; -------------------------
;; Initialize app
(defn fetch-docs! []
  (GET (str js/context "/docs") {:handler #(session/put! :docs %)}))

(defn mount-components []
  (r/render [#'navbar] (.getElementById js/document "navbar"))
  (r/render [#'page] (.getElementById js/document "app")))

(defn init! []
  (load-interceptors!)
  (fetch-docs!)
  (hook-browser-navigation!)
  (mount-components))

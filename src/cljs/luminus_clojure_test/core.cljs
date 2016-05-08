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

(def state (r/atom "good"))
(defn input-text [label-text]
  [:div
    [:label label-text]
    [:input {:type "text"
             :value @state
             :on-change #(reset! state (-> % .-target .-value))}]
    [:label @state]])

(defn btn-test
  []
  [:button {:on-click #(reset! state "bad !")} "click"])

(defn input-field [label-text id]
  (let [value (r/atom nil)]
    (fn []
      [:div
        [:label "The value is: " @value]
        [:input {:type "text"
                 :value @value
                 :on-change #(reset! value (-> % .-target .-value))}]])))

(defn render-simple []
  (r/render-component [input-field]
    (.-body js/document)))

(defn about-page []
  [:div.container
   [:div.row
    [:div.col-md-12
     (input-text "test input")
     (input-field "test input" 3)
     (btn-test)
     "this is the story of luminus-clojure-test... work in progress"]
    [:div#test.col-md-12]]])

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

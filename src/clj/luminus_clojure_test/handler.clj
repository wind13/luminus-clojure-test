(ns luminus-clojure-test.handler
  (:require [compojure.core :refer [routes wrap-routes]]
            [luminus-clojure-test.layout :refer [error-page]]
            [luminus-clojure-test.routes.home :refer [home-routes]]
            [luminus-clojure-test.routes.services :refer [service-routes]]
            [compojure.route :as route]
            [luminus-clojure-test.middleware :as middleware]))

(def app-routes
  (routes
    #'service-routes
    (wrap-routes #'home-routes middleware/wrap-csrf)
    (route/not-found
      (:body
        (error-page {:status 404
                     :title "page not found"})))))

(def app (middleware/wrap-base #'app-routes))

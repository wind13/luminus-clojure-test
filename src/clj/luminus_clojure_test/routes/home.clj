(ns luminus-clojure-test.routes.home
  (:require [luminus-clojure-test.layout :as layout]
            [compojure.core :refer [defroutes GET]]
            [ring.util.http-response :as response]
            [clojure.java.io :as io]))

(defn home-page []
  (layout/render "home.html"))

(defroutes home-routes
  (GET "/" [] (home-page))
  (GET "/hello" [] "Show something")
  (GET "/docs" [] (response/ok (-> "docs/docs.md" io/resource slurp))))

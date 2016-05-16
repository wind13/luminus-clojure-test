(ns luminus-clojure-test.routes.services
  (:require [ring.util.http-response :refer :all]
            [conman.core :as conman]
            [clojure.tools.logging :as log]
            [luminus-clojure-test.db.core :refer [*db*] :as db]
            [compojure.api.sweet :refer :all]
            [schema.core :as s]))

(defapi service-routes
  {:swagger {:ui "/swagger-ui"
             :spec "/swagger.json"
             :data {:info {:version "1.0.0"
                           :title "Sample API"
                           :description "Sample Services"}}}}
  (context "/api" []
    :tags ["thingie"]

    (GET "/plus" []
      :return       Long
      :query-params [x :- Long, {y :- Long 1}]
      :summary      "x+y with query-parameters. y defaults to 1."
      (ok (+ x y)))

    (POST "/minus" []
      :return      Long
      :body-params [x :- Long, y :- Long]
      :summary     "x-y with body-parameters."
      (ok (- x y)))

    (GET "/times/:x/:y" []
      :return      Long
      :path-params [x :- Long, y :- Long]
      :summary     "x*y with path-parameters"
      (ok (* x y)))

    (POST "/divide" []
      :return      Double
      :form-params [x :- Long, y :- Long]
      :summary     "x/y with form-parameters"
      (ok (/ x y)))

    (GET "/power" []
      :return      Long
      :header-params [x :- Long, y :- Long]
      :summary     "x^y with header-parameters"
      (ok (long (Math/pow x y))))

    (GET "/users" []
      :return String
      :summary     "test get user from db."
      (log/info (db/get-user {:id "foo"}))
      (ok (str (db/get-user {:id "foo"}))))

    (GET "/create" []
      :return Boolean
      :summary "test create user."

      (db/create-user!
        {:id "foo"
         :first_name "Sam"
         :last_name "Smith"
         :email "sam.smith@example.com"})

      (ok true))))

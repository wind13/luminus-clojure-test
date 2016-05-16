(ns luminus-clojure-test.db.core
  (:require
    [conman.core :as conman]
    [mount.core :refer [defstate] :as mount]
    [luminus-clojure-test.config :refer [env]]))

(defstate ^:dynamic *db*
          :start (conman/connect!
                   {:datasource
                    (doto (org.h2.jdbcx.JdbcDataSource.)
                          (.setURL (env :database-url))
                          (.setUser "")
                          (.setPassword ""))})
          :stop (conman/disconnect! *db*))

(conman/bind-connection *db* "sql/queries.sql")

;; Start the database
(mount/start
      #'luminus-clojure-test.config/env
      #'luminus-clojure-test.db.core/*db*)

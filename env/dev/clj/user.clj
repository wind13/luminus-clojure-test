(ns user
  (:require [mount.core :as mount]
            luminus-clojure-test.core))

(defn start []
  (mount/start-without #'luminus-clojure-test.core/repl-server))

(defn stop []
  (mount/stop-except #'luminus-clojure-test.core/repl-server))

(defn restart []
  (stop)
  (start))



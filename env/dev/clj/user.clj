(ns user
  (:require [mount.core :as mount]
            [luminus-clojure-test.figwheel :refer [start-fw stop-fw cljs]]
            luminus-clojure-test.core))

(defn start []
  (mount/start-without #'luminus-clojure-test.core/repl-server))

(defn stop []
  (mount/stop-except #'luminus-clojure-test.core/repl-server))

(defn restart []
  (stop)
  (start))



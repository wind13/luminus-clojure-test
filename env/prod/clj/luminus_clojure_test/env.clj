(ns luminus-clojure-test.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[luminus-clojure-test started successfully]=-"))
   :middleware identity})

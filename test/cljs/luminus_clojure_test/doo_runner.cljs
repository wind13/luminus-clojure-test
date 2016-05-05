(ns luminus-clojure-test.doo-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [luminus-clojure-test.core-test]))

(doo-tests 'luminus-clojure-test.core-test)


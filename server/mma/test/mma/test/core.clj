(ns mma.core-test
  (:require [clojure.test :refer :all]
            [mma.core :refer :all]
            )
  (:use [midje.sweet]
        [ring.mock.request]
        ))

(fact "Math Works yay"
      (+ 1 1) => 3
      )

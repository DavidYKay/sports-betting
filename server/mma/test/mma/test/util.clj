(ns mma.test.util
  (:require [clojure.test :refer :all]
            [mma.util :refer :all]
            )
  (:use [midje.sweet]
        [ring.mock.request]
        ))


(fact "Math Works yay"
      (+ 1 1) => 2
      )

(fact "Name stripping"
      (strip-nickname "Anderson \"The Spider\" Silva") => "Anderson Silva"
      (strip-nickname "Cain Velasquez") => "Cain Velasquez"

      (strip-nickname "Junior \"Cigano\" Dos Santos") => "Junior Dos Santos"
      )
(fact "Name Matching"

      (match-names "Junior \"Cigano\" Dos Santos" "Junior Dos Santos") => true
      (match-names "Junior Dos Santos" "Junior Dos Santos") => true
      (match-names "Barf" "Junior Dos Santos") => false

      )

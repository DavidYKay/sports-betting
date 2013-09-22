(ns mma.test.vendor.bovada
  (:require [clojure.test :refer :all]
            [mma.vendor.bovada :refer :all]
            )
  (:use [midje.sweet]
        [ring.mock.request]
        ))

(fact
  ;(get-odds-for-fighter "Junior \"Cigano\" Dos Santos") => [[["Cain Velasquez" -240] ["Junior \"Cigano\" Dos Santos" 190]]]
  ;(get-odds-for-fighter "Junior Dos Santos") => ((["Cain Velasquez" -240] ["Junior \"Cigano\" Dos Santos" 190]))
  )

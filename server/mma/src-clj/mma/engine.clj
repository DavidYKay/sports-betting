(ns mma.engine
  (:use [clojure.string :only [split trim]])
  (:require [mma.vendor.sentiment :as sentiment]
            [clojure.string :as string]
            [clojure.math.numeric-tower :as math])
  )

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; Protocols
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defprotocol Odds
  (a-wins? [x])
  (b-wins? [x]))

(defn- fractional-to-percent [fractional]
  (let [terms (map #(Integer/parseInt %) (split fractional #":"))]
    (/ (last terms)
       (reduce + terms))))

(defn- american-to-percent [american]
  (let [divided (math/abs (/ american 100))]
    (if (pos? american)
      (fractional-to-percent (string/join ":" [divided 1]))
      (fractional-to-percent (string/join ":" [1 divided]))
      )))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; Implementations
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(deftype FractionalOdds [a b]
  Odds
  (a-wins? [x]
    (fractional-to-percent a))
  (b-wins? [x]
    (fractional-to-percent b)))

(deftype AmericanOdds [a b]
  Odds
  (a-wins? [x]
    (american-to-percent a))
  (b-wins? [x]
    (american-to-percent b)))

(deftype PercentOdds [a b]
  Odds
  (a-wins? [x]
    a)
  (b-wins? [x]
    b))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; Code
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn pythagorean [a b]
  (math/sqrt
    (+ (* a a) (* b b))))

(defn sentiment-to-percentage [a b]
  (let [raw-score (/ (math/abs (- b a))
                      (math/sqrt 2))
        max-score (math/sqrt 2)
        percentage (/ raw-score max-score)
        inverse (- 1 percentage)]
      [percentage inverse]))

(defn predict-fight [fight]
  (let [fighters (:fighters fight)
        sentiments (map #(:score (sentiment/get-sentiment (:name %))) fighters)
        [a b] (sentiment-to-percentage (first sentiments) (last sentiments))
        ]
    ; (PercentOdds. 0.60 0.40)
    ;(PercentOdds. a b)
    [a b]
    ))

(defn get-odds [fight]
  (FractionalOdds. "4:1" "1:6")
  ;(AmericanOdds. "" ""
  )

(defn should-i-bet? [fight]
  (let [prediction (predict-fight fight)
        odds (get-odds fight)
        should-bet (if (or (> (a-wins? prediction)
                              (a-wins? odds))
                           (> (b-wins? prediction)
                              (b-wins? odds)))
                     true
                     false)
        ]
    {:should-bet should-bet
     :prediction {:a (a-wins? prediction)
                  :b (b-wins? prediction)}
     :odds {:a (a-wins? odds)
            :b (b-wins? odds)}}))

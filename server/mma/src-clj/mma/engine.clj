(ns mma.engine
  (:use
    [clojure.string :only [split trim]]
    [mma.util :only [strip-nickname]]
        )
  (:require [mma.vendor.sentiment :as sentiment]
            [mma.vendor.bovada :as bovada]
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
  (let [abs-val (math/abs american)]
    (if (pos? american)
      (/ 100 (+ 100 abs-val))
      (/ abs-val (+ 100 abs-val)))))

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

(deftype UnknownOdds []
  Odds
  (a-wins? [x]
    0.5)
  (b-wins? [x]
    0.5))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; Converter
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn web-odds-to-odds [web-odds fight]
  (let [fighters (:fighters fight)
        [[_ a] [_ b]] web-odds]
    (if (nil? web-odds)
      (do
        (println "WTF!!!!!!")
        (UnknownOdds.)
        )
      (do
        (println "converting web odds: " web-odds)
        (AmericanOdds. a b)))))

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
        raw-sentiments (map #(sentiment/get-sentiment (strip-nickname (:name %))) fighters)
        ; TODO: order these guys properly to correspond with A and B
        ]
    (println "checking sentiments: " raw-sentiments)
    (if (contains? (reduce merge raw-sentiments) :error)
      (UnknownOdds.)
      (let [sentiments (map :score raw-sentiments)
            [a b] (sentiment-to-percentage (first sentiments) (last sentiments))]
        (PercentOdds. a b)))))

(defn get-odds [fight]
  (let [web-odds (bovada/get-odds fight)]
    (web-odds-to-odds web-odds fight)))

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

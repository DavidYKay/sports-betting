(ns mma.util
  (:use [clojure.string :only [trim join]])
  (:require [clojure.string :as string])
  )

(defn strip-nickname [n]
  (let [
        ; matches nicknames and bare names
        raw-pieces (re-matches #"(\w+)( \".+\")? (.+)" n)
        pieces (vector (nth raw-pieces 1) (last raw-pieces))
        ]
    (join " " pieces)))

(defn match-names [a b]
  (let [without-nicks (map strip-nickname a b)]
    (cond
      (= a b) true
      )))

(defn remove-whitespace [s]
  (trim (string/replace s #"\s+" " ")))

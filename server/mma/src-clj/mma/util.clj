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
  (let [without-nicks (map strip-nickname [a b])]
    (println "matching name a: "
             (first without-nicks))
    (println "matching name b: "
             (last without-nicks))
    (cond
      (= a b) => true
      :default => false
      )))

(defn remove-whitespace [s]
  (trim (string/replace s #"\s+" " ")))

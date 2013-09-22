(ns mma.util
  (:use [clojure.string :only [trim join]])
  )

(defn strip-nickname [n]
  (let [pieces (map trim (clojure.string/split n #"\""))
        ]
    (join " " (vector (first pieces) (last pieces)))))

(defn match-names [a b]
  (let [without-nicks (map strip-nickname a b)]
    (cond
      (= a b) true
      )))

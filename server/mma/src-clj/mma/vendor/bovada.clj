(ns mma.vendor.bovada
  (:require [net.cgrand.enlive-html :as html]
            [clj-http.client :as client]
            [clojure.string :as string]
            )
  (:use [clojure.string :only [split trim]]
        [mma.vendor.common])
  )


(defn get-all-odds []
  (binding [clj-http.core/*cookie-store* (clj-http.cookies/cookie-store)]
    (let [body (html-string (:body (client/get "http://sports.bovada.lv/sports-betting/mixed-martial-arts.jsp")))
          event-schedule  (html/select body [:#event-schedule])
          raw-names (map trim (map html/text (html/select body [:div.competitor-name-props :span.left])))
          names (filter (fn [n]
                          (if (or (substring? #".*aired.*" n)
                                  (substring? #".*Live.*" n))
                            false
                            true))
                        raw-names)
          odds (map #(Integer/parseInt (html/text %)) (html/select body [:a.lineOdd]))
          ]
      (partition 2 (map vector names odds))
      )))

(defn get-odds [fight]

)

(defn get-odds-for-fighter [n]
  (let [all-odds (get-all-odds)
        fighter-matches? (fn [fighter] (let [fighter-name (first fighter)]
                                         (if (= fighter-name n)
                                           true
                                           false)))
        filtered-odds (filter (fn [fight]
                                (let [fighter-a (fighter-matches? (first fight))
                                      fighter-b (fighter-matches? (last fight))
                                      result (filter #(not (nil? %)) [fighter-a fighter-b])
                                      final-result (first (remove false? result))
                                      ]
                                  final-result))
                              all-odds)
        delta (- (count all-odds) (count filtered-odds))]
    filtered-odds
    ))

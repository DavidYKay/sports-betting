(ns mma.vendor.bovada
  (:require [net.cgrand.enlive-html :as html]
            [clj-http.client :as client]
            [clojure.string :as string]
            )
  (:use [clojure.string :only [split trim]]
        [mma.vendor.common])
  )

(defn get-odds [fight]
  (binding [clj-http.core/*cookie-store* (clj-http.cookies/cookie-store)]
    (let [body (html-string (:body (client/get "http://sports.bovada.lv/sports-betting/mixed-martial-arts.jsp")))
          event-schedule  (html/select body [:#event-schedule])
          raw-names (map html/text (html/select body [:div.competitor-name-props :span.left]))
          names (filter (fn [n]
                          (if (or (substring? #".*aired.*" n)
                                  (substring? #".*Live.*" n))
                            false
                            true))
                        raw-names)
          odds (map html/text (html/select body [:a.lineOdd]))
          ]
      (partition 2 (map vector names odds))
      )))

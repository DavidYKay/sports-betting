(ns mma.vendor.bovada
  (:require [net.cgrand.enlive-html :as html]
            [clj-http.client :as client]
            [clojure.string :as string]
            )
  (:use [clojure.string :only [split trim]]
        [mma.vendor.common])
  )

(defn get-odds [fight]
  (let [body (fetch-url "http://sports.bovada.lv/sports-betting/mixed-martial-arts.jsp")
        stat-texts  (html/select body [:#event-schedule])
        ]
    (binding [clj-http.core/*cookie-store* (clj-http.cookies/cookie-store)]
      (client/get "http://sports.bovada.lv/sports-betting/mixed-martial-arts.jsp")
      ;(client/get "http://sports.bovada.lv/sports-betting/mixed-martial-arts.jsp")
      ;stat-texts
      )))



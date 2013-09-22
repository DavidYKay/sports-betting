(ns mma.vendor.sentiment
  (:require [net.cgrand.enlive-html :as html]
            [clj-http.client :as client]
            [clojure.string :as string]
            )
  (:use [clojure.string :only [split trim]]
        [mma.vendor.common]
        [clojure.data.json :only [read-str]]
        [ring.util.codec :only [url-encode]]
        )
  )

(defn get-sentiment [fighter-name]
  (do
    (println "getting sentiment for fighter: " fighter-name)
    (let [response (client/get (str "http://1.crumbsbot.appspot.com/tweetsentiments?q=" (url-encode fighter-name)))
          ]
      (if (and (< (:status response) 300)
               (not (string/blank? (:body response))))
        (let [json (clojure.walk/keywordize-keys (read-str (:body response)))
              cleaned (conj json {:score (read-string (:score json))})]
          cleaned)
        {:error "Failed to fetch sentiment"
         :status  (:status response)}
        ))))

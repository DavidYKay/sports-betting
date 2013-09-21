(ns mma.vendor.ufc
  (:require [net.cgrand.enlive-html :as html]
            [clj-http.client :as client])
  (:use [clojure.string :only [trim]])
  )

(def DEFAULT-FIGHT-URL "http://www.ufc.com/event/UFC165")

(defn fetch-url [url]
  (html/html-resource (java.net.URL. url)))

(defn to-text [nodes]
  (map (fn [x] (trim (html/text x)))
       nodes))

(defn get-names [doc]
  (let [
        first-names (map (fn [node]
                           (trim (last (:content node))))
                         (html/select doc [:div.fighter-first-name]))
        last-names (map (fn [node]
                           (trim (last (:content node))))
                         (html/select doc [:div.fighter-last-name]))
        nicknames (map (fn [node]
                           (trim (last (:content node))))
                         (html/select doc [:div.fighter-nickname]))
        ]
    (map (fn [a b c]
           {:nickname a
            :first-name b
            :last-name c})
           nicknames
         first-names
         last-names)
    ;last-names
    ))

(defn get-fight [url]
  ;(let [body (fetch-url "http://www.ufc.com/event/UFC165")
  (let [body (fetch-url url)
        stat-texts  (html/select body [:div#fighter-stats :.stat-section :div.stat-text])
        stat-titles (html/select body [:div#fighter-stats :.stat-section :div.stat-title])
        stats (zipmap
                (map html/text stat-titles)
                (partition 2 (map html/text stat-texts)))
        names (get-names body)
        ]
      {:stats stats
       :names names}))

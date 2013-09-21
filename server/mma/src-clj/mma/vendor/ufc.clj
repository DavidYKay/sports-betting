(ns mma.vendor.ufc
  (:require [net.cgrand.enlive-html :as html]
            [clj-http.client :as client]
            [clojure.string :as string]
            )
  (:use [clojure.string :only [split trim]])
  )

(def DEFAULT-FIGHT-URL "http://www.ufc.com/event/UFC165")
(def DEFAULT-FIGHTER-URL "http://www.ufc.com/fighter/Jon-Jones")

(defn fetch-url [url]
  (html/html-resource (java.net.URL. url)))

(defn to-text [nodes]
  (map (fn [x] (trim (html/text x)))
       nodes))

(defn get-fighter [url]
  (let [body (fetch-url url)
        skills  (map trim (split (html/text (first (html/select body [:#fighter-skill-summary]))) #","))
        record  (split (trim (html/text (first (html/select body [:#fighter-skill-record]))) ) #"-")

        weight (Integer/parseInt (nth (split (html/text (first (html/select body [:#fighter-weight]))) #" ") 3))
        height (Integer/parseInt (last (re-matches #".+ (\d+) cm .*" (html/text (first (html/select body [:#fighter-height]))))))
        age (Integer/parseInt (first (split (html/text (first (html/select body [:#fighter-age]))) #" ")))
        hometown (trim (string/replace (html/text (first (html/select body [:#fighter-from]))) #"\s+" " "))
        lives-in (trim (string/replace (html/text (first (html/select body [:#fighter-lives-in]))) #"\s+" " "))

        final-result {:skills skills
                      :record record
                      :weight weight
                      :height height
                      :age age
                      :hometown hometown
                      :lives-in lives-in
                      }
        ]
    ;weight
    ;height
    ;lives-in
    final-result
    ))

(defn get-names [doc]
  (let [select-and-trim (fn [selector] (map (fn [node]
                                              (trim (last (:content node))))
                                            (html/select doc selector)))
        first-names (select-and-trim [:div.fighter-first-name])
        last-names (select-and-trim [:div.fighter-last-name])
        nicknames (select-and-trim [:div.fighter-nickname])]
    (map (fn [a b c]
           {:nickname a
            :first-name b
            :last-name c})
         nicknames
         first-names
         last-names)))

(defn get-fight [url]
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

(ns mma.vendor.ufc
  (:require [net.cgrand.enlive-html :as html]
            [clj-http.client :as client]
            [clojure.string :as string]
            )
  (:use [mma.vendor.common]
        [mma.util :only [remove-whitespace]]
        [clojure.string :only [split trim lower-case]])
  )

(def DEFAULT-FIGHT-URL "http://www.ufc.com/event/UFC165")
(def DEFAULT-FIGHTER-URL "http://www.ufc.com/fighter/Jon-Jones")

(defn get-fighter [url]
  (let [body (fetch-url url)
        skills  (map trim (split (html/text (first (html/select body [:#fighter-skill-summary]))) #","))
        record  ((fn [r]
                   (let [[w l t] r] {:win w
                                     :loss l
                                     :tie t}))
                        (split (trim (html/text (first (html/select body [:#fighter-skill-record]))) ) #"-"))
        weight (Integer/parseInt (nth (split (html/text (first (html/select body [:#fighter-weight]))) #" ") 3))
        height (Integer/parseInt (last (re-matches #".+ (\d+) cm .*" (html/text (first (html/select body [:#fighter-height]))))))
        age (Integer/parseInt (first (split (html/text (first (html/select body [:#fighter-age]))) #" ")))
        hometown (remove-whitespace (html/text (first (html/select body [:#fighter-from]))))
        lives-in (remove-whitespace (html/text (first (html/select body [:#fighter-lives-in]))))

        outcomes (map (fn [res]
                      (let [outcome (second (:content res))]
                        (lower-case (or (:class (:attrs outcome))
                                        (first (:content outcome))))))
                    (html/select body [:.result]))

        match-methods (map (fn [v]
                        (let [[round method modifier] v
                              base {:round round
                                    :method method}
                              ]
                          (if (nil? modifier)
                            base
                            (conj base {:modifier modifier }))))
                        (map (fn [node]
                             (let [finishing-s (remove-whitespace (html/text node))

                                   pieces (string/split (string/replace finishing-s #"-" "") #" ")]
                               (remove string/blank? pieces)
                               ))
                           (html/select body [:.fighter1 :.method])))

        match-opponents (let [raw-fighters (map #(remove-whitespace (html/text %))
                                           (html/select body [:.fighter]))
                             most-common (first (last (sort-by val (frequencies raw-fighters))))
                             fighters (remove #(= most-common %) raw-fighters)]
                         fighters)

        opponent-links (map #(:href (:attrs %)) (html/select body [:.fighter :a]))

        match-dates (map #(remove-whitespace (nth (:content %) 2))
                        (html/select body [:.fighter1 :.event]))

        final-result {:skills skills
                      :record record
                      :weight weight
                      :height height
                      :age age
                      :hometown hometown
                      :lives-in lives-in
                      :matches (map (fn [opponent opponent-link date outcome method]
                                      {:opponent        opponent
                                       :opponent-link   opponent-link
                                       :date            date
                                       :outcome         outcome
                                       :method          method})
                                      match-opponents
                                      opponent-links
                                      match-dates
                                      outcomes
                                      match-methods)
                      }]
    final-result
    ;opponent-links
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
        names (get-names body)]
      {:stats stats
       :names names}))

(defn get-fighter-opponents
  "Fetch details on all the fighters that this fighter has fought"
  [fighter]


  )

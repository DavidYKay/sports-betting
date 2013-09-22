(ns mma.crawler
  (:require [mma.vendor.ufc :as ufc]
            [mma.db :as db])
  )

(defn get-fighter [fighter-name]
  (let [db-fighter (db/get-fighter fighter-name)]
   (if db-fighter
     db-fighter
     (let [ufc-fighter (ufc/get-fighter fighter-name)]
       (db/save-fighter! ufc-fighter)
       ufc-fighter))))

(defn crawl
  "crawl the UFC site beginning at a fighter"
  [start-url levels fighters]
  (if (= levels 0)
    fighters
    (let [fighter (get-fighter start-url)
          matches (:matches fighter)
          opponents (map :opponent matches)
          ]
      (map (fn [opponent]
             (crawl opponent (dec levels) (conj fighters fighter)))
           opponents)
      )))

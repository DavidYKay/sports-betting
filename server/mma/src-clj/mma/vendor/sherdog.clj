(ns mma.vendor.sherdog
  (:require [net.cgrand.enlive-html :as html]
            [clj-http.client :as client]
            [clojure.string :as string]
            )
  (:use [clojure.string :only [split trim]]
        [mma.vendor.common])
  )

(def DEFAULT-FIGHT-URL "http://www.sherdog.com/events/UFC-165-Jones-vs-Gustafsson-30249")
(def DEFAULT-FIGHTER-URL "http://www.sherdog.com/fighter/Jon-Jones-27944")

(defn get-fight [fight]
  (let [body (fetch-url DEFAULT-FIGHT-URL)
        ;event-schedule (html/select body [:div.footer :div.resume :tbody :tr :td])
        ;values (map :content (html/select body [:tbody :tr :td]))
        ;headings (map html/text (html/select body [:tbody :tr :td]))
        ;headings (map html/text (html/select body [:table.resume :tr :td]))
        ;headings (map html/text (html/select body [:table.resume :tr :td]))
        values (html/select body [:td])
        ]
    ;body
    values
    ;headings
    ))

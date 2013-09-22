(ns mma.db
  (:require [monger.core :as mg]
            [monger.collection :as mc])
  (:use [monger.conversion :only [from-db-object]])
  (:import [com.mongodb MongoOptions ServerAddress]
           [org.bson.types ObjectId]))

(defn connect! []
  (mg/connect!)
  (mg/set-db! (mg/get-db "ufc"))
  )

(defn disconnect! []
  (mg/disconnect!)
  )


(defn run-mongo []
  (mg/connect!)

  ;; localhost, default port
  (mg/set-db! (mg/get-db "monger-test"))

  ;; with explicit document id (recommended)

  ;; multiple documents at once
  (mc/insert-batch "documents" [{:first_name "John" :last_name "Lennon"}
                               {:first_name "Paul" :last_name "McCartney"}])

  (println "Documents: " (mc/find-maps "documents"))

  (mg/disconnect!)
  )


(defn save-fighter! [fighter]
  (mc/insert "fighters" fighter))

(defn get-fighter [fighter-name]
  (mc/find-one-as-map "fighters"
                      { :name fighter-name }))

(defn has-fighter? [fighter-name]
  (not (nil? (get-fighter fighter-name))))

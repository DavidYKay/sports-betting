(ns mma.vendor.common
  (:require [net.cgrand.enlive-html :as html])
  (:use [clojure.string :only [trim]])
)

(defn fetch-url [url]
  (html/html-resource (java.net.URL. url)))

(defn to-text [nodes]
  (map (fn [x] (trim (html/text x)))
       nodes))

(ns mma.core
  (:use [compojure.core]
         [ring.adapter.jetty :only [run-jetty]]
         [hiccup.page :only [include-css include-js html5]]
         )
  (:require [compojure.handler :as handler]
            [ring.server.standalone :as ring-server]
            )
  )

(defroutes app-routes
  (GET "/" [] "Hello World")

  (GET "/fight-detail" []
       (html5
         [:h1 "WELCOME TO DIE"]
         [:img {:src "http://www.fightersgeneration.com/np6/char/magneto-mvc3.jpg"}]
         [:ul
          (for [x (range 20)]
            [:li x])]))
  )

(def app
  (handler/site app-routes))

(defn run-app []
  "Runs the web server"
  (run-jetty app {:port 3000}))

(defn run-app-dev []
  "Run server in REPL - Dev mode ONLY"
  (do
    (use 'ring.server.standalone)
    (ring-server/serve app)))

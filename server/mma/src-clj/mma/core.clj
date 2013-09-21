(ns mma.core
  (:use [compojure.core]
         [ring.adapter.jetty :only [run-jetty]]
         )
  (:require [compojure.handler :as handler]
            [ring.server.standalone :as ring-server]
            )
  )

(defroutes app-routes
  (GET "/" [] "Hello World")
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

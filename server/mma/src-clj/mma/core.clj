(ns mma.core
  (:use [compojure.core]
         [ring.adapter.jetty :only [run-jetty]]
         [hiccup.page :only [include-css include-js html5]]
         )
  (:require [compojure.handler :as handler]
            [ring.server.standalone :as ring-server]
            [compojure.route :as route]
            [mma.engine :as engine]
            [mma.env :as env]
            [clj-style.core :as cs]
            )
  )

(cs/defrule  div-foo
  [:div#foo
   :margin "0px"
   [:span.bar
    :color "red"
    :font-weight "bold"
    [:a:hover
     :text-decoration "none"]]])

div-foo

(defroutes app-routes
  (GET "/" [] "Hello World")

  (GET "/fight-detail" []
       (html5
         [:head
          [:title "Fight Detail Page"]
          (include-css (str "/css/bootstrap.css"))
          (include-js (str "/js/bootstrap.js"))
          ]
         [:h1 "Choose your fighter!"]
         [:img {:src "http://www.fightersgeneration.com/np6/char/magneto-mvc3.jpg" :width 400 :height 600 }]
         [:as "VS"]
         [:img {:src "http://static.giantbomb.com/uploads/original/3/34651/1814401-juggernaut.png" :width 400 :height 600 }]


         [:button "Test"]
         [:ul
          (for [x (range 10)]
            [:li x])]))

  (GET "/fight-details" {{fighter-a :fighter-a
                          fighter-b :fighter-b} :params}
       (let [fight {:fighters [{:name fighter-a} {:name fighter-b} ]}
             should-i-bet (engine/should-i-bet? fight)
             ]
         (println "Bet reccos: " should-i-bet)
         (html5
           [:h1 (str fighter-a " vs " fighter-b)]

           [:h3 "Fight Recommendations:"]

           [:p (str should-i-bet)]
           )))

  (route/resources "/")
  )

(def app
  (do
    (env/set-proxy)
    (handler/site app-routes)
    ))

(defn run-app []
  "Runs the web server"
  (run-jetty app {:port 3000}))

(defn run-app-dev []
  "Run server in REPL - Dev mode ONLY"
  (do
    (use 'ring.server.standalone)
    (ring-server/serve app)))

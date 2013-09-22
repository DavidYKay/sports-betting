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
            [mma.db :as db]
            [mma.crawler :as crawler]
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
       "hello world")

  (GET "/fight-details" {{fighter-a-name :fighter-a
                          fighter-b-name :fighter-b} :params}
       (let [fighters [(crawler/get-fighter fighter-a-name)
                       (crawler/get-fighter fighter-b-name)]
             fight {:fighters fighters}
             should-i-bet (engine/should-i-bet? fight)
             ]
         (println "Bet reccos: " should-i-bet)
         (println "Picture a: " (:picture (first fighters)))
         (println "Picture b: " (:picture (last fighters)))
         (html5
           [:head
            [:title "Fight Detail Page"]
            (include-css (str "/css/bootstrap.css"))
            (include-js (str "/js/bootstrap.js"))
            ]
           [:div {:class "row"}
            [:div {:class "span6"}

             [:h1 fighter-a-name]
             ;[:img {:src "http://www.fightersgeneration.com/np6/char/magneto-mvc3.jpg" :width 400 :height 600 }]
             [:img {:src (:picture (first fighters)) :width 400 :height 600 }]

             [:h3 (str "Current Betting odds: " (:a (:odds should-i-bet)))]
             [:h3 (str "Estimated Win Percentage: " (:a (:prediction should-i-bet)))]
             ]
            [:div {:class "span6"}
             [:h1 fighter-b-name]
             ;[:img {:src "http://static.giantbomb.com/uploads/original/3/34651/1814401-juggernaut.png" :width 400 :height 600 }]
             [:img {:src (:picture (last fighters)) :width 400 :height 600 }]
             [:h3 (str "Current Betting odds: " (:b (:odds should-i-bet)))]
             [:h3 (str "Estimated Win Percentage: " (:b (:prediction should-i-bet)))]
             ]
            ]
           [:button "Test"]
           [:ul
            (for [x (range 10)]
              [:li x])])))

  (route/resources "/")
  )

(def app
  (do
    (env/set-proxy)
    (db/connect!)
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

(defproject mma "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]

                 [compojure "1.1.5"]
                 [ring/ring-jetty-adapter "1.1.0"]

                 ; mainly for dev
                 [ring-server "0.2.8"]

                 [webfui "0.2.1"]
                 [clj-http "0.6.3"]
                 [org.clojure/data.json "0.2.1"]
                 [hiccup "1.0.1"]
                 ]
  :source-paths ["src-clj"]
  :min-lein-version "2.0.0"
  :plugins [[lein-ring "0.8.5"]
            [lein-cljsbuild "0.2.10"]
            [lein-midje "3.1.2"]
            [lein-beanstalk "0.2.7"]
            ]
  :ring {:handler mma.handler/app}
  :profiles {:dev {:dependencies [[ring-mock "0.1.5"]
                                  [midje "1.6-beta1"]
                                  ;[com.stuartsierra/lazytest "1.2.3"]
                                  [lein-midje-lazytest "0.1.0"]
                                  ]}})

(ns mma.test.engine
  (:require [clojure.test :refer :all]
            )
  (:import [mma.engine FractionalOdds AmericanOdds PercentOdds])
  (:use [midje.sweet]
        [ring.mock.request]
        [mma.engine]
        ))

(fact "Fractional odds compute correctly."
      (let [odds (FractionalOdds. "4:1" "1:6") ]
        (a-wins? odds) => 1/5
        (b-wins? odds) => 6/7
        ))

(fact "Percent odds compute correctly."
      (let [odds (PercentOdds. 0.40 0.60) ]
        (a-wins? odds) => 0.40
        (b-wins? odds) => 0.60
        ))

(fact "American odds compute correctly."
      (let [odds (AmericanOdds. -400 800)]
        (a-wins? odds) => 4/5
        (b-wins? odds) => 1/9
        )

      (let [odds (AmericanOdds. -200 600)]
        (a-wins? odds) => 2/3
        (b-wins? odds) => 1/7
        )
      )

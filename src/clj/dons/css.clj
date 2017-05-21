(ns dons.css
  (:require [garden.def :refer [defstyles]]))

(def width "1600px")
(def height "1050px")

(defstyles screen
  [:html {
          :background "url(../img/background.png) repeat"
          }]

  [:.game-container
   {
    :width width
    :height height
    :color "#fff"
    }]

  [:.game {
           :height "100%" :min-height "100%"
           :width "100%" :min-width "100%"
           }]

  [:.header {:height "33%" :min-height "33%"}]
  [:.main {:height "33%" :min-height "33%"}]
  [:.player-area {
                  :height "33%" :min-height "33%"
                  :vertical-align "bottom"
                  :padding-top "115px"}]

  [:.card
   {
    :height "85px" :min-height "85px"
    :width "64px" :min-width "64px"

    }
   [:.title {:position "relative"
             :font-size "small"
             :top "-85px"
             :left "3px"
             :color "white"}]
   ]

  [:.stats
   {:font-size "xx-large"}
   ]

  [:.player-influence
   {:font-size "xx-large"}
   ]
  
  )

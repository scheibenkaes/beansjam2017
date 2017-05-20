(ns dons.css
  (:require [garden.def :refer [defstyles]]))

(defstyles screen
  [:body {}]

  [:.game-container
   {
    :width "1024px"
    :height "768px"
    :background "url(../img/background.png)"
    :color "#fff"
    }]

  [:.game {
           :height "100%" :min-height "100%"
           :width "100%" :min-width "100%"
           }]

  [:.header {:height "33%" :min-height "33%"}]
  [:.main {:height "33%" :min-height "33%"}]
  [:.player-area {:height "33%" :min-height "33%"
                  :vertical-align "bottom"
                  :padding-top "115px"}]
  )

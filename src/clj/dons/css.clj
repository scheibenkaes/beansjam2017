(ns dons.css
  (:require [garden.def :refer [defstyles]]))

(def width "1600px")
(def height "1050px")

(defstyles screen
  [:html {
          :background "url(../img/background.png) repeat"
          :width "100%"
          :height "100%"
          }]

  [:.end-screen
   {:font-size "xx-large"}

   [:.result
    {:color "#fff"}]
   
   [:h1
    {:color "#fff"}
    

    ]]
  
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

  [:.header {:height "33%" :min-height "33%"}
   [:h2 {:font-size "xx-large"}]

   [:.opponent
    {:font-size "xx-large"}]
   ]
  [:.main {:height "33%" :min-height "33%"}]
  [:.player-area {
                  :height "33%" :min-height "33%"
                  :vertical-align "bottom"
                  :padding-top "115px"}]

  [:&.card
   {
;;    :height "85px" :min-height "85px"
    :width "128px" :min-width "128px"
    :background-color "#323c39"
    :border "none"
    :box-shadow ""
    :display "inline-block"
    }
   [:.title {:position "absolute"
             :font-size "small"
             :top "9px"
             :left "9px"
             :font-weight "bold"
             :color "black"}]

   [:.cost {:position "absolute"
            
            :font-size "small"
            :top "7px"
            :left "108px"
            :font-weight "bold"
            :color "black"}]
   ]

  [:.stats
   {:font-size "xx-large"}
   ]

  [:.player-influence
   {:font-size "xx-large"}
   ]
  
  )

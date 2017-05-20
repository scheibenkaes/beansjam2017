(ns dons.subs
    (:require-macros [reagent.ratom :refer [reaction]])
    (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
 :player-don
 (fn [db]
   (:player/don db)))

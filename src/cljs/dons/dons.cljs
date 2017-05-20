(ns dons.dons
  (:require [re-frame.core :as re-frame]))

(defn don
  ""
  [& {:keys [id img]}]
  {:id id
   :img img})

(def don-pedro
  (don :id :don/pedro :img "img/don-pedro.png"))


(defn Don
  [{img :img}]
  [:div.don
   [:img {:src img}]])

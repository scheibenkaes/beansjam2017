(ns dons.db
  (:require [dons.dons :as dons]))

(defn initial-game-state
  ""
  []
  {})


(def default-db
  {:player/don dons/don-pedro
   :game/stats {:money 0 :influence 0}
   :game/game (initial-game-state)})

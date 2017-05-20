(ns dons.db
  (:require [dons.dons :as dons]
            [dons.logic :as logic]))

(def default-db
  {:player/don dons/don-pedro
   :game/game logic/initial-state})

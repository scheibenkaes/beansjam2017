(ns dons.logic)

(def initial-stats
  {:influence 0
   :money     0})


(def initial-state
  {:player-hand        []
   :opponent-hand      []
   :player-influence   0
   :opponent-influence 0
   :stats              initial-stats
   :blackmarket        []})


(defn ->card [& {:keys [id img title effect text planet? cost]
                 :or   {img     "img/card.png" title "No Title" effect identity text ""
                        planet? false          cost  1}}]
  {:id      id
   :cost    cost
   :card?   true
   :planet? planet?
   :img     img
   :title   title
   :text    text
   :effect  effect})

;;; CARDS

(def noob
  (->card :id :card/noob
          :title "Space Noob"
          :effect (fn [state]
                    (update-in state [:stats :influence] (partial + 1)))))

(def collector
  (->card :id :card/collector
          :title "Eintreiber"
          :effect (fn [state]
                    (update-in state [:stats :money] (partial + 1)))))

(def concealer
  (->card :id :card/concealer
          :title "Space Hehler"
          :effect (fn [state]
                    (update-in state [:stats :money] (partial + 2)))))

(def goons
  (->card :id :card/goons
          :title "Schlägertypen"
          :effect (fn [state]
                    (update-in state [:stats :influence] (partial + 2)))))

#_((:effect goons) initial-state)

;;; PLANETS

(def jackson
  (->card :id :planet/jackson
          :planet? true
          :cost 5
          :titel "Jackson"))

(def mars
  (->card :id :planet/mars
          :planet? true
          :cost 6
          :titel "Mars"))

(def all-cards
  #{noob collector concealer goons jackson mars})

(def all-planets
  (filter :planet? all-cards))

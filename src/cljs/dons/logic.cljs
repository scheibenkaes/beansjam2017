(ns dons.logic)


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


;;; events

(def initial-stats
  {:influence 0
   :money     0})


(def initial-state
  {:player-hand        {}
   :opponent-hand      []
   :game-state         :state/pre-game
   :player-deck        []
   :opponent-deck      []
   :cards-being-played []
   :player-discard     []
   :opponent-discard   []
   :turn               :player
   :player-influence   0
   :opponent-influence 0
   :stats              initial-stats
   :blackmarket        []})

(def num-cards-at-begin 5)

(defn initial-deck []
  (concat
   (repeat 7 collector)
   (repeat 3 noob)))

(def num-blackmarket 5)

(defn initial-blackmarket-deck
  ""
  []
  (concat
   (repeat 1 jackson)
   (repeat 1 mars)
   (repeat 10 goons)
   (repeat 10 collector)
   (repeat 10 concealer)
   (repeat 10 noob)))


(defn to-indexed-map [coll]
  (into (sorted-map)
        (map-indexed (fn [idx c] [idx c]) coll)))

(defmulti game-event (fn [[e _] state] e))

(defmethod game-event :event/setup
  [_ state]
  (let [[player-hand player-deck]     (split-at num-cards-at-begin (shuffle (initial-deck)))
        [opponent-hand opponent-deck] (split-at num-cards-at-begin (shuffle (initial-deck)))]
    (assoc state
           :game-state :state/started
           :player-deck player-deck
           :opponent-deck opponent-deck
           :player-hand (to-indexed-map player-hand)
           :opponent-hand (to-indexed-map opponent-hand))))

(defmethod game-event :event/play-card
  [[_ {idx :idx card :card}] {:keys [player-hand
                                     cards-being-played] :as state}]
  (let [trigger-effect (fn [state]
                         ((:effect card) state))]
    (-> state
        (update-in [:cards-being-played] conj card)
        (update-in [:player-hand] dissoc idx)
        trigger-effect)))


(def hand-size 5)

(defn has-enough-cards [deck]
  (>= (count deck) hand-size))

(defn cards-needed-after-shuffle [deck ])

;; * Einfluss wird gut geschrieben
;;
;; * Geld verfällt
;;
;; * Gespielte Karten auf den Ablagestapel
;;
;; * Sind weniger als 5 Karten im Nachziehstapel?
;; Ja: Gespielte + Abgelegte bilden neues Deck
;;     Es werden 5 - x Karten vom Deck gezogen
;; Nein: 5 Ziehen
;; 
;; * Der andere ist dran
;; 
(defmethod game-event :event/turn-done
  [[_ who] {:keys [player-hand
                   player-deck
                   player-discard
                   
                   opponent-hand
                   opponent-deck
                   opponent-discard

                   stats
                   cards-being-played]
            :as state}]
  (println "done" who)
  (let [was-player? (= who :player)
        deck        (if was-player? player-deck opponent-deck)
        discard     (if was-player? player-discard opponent-discard)

        discard-after-play (concat discard cards-being-played)
        influence-gained   (:influence stats)
        remaining-in-deck  (count deck)]
    (assoc state
           :cards-being-played []
           :stats initial-stats
           :opponent-hand (if was-player? opponent-hand [])
           :turn (if was-player? :opponent :player)))
 )


(defmethod game-event :default [_ state] state)


(ns dons.cards)


(defn ->card [& {:keys [id img title effect]
                 :or {img "img/card.png" title "No Title" effect identity}}]
  {:id id
   :card? true
   :img img
   :title title
   :effect effect})


(defn Card [{:keys [title img id]}]
  (let []
    (fn []
      [:div.card {:class (str id)}
       [:figure.image [:img {:src img}]]
       [:span.title title]])))

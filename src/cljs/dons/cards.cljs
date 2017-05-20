(ns dons.cards)


(defn ->card [& {:keys [id img title]
                 :or {img "img/card.png" title "No Title"}}]
  {:id id
   :card? true
   :img img
   :title title})


(defn Card [{:keys [title img id]}]
  (let []
    (fn []
      [:div.card {:class (str id)}
       [:figure.image [:img {:src img}]]
       [:span.title title]])))

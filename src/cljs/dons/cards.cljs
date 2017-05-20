(ns dons.cards)

(defn Card [{:keys [title img id]}]
  (fn []
    [:div.card {:class (str id)}
     [:figure.image [:img {:src img}]]
     [:span.title title]]))

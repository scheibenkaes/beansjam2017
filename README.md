# Spielidee

`Dons` ist ein Weltraum-Mafia-Deck-Building-Kartenspiel.
Ziel des Spiel ist es den eigenen Clan zur Vorherrschaft zu verhelfen, indem man seinen eignen Einfluss ausreichend vergrößert.

Da es im Weltraum recht demokratisch zugeht reicht, reicht hierfür die relative Mehrheit von 50 oder mehr Einflusspunkten.

# Regeln

Am Anfang des Spiels besteht das Deck jedes Spielers aus 10 einfachen Karten, welchen man über den Lauf des Spiels neue hinzufügt, in dem man neue Karten kauft.

Es wird abwechselnd gespielt, der Spieler beginnt. Anschließend ist die KI *hust* an der Reihe.
Zu Beginn jeder Runde hat der Spieler 5 Karten auf der Hand, die er alle ausspielt.
Jede Karte hat dabei einen eigenen Effekt.
Sie wirkt sich entweder auf das Geld ($), den gewonnenen Einfluss (💪), oder beides aus.
"Space Hehler" zB geben einem 2$ für diese Runde, "Schlägertypen" 2💪.


Nachdem der Spieler seine Karten gespielt hat, kann er neue Karten vom Schwarzmarkt erwerben.
Diese Neuerwerbungen wandern in den Ablagestapel und werden später wieder unter das Deck gemischt.
Man kann beliebig viele Karten kaufen, solange man genug Geld in der Runde zur Verfügung hat.
Die Kosten für eine Karte erscheinen oben rechts.
Nach Abschluss der Runde werden automatisch wieder 5 neue Karten nachgezogen.

Nicht ausgegebene $ in dieser Runde verfallen, 💪 werden zum Abschluss der Runde dem Gesamtstand hinzugefügt.

## Karten

Es gibt normale Karten und Planeten-Karten.
Letzte zeichnen sich dadurch aus bessere Werte zu haben.
Allerdings gibt es jeden Planeten nur einmal zu erwerben.

# Was fehlt?

Aufgrund der spontanen Teilnahme war leider keine Zeit das Spiel tatsächlich fertig zu stellen.

- Alte Karten aus dem Deck entfernen
- Weitere Karteneffekte wie Nachziehen oder Angriffe auf den Gegner, ...
- etwas das man als Grafik oder GUI bezeichnen könnte. 


## 

A [re-frame](https://github.com/Day8/re-frame) application designed to ... well, that part is up to you.

## Development Mode

### Start Cider from Emacs:

Put this in your Emacs config file:

```
(setq cider-cljs-lein-repl "(do (use 'figwheel-sidecar.repl-api) (start-figwheel!) (cljs-repl))")
```

Navigate to a clojurescript file and start a figwheel REPL with `cider-jack-in-clojurescript` or (`C-c M-J`)

### Compile css:

Compile css file once.

```
lein garden once
```

Automatically recompile css file on change.

```
lein garden auto
```

### Run application:

```
lein clean
lein figwheel dev
```

Figwheel will automatically push cljs changes to the browser.

Wait a bit, then browse to [http://localhost:3449](http://localhost:3449).

## Production Build


To compile clojurescript to javascript:

```
lein clean
lein cljsbuild once min
```

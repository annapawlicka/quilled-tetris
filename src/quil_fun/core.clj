(ns quil-fun.core
  (:use quil.core))

(def xmax 30)
(def ymax 200)
(def piece-width 5)

(def step 10)

(def y-state (atom [{:x 0 :y 0}]))

(defn add-piece []
  (swap! y-state conj {:x (* (rand-int (/ xmax piece-width)) piece-width) :y 0}))

(defn update-state [state]
  (let [[fst & rst] state
        update-fst (update-in fst [:y] + step)]
    (cons update-fst rst)))

(defn collide [piece-1 piece-2]
  (and (= (:x piece-1) (:x piece-2))
       (= (+ 10 (:y piece-1)) (:y piece-2))))

(defn check-collision []
  (let [moving-piece (first @y-state)
        others (rest @y-state)]
    (some #(collide moving-piece %) others)))

(defn bottom? []
  (let [moving-piece (first @y-state)]
    (= ymax (:y moving-piece))))

(defn update-pieces []
  (swap! y-state update-state)
  (when (or (bottom?)
            (check-collision))
    (add-piece)))

(defn setup []
  (reset! y-state [])
  (add-piece)
  (smooth)                          ;;Turn on anti-aliasing
  (frame-rate 10)                    ;;Set framerate to 1 FPS
  (background 200))                 ;;Set the background colour to
                                    ;;  a nice shade of grey.

(defn draw []
  (update-pieces)
  (background 230)
  (stroke 5)             ;;Set the stroke colour to a random grey
  (fill 30)               ;;Set the fill colour to a random grey


  (doseq [piece @y-state]
    (rect (:x piece) (:y piece) 5 10))) ;;Draw a circle at x y with the correct diameter

(defsketch example                  ;;Define a new sketch named example
  :title "Oh so many grey circles"  ;;Set the title of the sketch
  :setup setup                      ;;Specify the setup fn
  :draw draw                        ;;Specify the draw fn
  :size [xmax ymax])                  ;;You struggle to beat the golden ratio

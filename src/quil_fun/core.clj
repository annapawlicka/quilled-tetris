(ns quil-fun.core
  (:use quil.core))

(def xmax 323)
(def ymax 200)

(def step 10)

(def y-state (atom [{:x 0 :y 0}]))

(defn add-piece []
  (swap! y-state conj {:x (rand-int xmax) :y 0}))

(defn update-state [state]
  (let [[fst & rst] state
        update-fst (update-in fst [:y] + step)]
    (cons update-fst rst)))


(defn update-pieces []
  (swap! y-state update-state)
  (when (zero? (mod (frame-count) (/ ymax step)))
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
    (rect (:x piece) (:y piece) 4 10))) ;;Draw a circle at x y with the correct diameter

(defsketch example                  ;;Define a new sketch named example
  :title "Oh so many grey circles"  ;;Set the title of the sketch
  :setup setup                      ;;Specify the setup fn
  :draw draw                        ;;Specify the draw fn
  :size [xmax ymax])                  ;;You struggle to beat the golden ratio

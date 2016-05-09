(ns vitamin-d.core
  (:require [quil.core :refer :all]
            [quil.middleware :as m])
  (:gen-class))

(def slots-tick 40)
(def mood-tick 60)
(def success-tick 60)
(def time-tick 60)
(def btn-dimension {:w 180 :h 50})
(def btn-out-coord {:x 100 :y 450})
(def btn-in-coord  {:x 320 :y 450})
(def stats-icons   {:mood "sun.png"
                    :success "money.png"})
(def slot-images   {:0 "beers.png"
                    :1 "computer.png"
                    :2 "fries.png"
                    :3 "book.png"})
(def initial-state
  {:countdown 10
   :stats
     {:mood 50
      :success 50}
   :slots [0 0 0]
   :running false})

(defn reset [state]
  initial-state)

(defn check-if-alive [state]
  (if (or
        (<= (:success (:stats state)) 0)
        (<= (:mood (:stats state)) 0)
        (<= (:countdown state) 0))
    (reset state)
    state))

(defn increase-stats [state stat]
  (update-in state [:stats stat] inc))

(defn is-in-button []
  (if (and
        (> (mouse-x) (:x btn-out-coord))
        (< (mouse-x) (+ (:x btn-out-coord) (:w btn-dimension)))
        (> (mouse-y) (:y btn-out-coord))
        (< (mouse-y) (+ (:y btn-out-coord) (:h btn-dimension))))
    :mood
    (if (and
          (> (mouse-x) (:x btn-in-coord))
          (< (mouse-x) (+ (:x btn-in-coord) (:w btn-dimension)))
          (> (mouse-y) (:y btn-in-coord))
          (< (mouse-y) (+ (:y btn-in-coord) (:h btn-dimension))))
      :success
      false)))

(defn handle-mouse [state]
  (let [button-name (is-in-button)
        slots (:slots state)]
    (if (= (nth slots 0) (nth slots 1) (nth slots 2))
      (if (and (mouse-pressed?) button-name)
        (increase-stats state button-name)
        state)
      state)))

(defn check-for-tick [num]
  (= (mod (frame-count) num) 0))

(defn update-countdown [state]
   (if (check-for-tick time-tick)
     (update-in state [:countdown] dec)
     state))

(defn update-mood [state]
   (if (check-for-tick mood-tick)
     (update-in state [:stats :mood] dec)
     state))

(defn update-success [state]
   (if (check-for-tick success-tick)
     (update-in state [:stats :success] dec)
     state))

(defn generate-slot []
  (rand-int 4))

(defn update-slots [state]
  (if (check-for-tick slots-tick)
    (assoc state :slots [(generate-slot) (generate-slot) (generate-slot)])
    state))

(defn setup []
  (text-font (create-font "Quicksand-Bold" 30))
  initial-state)

(defn load-slot-image [i state]
  (let [num (nth (:slots state) i)]
    (load-image ((keyword (str num)) slot-images))))

(defn draw-stats-counter [state description key x y]
 (text description x (- y 20))
  ; divided by 10 because only 1 icon per every 10 stats points
 (doseq [i (range (/ (key (:stats state)) 10))]
   (image (load-image (key stats-icons)) (+ x (* 25 i)) y 20 18)))

(defn draw-ui []
  (no-stroke)
  ; count down bar
  (fill 100 200 200)
  (rect 100 68 400 20)
  ; buttons
  (rect (:x btn-out-coord) (:y btn-out-coord) (:w btn-dimension) (:h btn-dimension))
  (rect (:x btn-in-coord) (:y btn-in-coord) (:w btn-dimension) (:h btn-dimension))
  ; switch to black for all the text
  (fill 0))

(defn draw-stats [state]
  (text-size 30)
  (text (str "Time left: " (:countdown state) "s") 200 80)
  (text-size 16)
  (draw-stats-counter state "MOOD" :mood 100 180)
  (draw-stats-counter state "SUCCESS" :success 380 180))

(defn draw-slots [state]
  (doseq [i (range 3)]
    (image (load-slot-image i state) (+ 100 (* 150 i)) 260 100 100)))

(defn draw-buttons []
  (text-size 28)
  ; this can probably be refactor
  (if (and
           (mouse-pressed?)
           (= (is-in-button) :mood))
    (fill 255)
    (fill 0))
  (text "Go outside" 110 485)
  (if (and
           (mouse-pressed?)
           (= (is-in-button) :success))
    (fill 255)
    (fill 0))
  (text "Stay inside" 330 485))

(defn draw-start-screen []
  (image (load-image "start.png") 0 0))

(defn draw [state]
  (background 255)
  (if (:running state)
    (do
      (draw-ui)
      (draw-stats state)
      (draw-slots state)
      (draw-buttons))
    (do
      (draw-start-screen))))

(defn update-running [state]
  (if (and
        (= (mod (frame-count) 100) 0)
        (= (:running state) false))
    (assoc state :running true)
    state))

(defn update [state]
  (-> state
    (update-running)
    (check-if-alive)
    (update-slots)
    (update-mood)
    (update-success)
    (update-countdown)
    (handle-mouse)))

(defsketch example
  :title "Vitamin D"
  :setup setup
  :draw draw
  :update update
  :size [600 600]
  :middleware [m/fun-mode])

(defn -main [& args])

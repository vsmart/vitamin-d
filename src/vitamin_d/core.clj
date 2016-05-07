(ns vitamin-d.core
  (:require [quil.core :refer :all]
            [quil.middleware :as m]))

(def slots-tick 80)
(def mood-tick 60)
(def success-tick 60)
(def time-tick 60)
(def btn-dimension {:w 150 :h 50})
(def btn-out-coord {:x 150 :y 400})
(def btn-in-coord {:x 350 :y 400})
(def initial-state
  {:countdown 100
   :stats
     {:mood 50
      :success 50}
   :slots [0 0 0]})

(defn reset [state]
  (delay-frame 3000)
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
  initial-state)

(defn load-slot-image [i state]
  (let [num (nth (:slots state) i)]
  (cond
    (= num 0) (load-image "white.png")
    (= num 1) (load-image "blue.png")
    (= num 2) (load-image "green.png")
    :else (load-image "brown.png"))))

(defn draw-slots [state]
  (doseq [i (range 3)]
    (image (load-slot-image i state) (+ 200 (* 50 i)) 250 40 40)))

(defn draw [state]
  (background 255)
  (fill 100 40 80)
  (text (str "Time left: " (:countdown state)) 250 50)
  (text (str "Mood: " (:mood (:stats state))) 200 100)
  (text (str "Success: " (:success (:stats state))) 300 100)
  ;(text (str (:slots state)) 250 200)
  (draw-slots state)
  (rect (:x btn-out-coord) (:y btn-out-coord) (:w btn-dimension) (:h btn-dimension))
  (rect (:x btn-in-coord) (:y btn-in-coord) (:w btn-dimension) (:h btn-dimension))
  (fill 250)
  (text "Go outside" 160 420)
  (text "Stay inside" 360 420))

(defn update [state]
  (-> state
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

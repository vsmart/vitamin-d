(ns vitamin-d.core
  (:require [quil.core :refer :all]
            [quil.middleware :as m]))

(def slots-tick 80)
(def mood-tick 60)
(def success-tick 60)
(def time-tick 60)

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
  (rand-int 5))

(defn update-slots [state]
  (if (check-for-tick slots-tick) 
    (assoc state :slots [(generate-slot) (generate-slot) (generate-slot)])
    state))
    
(defn setup []
  {:countdown 100
   :stats 
     {:mood 50 
      :success 50}
   :slots [0 0 0]})

(defn draw [state]
  (background 255)
  (fill 100 40 80)
  (text (str "Time left: " (:countdown state)) 250 50)
  (text (str "Mood: " (:mood (:stats state))) 200 100)
  (text (str "Success: " (:success (:stats state))) 300 100)
  (text (str (:slots state)), 250 200)
  (rect 150 400 150 50)
  (rect 350 400 150 50)
  (fill 250)
  (text "Go outside" 160 420)
  (text "Stay inside" 360 420))

(defn update [state]
  (-> state
    (update-slots)
    (update-mood)
    (update-success)
    (update-countdown)))
  
  
(defsketch example
  :title "Vitamin D"
  :setup setup
  :draw draw
  :update update
  :size [600 600]
  :middleware [m/fun-mode])

(defn -main [& args])

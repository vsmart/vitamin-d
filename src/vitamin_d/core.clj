(ns vitamin-d.core
  (:require [quil.core :refer :all]
            [quil.middleware :as m]))


(defn generate-slot []
  (rand-int 5))

(defn update-slots [state]
  (if (= (mod (frame-count) 80) 0) 
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
  (update-slots state))
  
  


(defsketch example
  :title "Vitamin D"
  :setup setup
  :draw draw
  :update update
  :size [600 600]
  :middleware [m/fun-mode])

(defn -main [& args])

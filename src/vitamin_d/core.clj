(ns vitamin-d.core
  (:require [quil.core :refer :all]
            [quil.middleware :as m]))

(defn setup []
  {:countdown 100
   :stats 
     {:mood 50 
      :success 50}
   :slots [0 0 0]})

(defn draw [state]
  (background 255)
  (fill 192)
  (ellipse 100 100 30 30)
  (text (str "Time left: " (:countdown state)) 250 50)
  (text (str "Mood: " (:mood (:stats state))) 200 100)
  (text (str "Success: " (:success (:stats state))) 300 100))

(defn update [state]
  state)

(defsketch example
  :title "Vitamin D"
  :setup setup
  :draw draw
  :update update
  :size [600 600]
  :middleware [m/fun-mode])

(defn -main [& args])

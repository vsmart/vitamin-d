(defproject vitamin-d "0.0.1-SNAPSHOT"
  :description "Decide where you spend your time"
  :url "https://github.com/vsmart/vitamin-d/"
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [quil "2.2.6" :exclusions [org.clojure/clojure]]]
  :main vitamin-d.core
  :aot  [vitamin-d.core])

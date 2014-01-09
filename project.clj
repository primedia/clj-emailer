(defproject emailer "0.1.0"
  :description "Send an email message"
  :url "https://github.com/primedia/clj-emailer"
  :license {:name "MIT License (MIT)"
            :url "http://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure   "1.5.1"]
                 [org.clojure/tools.cli "0.2.4"]
                 [com.rentpath/dotenv   "1.0.2"]
                 [com.rentpath/environs "1.0.1"]
                 [me.raynes/fs          "1.4.0"]
                 [com.draines/postal    "1.11.1"]]
  :main com.rentpath.emailer.core
  :aot [com.rentpath.emailer.core]
  ;; :repositories {"project" "file:repo"})
  :repositories [["iws_pair" "http://ec2-54-224-24-95.compute-1.amazonaws.com/maven/.m2"]])

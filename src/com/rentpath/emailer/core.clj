(ns ^{:author "Jack Morrill"
      :doc "Send email"}
  com.rentpath.emailer.core
  (:require [ clojure.java.io :as io ]
            [ clojure.string  :as string ]
            [ clojure.tools.cli :refer [parse-opts] ]
            [ com.rentpath.environs.core :as environs :refer :all]
            [ postal.core :as postal :only (send-message) ]
            [ com.rentpath.dotenv.core :as dotenv :refer :all ])
  (:gen-class :main true))


(defn parse-opts
  [["-f" "--from FROM" "From:"]

   ["-t" "--to TO" "To:"]

   ["-c" "--cc CC" "CC:"]

   ["-s" "--subject SUBJECT" "Subject:"]
   
   ["-u" "--username USER" "SMTP username"]
   
   ["-p" "--password PASSWORD" "SMTP password"]

   ["-u" "--user USER" "SMTP username"]

   ["-P" "--port PORT" "SMTP Port number"
    :default 465
    :parse-fn #(Integer/parseInt %)
    :validate [#(< 0 % 0x10000) "Must be a number between 0 and 65536"]]

   ["-H" "--smtphost HOST" "SMTP host"
    :default "smtp.gmail.com"]
   
   ["-h" "--help"]])

(defn usage [options-summary]
  (->> ["Stand-alone emailer program"
        ""
        "Usage: program-name [options] action"
        ""
        "Options:"
        options-summary
        ""
        "Actions:"
        "  start    Start a new server"
        "  stop     Stop an existing server"
        "  status   Print a server's status"
        ""
        "Please refer to the manual page for more information."]
       (string/join \newline)))

(defn send!
  "Send an email via SMTP"
  [& args]
  (postal/send-message
     ^{:host     (Env "EMAILER_SMTP_HOST")
       :user     (Env "EMAILER_SMTP_USER")
       :pass     (Env "EMAILER_SMTP_PASS")
       :ssl      :yes }
      {:from     (Env "EMAILER_FROM")
       :to       (Env "EMAILER_TO")
       :cc       (Env "EMAILER_CC"         :allow-nil true)
       :subject  (Env "EMAILER_SUBJECT"    :allow-nil true)
       :body     (Env "EMAILER_BODY"       :allow-nil true)}))

(defn -main
  "Stand-alone emailer"
  [& args]
  (dotenv!)
  (parse-opts args cli-options)
  (send!))

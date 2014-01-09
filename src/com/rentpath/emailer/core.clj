(ns ^{:author "Jack Morrill"
      :doc "Send email"}
  com.rentpath.emailer.core
  (:require [clojure.java.io :as io]
            [clojure.string  :as string]
            [clojure.tools.cli :refer [cli]]
            [com.rentpath.environs.core :as environs :refer :all]
            [postal.core :as postal :only (send-message)]
            [com.rentpath.dotenv.core :as dotenv :refer :all])
  (:gen-class :main true))

(dotenv!)

(defn- parse-args
  [args]
  (cli args
    ["-a" "--attachment FILE" "Attachment file"]
    ["-b" "--body BODY" "Message body"]
    ["-c" "--cc CC" "CC:"]
    ["-f" "--from FROM" "From:"]
    ["-H" "--smtphost HOST" "SMTP host"
      :default "smtp.gmail.com"]
    ["-p" "--password PASSWORD" "SMTP password"]
    ["-P" "--port PORT" "SMTP Port number"
      :default 465
      :parse-fn #(Integer/parseInt %)
      :validate [#(< 0 % 0x10000) "Must be a number between 0 and 65536"]]
    ["-s" "--subject SUBJECT" "Subject:"]
    ["-t" "--to TO" "To:"]
    ["-u" "--username USER" "SMTP username"]
    ["-?" "--help" "Show this help" :default false :flag true]))

(def mail-args
  { :host     (Env "EMAILER_SMTP_HOST")
    :user     (Env "EMAILER_SMTP_USER")
    :pass     (Env "EMAILER_SMTP_PASS")
    :ssl      :yes
    :from     (Env "EMAILER_FROM")
    :to       (Env "EMAILER_TO")
    :subject  (Env "EMAILER_SUBJECT" :allow-nil true)
    :body     [{:type "text/plain"
                :content (str (Env "EMAILER_BODY" :allow-nil true))}
               {:type :attachment
                :content (java.io.File. (Env "EMAILER_ATTACHMENT" :allow-nil true))}]})

;;(defn send!
;;  "Send an email via SMTP"
;;  [& args]
;;  (postal/send-message args))

(defn send!
  "Send an email via SMTP"
  [& args]
  (postal/send-message
    ^{:host     (Env "EMAILER_SMTP_HOST")
      :user     (Env "EMAILER_SMTP_USER")
      :pass     (Env "EMAILER_SMTP_PASS")
      :ssl      (Env "EMAILER_SMTP_SSL")}
     {:from     (Env "EMAILER_FROM")
      :to       (Env "EMAILER_TO")
      :subject  (Env "EMAILER_SUBJECT"    :allow-nil true)
      :body     [{:type "text/plain"
                  :content (str (Env "EMAILER_BODY" :allow-nil true))}
                 {:type :attachment
                  :content (java.io.File. (Env "EMAILER_ATTACHMENT" :allow-nil true))}]}))
(defn -main
  "Stand-alone emailer
   Usage: [env ENVIRONMENT=environment] java -jar emailer-0.1.0-standalone.jar [options]"
  [& args]
  (let [[options args banner] (parse-args args) {:keys [cc body from password port smtphost subject to username]} options]
    (parse-args args)
    (if (:help options)
      (println banner)
      (send! mail-args))))

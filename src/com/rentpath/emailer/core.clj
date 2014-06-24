(ns ^{:author "Jack Morrill"
      :doc "Send email"}
  com.rentpath.emailer.core
  (:require [clojure.java.io :as  io]
            [clojure.string  :as str]
            [clojure.tools.cli :refer [cli]]
            [postal.core :refer [send-message]]
            [com.rentpath.environs.core :refer [env]]
            [com.rentpath.dotenv.core :refer [dotenv!]])
  (:gen-class :main true))


(defn- parse-args
  [args]
  (cli args
    ["-a" "--attachment" "Attachment file"]
    ["-b" "--body" "Message body"]
    ["-c" "--cc" "CC:"]
    ["-f" "--from" "From:"]
    ["-H" "--smtphost" "SMTP host"
      :default "smtp.gmail.com"]
    ["-p" "--password" "SMTP password"]
    ["-P" "--port" "SMTP Port number"
      :default 465
      :parse-fn #(Integer/parseInt %)
      :validate [#(< 0 % 0x10000) "Must be a number between 0 and 65536"]]
    ["-s" "--subject" "Subject:"]
    ["-S" "--ssl" "If present, use SSL" :default false :flag true]
    ["-t" "--to" "To:"]
    ["-u" "--username" "SMTP username"]
    ["-?" "--help" "Show this help" :default false :flag true]))


(defn -main
  "Stand-alone emailer
   Usage: [env ENVIRONMENT=environment] java -jar emailer-0.1.0-standalone.jar [options]"
  [& args]
  (dotenv!)
  (let [[options args banner] (parse-args args)
        {:keys [subject body
                from to cc
                username password
                smtphost port ssl]} options]
    (if (:help options)
      (println banner)
      (send-message ^{:host (or smtphost (env "EMAILER_SMTP_HOST"))
                      :user (or username (env "EMAILER_SMTP_USER"))
                      :pass (or password (env "EMAILER_SMTP_PASS"))
                      :ssl  (or ssl      (env "EMAILER_SMTP_SSL"))}

                    {:from  (or from     (env "EMAILER_FROM"))
                     :to    (or to       (env "EMAILER_TO"))
                     :body  (or body [{:type "text/plain"
                                       :content (or (env "EMAILER_BODY" :allow-nil true) "")}
                                      {:type :attachment
                                       :content (io/file (env "EMAILER_ATTACHMENT" :allow-nil true))}])
                     :subject (or subject (env "EMAILER_SUBJECT"))}))))


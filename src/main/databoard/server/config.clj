(ns databoard.server.config
  (:require
   [com.fulcrologic.fulcro.server.config :refer [load-config!]]
   [mount.core :as mount]
   [taoensso.timbre :as log]))

(defn configure-logging! [config]
  (let [{:keys [taoensso.timbre/logging-config]} config]
    (log/info "Configuring Timbre with" logging-config)
    (log/merge-config! logging-config)))

(mount/defstate config
  :start
  (let [{:keys [config]} (mount/args)
        configuration (load-config! {:defaults-path "databoard/server/config/defaults.edn"
                                     :config-path config})]
    (log/info "Loaded config" config)
    (configure-logging! configuration)
    configuration))

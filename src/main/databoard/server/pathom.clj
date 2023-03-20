(ns databoard.server.pathom
  (:require
   [com.wsscode.pathom3.connect.indexes :as pci]
   [com.wsscode.pathom3.connect.operation :as pco]
   [com.wsscode.pathom3.interface.eql :as p.eql]
   [mount.core :refer [defstate]])
  (:import
   (java.util Date)))

(pco/defresolver server-time-resolver [_env {:as params}]
  {::pco/output [:server/time]}
  (println "current-system-time resolver, params:" params)
  {:server/time (Date.)})
#_(server-time-resolver {} {})

(def operations
  [server-time-resolver])

(def base-env
  (-> {}
      (pci/register operations)))

(defstate parser
  :start
  (let [process (p.eql/boundary-interface base-env)]
    (fn [env tx]
      (let [env' (-> env)
            response (process env' tx)]
        response))))

#_(comment
   (parser {} [:server/time]))

(ns databoard.server.pathom
  (:require
   [com.wsscode.pathom3.connect.indexes :as pci]
   [com.wsscode.pathom3.connect.operation :as pco]
   [com.wsscode.pathom3.interface.eql :as p.eql]
   [databoard.server.translations.locale-resolver :as locale-resolver]
   [mount.core :refer [defstate]])
  (:import
   (java.util Date)))

(pco/defresolver server-time-resolver [_env {:as params}]
  {::pco/output [:server/time]}
  (println "current-system-time resolver, params:" params)
  {:server/time (Date.)})
#_(server-time-resolver {} {})

(def operations
  [server-time-resolver
   locale-resolver/resolvers])

(def base-env
  (-> {:com.wsscode.pathom3.error/lenient-mode? false}
      (pci/register operations)))

(defstate parser
  :start
  (let [process (p.eql/boundary-interface base-env)]
    (fn [env tx]
      (let [env' (-> env)
            response (process env' tx)]
        response))))

#_(comment
   (parser {} [:server/time])
   (parser {} '[({:com.fulcrologic.fulcro-i18n.i18n/translations
                  [:com.fulcrologic.fulcro-i18n.i18n/locale
                   :com.fulcrologic.fulcro-i18n.i18n/translations]}
                 {:locale :pl})]))

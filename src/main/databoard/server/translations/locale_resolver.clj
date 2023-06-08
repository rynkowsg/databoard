(ns databoard.server.translations.locale-resolver
  (:require
   [com.fulcrologic.fulcro-i18n.i18n :as i18n]
   [com.wsscode.pathom3.connect.operation :as pco]
   [com.wsscode.pathom3.connect.planner :as pcp]))

;; todo: change filename, maybe remove the dir translations/, or use the smae ns or package as cljs
(pco/defresolver locale-resolver [env _input]
  {::pco/output [::i18n/translations]}
  (let [locale (:locale (pco/params env))]
    (Thread/sleep 2500)
    (i18n/load-locale "po-files" locale)
    (when-let [translations (i18n/load-locale "po-files" locale)]
      {::i18n/translations translations})))

(def resolvers [locale-resolver])

#_(comment
   (i18n/load-locale "po-files" :pl)
   (locale-resolver {::pcp/node {::pcp/params {:locale :en}}} {})
   (locale-resolver {::pcp/node {::pcp/params {:locale :pl}}} {})
   (locale-resolver {::pcp/node {::pcp/params {:locale :es_ES}}} {})

   (require '[databoard.server.pathom :refer [parser]])
   (parser {} '[({::i18n/translations [::i18n/locale ::i18n/translations]}
                 {:locale :en})]))

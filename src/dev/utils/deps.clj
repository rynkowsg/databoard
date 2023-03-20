(ns utils.deps
  (:require
   [clojure.edn :as edn]
   [clojure.java.io :as io]
   [clojure.tools.deps.alpha.repl :as tools-deps]))

(defn refresh-deps
  []
  (let [deps-edn    (-> "deps.edn"
                        (io/file)
                        (slurp)
                        (edn/read-string))
        common-deps (:deps deps-edn)
        dev-deps    (-> deps-edn :aliases :dev :extra-deps)
        deps        (merge common-deps dev-deps)]
    (tools-deps/add-libs deps)
    deps))
#_(refresh-deps)

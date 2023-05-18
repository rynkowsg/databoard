(ns user
  (:require
   [clojure.spec.alpha :as s]
   [clojure.tools.namespace.repl :as tools-ns]
   [expound.alpha :as expound]
   [shadow.cljs.devtools.api :as shadow]))

;; Ensure we only refresh the source we care about. This is important
;; because `resources` is on our classpath and we don't want to
;; accidentally pull source from there when cljs builds cache files there.
(tools-ns/set-refresh-dirs
  "development/src"
  "bases/databoard-old/src"
  "bases/databoard-old/test")

(alter-var-root #'s/*explain-out* (constantly expound/printer))

;; NOTE: To start working with server: Require development.clj, and use start there.
;; This leads to faster and more reliable REPL startup
;; in cases where your app is busted.

;; If using IntelliJ, Use actions to "Add new REPL command", and add this (dropping the comment around it),
;; then add a keyboard shortcut to it. Then you can start your server quickly once the REPL is going:

(comment
 (require 'development)
 (in-ns 'development)
 (restart))

;; Alternatively you can use (init) function below.
;; Either type it in the REPL or use it as a shortcut to REPL command.
;; On execution, it will either:
;; - load development namespace and re-start the server (when run in server REPL), or
;; - load CLJS REPL (when run in shadow-cljs REPL).

(defn start-client-repl []
  (shadow/repl :main))

(defn init-server-dev []
  (println "Initiated loading dev...")                      ;; refresh takes a bit and one might get impression nothing happen
  (require '[development])
  (in-ns 'development)
  ((requiring-resolve 'development/restart))
  :loaded)

(defn init []
  (let [shadow? (or (contains? (System/getenv) "SHADOW_CLI_PID") ;; set automatically when run from npm
                    (contains? (System/getenv) "SHADOW_CLJS"))] ;; has to be set when any other way
    (if shadow? (start-client-repl) (init-server-dev))))

#_(init)

;; Similarly, you can add lines below to REPL shortcut,
;; making it a command shortcut compatible with both CLJ & CLJS REPL.

(comment
 (require 'user)
 (in-ns 'user)
 (init))

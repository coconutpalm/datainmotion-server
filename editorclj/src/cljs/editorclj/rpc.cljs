(ns editorclj.rpc
  (:require-macros
    [javelin.core :refer [defc defc=]])
  (:require
   [javelin.core]
   [castra.core :refer [mkremote]]))

(defc state nil)
(defc error nil)
(defc loading [])

(defc= random-number  (get state :random))
(defc= session-number (get state :session))

(def get-state
  (mkremote 'editorclj.api/get-state state error loading))

(defn init []
  (get-state)
  (js/setInterval get-state 1000))

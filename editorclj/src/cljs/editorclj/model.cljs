(ns editorclj.model
  (:require-macros
    [javelin.core :refer [defc defc=]])
  (:require
   [javelin.core :refer [cell]]
   [castra.core :refer [mkremote]]))


;; Init
(set! cljs.core/*print-fn* #(.log js/console %))


;; Session state
(def codemirror (atom nil))

(defc state {})
(defc error nil)
(defc= error-message (when error (.-message error)))
(defc loading [])
(def clear-error! #(reset! error nil))


;; RPC status
(defc= loaded? (not= {} state))
(defc= loading? (seq loading))


;; Domain objects
(defc= workspace-path      (:current-workspace state))
(defc= workspace           (get (:workspaces state) workspace-path))
(defc= all-workspaces      (sort (map (fn [dir] (last (.split dir "/"))) (keys (:workspaces state)))))
(defc= project-tree        (:project-tree workspace))
(defc= file-path           (:file-path workspace))
(defc= filename            (:filename workspace))
(defc= saved-file-contents (:saved-file-contents workspace))


;; Controllers
(defn editor-doc []
  (when-let [editor @codemirror]
    (.-doc editor)))


(defc= initial-editor-state
  (let [file (:saved-file-contents workspace)]
    (when-let [doc (editor-doc)]
      (let [editor-file (.getValue doc)]
        (when (empty? editor-file)
          (.setValue doc file)
          (.markClean doc))))
    file))


(defn set-editor! [editor]
  (reset! codemirror editor)
  (.setValue (editor-doc) @saved-file-contents))


;; RPC API
(def save-editor! (mkremote 'editorclj.api/save-file! state error loading))
(def get-state    (mkremote 'editorclj.api/get-state state error loading))


;; Autosave
(defn save-if-dirty! []
  (when-let [doc (editor-doc)]
    (when-not (.isClean doc)
      (save-editor! @file-path @filename (.getValue doc))
      (.markClean doc)
      (println (str "Saved " @file-path "/" @filename)))))


(defn init []
  (get-state)
  (js/setInterval save-if-dirty! 1000))

(ns editorclj.model
  (:require-macros
    [javelin.core :refer [defc defc=]])
  (:require
   [clojure.pprint :refer [pprint]]
   [javelin.core :refer [cell]]
   [castra.core :refer [mkremote]]
   [hoplon.core :refer [link script]]
   [editorclj.codemirror :as editor]
   [editorclj.html :as html]))


(defc content-handlers {"text/x-gfm"           {:name "Markdown"
                                                :new identity
                                                :open identity
                                                :rename identity
                                                :save identity}
                        "application/edn"      {:name "Clojure EDN"
                                                :new identity
                                                :open identity
                                                :rename identity
                                                :save identity}
                        "text/x-clojure"       {:name "Clojure"
                                                :new identity
                                                :open identity
                                                :rename identity
                                                :save identity}
                        "text/x-clojurescript" {:name "ClojureScript"
                                                :new identity
                                                :open identity
                                                :rename identity
                                                :save identity}
                        "text/url"             {:name "Embedded IFrame"
                                                :new identity
                                                :open identity
                                                :rename identity
                                                :save identity}})


(defc workspaces [{:name "Home"
                   :system true
                   :contents [{:name "welcome.dm"
                               :id "welcome.dm"        ; For clj/cljs, this is the full package/name; for files, the path/name
                               :local-path "welcome.dm"
                               :remote-path nil
                               :mime-type "text/x-gfm"}
                              {:name "editor-settings.dm"
                               :id "editor-settings.dm" ; For clj/cljs, this is the full package/name; for files, the path/name
                               :local-path "editor-settings.dm"
                               :remote-path nil
                               :mime-type "text/x-gfm"}
                              {:name "workspaces.dm"
                               :id "workspaces.dm" ; For clj/cljs, this is the full package/name; for files, the path/name
                               :local-path "workspaces.dm"
                               :remote-path nil
                               :mime-type "text/x-gfm"}
                              {:name "debug.edn"
                               :id "debug.edn" ; For clj/cljs, this is the full package/name; for files, the path/name
                               :local-path "debug.edn"
                               :remote-path nil
                               :mime-type "application/edn"}]
                   :split {:none "welcome.dm"} ; #{:none buffer, :horizontal {:top buffer|:split {} :bottom buffer}, :vertical...}
                   }])


(defc= sorted-workspaces (apply conj [(first workspaces)] (sort (fn [a b] (< (:name a) (:name b))) (rest workspaces))))
(defc current-workspace-index 0)
(defc= current-workspace (get sorted-workspaces current-workspace-index))
(defc= buffers-by-mime (let [workspace-contents (:contents current-workspace)]
                           (->> workspace-contents
                                (map :mime-type)
                                (into #{})
                                (vec)
                                (map (fn [mime-type]
                                       [(:name (get content-handlers mime-type mime-type))
                                        (filter #(= mime-type (:mime-type %)) workspace-contents)]))
                                (into {}))))
(defc= buffers-by-key (->> (:contents current-workspace)
                           (map (fn [buffer]
                                  [(:id buffer) buffer]))
                           (into {})))
(defc= buffer-type-names (sort (keys buffers-by-mime)))



;; Old model below


;; Common settings
(set! cljs.core/*print-fn* #(.log js/console %))


;; Code Mirror -- temporary hack
(defc codemirror nil)

(defn editor-doc []
  (when-let [editor @codemirror]
    (.-doc editor)))


;; Session state from the server

(defc state {})
(defc error nil)
(defc= error-message (when error (.-message error)))
(defc loading [])
(def clear-error! #(reset! error nil))

;; RPC status
(defc= loaded? (not= {} state))
(defc= loading? (seq loading))

(defn mountpoint-name [dir] (if (string? dir) (last (.split dir "/"))))

;; Destructure the session state map using cells
(defc= workspace-path      (:current-workspace state))
(defc= current-workspace-  (get (:workspaces state) workspace-path))
(defc= sorted-workspaces-  (sorted-map-by (fn [k1 k2] (< (mountpoint-name k1) (mountpoint-name k2))) (seq (:workspaces state))))
(defc= project-tree        (:project-tree current-workspace-))
(defc= path                (:file-path current-workspace-))
(defc= filename            (:filename current-workspace-))
(defc= contents            (:saved-file-contents current-workspace-))

;; -- CodeMirror concerns are getting mixed in here


(defc= editor-file
  (when (and filename contents (editor-doc) codemirror)
    (let [editor-file (.getValue (editor-doc))]
      (when (empty? editor-file)
        (.setValue (editor-doc) contents)
        (.markClean (editor-doc))
        (editor/set-mode codemirror filename)
        (html/refresh-layout)
        (.focus codemirror)))
    contents))


(defn set-editor! [editor]
  (reset! codemirror editor))


;; RPC API
(def save-editor! (mkremote 'editorclj.api/save-file! state error loading))
(def get-state    (mkremote 'editorclj.api/get-state state error loading))


;; Autosave
(defn save-if-dirty! []
  (when-let [doc (editor-doc)]
    (when-not (.isClean doc)
      (save-editor! path filename (.getValue doc))
      (.markClean doc)
      (println (str "Saved " path "/" filename)))))


(defn init []
  (get-state)
  (js/setInterval save-if-dirty! 5000))

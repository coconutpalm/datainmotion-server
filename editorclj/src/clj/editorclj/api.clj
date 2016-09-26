(ns editorclj.api
  (:require [castra.core :refer [defrpc *session*]]
            [clojure.test :refer :all]
            [clojure.set :only [union]]
            [clojure.string :as str]
            [catnip.profile :as pro]
            [clj-foundation.patterns :as p :refer [let-map]]
            [clj-foundation.errors :as err]
            [clj-foundation.io :as io]))


;; Project tree handling ---------------------------------------------------

(defn gitignore
  "Return the set of match expressions in the project's .gitignore file or #{} if none."
  [project]
  (let [f (str project "/.gitignore")]))


(defn project-tree [project-root] {})

;; Home workspace handling ---------------------------------------------------

(defn home-workspace-dir [] (.getCanonicalPath (pro/profile-dir)))

(defn open-workspace [workspace-dir]
  (let-map [name                (last (.split workspace-dir "/"))
            welcome-file        (when (= ".dm" name) (slurp (str workspace-dir "/" pro/profile-filename)))
            root-directory      workspace-dir
            file-path           workspace-dir
            filename            (if welcome-file pro/profile-filename "")
            saved-file-contents (if welcome-file welcome-file
                                    (str "Unable to load " (home-workspace-dir) "/" pro/profile-filename))
            project-tree        (project-tree root-directory)]))


;; Application state definition / transitions --------------------------------

(def initial-state
  (let-map [home-workspace    (home-workspace-dir)
            current-workspace home-workspace
            workspaces        {home-workspace (open-workspace home-workspace)}]))


(def state (atom initial-state))


(defrpc save-file! [path filename contents]
  (spit (str path "/" filename) contents)
  (println (str "Saved " path "/" filename))
  (let [workspace (:current-workspace @state)]
    (swap! state assoc-in [:workspaces workspace :saved-file-contents] contents)))


(defrpc get-state []
  @state)


(run-tests)

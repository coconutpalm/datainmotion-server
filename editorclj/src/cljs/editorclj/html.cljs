(ns editorclj.html
  "HTML / DOM manipulation functions"
  (:require [goog.dom :as dom]
            [goog.dom.classes :as classes]
            [goog.events :as events]
            [hoplon.core :as h :refer [script link div span h4 text]]
            [javelin.core :refer [cell]])
  (:require-macros
   [javelin.core :refer [defc defc=]])
  (:import [goog Timer]))


;; Find / process HTML elements

(defn by-id
  "Get an element by its id"
  [id]
  (.getElementById js/document (name id)))


(defn length
  "Return the number of elements in an HTMLCollection"
  [nodes]
  (. nodes -length))


(defn item
  "Get the item at offset n in HTMLCollection"
  [nodes n] (.item nodes n))


(defn as-seq
  "Explicitly coerce an HTML collection to a seq"
  [nodes]
  (for [i (range (length nodes))] (item nodes i)))


(defn by-tag
  "Returns the seq of elements with the specified tag name"
  [tag]
  (as-seq
   (.getElementsByTagName js/document (name tag))))


;; Bootstrap progress dialog

(defc progress-title "")
(defc progress-start-value 0)
(defc progress-end-value 1)
(defc progress-current-value 0)
(defc= progress-percent (-> progress-current-value
                            #(/ % (- progress-end-value progress-start-value))
                            (* 100)))


(defn progress-dialog
  "Return a Bootstrap progress dialog element connected to the above cells."
  []
  (div :class "modal fade" :id "progress-dialog" :tabindex "-1" :role "dialog" :aria-labelledby "progress-dialog"
            (div :class "modal-dialog" :role "document"
                 (div :class "modal-content"
                      (div :class "modal-header"
                           (h4 :class "modal-title" :id "progress-modal" (text "~{progress-title}")))
                      (div :class "modal-body"
                           (div :class "progress"
                                (div :class "progress-bar progress-bar-success progress-bar-striped"
                                     :role "progressbar"
                                     :style "width: 80%"
                                     :aria-valuemin "~{progress-start-value}"
                                     :aria-valuemax "~{progress-end-value}"
                                     :aria-valuenow "~{progress-current-value}"
                                     (span :class "sr-only" (text "~{progress-percent}% Complete (success)")))))))))


(def progress-dialog-options
  {:keyboard false})


(defn open-progress
  "Open the progress meter with the specified title"
  [t start end]
  (reset! progress-title t)
  (reset! progress-start-value start)
  (reset! progress-end-value end)
  (reset! progress-current-value start)
  (-> (js/$ "#progress-dialog") (.modal (clj->js progress-dialog-options)))
  (-> (js/$ "#progress-dialog") (.modal "show")))


(defn close-progress
  []
  (-> (js/$ "#progress-dialog") (.modal "hide")))


;; Make JS browser collections seqable

(extend-type js/NodeList
  ISeqable
  (-seq [array] (array-seq array 0)))


(extend-type js/HTMLCollection
  ISeqable
  (-seq [array] (array-seq array 0)))


(extend-type js/FileList
  ISeqable
  (-seq [array] (array-seq array 0)))


(defn refresh-layout
  "Force the browser to relayout the page"
  []
  (set! (-> js/document .-body .-style .-zIndex) 1))


(defn append-child
  "Add child-node as a child of parent-node.  child-node may be a single dom node or
  a seq of dom nodes to be added."
  [parent-node child-node]
  (if (seq? child-node)
    (do (dom/appendChild parent-node (first child-node))
        (append-child parent-node (rest child-node)))
    (dom/appendChild parent-node child-node)))


(defn append-head
  "Add a node to the HTML head element."
  [child-node]
  (let [head (.-head js/document)]
    (append-child head child-node)))


(defn html
  "Return the html inside the specified dom node."
  [dom] (. dom -innerHTML))


(defn set-html!
  "Set the html inside the specified dom node."
  [dom content]
  (set! (. dom -innerHTML) content))


(defn load-script [url-or-tag continuation-fn]
  (let [head (first (by-tag "head"))
        script (if (string? url-or-tag)
                 (h/script :type "text/javascript" :src url-or-tag)
                 url-or-tag)]
    (aset script "onreadystatechange" continuation-fn)
    (aset script "onload" continuation-fn)
    (.appendChild head script)))


(defn get-scripts
  "Load scripts in order.

  baseurl is a base URL to apply to the head of each script URL.
  scripts is a vector of scripts to load.
  script-complete is called repeatedly when each script is done loading.
  all-complete is the continuation function to call when scripts are all loaded."
  [baseurl scripts script-complete all-complete]
  (if (empty? scripts)
    (all-complete)
    (load-script (str baseurl (first scripts))
                 (fn []
                   (script-complete)
                   (get-scripts baseurl (rest scripts) script-complete all-complete)))))


(defn get-scripts-progress
  "Load scripts in order while displaying a progress bar.

  title is the title of the progress bar dialog
  baseurl is a base URL to apply to the head of each script URL.
  scripts is a vector of scripts to load.
  all-complete is the continuation function to call when scripts are all loaded."
  [title baseurl scripts all-complete]
  (open-progress title 0 (count scripts))
  (get-scripts baseurl scripts
               (fn []
                 (swap! progress-current-value inc))
               (fn []
                 (close-progress)
                 (all-complete))))


(defn stylesheet [path] (link :rel "stylesheet" :href path))

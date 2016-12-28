(ns editorclj.html
  "HTML / DOM manipulation functions"
  (:require [goog.dom :as dom]
            [goog.dom.classes :as classes]
            [goog.events :as events]
            [editorclj.progress :as progress]
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


(def last-script-number (atom 0))


(defc all-scripts {})
(defc= loading-scripts (mapcat (fn [[k status]] (if (:loaded status) [] [status])) (seq all-scripts)))
(defc= all-scripts-loaded (empty? loading-scripts))


(defn script-key [script-tag]
  (let [k (keyword (swap! last-script-number inc))]
    (swap! all-scripts assoc-in [k] {:id k
                                     :source (aget script-tag "outerHTML")
                                     :loaded false})
    k))


(defn script-loaded [script-key continuation-fn]
  (fn []
    (swap! all-scripts assoc-in [script-key :loaded] true)
    (continuation-fn)))


(defn load-script [url-or-tag continuation-fn]
  (let [head (first (by-tag "head"))
        script (if (string? url-or-tag)
                 (h/script :type "text/javascript" :src url-or-tag)
                 url-or-tag)
        key (script-key script)]
    (aset script "onreadystatechange" continuation-fn)
    (aset script "onload" continuation-fn)
    (.appendChild head script)

    key))


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
  (progress/open title 0 (count scripts))
  (get-scripts baseurl scripts
               (fn []
                 (swap! progress/current-value inc))
               (fn []
                 (progress/close)
                 (all-complete))))


(defn stylesheet [path] (link :rel "stylesheet" :href path))

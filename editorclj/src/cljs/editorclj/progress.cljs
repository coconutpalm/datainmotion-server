(ns editorclj.progress
  "Twitter Bootstrap progress dialog"
  (:require [goog.dom :as dom]
            [goog.dom.classes :as classes]
            [goog.events :as events]
            [hoplon.core :as h :refer [script link div span h4 text]]
            [javelin.core :refer [cell]])
  (:require-macros
   [javelin.core :refer [defc defc=]])
  (:import [goog Timer]))


(defc title "")
(defc start-value 0)
(defc end-value 1)
(defc current-value 0)
(defc= percent (-> current-value
                            #(/ % (- end-value start-value))
                            (* 100)))


(defn progress-dialog
  "Return a Bootstrap progress dialog element connected to the above cells."
  []
  (div :class "modal fade" :id "progress-dialog" :tabindex "-1" :role "dialog" :aria-labelledby "progress-dialog"
            (div :class "modal-dialog" :role "document"
                 (div :class "modal-content"
                      (div :class "modal-header"
                           (h4 :class "modal-title" :id "progress-modal" (text "~{title}")))
                      (div :class "modal-body"
                           (div :class "progress"
                                (div :class "progress-bar progress-bar-success progress-bar-striped"
                                     :role "progressbar"
                                     :style "width: 80%"
                                     :aria-valuemin "~{start-value}"
                                     :aria-valuemax "~{end-value}"
                                     :aria-valuenow "~{current-value}"
                                     (span :class "sr-only" (text "~{percent}% Complete (success)")))))))))


(def progress-dialog-options
  {:keyboard false})


(defn open
  "Open the progress meter with the specified title"
  [t start end]
  (reset! title t)
  (reset! start-value start)
  (reset! end-value end)
  (reset! current-value start)
  (-> (js/$ "#progress-dialog") (.modal (clj->js progress-dialog-options)))
  (-> (js/$ "#progress-dialog") (.modal "show")))


(defn close
  []
  (-> (js/$ "#progress-dialog") (.modal "hide")))

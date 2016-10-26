(ns editorclj.codemirror
  (:require-macros
   [javelin.core :refer [defc defc=]])
  (:require
   [editorclj.html :as html :refer [stylesheet]]
   [hoplon.core :as h :refer [script link]]
   [javelin.core :refer [cell]]
   [castra.core :refer [mkremote]]))


(def codemirror-modules
  ["lib/codemirror.js"

   "addon/display/fullscreen.js"
   "addon/display/autorefresh.js"
   "addon/display/panel.js"

   "addon/mode/loadmode.js"
   "addon/mode/multiplex.js"
   "addon/mode/overlay.js"
   "addon/mode/simple.js"

   "mode/xml/xml.js"
   "mode/markdown/markdown.js"
   "mode/gfm/gfm.js"
   "mode/javascript/javascript.js"
   "mode/css/css.js"
   "mode/htmlmixed/htmlmixed.js"
   "mode/haskell/haskell.js"
   "mode/haskell-literate/haskell-literate.js"
   "mode/clike/clike.js"
   "mode/clojure/clojure.js"
   "mode/groovy/groovy.js"
   "mode/haml/haml.js"
   "mode/nginx/nginx.js"
   "mode/perl/perl.js"
   "mode/python/python.js"
   "mode/r/r.js"
   "mode/ruby/ruby.js"
   "mode/shell/shell.js"
   "mode/yaml/yaml.js"
   "mode/dockerfile/dockerfile.js"
   "mode/go/go.js"
   "mode/sql/sql.js"

   "mode/meta.js"

   "keymap/emacs.js"
   "keymap/sublime.js"
   "keymap/vim.js"

   "addon/fold/foldcode.js"
   "addon/fold/foldgutter.js"
   "addon/fold/brace-fold.js"
   "addon/fold/indent-fold.js"
   "addon/fold/comment-fold.js"
   "addon/fold/markdown-fold.js"
   "addon/fold/xml-fold.js"

   "addon/edit/closebrackets.js"
   "addon/edit/closetag.js"
   "addon/edit/continuelist.js"
   "addon/edit/matchbrackets.js"
   "addon/edit/matchtags.js"
   "addon/edit/trailingspace.js"

   "addon/comment/comment.js"
   "addon/comment/continuecomment.js"
   "addon/dialog/dialog.js"

   "addon/search/search.js"
   "addon/search/jump-to-line.js"
   "addon/scroll/annotatescrollbar.js"
   "addon/search/matchesonscrollbar.js"
   "addon/search/searchcursor.js"
   "addon/search/match-highlighter.js"

   "addon/selection/active-line.js"
   "addon/selection/mark-selection.js"
   "addon/selection/selection-pointer.js"

   "addon/hint/show-hint.js"
   "addon/hint/anyword-hint.js"

   "addon/hint/css-hint.js"
   "addon/hint/html-hint.js"
   "addon/hint/javascript-hint.js"
   "addon/hint/show-hint.js"
   "addon/hint/sql-hint.js"
   "addon/hint/xml-hint.js"

   "addon/lint/coffeescript-lint.js"
   "addon/lint/css-lint.js"
   "addon/lint/html-lint.js"
   "addon/lint/javascript-lint.js"
   "addon/lint/json-lint.js"
   "addon/lint/lint.js"
   "addon/lint/yaml-lint.js"])


(def stylesheets
  ["addon/display/fullscreen.css"
   "addon/fold/foldgutter.css"
   "addon/dialog/dialog.css"
   "addon/search/matchesonscrollbar.css"
   "addon/hint/show-hint.css"
   "addon/lint/lint.css"
   "lib/codemirror.css"])


(def default-settings {:autoRefresh true
                       :lineNumbers true
                       :autoCloseBrackets true
                       :foldGutter true
                       :lineWrapping true
                       :extraKeys {"Ctrl-Space" "autocomplete"
                                   "Ctrl-/"     "toggleComment"
                                   "Ctrl-Home"  "goDocStart"
                                   "Ctrl-End"   "goDocEnd"}
                       :gutters ["CodeMirror-linenumbers" "CodeMirror-foldgutter"]
                       :theme "default"})


#_(defn cm-script [path] (script :type "text/javascript" :src (str "CodeMirror/" path)))



(defn load-codemirror-css
  []
  (doseq [s stylesheets]
    (html/append-head (stylesheet (str "/CodeMirror/" s))))
  (html/append-head (stylesheet "/codemirror-local.css")))


(defn init-codemirror []
  (set! js/CodeMirror.modeURL "CodeMirror/mode/%N/%N.js"))


(defn load-codemirror-js
  "Load CodeMirror js files"
  [continuation-fn]
  (html/get-scripts-progress "Loading Code Mirror"
                             "/CodeMirror/"
                             codemirror-modules
                             #(do (load-codemirror-css)
                                  (init-codemirror)
                                  (continuation-fn))))



;; See also http://stackoverflow.com/questions/18828658/how-to-kill-a-codemirror-instance
;;   ...and http://bl.ocks.org/jasongrout/5378313   CodeMirror live widgets
;;  https://jbt.github.io/markdown-editor and https://github.com/jbt/markdown-editor   GFM editor using CM
;;  https://github.com/NextStepWebs/simplemde-markdown-editor

;; WOOT for Code Mirror in Elm: https://github.com/coconutpalm/co
;; WOOT for Quill in Coffeescript: https://github.com/kroky/woot
;; WOOT for Ace in ScalaJS: https://github.com/d6y/wootjs
;; Generic WOOT in JS: https://github.com/TGOlson/woot-js

;; PeerJs - WebRTC http://peerjs.com/

;; Clojurescript self-hosted https://github.com/clojure/clojurescript/wiki/Bootstrapping-the-Compiler

;; Scala team's ClojureScript embedding: http://blog.scalac.io/2015/10/19/cljs-on-gh-pages.html
;; ScalaFiddle source https://github.com/scalafiddle/scalafiddle-core


(defn create-editor
  ([textarea config]
   (when-let [editor (.fromTextArea js/CodeMirror textarea (clj->js config))]
     (.focus editor)
     editor))
  ([config]
   (js/CodeMirror (.-body js/document) (clj->js config))))


;; Functions on top of editor instance

;; Content manipulation methods

;;function change() {
;;                   var val = modeInput.value, m, mode, spec;
;;                   if (m = /.+\.([^.]+)$/.exec(val)) {
;;                                                      var info = CodeMirror.findModeByExtension(m[1]);
;;                                                      if (info) {
;;                                                                 mode = info.mode;
;;                                                                 spec = info.mime;
;;                                                                 }
;;                                                      } else if (/\//.test(val)) {
;;                                                                                  var info = CodeMirror.findModeByMIME(val);
;;                                                                                  if (info) {
;;                                                                                             mode = info.mode;
;;                                                                                             spec = val;
;;                                                                                             }
;;                                                                                  } else {
;;                                                                                          mode = spec = val;
;;                                                                                          }
;;                   if (mode) {
;;                              editor.setOption("mode", spec);
;;                              CodeMirror.autoLoadMode(editor, mode);
;;                              document.getElementById("modeinfo").textContent = spec;
;;                              } else {
;;                                      alert("Could not find a mode corresponding to " + val);
;;                                      }
;;                   }


(let [file-ext-regex #".+\.([^.]+)$"]
  (defn set-mode
    "Set the editor's mode based on filename"
    [editor filename]
    (let [ext (last (re-seq file-ext-regex filename))
          info (if ext
                 (or (.findModeByExtension js/CodeMirror ext) (.findModeByMIME js/CodeMirror "text/x-gfm"))
                 (.findModeByMIME js/CodeMirror "text/x-gfm"))
          mode (.-mode info)
          spec (.-mime info)]
      (when mode
        (.autoLoadMode js/CodeMirror editor mode)
        (.setOption editor "mode" spec)))))


(defn get-value
   ([editor] (.getValue editor))
   ([editor sep] (.getValue editor sep)))

(defn set-value
   [editor value] (.setValue editor value))


(defn get-range
   ([editor from to]
    (.getRange editor (clj->js from) (clj->js to)))
   ([editor from to sep]
    (.getRange editor (clj->js from) (clj->js to) sep)))


(defn replace-range
   ([editor string from]
    (.replaceRange editor string (clj->js from)))
   ([editor string from to]
    (.replaceRange editor string (clj->js from) (clj->js to))))

(defn get-line
   [editor n]
   (.getLine editor n))

(defn set-line
   [editor n text]
   (.setLine editor n text))

(defn remove-line
   [editor n]
   (.removeLine editor n))

(defn line-count
   [editor]
   (.lineCount editor))

(defn first-line
   [editor]
   (.firstLine editor))

(defn last-line
   [editor]
   (.lastLine editor))

(defn get-line-handle
   [editor n]
   (.getLineHandle editor n))

(defn get-line-number
   [editor handle]
   (.getLineNumber editor handle))

(defn each-line
   ([editor function]
    (.eachLine editor function))
   ([editor start end function]
    (.eachLine editor start end function)))

(defn mark-clean
   [editor]
   (.markClean editor))

(defn change-generation
   [editor]
   (.changeGeneration editor))

(defn is-clean
   [editor]
   (.isClean editor))


; Cursor and selection methods
(defn get-selection
   [editor]
   (.getSelection editor))

; -
(defn replace-selection
   [editor replacement]
   (.replaceSelection editor replacement))

; - make it return a cljs obj
(defn get-cursor
   [editor]
   (.getCursor editor))

(defn something-selected
   [editor]
   (.somethingSelected editor))

(defn set-cursor
   [editor pos]
   (.setCursor editor (clj->js pos)))

; -
(defn set-selection
   [editor anchor]
   (.setSelection editor (clj->js anchor)))

; -
(defn extend-selection
   [editor from]
   (.extendSelection editor (clj->js from)))

(defn set-extending
   [editor value]
   (.setExtending editor value))

(defn has-focus
   [editor]
   (.hasFocus editor))

(defn find-pos-h
   [])

(defn find-pos-v
   [])


; Configuration methods

(defn set-option
   [editor option value]
   (.setOption editor option value))

(defn get-option
   [editor option]
   (.getOption editor option))

(defn add-key-map
   [])

(defn remove-key-map
   [])

(defn add-overlay
   [])

(defn remove-overlay
   [])

(defn on
   [])

(defn off
   [])



; Document management methods

(defn get-doc
   [editor]
   (.getDoc editor))

(defn get-editor
   [doc]
   (.getEditor doc))

(defn swap-doc
   [editor doc]
   (.swapDoc editor doc))

(defn copy
   [doc]
   (.copy doc))

(defn linked-doc
   [])

(defn unlink-doc
   [])

(defn iter-linked-docs
   [])


; History related methods
(defn undo
   [editor]
   (.undo editor))

(defn redo
   [editor]
   (.redo editor))

(defn history-size
   [editor]
   (.historySize editor))

(defn clear-history
   [editor]
   (.clearHistory editor))

(defn get-history
   [editor]
   (.getHistory editor))

(defn set-history
   [])



; Text marking methods

(defn mark-text [editor from to options]
  (.markText editor
            (clj->js from)
            (clj->js to)
            (clj->js options)))

(defn set-bookmark
   [])

(defn find-marks-at
   [])

(defn get-all-marks
  [])

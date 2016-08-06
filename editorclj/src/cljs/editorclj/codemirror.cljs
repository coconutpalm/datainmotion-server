(ns editorclj.codemirror
  (:require [cljsjs.codemirror]
            [editorclj.codemirror-refs :refer [CodeMirror]]
            [cljsjs.codemirror.addon.display.fullscreen]

            [cljsjs.codemirror.addon.mode.loadmode]
            [cljsjs.codemirror.addon.mode.multiplex]
            [cljsjs.codemirror.addon.mode.overlay]
            [cljsjs.codemirror.addon.mode.simple]

            [cljsjs.codemirror.mode.xml]
            [cljsjs.codemirror.mode.markdown]
            [cljsjs.codemirror.mode.gfm]
            [cljsjs.codemirror.mode.javascript]
            [cljsjs.codemirror.mode.css]
            [cljsjs.codemirror.mode.htmlmixed]
            [cljsjs.codemirror.mode.clike]
            [cljsjs.codemirror.mode.clojure]
            [cljsjs.codemirror.mode.groovy]
            [cljsjs.codemirror.mode.haml]
            [cljsjs.codemirror.mode.nginx]
            [cljsjs.codemirror.mode.perl]
            [cljsjs.codemirror.mode.python]
            [cljsjs.codemirror.mode.r]
            [cljsjs.codemirror.mode.ruby]
            [cljsjs.codemirror.mode.shell]
            [cljsjs.codemirror.mode.yaml]
            [cljsjs.codemirror.mode.dockerfile]
            [cljsjs.codemirror.mode.go]
            [cljsjs.codemirror.mode.sql]

            [cljsjs.codemirror.mode.meta]

            [cljsjs.codemirror.keymap.emacs]
            [cljsjs.codemirror.keymap.sublime]
            [cljsjs.codemirror.keymap.vim]

            [cljsjs.codemirror.addon.fold.foldcode]
            [cljsjs.codemirror.addon.fold.foldgutter]
            [cljsjs.codemirror.addon.fold.brace-fold]
            [cljsjs.codemirror.addon.fold.indent-fold]
            [cljsjs.codemirror.addon.fold.comment-fold]
            [cljsjs.codemirror.addon.fold.markdown-fold]
            [cljsjs.codemirror.addon.fold.xml-fold]

            [cljsjs.codemirror.addon.edit.closebrackets]
            [cljsjs.codemirror.addon.edit.closetag]
            [cljsjs.codemirror.addon.edit.continuelist]
            [cljsjs.codemirror.addon.edit.matchbrackets]
            [cljsjs.codemirror.addon.edit.matchtags]
            [cljsjs.codemirror.addon.edit.trailingspace]

            [cljsjs.codemirror.addon.comment.comment]
            [cljsjs.codemirror.addon.comment.continuecomment]
            [cljsjs.codemirror.addon.dialog.dialog]

            [cljsjs.codemirror.addon.search.search]
            [cljsjs.codemirror.addon.search.jump-to-line]
            [cljsjs.codemirror.addon.scroll.annotatescrollbar]
            [cljsjs.codemirror.addon.search.matchesonscrollbar]
            [cljsjs.codemirror.addon.search.searchcursor]
            [cljsjs.codemirror.addon.search.match-highlighter]

            [cljsjs.codemirror.addon.selection.active-line]
            [cljsjs.codemirror.addon.selection.mark-selection]
            [cljsjs.codemirror.addon.selection.selection-pointer]

            [cljsjs.codemirror.addon.hint.show-hint]
            [cljsjs.codemirror.addon.hint.anyword-hint]

            [cljsjs.codemirror.addon.hint.css-hint]
            [cljsjs.codemirror.addon.hint.html-hint]
            [cljsjs.codemirror.addon.hint.javascript-hint]
            [cljsjs.codemirror.addon.hint.show-hint]
            [cljsjs.codemirror.addon.hint.sql-hint]
            [cljsjs.codemirror.addon.hint.xml-hint]

            [cljsjs.codemirror.addon.lint.coffeescript-lint]
            [cljsjs.codemirror.addon.lint.css-lint]
            [cljsjs.codemirror.addon.lint.html-lint]
            [cljsjs.codemirror.addon.lint.javascript-lint]
            [cljsjs.codemirror.addon.lint.json-lint]
            [cljsjs.codemirror.addon.lint.lint]
            [cljsjs.codemirror.addon.lint.yaml-lint]))



(def Doc (.-Doc js/CodeMirror))


(defn create-editor
  ([textarea config]
   (.fromTextArea js/CodeMirror textarea (clj->js config)))

  ([config]
   (js/CodeMirror (.-body js/document) (clj->js config))))


; Functions on top of editor instance

; Content manipulation methods
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

(defn set-bookmard
	[])

(defn find-marks-at
	[])

(defn get-all-marks
  [])

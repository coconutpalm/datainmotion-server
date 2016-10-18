# editorclj

Making Code Mirror be the best programmer's editor ever.

# TODO

* Extensibility
  * Requirejs everything
  * Function to add (link :rel "stylesheet") to (head ...)

* Workspace is a UI construct only
  * Mount resource roots to display/edit
  * Display (in split-panes) editors/viewers for resources

* Resource root types
  * The current (lein, boot, mvn, gradle) project
  * Folder on server
  * HTTP URL
  * Files available via ssh to somewhere else

* Pluggable editor types
  * Text editor
  * Clojure REPL
  * Ammonite REPL
  * BASH shell
  * IFrame (displaying HTTP URL)

* Split panes in workspace areas
  * Multiple editors of the same resource are linked
  * Anything can go in any pane

* Clojure editing
  * Pre-load possible completions into map in browser based on ns form; supply autocompleter
  * Atomic Delimiters (e.g.: Deleting a paren, quote, etc. deletes to its match, then reformats)
  * Atomic whitespace (Deleting ws before a paren at beginning of line, moves s-expr to prior line, then reformats)
    * .markText method in CodeMirror
  * Formatting
  * Paredit / Parinfer
  * Semantic refactoring

# Java interop

* Need an object abstraction that looks/works the same from Java as from Clojure
  * (Don't want to have to lookup vars in order to access Clojure from Java)
* Compile Clojure to Scala's runtime
  * Probably keep the core persistent collection classes
  * Otherwise, compile a Clojure ns to a Scala object


# Hoplon / Castra / Javelin quick reference

* Cells

```clojure
(defc val "initial-value")
(reset! val "new-value")
```

* Formula cells, interpolation, and template loops over cells containing seqs:

```clojure
(defc= double-val (str val ", " val))        ; Automatically recalculated
(def double-val (cell= (str val ", " val)))  ; (Same meaning as prior line)

(h1 "Hello, ~{double-val}")

(loop-tpl :bindings [var1 val1 var2 val2]
  (div :id var1 (text var2)))

; e.g.:
(ol
  (loop-tpl :bindings [n history]
    (li (cell= (str "n was " n)))))
```

* Inside regular code, cells are like refs

```clojure
(println @double-val)
```

* Responding to DOM events

```clojure
(a :click #(reset! val double-val))
```

* Client-server communication

  * On the server

```clojure
(def state (atom {server-state}))

(defrpc rpc-event-fn [arg1 arg2 ... argn]
  (do-something-in-response-to-the-event)
  (swap! state calculate-new-state))
```

  * Client-side

```clojure
; Session state, plus RPC state
(defc state {})
(defc error nil)
(defc= error-message (when error (.-message error)))
(defc loading [])
(def clear-error! #(reset! error nil))

; Define RPC apis inside client
(def rpc-event-fn (mkremote 'package/rpc-event-fn state error loading))  ; Notice, parameters aren't declared here

; Call RPC on server from client
(defn dom-event-handler [_]
  (rpc-event-fn arg1 arg2 argn))  ; Parameters here must match those declared on the server
```

## Dependencies

- java 1.7+
- [boot][1]

## Usage
### Development
1. Start the `dev` task. In a terminal run:
    ```bash
    $ boot dev
    ```
    This will give you a  Hoplon development setup with:
    - auto compilation on file changes
    - audible warning for compilation success or failures
    - auto reload the html page on changes
    - Clojurescript REPL

2. Go to [http://localhost:8000][3] in your browser. You should see "Hello,
Hoplon and Castra!" with random numbers that are generated on the server and
transmited to the client. But you should change that to what you want.

3. If you edit and save a file, the task will recompile the code and reload the
   browser to show the updated version.

.### Production
1. Run the `prod` task. In a terminal run:
    ```bash
    $ boot prod
    ```
2. The compiled files will be on the `target/` directory. This will use
   advanced compilation and prerender the html.

### Deployment

1. Create an application in the Heroku dashboard
1. Build a WAR file with `boot make-war`

## License

Copyright © 2016, **Your Name Goes Here**

[1]: http://boot-clj.com
[2]: https://github.com/hoplon/castra
[3]: http://localhost:8000
[4]: https://hoplon.io

(set-env!
 :repositories #(conj % '["jitpack" {:url "https://jitpack.io"}])

 :dependencies '[[org.clojure/clojure       "1.8.0"]
                 [org.clojure/clojurescript "1.9.229"]
                 [org.clojure/core.async    "0.2.395"]

                 [adzerk/boot-cljs          "1.7.228-1"]
                 [adzerk/boot-reload        "0.4.12"]
                 [boot-deps                 "0.1.6"]
                 [compojure                 "1.6.0-beta1"]
                 [hoplon/boot-hoplon        "0.2.2"]
                 [hoplon/castra             "3.0.0-alpha4"]
                 [hoplon/hoplon             "6.0.0-alpha16"]
                 [org.clojure/clojure       "1.8.0"]
                 [org.clojure/clojurescript "1.9.93"]
                 [compliment                "0.3.0"]
                 [clojail "1.0.6"]
                 [nightlight                "1.0.0" :scope "test"]

                 [pandeiro/boot-http        "0.7.3"]
                 [ring                      "1.5.0"]
                 [ring/ring-defaults        "0.2.1"]
                 [cheshire                  "5.6.3"]
                 [clj-info                  "0.3.1"]
                 [clj-stacktrace            "0.2.8"]
                 [com.cemerick/pomegranate  "0.3.1"]

                 ;; Misc foundation libraries
                 [im.chit/gita "0.2.5"]
                 [mount "0.1.10"] ; TODO: Implement!
                 [defprecated "0.1.3"]
                 [org.blancas/morph "0.3.0"] ; MONADS that work!
                 [marick/structural-typing "2.0.5"]  ; Specs at runtime

                 ;; clj-foundation toolchain
                 [com.github.shopsmart/clj-foundation "0.9.15"]

                 ;; Lucid toolchain -- http://docs.caudate.me/lucidity/index.html
                 [im.chit/lucid.core.aether   "1.2.0"]
                 [im.chit/lucid.core.asm "1.2.0"]
                 [im.chit/lucid.core.code "1.2.0"]
                 [im.chit/lucid.core.debug "1.2.0"]
                 [im.chit/lucid.core.inject "1.2.0"]
                 [im.chit/lucid.core.namespace "1.2.0"]
                 [im.chit/lucid.mind "1.2.0"]    ; Easily explore Java objects from Clojure
                 [im.chit/lucid.publish "1.2.0"] ; Render Clojure code as fluent HTML; the code IS the doc
                                                 ; IMO, needs to support Markdown or AsciiDoc
                 [im.chit/lucid.query "1.2.0"]   ; Query and transform Clojure source code
                 [rewrite-clj "0.5.2"]           ; Useful with lucid.query
                 [im.chit/lucid.space "1.2.0"]   ; Query and manipulate Maven-style artifacts from Clojre
                 [im.chit/lucid.unit "1.2.0"]    ; Import unit tests as docstrings

                 ;; Hara toolchain -- http://docs.caudate.me/hara/index.html
                 [im.chit/hara.class.checks "2.4.5"]
                 [im.chit/hara.class.enum "2.4.5"]
                 [im.chit/hara.class.inheritance "2.4.5"]
                 [im.chit/hara.class.multi "2.4.5"]
                 [im.chit/hara.common.checks "2.4.5"]
                 [im.chit/hara.common.error "2.4.5"]
                 [im.chit/hara.common.primitives "2.4.5"]
                 [im.chit/hara.common.state "2.4.5"]
                 [im.chit/hara.common.string "2.4.5"]
                 [im.chit/hara.common.watch "2.4.5"]
                 [im.chit/hara.concurrent.latch "2.4.5"]
                 [im.chit/hara.concurrent.notification "2.4.5"]
                 [im.chit/hara.concurrent.pipe "2.4.5"]
                 ;; concurrent.propogate not included; Javelin's cell implementation would be preferable
                 ;; or something more closely adhering to the Cells Manifesto
                 [im.chit/hara.concurrent.ova "2.4.5"]
                 [im.chit/hara.concurrent.procedure "2.4.5"]
                 [im.chit/hara.data.map "2.4.5"]
                 [im.chit/hara.data.seq "2.4.5"]
                 [im.chit/hara.data.nested "2.4.5"]
                 [im.chit/hara.data.diff "2.4.5"]
                 [im.chit/hara.data.combine "2.4.5"]
                 [im.chit/hara.data.complex "2.4.5"]
                 [im.chit/hara.data.path "2.4.5"]
                 [im.chit/hara.data.record "2.4.5"]
                 [im.chit/hara.event "2.4.5"]          ; Provides delimited continuations
                 [im.chit/hara.expression.compile "2.4.5"]
                 [im.chit/hara.expression.load "2.4.5"]
                 [im.chit/hara.expression.shorthand "2.4.5"]
                 [im.chit/hara.extend.abstract "2.4.5"]
                 [im.chit/hara.extend.all "2.4.5"]
                 [im.chit/hara.function.args "2.4.5"]
                 [im.chit/hara.function.dispatch "2.4.5"]
                 [im.chit/hara.group "2.4.5"]          ; A group is an Entity in the relational db sense
                 [im.chit/hara.io.classloader "2.4.5"] ; Create isolated namespaces with classloaders
                 [im.chit/hara.io.environment "2.4.5"] ; Query the Java/Clojure runtime environment
                 [im.chit/hara.io.project "2.4.5"]     ; Parse lein project files; returns clj files in project
                 [im.chit/hara.io.file "2.4.5"]        ; Enhanced clojure.java.io rewritten using nio
                 [im.chit/hara.io.scheduler "2.4.5"]
                 [im.chit/hara.io.watch "2.4.5"]       ; NIO filesystem watching implemented as Clojure watches
                 [im.chit/hara.namespace.eval "2.4.5"] ; Eval within a given or temp namespace
                 [im.chit/hara.object "2.4.5"]         ; *** Easier Java interop from Clojre
                 [im.chit/hara.reflect "2.4.5"]        ; More easier Java interop
                 [im.chit/hara.sort.hierarchical "2.4.5"]
                 [im.chit/hara.sort.topological "2.4.5"]
                 [im.chit/hara.string.case "2.4.5"]
                 [im.chit/hara.string.path "2.4.5"] ; Filesystem-like paths in strings
                 [im.chit/hara.test "2.4.5"]        ; A test framework designed for fluent program expression
                 [im.chit/hara.time "2.4.5"]        ; Unify time representations on the JVM
                 [im.chit/hara.zip "2.4.5"]         ; Traverse and modify Clojure data structure graphs

                 [org.clojure/tools.nrepl   "0.2.12"]
                 [refactor-nrepl            "2.3.0-SNAPSHOT"]
                 [cider/cider-nrepl         "0.14.0-SNAPSHOT"]]


 :resource-paths #{"resources" "src/clj"}
 :source-paths   #{"src/cljs" "src/hl"})


(require
 '[adzerk.boot-cljs      :refer [cljs]]
 '[adzerk.boot-reload    :refer [reload]]
 '[hoplon.boot-hoplon    :refer [hoplon prerender]]
 '[pandeiro.boot-http    :refer [serve]]
 '[nightlight.boot       :refer [night]]
 '[boot.repl])


(deftask ancient
  "Show what needs to be upgraded."
  []
  (fn [continue]
    (fn [event]
      (ancient))))


#_(deftask repl-cider
   "Launch nrepl in the project, and echo the port for remote connection"
   []
   (fn [continue]
     (fn [event]
       (require 'clojure.core)
       (require 'clojure.core.protocols)
       (require 'clojure.core.reducers)
       (require 'refactor-nrepl.middleware)
       (require 'clojure.tools.nrepl.server)
       (require 'cider.nrepl)
       (let [start-server    (resolve 'clojure.tools.nrepl.server/start-server)
             default-handler (resolve 'clojure.tools.nrepl.server/default-handler)
             cider           (resolve 'cider.nrepl/cider-middleware)
             refactor        (resolve 'refactor-nrepl.middleware/wrap-refactor)]
         (let [server (start-server :port 0 :handler (apply default-handler refactor (map resolve (var-get cider))))]
           (println "nRepl: Started on " (:port server))
           (continue event)
           @(promise))))))


(deftask dev
  "Build editorclj for local development with regular nrepl server."
  []
  (comp
   (night "--port" "7000")
   (serve
    :port    8000
    :handler 'editorclj.handler/app
    :reload  true)
   (watch)
   (speak)
   (hoplon)
   (reload)
   (cljs)))


(deftask prod
  "Build editorclj for production deployment."
  []
  (comp
   (hoplon)
   (cljs :optimizations :advanced)
   (prerender)))


(deftask make-war
  "Build a war for deployment"
  []
  (comp (hoplon)
        #_(sift :add-jar
              {'cljsjs/codemirror #"cljsjs/codemirror/development/codemirror.css"})
        #_(sift :move
              {#"cljsjs/codemirror/development/codemirror.css"
               "vendor/codemirror/codemirror.css"})
        (cljs :optimizations :advanced)
        (uber :as-jars true)
        (web :serve 'my-app.handler/app)
        (war)
        (target :dir #{"target"})))

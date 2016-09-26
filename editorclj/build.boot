(set-env!
 :repositories #(conj % '["jitpack" {:url "https://jitpack.io"}])

 :dependencies '[[adzerk/boot-cljs          "1.7.228-1"]
                 [adzerk/boot-reload        "0.4.12"]
                 [compojure                 "1.6.0-beta1"]
                 [hoplon/boot-hoplon        "0.2.2"]
                 [hoplon/castra             "3.0.0-alpha4"]
                 [hoplon/hoplon             "6.0.0-alpha16"]
                 [org.clojure/clojure       "1.8.0"]
                 [org.clojure/clojurescript "1.9.93"]
                 [compliment                "0.3.0"]
                 [com.github.shopsmart/clj-foundation "0.9.11"]
                 [pandeiro/boot-http        "0.7.3"]
                 [ring                      "1.5.0"]
                 [ring/ring-defaults        "0.2.1"]
                 [cljsjs/d3                 "4.2.0-0"]
                 [cheshire                  "5.6.3"]
                 [clj-info                  "0.3.1"]
                 [clj-stacktrace            "0.2.8"]]

 :resource-paths #{"resources" "src/clj"}
 :source-paths   #{"src/cljs" "src/hl"})

(require
 '[adzerk.boot-cljs      :refer [cljs]]
 '[adzerk.boot-reload    :refer [reload]]
 '[hoplon.boot-hoplon    :refer [hoplon prerender]]
 '[pandeiro.boot-http    :refer [serve]]
 '[boot.repl])


(deftask dev
  "Build editorclj for local development with regular nrepl server."
  []
  (comp
   (serve
    :port    8000
    :handler 'editorclj.handler/app
    :reload  true)
   (repl)
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
        (sift :add-jar
              {'cljsjs/codemirror #"cljsjs/codemirror/development/codemirror.css"})
        (sift :move
              {#"cljsjs/codemirror/development/codemirror.css"
               "vendor/codemirror/codemirror.css"})
        (cljs :optimizations :advanced)
        (uber :as-jars true)
        (web :serve 'my-app.handler/app)
        (war)
        (target :dir #{"target"})))

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
                 [com.github.shopsmart/clojure-navigation "1.0.1"]
                 [pandeiro/boot-http        "0.7.3"]
                 [ring                      "1.5.0"]
                 [ring/ring-defaults        "0.2.1"]
                 [cljsjs/codemirror         "5.11.0-2"]
                 [cljsjs/d3                 "4.2.0-0"]]
 :resource-paths #{"resources" "src/clj"}
 :source-paths   #{"src/cljs" "src/hl"})


(require
  '[adzerk.boot-cljs      :refer [cljs]]
  '[adzerk.boot-reload    :refer [reload]]
  '[hoplon.boot-hoplon    :refer [hoplon prerender]]
  '[pandeiro.boot-http    :refer [serve]])


(deftask dev
  "Build editorclj for local development."
  []
  (comp
   (sift :add-jar {'cljsjs/codemirror #"cljsjs/codemirror/development/codemirror.css"})
   (sift :move {#"cljsjs/codemirror/development/codemirror.css" "vendor/codemirror/codemirror.css"})
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
   (sift :add-jar
         {'cljsjs/codemirror #"cljsjs/codemirror/development/codemirror.css"})
   (sift :move
         {#"cljsjs/codemirror/development/codemirror.css"
          "vendor/codemirror/codemirror.css"})
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

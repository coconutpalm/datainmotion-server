(ns editorclj.index
  (:require-macros
   [javelin.core :refer [defc defc=]])
  (:require
   [javelin.core :refer [cell]]
   [castra.core :refer [mkremote]]
   [hoplon.core :refer [link script]]
   [editorclj.html :as html]))


(defc page-title "Data in Motion")


(def css
  [(link :rel "stylesheet"
         :href "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css"
         :integrity "sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7"
         :crossorigin "anonymous")

   (link :rel "stylesheet"
         :href "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap-theme.min.css"
         :integrity "sha384-fLW2N01lMqjakBkx3l/M9EahuwpSfeNvV63J5ezn3uZzapT0u7EYsXMjQV+0En5r"
         :crossorigin "anonymous")

   (link :rel "stylesheet"
         :href "/ie10/ie10-viewport-bug-workaround.css")

   (link :rel "stylesheet"
         :href "/splitter/splitter.css")

   (link :rel "stylesheet"
         :href "app.css")])


(def scripts
  [(script :src "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"
           :integrity "sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS"
           :crossorigin "anonymous")

   (script :src "/ie10/ie10-viewport-bug-workaround.js")

   (script :src "/splitter/splitter.min.js")

   (script :src "http://cdn.jsdelivr.net/pouchdb/6.0.7/pouchdb.min.js")])

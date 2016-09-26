;; This Source Code Form is subject to the terms of the Mozilla Public
;; License, v. 2.0. If a copy of the MPL was not distributed with this file,
;; You can obtain one at http://mozilla.org/MPL/2.0/.

(ns catnip.profile
  (:require [clojure.java.io :as io]
            [clojure.pprint :as pprint]
            [dm.eval :as dm]
            [cheshire.custom :as json]))


(def profile-filename "welcome.dm")


(defn profile-dir []
  (let [path (io/file (System/getProperty "user.home") ".dm")]
    (.mkdirs path)
    path))


(defn- profile-path []
  (io/file (profile-dir) profile-filename))


(defn save-default-profile []
  (let [profile (slurp (io/resource "public/welcome.dm"))]
    (println "Generating default profile")
    (spit (profile-path) profile)))


(defn profile-file []
  (let [path (profile-path)]
    (if-not (.exists path)
      (save-default-profile))

    (slurp (profile-path))))


(defn read-profile []
  (dm/eval (profile-file)))


(defn wrap-profile []
  (let [profile (read-profile)]
    (str "window.DmProfile = "
         (json/generate-string profile)
         ";\n")))

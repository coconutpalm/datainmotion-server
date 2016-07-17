resolvers += "Typesafe repository" at "https://repo.typesafe.com/typesafe/releases/"

// The Play plugin
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.5.4")

addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "3.0.0")

// web plugins

addSbtPlugin("com.typesafe.sbt" % "sbt-coffeescript" % "1.0.0")

addSbtPlugin("com.typesafe.sbt" % "sbt-less" % "1.1.0")

addSbtPlugin("com.typesafe.sbt" % "sbt-jshint" % "1.0.4")

addSbtPlugin("com.typesafe.sbt" % "sbt-rjs" % "1.0.8")

addSbtPlugin("com.typesafe.sbt" % "sbt-digest" % "1.1.1")

addSbtPlugin("com.typesafe.sbt" % "sbt-mocha" % "1.1.0")

addSbtPlugin("org.scala-js" % "sbt-scalajs" % "0.6.10")

addSbtPlugin("com.vmunier" % "sbt-play-scalajs" % "0.3.1")

addSbtPlugin("com.geirsson" % "sbt-scalafmt" % "0.2.10")

val plugins = (project in file(".")).
  dependsOn(sbtOsgi)
//  dependsOn(sbtTrafficland)

def sbtOsgi = uri("git://github.com/coconutpalm/sbt-osgi.git")

//def sbtTrafficland = uri("git://github.com/coconutpalm/tl-os-sbt-plugins.git")
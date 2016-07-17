//
// Uses TrafficLand's SBT plugin set: https://github.com/coconutpalm/tl-os-sbt-plugins
// Also: https://github.com/coconutpalm/sbt-osgi
//       TODO: Merge https://github.com/arjenw/sbt-osgi/network
//
package editor

import sbt._
import sbt.Project.projectToRef
import Keys._
import trafficland.opensource.sbt.plugins._

object Build extends Build {

  import sbt.Project.projectToRef

  def sbtOsgi = uri("git@github.com:coconutpalm/sbt-osgi.git")

  lazy val clients = Seq(scalajsclient)
  lazy val scalaV = "2.11.8"

  lazy val playserver = (project in file("play")).settings(
    scalaVersion := scalaV,
    scalaJSProjects := clients,
    libraryDependencies ++= Seq(
      "com.lihaoyi" %% "scalatags" % "0.5.5",
      "org.webjars" % "jquery" % "3.0.0"
    )
  ).enablePlugins(PlayScala).
    StandardPluginSet.plugs.
    aggregate(clients.map(projectToRef): _*).
    dependsOn(sharedJvm).
    dependsOn(sbtOsgi)



  lazy val scalajsclient = (project in file("scalajs")).settings(
    scalaVersion := scalaV,
    persistLauncher := true,
    persistLauncher in Test := false,
    //  sourceMapsDirectories += sharedJs.base / "..",
    unmanagedSourceDirectories in Compile := Seq((scalaSource in Compile).value),
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "0.8.2",
      "com.lihaoyi" %%% "scalatags" % "0.5.5"
    )
  ).enablePlugins(ScalaJSPlugin, ScalaJSPlay).
    StandardPluginSet.plugs.
    dependsOn(sharedJs)

  lazy val shared = (crossProject.crossType(CrossType.Pure) in file("shared")).
    settings(scalaVersion := scalaV).
    StandardPluginSet.plugs.
    dependsOn(sbtOsgi).
    jsConfigure(_ enablePlugins ScalaJSPlay)
  //  jsSettings(sourceMapsBase := baseDirectory.value / "..")

  lazy val sharedJvm = shared.jvm
  lazy val sharedJs = shared.js

  // loads the Play project at sbt startup
  onLoad in Global := (Command.process("project playserver", _: State)) compose (onLoad in Global).value

  // for Eclipse users
  EclipseKeys.skipParents in ThisBuild := false


  fork in run := true
}

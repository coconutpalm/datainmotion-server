package views

import scalatags.Text
import scalatags.Text._

object MainView {
  import scalatags.Text.all._

  def apply(contents: Seq[Modifier])(implicit env: play.Environment) = {
    html(
      head(
        // Basic HTML metadata
        meta(charset := "utf-8"),
        meta(attr("http_equiv") := "X-UA-Compatible", content := "IE=edge"),
        meta(name := "viewport", content := "width=device-width, initial-scale-1"),

        meta(name := "description", content := ""),  // FIXME
        meta(name := "author", content := "Daid Orme; ShopSmart, LLC."),

        // Page title
        tag("title")("Data in Motion"),

        // Twitter bootstrap
        link(rel := "stylesheet",
             href := "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css",
             attr("integrity") := "sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7",
             attr("crossorigin") := "anonymous"),

        link(rel := "stylesheet",
             href := "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap-theme.min.css",
             attr("integrity") := "sha384-fLW2N01lMqjakBkx3l/M9EahuwpSfeNvV63J5ezn3uZzapT0u7EYsXMjQV+0En5r",
             attr("crossorigin") := "anonymous"),

        // Legacy browser compatibility
        comment("<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->"),
        conditionalComment("lt IE 9",
                           script(src := "https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"),
                           script(src := "https://oss.maxcdn.com/respond/1.4.2/respond.min.js"))

      ),

      body(attr("role") := "document",
             nav(class := "navbar navbar-inverse navbar-fixed-top"),
        contents,
           // Bootstrap.js
           script(src := "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js",
                  attr("integrity") := "sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS",
                  attr("crossorigin") := "anonymous"),

           // include the Scala.js scripts that sbt-play-scalajs has copied from the "client"
           // project to the Play public target folder
           scripts("scalajsclient")))
  }


  def scripts(projectName: String)(implicit env: play.Environment) =
    Seq(selectScript(projectName), launcher(projectName))

  def selectScript(projectName: String)(
      implicit env: play.Environment): TypedTag[String] = {
    if (env.isProd) {
      script(src := s"/assets/${projectName.toLowerCase}-opt.js",
             `type` := "text/javascript")
    } else {
      script(src := s"/assets/${projectName.toLowerCase}-fastopt.js",
             `type` := "text/javascript")
    }
  }

  def launcher(projectName: String) = {
    script(src := s"/assets/${projectName.toLowerCase}-launcher.js",
           `type` := "text/javascript")
  }



  def comment(comment:String) = raw("<!-- " +
                                      scalatags.Escaping.escape(comment, new StringBuilder) +
                                      " -->")

  def conditionalComment(condition:String, xs:Text.Modifier *) =
    Seq(raw("<!--[if " + scalatags.Escaping.escape(condition,new StringBuilder) + "]>")) ++
        xs.toSeq :+
        raw("<![endif]-->")



}

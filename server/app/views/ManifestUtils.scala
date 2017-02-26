package views

import java.io.File

import play.api.libs.json.{JsString, JsValue, Json}

import scala.io.Source
import scala.util.{Failure, Success, Try}

object ManifestUtils {
  def fromManifest(file: String): String = {
    val manifest: Try[JsValue] = Try(Json.parse(
      // TODO: Inject the contents of the manifest
      Source.fromFile(new File("public/manifest.json")).getLines.mkString
    ))
    println(file)
    println(manifest)
    manifest match {
      case Success(m) =>
        println(file + " -> " + (m \ file).get.as[String])
        (m \ file).get.as[String]
      case Failure(e) => "nope" // TODO: Better error handling
    }
  }
}

package controllers
import play.api.mvc._

class Application extends Controller {
  def index(route: String) = Action {
    // TODO: Return a 404 if the route isn't valid
    Ok(views.html.index())
  }
}

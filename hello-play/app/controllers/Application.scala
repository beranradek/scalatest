package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import views._
import com.etnetera.play.tfs._

/**
 * Controllers are in package controllers by naming conventions,
 * they extend play.api.mvc.Controller.
 * Controller method should return an Action which takes a Scala expression that evaluates to a Result
 */
object Application extends Controller with TfsTemplates {
    
  /**
  * Home page
  */
  def index = Action {
    Ok(template("hello.tfs"))
    // by naming convention index.scala.html template got compiled into views.html.index class 
    // (in target source dir), that extends BaseScalaTemplate.
    // We pass in helloForm variable/value (parameter expected by the template)
    // Ok(html.index(helloForm))
  }
  
//  /**
//   * Handles the form submission.
//   * Request contains all parameters in request.queryString, which returns a Map[String, Seq[String]].
//   */
//  def sayHello = Action { implicit request =>
//    helloForm.bindFromRequest.fold(
//      formWithErrors => BadRequest(html.index(formWithErrors)),
//      {case (name, repeat, color) => Ok(html.hello(name, repeat.toInt, color))}
//    )
//  }
  
  // DSL for form specification (play.api.data.Form)
  val helloForm = Form(
    // tuple is method in Forms object, returns Mapping[T]
    tuple(
      // two mandatory fields and one optional
      "name" -> nonEmptyText,
      "repeat" -> number(min = 1, max = 100),
      "color" -> optional(text)
    )
  )
  
}

package controllers

import play.api._
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import models.{Person,DB}
import play.api.libs.json.Json

class Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }
  
  def addPerson = Action { implicit request =>
    val person = formPersona.bindFromRequest.get
    Ok("DOJO Scala Ockham")
    Redirect(routes.Application.index)
  }

  val formPerson: Form[Person] = Form {
    mapping (
        "name" -> text,
        "age" -> number
    )(Person.apply)(Person.unapply)
  }
  
  def getPersons = Action {
    val persons = DB.query[Person].fetch
    Ok(Json.toJson(persons))
  }
  
  def getByAge(a:Int) = Action {
    val persons = DB.query[Person].fetch
    val (minors, adults) =  persons partition (_.age <18)
    if (a==1) Ok(Json.toJson(adults)) else Ok(Json.toJson(minors))
  }
  
  def savePerson = Action { implicit request =>
    DB.save(Person(name,age))
    Redirect(routes.Application.index)
  }

}

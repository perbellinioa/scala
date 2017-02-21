package controllers

import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._

/**
  * Created by Olivier Perbellini on 18/02/2017.
  */
class WeKanbanApplication extends Controller {

  case class UpdateStory(val number: Int, val phase: String)

  val storyForm = Form {
     mapping("number" -> number,
             "title" -> text,
             "phase" -> default(text, "ready"))(Story.apply)(Story.unapply)
   }

  val updateStoryForm = Form {
    mapping("number" -> number,
            "phase" -> text)(UpdateStory.apply)(UpdateStory.unapply)
                             }

  def cardForm = Action { implicit request =>
    val message = request.queryString.get("message")
    if (message.isDefined)
      Ok(views.html.createstory(message.get(0)))
    else
      Ok(views.html.createstory("Card Creation"))
  }

  def cardCreate = Action { implicit request =>
    val story = storyForm.bindFromRequest.get
    story.save match {
      case Right(message) => Redirect("/card", Map("message" -> Seq(message)))
      case Left(error) => NotAcceptable(error.getMessage)
    }
  }

  def board = Action { implicit request =>
    Ok(views.html.board())
                     }
  def cardMove = Action {implicit request =>
    val story = updateStoryForm.bindFromRequest.get
    val toUpdate = Story.findByNumber(story.number).getOrElse(null)
    if (toUpdate == null) {
      NotAcceptable("The Story to Update must have a valid number")
    }else {
    toUpdate.updatePhase(story.phase) match {
      case Right(message) => Ok(message)
      case Left(error) => NotAcceptable(error.getMessage)
    }
    }
  }
}
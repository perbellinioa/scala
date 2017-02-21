package controllers

import play.api.Play.current
import anorm._
import play.api.db.DB

import scala.xml.dtd.ValidationException

/**
  * Created by Olivier Perbellini on 20/02/2017.
  */
case class Story (val number: Int, val title: String, val phase: String) {

  private def phaseLimits = Map("ready" -> Some(3), "dev" -> Some(2), "test" -> Some(2), "deploy" -> None)


  private[this] def validate = {
    if (number == 0 || title.isEmpty) {
      throw new ValidationException("Both number and title are required")
    }
    if (Story.findByNumber(number).getOrElse(null) != null){
      throw new ValidationException("The story number is not unique")
    }
  }

  private[this] def validateLimit(phase: String) = {
    val size: Int = Story.findAllByPhase(phase).size
    if (size == phaseLimits(phase).getOrElse(-1)) {
      throw new ValidationException("You cannot exceed the limit set for the phase: " + phase)
    }
  }

  def save: Either[Throwable, String] = DB.withTransaction { implicit c =>
    try {
      validate
      SQL("INSERT INTO STORIES(id, title, phase) VALUES ({number}, {title}, {phase})")
        .on('number -> number,
            'title -> title,
            'phase -> phase)
        .execute();
      Right("Story created successfully")
    }catch {
      case exception: Throwable => exception.printStackTrace(); Left(exception)
    }
  }

  def updatePhase(phase: String): Either[Throwable, String] = DB.withTransaction { implicit c =>
    try {
      validateLimit(phase)
      SQL("UPDATE STORIES SET phase = {phase} WHERE id = {number}").on('phase -> phase, 'number -> number).execute();
      Right("Story " + number + " moved to phase: " + phase)
    } catch {
      case exception: Throwable => exception.printStackTrace(); Left(exception)
    }
  }

}

case object Story {

  def p: RowParser[Story] = Macro.parser[Story]("id", "title", "phase")


  def findAllByPhase(phase: String): Iterable[Story] = DB.withTransaction { implicit c =>
    SQL("SELECT * FROM STORIES WHERE phase = {phase}").on('phase -> phase).as(p.*)
  }

  def findByNumber(number: Int): Option[Story] = DB.withTransaction {implicit c =>
    SQL("SELECT * FROM STORIES WHERE id = {number}").on('number -> number).as(p.singleOpt)
  }
}
package pl.michalkowol.controller

import pl.michalkowol.common.json.jackson._
import pl.michalkowol.db.Person
import pl.michalkowol.repository.PeopleRepository
import play.api.mvc.{Action, Controller}

import scala.util.{Failure, Success}

// scalastyle:off public.methods.have.type

class People(peopleRepository: PeopleRepository) extends Controller {

  def all = Action {
    Ok(peopleRepository.all.sortBy(_.age).toJson)
  }

  def create = Action(parse.text) { personReq =>
    val personInput = personReq.body.fromJson[Person]
    personInput.flatMap(peopleRepository.create) match {
      case Success(person) => Ok(person.toJson)
      case Failure(ex) => BadRequest(ex.toJson)
    }
  }
}

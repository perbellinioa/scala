import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._
import play.api.test.Helpers._
import play.api.test._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class ApplicationSpec extends Specification {

  "Application" should {

    "send 404 on a bad request" in new WithApplication{
      val boum = route(FakeRequest(GET, "/boum")).get

      status(boum) must equalTo(NOT_FOUND)
      contentType(boum) must equalTo(Some("text/html"))
    }

    "render the index page" in new WithApplication{
      val home = route(FakeRequest(GET, "/")).get

      status(home) must equalTo(SEE_OTHER)
      headers(home) must equalTo(Map("Location" -> "/card"))
      redirectLocation(home) must equalTo(Some("/card"))
    }
  }
}

import models.Location
import org.scalatest.{FlatSpec, Matchers}

class LocationSpec extends FlatSpec with Matchers {

  "A board location" should "blow up with illegal values" in {
    val invalidLocations = List[Tuple2[Int,Int]]((0, 0), (0, 10), (10, 0), (10, 10))
    for ((rank, file) <- invalidLocations) {
      try {
        Location(rank, file)
        fail("Should have thrown an exception")
      } catch {
        case ex: IllegalArgumentException => // Success
      }
    }
  }

  it should "handle adding a delta" in {
    val loc: Location = (3, 5)
    val result = loc + (2, -1)
    result should contain (Location(5, 4))
  }

  it should "handle adding a delta that goes off the board" in {
    val start: Location = (5, 5)
    val deltas = Seq((-5, 0), (5, 0), (0, -5), (0, 5))
    deltas.foreach(d => withClue(d) { (start + d) shouldBe None})
  }
}

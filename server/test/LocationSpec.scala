import models.Location
import org.scalatest.FlatSpec

class LocationSpec extends FlatSpec {

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
}

import models._
import org.scalatest.{FlatSpec, Matchers}
import play.api.libs.json._
import models.JsonConverters._
import models.Player._

class JsonConvertersSpec extends FlatSpec with Matchers {
  behavior of "JsonConverters"

  it should "convert non-promotable pieces to JSON" in {
    val piece = King(PlayerOne)
    val expectedJson = Json.obj(
      "type" -> "king",
      "player" -> "PlayerOne"
    )

    Json.toJson(piece) shouldBe expectedJson
  }

  it should "convert promotable pieces to JSON" in {
    val lance = Lance(PlayerTwo)
    val lanceJson = Json.obj(
      "type" -> "lance",
      "player" -> "PlayerTwo",
      "promoted" -> false
    )

    Json.toJson(lance) shouldBe lanceJson

    val promotedLance = Lance(PlayerTwo, promoted=true)
    val promotedLanceJson = Json.obj(
      "type" -> "lance",
      "player" -> "PlayerTwo",
      "promoted" -> true
    )

    Json.toJson(promotedLance) shouldBe promotedLanceJson
  }

  it should "convert a board location to JSON" in {
    val location = Location(5, 3)
    val expectedValue = Json.obj(
      "rank" -> 5,
      "file" -> 3
    )

    Json.toJson(location) shouldBe expectedValue
  }

  it should "convert a board state to JSON" in {
    val promotedPawn = Pawn(PlayerTwo, promoted=true)
    val king = King(PlayerTwo)
    val pieces = Map[Location, Piece](
      Location(4, 2) -> promotedPawn,
      Location(5, 9) -> king
    )
    val board = ShogiBoard(pieces)

    val json = Json.toJson(board).as[JsArray]

    json.value.size shouldBe 2
    for(i <- 0 to 1) {
      json(i) match {
        case JsDefined(obj) => {
          // Other tests are checking the contents of these
          (obj \ "location") shouldBe a [JsDefined]
          (obj \ "piece") shouldBe a [JsDefined]
        }
        case JsUndefined() => fail()
      }
    }
  }

  it should "convert pieces in hand to JSON" in {
    val pih = PiecesInHand() + Pawn(PlayerOne) + Pawn(PlayerOne) + Lance(PlayerTwo)
    val json = Json.toJson(pih).as[JsArray]

    val expectedPawnJson = Json.obj(
      "piece" -> Json.obj(
        "type" -> "pawn",
        "player" -> "PlayerOne",
        "promoted" -> false
      ),
      "number" -> 2
    )
    val expectedLanceJson = Json.obj(
      "piece" -> Json.obj(
        "type" -> "lance",
        "player" -> "PlayerTwo",
        "promoted" -> false
      ),
      "number" -> 1
    )

    json.value.size shouldBe 2
    for (i <- 0 to 1) {
      json(i) match {
        case JsDefined(obj) => (obj \ "piece" \ "type").get match {
          case JsString("pawn") => obj should equal(expectedPawnJson)
          case JsString("lance") => obj should equal(expectedLanceJson)
          case JsString(t) => fail("unexpected piece type: " + t)
          case _ => fail()
        }
        case JsUndefined() => fail()
      }
    }
  }

  it should "convert empty pieces in hand to JSON" in {
    val pih = PiecesInHand()
    Json.toJson(pih) shouldBe Json.arr()
  }

  it should "convert game state to JSON" in {
    val gameState = GameState(PlayerTwo, ShogiBoard(Map()), PiecesInHand())
    val json = Json.toJson(gameState)

    (json \ "playerToMove").get.as[String] shouldEqual "PlayerTwo"
    (json \ "board") shouldBe a [JsDefined]
    (json \ "piecesInHand") shouldBe a [JsDefined]
  }
}
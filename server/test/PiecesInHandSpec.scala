import models.Player._
import models.{King, Pawn, PiecesInHand}
import org.scalatest.{FlatSpec, Matchers}

class PiecesInHandSpec extends FlatSpec with Matchers {
  behavior of "Pieces in hand"

  it should "have no pieces when empty" in {
    val p = PiecesInHand()
    p.hasInHand(Pawn(Black)) shouldBe false
    p.hasInHand(King(White)) shouldBe false
  }

  it should "add a single piece" in {
    val p = PiecesInHand() + Pawn(Black)
    p.hasInHand(Pawn(Black)) shouldBe true
  }

  it should "track multiple instances of the same piece" in {
    val p = PiecesInHand() + Pawn(Black) + Pawn(Black)
    p.hasInHand(Pawn(Black)) shouldBe true

    val p2 = p - Pawn(Black)
    p2.hasInHand(Pawn(Black)) shouldBe true

    val p3 = p2 - Pawn(Black)
    p3.hasInHand(Pawn(Black)) shouldBe false
  }

  it should "provide the pieces for only one player" in {
    val p = PiecesInHand() + Pawn(Black) + Pawn(White)
    val p1Pieces = p.piecesInHand(Black)

    p1Pieces.hasInHand(Pawn(Black)) shouldBe true
    p1Pieces.hasInHand(Pawn(White)) shouldBe false
  }

  it should "behave well when removing a piece that is not available" in {
    val p = PiecesInHand() + King(White)
    val removedTwice = p - King(White) - King(White)

    removedTwice.hasInHand(King(White)) shouldBe false
  }

  it should "treat removing the last piece as if it were never added" in {
    val empty = PiecesInHand()
    val removed = PiecesInHand() + King(Black) - King(Black)

    empty shouldEqual removed
  }

  it should "explode when given non-positive piece counts" in {
    try {
      PiecesInHand(Map(Pawn(Black) -> -1))
      fail("Should have thrown an exception")
    } catch {
      case _: IllegalArgumentException => // Pass
    }

    try {
      PiecesInHand(Map(Pawn(Black) -> 0))
    } catch {
      case _: IllegalArgumentException => // Pass
    }
  }

  it should "ignore the promoted status of added pieces" in {
    val p = PiecesInHand() + Pawn(Black) + Pawn(Black, true)
    p.numberInHand(Pawn(Black)) shouldBe 2
    p.numberInHand(Pawn(Black, true)) shouldBe 2
  }

  it should "ignore the promoted status when checking availability" in {
    val p = PiecesInHand() + Pawn(Black)
    p.numberInHand(Pawn(Black, true)) shouldBe 1
  }
}

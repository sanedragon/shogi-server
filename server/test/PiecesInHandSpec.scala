import models.Player._
import models.{King, Pawn, PiecesInHand}
import org.scalatest.{FlatSpec, Matchers}

class PiecesInHandSpec extends FlatSpec with Matchers {
  behavior of "Pieces in hand"

  it should "have no pieces when empty" in {
    val p = PiecesInHand()
    p.hasInHand(Pawn(PlayerOne)) shouldBe false
    p.hasInHand(King(PlayerTwo)) shouldBe false
  }

  it should "add a single piece" in {
    val p = PiecesInHand() + Pawn(PlayerOne)
    p.hasInHand(Pawn(PlayerOne)) shouldBe true
  }

  it should "track multiple instances of the same piece" in {
    val p = PiecesInHand() + Pawn(PlayerOne) + Pawn(PlayerOne)
    p.hasInHand(Pawn(PlayerOne)) shouldBe true

    val p2 = p - Pawn(PlayerOne)
    p2.hasInHand(Pawn(PlayerOne)) shouldBe true

    val p3 = p2 - Pawn(PlayerOne)
    p3.hasInHand(Pawn(PlayerOne)) shouldBe false
  }

  it should "provide the pieces for only one player" in {
    val p = PiecesInHand() + Pawn(PlayerOne) + Pawn(PlayerTwo)
    val p1Pieces = p.piecesInHand(PlayerOne)

    p1Pieces.hasInHand(Pawn(PlayerOne)) shouldBe true
    p1Pieces.hasInHand(Pawn(PlayerTwo)) shouldBe false
  }

  it should "behave well when removing a piece that is not available" in {
    val p = PiecesInHand() + King(PlayerTwo)
    val removedTwice = p - King(PlayerTwo) - King(PlayerTwo)

    removedTwice.hasInHand(King(PlayerTwo)) shouldBe false
  }

  it should "treat removing the last piece as if it were never added" in {
    val empty = PiecesInHand()
    val removed = PiecesInHand() + King(PlayerOne) - King(PlayerOne)

    empty shouldEqual removed
  }

  it should "explode when given non-positive piece counts" in {
    try {
      PiecesInHand(Map(Pawn(PlayerOne) -> -1))
      fail("Should have thrown an exception")
    } catch {
      case _: IllegalArgumentException => // Pass
    }

    try {
      PiecesInHand(Map(Pawn(PlayerOne) -> 0))
    } catch {
      case _: IllegalArgumentException => // Pass
    }
  }

  it should "ignore the promoted status of added pieces" in {
    val p = PiecesInHand() + Pawn(PlayerOne) + Pawn(PlayerOne, true)
    p.numberInHand(Pawn(PlayerOne)) shouldBe 2
    p.numberInHand(Pawn(PlayerOne, true)) shouldBe 2
  }

  it should "ignore the promoted status when checking availability" in {
    val p = PiecesInHand() + Pawn(PlayerOne)
    p.numberInHand(Pawn(PlayerOne, true)) shouldBe 1
  }
}

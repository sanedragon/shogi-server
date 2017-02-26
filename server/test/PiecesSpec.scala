import models.Player._
import models._
import org.scalatest.{FlatSpec, Matchers}

class PiecesSpec extends FlatSpec with Matchers {
  import Piece.pieceName

  behavior of "Pieces"

  it should "convert pieces to names" in {
    pieceName(Pawn(PlayerOne)) shouldBe "pawn"
    pieceName(Bishop(PlayerOne)) shouldBe "bishop"
    pieceName(Rook(PlayerOne)) shouldBe "rook"
    pieceName(Lance(PlayerOne)) shouldBe "lance"
    pieceName(Knight(PlayerOne)) shouldBe "knight"
    pieceName(Silver(PlayerOne)) shouldBe "silver"
    pieceName(Gold(PlayerOne)) shouldBe "gold"
    pieceName(King(PlayerOne)) shouldBe "king"
  }

  it should "use the same name for promoted and non-promoted pieces" in {
    pieceName(Pawn(PlayerOne)) should equal (pieceName(Pawn(PlayerOne, true)))
    pieceName(Bishop(PlayerOne)) should equal (pieceName(Bishop(PlayerOne, true)))
    pieceName(Rook(PlayerOne)) should equal (pieceName(Rook(PlayerOne, true)))
    pieceName(Lance(PlayerOne)) should equal (pieceName(Lance(PlayerOne, true)))
    pieceName(Knight(PlayerOne)) should equal (pieceName(Knight(PlayerOne, true)))
    pieceName(Silver(PlayerOne)) should equal (pieceName(Silver(PlayerOne, true)))
  }
}

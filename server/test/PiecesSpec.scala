import models.Player._
import models._
import org.scalatest.{FlatSpec, Matchers}

class PiecesSpec extends FlatSpec with Matchers {
  import Piece.pieceName

  behavior of "Pieces"

  it should "convert pieces to names" in {
    pieceName(Pawn(Black)) shouldBe "pawn"
    pieceName(Bishop(Black)) shouldBe "bishop"
    pieceName(Rook(Black)) shouldBe "rook"
    pieceName(Lance(Black)) shouldBe "lance"
    pieceName(Knight(Black)) shouldBe "knight"
    pieceName(Silver(Black)) shouldBe "silver"
    pieceName(Gold(Black)) shouldBe "gold"
    pieceName(King(Black)) shouldBe "king"
  }

  it should "use the same name for promoted and non-promoted pieces" in {
    pieceName(Pawn(Black)) should equal (pieceName(Pawn(Black, true)))
    pieceName(Bishop(Black)) should equal (pieceName(Bishop(Black, true)))
    pieceName(Rook(Black)) should equal (pieceName(Rook(Black, true)))
    pieceName(Lance(Black)) should equal (pieceName(Lance(Black, true)))
    pieceName(Knight(Black)) should equal (pieceName(Knight(Black, true)))
    pieceName(Silver(Black)) should equal (pieceName(Silver(Black, true)))
  }
}

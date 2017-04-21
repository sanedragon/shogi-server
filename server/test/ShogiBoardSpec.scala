import models._
import org.scalatest.{FlatSpec, Matchers}
import models.Player._

class ShogiBoardSpec extends FlatSpec with Matchers {
  behavior of "A Shogi board"

  it should "start with all the pieces in the correct places" in {
    val board = ShogiBoard.initialSetup()

    board.pieceAt (1, 1) shouldEqual Some(Lance(White))
    board.pieceAt (1, 2) shouldEqual Some(Knight(White))
    board.pieceAt (1, 3) shouldEqual Some(Silver(White))
    board.pieceAt (1, 4) shouldEqual Some(Gold(White))
    board.pieceAt (1, 5) shouldEqual Some(King(White))
    board.pieceAt (1, 6) shouldEqual Some(Gold(White))
    board.pieceAt (1, 7) shouldEqual Some(Silver(White))
    board.pieceAt (1, 8) shouldEqual Some(Knight(White))
    board.pieceAt (1, 9) shouldEqual Some(Lance(White))
    board.pieceAt (2, 2) shouldEqual Some(Bishop(White))
    board.pieceAt (2, 8) shouldEqual Some(Rook(White))

    board.pieceAt (9, 1) shouldEqual Some(Lance(Black))
    board.pieceAt (9, 2) shouldEqual Some(Knight(Black))
    board.pieceAt (9, 3) shouldEqual Some(Silver(Black))
    board.pieceAt (9, 4) shouldEqual Some(Gold(Black))
    board.pieceAt (9, 5) shouldEqual Some(King(Black))
    board.pieceAt (9, 6) shouldEqual Some(Gold(Black))
    board.pieceAt (9, 7) shouldEqual Some(Silver(Black))
    board.pieceAt (9, 8) shouldEqual Some(Knight(Black))
    board.pieceAt (9, 9) shouldEqual Some(Lance(Black))
    board.pieceAt (8, 2) shouldEqual Some(Rook(Black))
    board.pieceAt (8, 8) shouldEqual Some(Bishop(Black))

    for (file <- 1 to 9) {
      board.pieceAt (3, file) shouldEqual Some(Pawn(White))
      board.pieceAt (7, file) shouldEqual Some(Pawn(Black))
    }

    // Check the empty spaces around the rooks and bishops
    for (file <- 1 to 9; if file != 2 && file != 8) {
      board.pieceAt (2, file) shouldBe None
      board.pieceAt (8, file) shouldBe None
    }

    // Check the empty spaces in the middle of the board
    for (rank <- 4 to 6; file <- 1 to 9) {
      board.pieceAt (rank, file) shouldBe None
    }
  }

  it should "be creatable with no pieces" in {
    val board = ShogiBoard(Map())

    for (r <- 1 to 9; f <- 1 to 9) {
      board.pieceAt (r, f) shouldBe None
    }
  }

  it should "be creatable with some pieces" in {
    val onTheBoard = Map[Location,Piece](Location(5,5) -> King(Black))
    val board = ShogiBoard(onTheBoard)

    board.pieceAt(5, 5) shouldEqual Some(King(Black))
    for (r <- 1 to 9; f <- 1 to 9; if r != 5 && f != 5) {
      board.pieceAt (r, f) shouldBe None
    }
  }
}

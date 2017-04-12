import fastparse.core.Parsed
import models._
import org.scalatest.{FunSpec, Matchers}

class SfenParserSpec extends FunSpec with Matchers {
  import models.SfenParser._
  import models.Player._

  describe("SfenParser") {
    describe("when parsing a game state") {
      it("should build an empty game") {
        val emptySfen = "9/9/9/9/9/9/9/9/9 B -"
        val Parsed.Success(state, _) = gameState.parse(emptySfen)
        state shouldEqual GameState(PlayerOne, ShogiBoard(Map()), PiecesInHand())
      }

      it("should build the initial state of a game") {
        val initialSfen = "lnsgkgsnl/1r5b1/ppppppppp/9/9/9/PPPPPPPPP/1B5R1/LNSGKGSNL B -"
        val Parsed.Success(state, _) = gameState.parse(initialSfen)
        state shouldEqual GameState(PlayerOne, ShogiBoard.initialSetup(), PiecesInHand())
      }

      it("should build a mid-game state") {
        val sfen = "ln1gk2nl/1r4g2/ps2pp1pp/3p4+b/2p6/2P4R1/P1NPP3P/1SGK4+B/L6NL B s4pS2P"
        val Parsed.Success(state, _) = gameState.parse(sfen)

        state.playerToMove shouldEqual PlayerOne

        state.board.pieces.size shouldEqual 31
        state.board.pieceAt(1, 9) should contain(Lance(PlayerTwo))
        state.board.pieceAt(1, 8) should contain(Knight(PlayerTwo))
        state.board.pieceAt(1, 6) should contain(Gold(PlayerTwo))
        state.board.pieceAt(1, 5) should contain(King(PlayerTwo))
        state.board.pieceAt(1, 2) should contain(Knight(PlayerTwo))
        state.board.pieceAt(1, 1) should contain(Lance(PlayerTwo))
        state.board.pieceAt(2, 8) should contain(Rook(PlayerTwo))
        state.board.pieceAt(2, 3) should contain(Gold(PlayerTwo))
        state.board.pieceAt(3, 9) should contain(Pawn(PlayerTwo))
        state.board.pieceAt(3, 8) should contain(Silver(PlayerTwo))
        state.board.pieceAt(3, 5) should contain(Pawn(PlayerTwo))
        state.board.pieceAt(3, 4) should contain(Pawn(PlayerTwo))
        state.board.pieceAt(3, 2) should contain(Pawn(PlayerTwo))
        state.board.pieceAt(3, 1) should contain(Pawn(PlayerTwo))
        state.board.pieceAt(4, 6) should contain(Pawn(PlayerTwo))
        state.board.pieceAt(4, 1) should contain(Bishop(PlayerTwo, promoted = true))
        state.board.pieceAt(5, 7) should contain(Pawn(PlayerTwo))
        state.board.pieceAt(6, 7) should contain(Pawn(PlayerOne))
        state.board.pieceAt(6, 2) should contain(Rook(PlayerOne))
        state.board.pieceAt(7, 9) should contain(Pawn(PlayerOne))
        state.board.pieceAt(7, 7) should contain(Knight(PlayerOne))
        state.board.pieceAt(7, 6) should contain(Pawn(PlayerOne))
        state.board.pieceAt(7, 5) should contain(Pawn(PlayerOne))
        state.board.pieceAt(7, 1) should contain(Pawn(PlayerOne))
        state.board.pieceAt(8, 8) should contain(Silver(PlayerOne))
        state.board.pieceAt(8, 7) should contain(Gold(PlayerOne))
        state.board.pieceAt(8, 6) should contain(King(PlayerOne))
        state.board.pieceAt(8, 1) should contain(Bishop(PlayerOne, promoted = true))
        state.board.pieceAt(9, 9) should contain(Lance(PlayerOne))
        state.board.pieceAt(9, 2) should contain(Knight(PlayerOne))
        state.board.pieceAt(9, 1) should contain(Lance(PlayerOne))

        val expected = Map[Piece, Int](Silver(PlayerTwo) -> 1, Pawn(PlayerTwo) -> 4, Silver(PlayerOne) -> 1,
          Pawn(PlayerOne) -> 2)
        state.piecesInHand shouldEqual PiecesInHand(expected)
      }
    }

    describe("when parsing the board state") {
      it("should build an initial state") {
        val initialSfen = "lnsgkgsnl/1r5b1/ppppppppp/9/9/9/PPPPPPPPP/1B5R1/LNSGKGSNL"
        val Parsed.Success(state, _) = boardState.parse(initialSfen)
        state shouldEqual ShogiBoard.initialSetup()
      }

      it("should build an empty state") {
        val emptySfen = "9/9/9/9/9/9/9/9/9"
        val Parsed.Success(state, _) = boardState.parse(emptySfen)
        state shouldEqual ShogiBoard(Map())
      }
    }

    describe("when parsing the player to move") {
      it("should use PlayerOne for black") {
        val Parsed.Success(result, _) = playerToMove.parse("B")
        result shouldEqual PlayerOne
      }

      it("should use PlayerTwo for white") {
        val Parsed.Success(result, _) = playerToMove.parse("W")
        result shouldEqual PlayerTwo
      }
    }

    describe("when parsing the pieces in hand") {
      it("should handle an empty list") {
        val Parsed.Success(result, _) = piecesInHand.parse("-")
        result shouldEqual PiecesInHand()
      }

      it("should handle single pieces") {
        val Parsed.Success(result, _) = piecesInHand.parse("pBnR")
        result shouldEqual PiecesInHand() + Pawn(PlayerTwo) + Bishop(PlayerOne) +
          Knight(PlayerTwo) + Rook(PlayerOne)
      }

      it("should handle multiple pieces") {
        val Parsed.Success(result, _) = piecesInHand.parse("2S4p")
        result shouldEqual PiecesInHand() + Silver(PlayerOne) + Silver(PlayerOne) +
          Pawn(PlayerTwo) + Pawn(PlayerTwo) + Pawn(PlayerTwo) + Pawn(PlayerTwo)
      }
    }

    describe("when parsing the move number") {
      it("should read an integer") {
        val Parsed.Success(result, _) = moveNumber.parse("123")
        result shouldEqual 123
      }
    }
  }
}

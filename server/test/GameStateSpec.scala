import models._
import models.Player._
import org.scalatest.{FlatSpec, FunSpec, Matchers}

class GameStateSpec extends FunSpec with Matchers {

  describe("GameState") {
    describe("when building a state from SFEN") {
      it("should build the initial state") {
        val initialSfen = "lnsgkgsnl/1r5b1/ppppppppp/9/9/9/PPPPPPPPP/1B5R1/LNSGKGSNL B -"
        val state = GameState.fromSfen(initialSfen);
        state shouldEqual GameState(PlayerOne, ShogiBoard.initialSetup(), PiecesInHand())
      }

      it("should explode on bad input") {
        an [Exception] should be thrownBy GameState.fromSfen("Bad input")
      }
    }
  }
}

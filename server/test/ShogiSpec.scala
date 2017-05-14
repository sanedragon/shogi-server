import fastparse.core.Parsed.Success
import models._
import org.scalatest.{FunSpec, Matchers}

import scala.util.Failure

class ShogiSpec extends FunSpec with Matchers {
  def initialState(player: Player.Value): String = "lnsgkgsnl/1r5b1/ppppppppp/9/9/9/PPPPPPPPP/1B5R1/LNSGKGSNL " +
    (if (player == Player.Black) "B" else "W") + " -"

  describe("Shogi game logic") {

    describe("when moving a piece on the board") {

      it("prevents the wrong player from moving") {
        testIllegalMoves(
          ("wrong player", initialState(Player.Black), BoardMove((3, 9), (4, 9)))
        )
      }

      it("prevents moving from an empty square") {
        testIllegalMoves(
          ("no piece", initialState(Player.Black), BoardMove((5, 5), (5, 6)))
        )
      }

      it("prevents moving to a square occupied by a friendly piece") {
        testEntireBoard("9/9/9/9/9/5P3/5P3/9/9 B -", (7, 4), Set())
        testEntireBoard("9/9/9/9/9/6p2/6p2/9/9 W -", (6, 3), Set())
        testEntireBoard(initialState(Player.Black), (9, 5), Set((8, 6), (8, 5), (8, 4)))
        testEntireBoard(initialState(Player.White), (1, 5), Set((2, 6), (2, 5), (2, 4)))
        testEntireBoard(initialState(Player.Black), (9, 1), Set((8, 1)))
        testEntireBoard(initialState(Player.White), (1, 1), Set((2, 1)))
      }

      it("captures an opposing piece") {
        testLegalMoves(
          ("black captures non-promoted", "9/9/9/4p4/4P4/9/9/9/9 B -", BoardMove((5, 5), (4, 5)), "9/9/9/4P4/9/9/9/9/9 W p"),
          ("black captures promoted", "9/9/9/4+p4/4P4/9/9/9/9 B -", BoardMove((5, 5), (4, 5)), "9/9/9/4P4/9/9/9/9/9 W p")
        )
      }

      describe("a pawn") {

        it("moves legally") {
          testEntireBoard("9/9/5p3/9/9/9/6P2/9/9 B -", (7, 3), Set((6, 3)))
          testEntireBoard("9/9/5p3/9/9/9/6P2/9/9 W -", (3, 4), Set((4, 4)))
        }

        it("can promote by moving into the zone") {
          testEntireBoard("9/9/9/4P4/9/9/9/9/9 B -", (4, 5), Set((3, 5)), promote = true)
          testEntireBoard("9/9/9/9/9/4p4/9/9/9 W -", (6, 5), Set((7, 5)), promote = true)
        }

        it("can promote by moving within the zone") {
          testEntireBoard("9/9/4P4/9/9/9/9/9/9 B -", (3, 5), Set((2, 5)), promote = true)
          testEntireBoard("9/9/9/9/9/9/4p4/9/9 W -", (7, 5), Set((8, 5)), promote = true)
        }

        it("can promote by moving to the back rank") {
          testEntireBoard("9/3P5/9/9/9/9/9/9/9 B -", (2, 6), Set((1, 6)), promote = true)
          testEntireBoard("9/9/9/9/9/9/9/4p4/9 W -", (8, 5), Set((9, 5)), promote = true)
        }

        it("cannot promote outside the zone") {
          testIllegalMoves(
            ("black", "9/9/9/9/9/9/6P2/9/9 B -", BoardMove((7, 3), (6, 3), promote = true)),
            ("white", "9/9/5p3/9/9/9/9/9/9 W -", BoardMove((3, 4), (4, 4), promote = true))
          )
        }

        it("cannot move onto the last rank without promoting") {
          testIllegalMoves(
            ("black", "9/4P4/9/9/9/9/9/9/9 B -", BoardMove((2, 5), (1, 5))),
            ("white", "9/9/9/9/9/9/9/4p4/9 W -", BoardMove((8, 5), (9, 5)))
          )
        }
      }

      describe("a promoted pawn") {
        
        it("moves as a gold") {
          testGoldMoves("+p")
        }

        it("moves correctly at the edge of the board") {
          testEntireBoard("9/9/9/9/9/9/9/8+P/9 B -", (8, 1), Set((7, 1), (7, 2), (8, 2), (9, 1)))
        }

        it("cannot promote again") {
          testIllegalMoves(
            ("black moves into the zone", "9/9/9/4+P4/9/9/9/9/9 B -", BoardMove((4, 5), (3, 5), promote = true)),
            ("black moves inside the zone", "9/9/4+P4/9/9/9/9/9/9 B -", BoardMove((3, 5), (2, 5), promote = true)),
            ("white moves into the zone", "9/9/9/9/9/4+p4/9/9/9 W -", BoardMove((6, 5), (7, 5), promote = true)),
            ("white moves inside the zone", "9/9/9/9/9/9/9/4+p4/9 W -", BoardMove((8, 5), (8, 4), promote = true))
          )
        }
      }

      describe("a knight") {

        it("moves legally") {
          testEntireBoard("9/9/9/9/9/9/2N6/9/9 B -", (7, 7), Set((5, 8), (5, 6)))
          testEntireBoard("9/9/3n5/9/9/9/9/9/9 W -", (3, 6), Set((5, 7), (5, 5)))
        }

        it("moves correctly at the edge of the board") {
          testEntireBoard("9/9/9/9/9/8N/9/9/9 B -", (6, 1), Set((4, 2)))
          testEntireBoard("9/9/9/n8/9/9/9/9/9 W -", (4, 9), Set((6, 8)))
        }

        it("can promote by moving into the zone") {
          testEntireBoard("9/9/9/9/2N6/9/9/9/9 B -", (5, 7), Set((3, 6), (3, 8)), promote = true)
          testEntireBoard("9/9/9/9/6n2/9/9/9/9 W -", (5, 3), Set((7, 4), (7, 2)), promote = true)
        }

        it("cannot promote outside the zone") {
          testIllegalMoves(
            ("black", "9/9/9/9/9/9/2N6/9/9 B -", BoardMove((7, 7), (5, 8), promote = true)),
            ("white", "9/9/3n5/9/9/9/9/9/9 W -", BoardMove((3, 6), (5, 7), promote = true))
          )
        }

        it("cannot move onto the last two ranks without promoting") {
          testIllegalMoves(
            ("black to back rank", "9/9/6N3/3N5/9/9/9/9/9 B -", BoardMove((3, 3), (1, 2))),
            ("black to second-to-back rank", "9/9/6N3/3N5/9/9/9/9/9 B -", BoardMove((4, 6), (2, 5))),
            ("white to back rank", "9/9/9/9/9/1n7/5n2/9/9 W -", BoardMove((7, 3), (9, 4))),
            ("white to second-to-back rank", "9/9/9/9/9/1n7/5n2/9/9 W -", BoardMove((6, 8), (8, 9)))
          )
        }
      }

      describe("a promoted knight") {

        it("moves as a gold") {
          testGoldMoves("+n")
        }
      }

      describe("a silver") {

        it("moves legally") {
          val board = "9/9/8s/1s7/9/9/6S2/9/2S6"
          testEntireBoard(board + " B -", (9, 7), Set((8, 8), (8, 7), (8, 6)))
          testEntireBoard(board + " B -", (7, 3), Set((6, 4), (6, 3), (6, 2), (8, 4), (8, 2)))
          testEntireBoard(board + " W -", (4, 8), Set((5, 9), (5, 8), (5, 7), (3, 9), (3, 7)))
          testEntireBoard(board + " W -", (3, 1), Set((4, 2), (4, 1), (2, 2)))
        }

        it("can promote by moving into the zone") {
          val board = "9/9/9/5S3/9/1s7/9/9/9"
          testEntireBoard(board + " B -", (4, 4), Set((3, 5), (3, 4), (3, 3)), promote = true)
          testEntireBoard(board + " W -", (6, 8), Set((7, 9), (7, 8), (7, 7)), promote = true)
        }

        it("can promote by moving within the zone") {
          val board = "9/4S4/9/9/9/9/9/6s2/9"
          testEntireBoard(board + " B -", (2, 5), Set((1, 6), (1, 5), (1, 4), (3, 6), (3, 4)), promote = true)
          testEntireBoard(board + " W -", (8, 3), Set((9, 4), (9, 3), (9, 2), (7, 4), (7, 2)), promote = true)
        }

        it("can promote by moving out of the zone") {
          testLegalMoves(
            ("black", "9/9/1S7/9/9/9/5s3/9/9 B -", BoardMove((3, 8), (4, 7), promote = true), "9/9/9/2+S6/9/9/5s3/9/9 W -"),
            ("white", "9/9/1S7/9/9/9/5s3/9/9 W -", BoardMove((7, 4), (6, 3), promote = true), "9/9/1S7/9/9/6+s2/9/9/9 B -")
          )
        }
      }

      describe("a promoted silver") {

        it("moves as a gold") {
          testGoldMoves("+s")
        }

        it("cannot promote again") {
          val board = "9/4+S4/9/9/9/9/9/6+s2/9"
          testEntireBoard(board + " B -", (2, 5), Set(), promote = true)
          testEntireBoard(board + " W -", (8, 3), Set(), promote = true)
        }
      }

      describe("a gold") {

        it("moves as (duh) a gold") {
          testGoldMoves("g")
        }

        it("cannot promote") {
          testEntireBoard("9/4G4/9/9/9/9/9/7g1/9 B -", (2, 5), Set(), promote = true)
          testEntireBoard("9/4G4/9/9/9/9/9/7g1/9 W -", (8, 2), Set(), promote = true)
        }
      }

      describe("a king") {

        it("moves legally") {
          testEntireBoard("9/4k4/9/9/9/9/9/4K4/9 B -", (8, 5), Set((7, 4), (7, 5), (7, 6), (8, 4), (8, 6), (9, 4),
            (9, 5), (9, 6)))
          testEntireBoard("9/4k4/9/9/9/9/9/4K4/9 W -", (2, 5), Set((1, 4), (1, 5), (1, 6), (2, 4), (2, 6), (3, 4),
            (3, 5), (3, 6)))
        }

        it("cannot promote") {
          testEntireBoard("9/4K4/9/9/9/9/9/4k4/9 B -", (2, 5), Set(), promote = true)
          testEntireBoard("9/4K4/9/9/9/9/9/4k4/9 W -", (8, 5), Set(), promote = true)
        }
      }

      describe("a lance") {

        it("moves legally") {
          testEntireBoard("l8/9/9/9/9/9/9/9/8L B -", (9, 1), Set((8, 1), (7, 1), (6, 1), (5, 1), (4, 1), (3, 1), (2, 1)))
          testEntireBoard("l8/9/9/9/9/9/9/9/8L W -", (1, 9), Set((2, 9), (3, 9), (4, 9), (5, 9), (6, 9), (7, 9), (8, 9)))
          testEntireBoard("9/9/9/9/4L4/9/9/9/9 B -", (5, 5), Set((4, 5), (3, 5), (2, 5)))
        }

        it("can promote by moving within the zone") {
          testLegalMoves(
            ("black", "9/9/5L3/9/9/9/9/9/9 B -", BoardMove((3, 4), (2, 4), promote = true), "9/5+L3/9/9/9/9/9/9/9 W -"),
            ("white", "9/9/9/9/9/9/2l6/9/9 W -", BoardMove((7, 7), (8, 7), promote = true), "9/9/9/9/9/9/9/2+l6/9 B -")
          )
        }

        it("can promote by moving into the zone") {
          testEntireBoard("l8/9/9/9/9/9/9/9/8L B -", (9, 1), Set((3, 1), (2, 1), (1, 1)), promote = true)
          testEntireBoard("l8/9/9/9/9/9/9/9/8L W -", (1, 9), Set((7, 9), (8, 9), (9, 9)), promote = true)
        }

        it("cannot move to the last rank without promoting") {
          testIllegalMoves(
            ("black", "l8/9/9/9/9/9/9/9/8L B -", BoardMove((9, 1), (1, 1))),
            ("white", "l8/9/9/9/9/9/9/9/8L W -", BoardMove((1, 9), (9, 9)))
          )
        }
      }

      describe("a promoted lance") {

        it("moves like a gold") {
          testGoldMoves("+l")
        }

        it("cannot promote again") {
          testEntireBoard("9/9/2+L6/9/9/9/9/9/9 B -", (3, 7), Set(), promote = true)
          testEntireBoard("9/9/9/9/9/9/9/7+l1/9 W -", (8, 2), Set(), promote = true)
        }
      }

      describe("a rook") {

        it("moves legally") {
          testEntireBoard("9/9/2R6/9/9/9/9/9/9 B -", (3, 7), Set((1, 7), (2, 7), (4, 7), (5, 7), (6, 7), (7, 7), (8, 7),
            (9, 7), (3, 9), (3, 8), (3, 6), (3, 5), (3, 4), (3, 3), (3, 2), (3, 1)))
          testEntireBoard("9/9/9/9/9/9/9/7r1/9 W -", (8, 2), Set((1, 2), (2, 2), (3, 2), (4, 2), (5, 2), (6, 2), (7, 2),
            (9, 2), (8, 9), (8, 8), (8, 7), (8, 6), (8, 5), (8, 4), (8, 3), (8, 1)))
        }

        it("can promote by moving within or out of the zone") {
          testEntireBoard("9/9/2R6/9/9/9/9/9/9 B -", (3, 7), Set((1, 7), (2, 7), (4, 7), (5, 7), (6, 7), (7, 7), (8, 7),
            (9, 7), (3, 9), (3, 8), (3, 6), (3, 5), (3, 4), (3, 3), (3, 2), (3, 1)), promote = true)
          testEntireBoard("9/9/9/9/9/9/9/7r1/9 W -", (8, 2), Set((1, 2), (2, 2), (3, 2), (4, 2), (5, 2), (6, 2), (7, 2),
            (9, 2), (8, 9), (8, 8), (8, 7), (8, 6), (8, 5), (8, 4), (8, 3), (8, 1)), promote = true)
        }

        it("can promote by moving into the zone") {
          testEntireBoard("9/9/9/9/9/9/9/7R1/9 B -", (8, 2), Set((1, 2), (2, 2), (3, 2)), promote = true)
          testEntireBoard("9/9/2r6/9/9/9/9/9/9 W -", (3, 7), Set((7, 7), (8, 7), (9, 7)), promote = true)
        }
      }

      describe("a promoted rook") {

        it("moves legally") {
          testEntireBoard("9/9/2+R6/9/9/9/9/9/9 B -", (3, 7), Set((1, 7), (2, 7), (4, 7), (5, 7), (6, 7), (7, 7), (8, 7),
            (9, 7), (3, 9), (3, 8), (3, 6), (3, 5), (3, 4), (3, 3), (3, 2), (3, 1), (2, 8), (2, 6), (4, 8), (4, 6)))
          testEntireBoard("9/9/9/9/9/9/9/7+r1/9 W -", (8, 2), Set((1, 2), (2, 2), (3, 2), (4, 2), (5, 2), (6, 2), (7, 2),
            (9, 2), (8, 9), (8, 8), (8, 7), (8, 6), (8, 5), (8, 4), (8, 3), (8, 1), (7, 3), (7, 1), (9, 3), (9, 1)))
        }

        it("cannot promote again") {
          testEntireBoard("9/9/2+R6/9/9/9/9/9/9 B -", (3, 7), Set(), promote = true)
          testEntireBoard("9/9/9/9/9/9/9/7+r1/9 W -", (8, 2), Set(), promote = true)
        }
      }

      describe("a bishop") {

        it("moves legally") {
          testEntireBoard("9/9/9/9/9/9/2B6/9/9 B -", (7, 7), Set((6, 8), (5, 9), (6, 6), (5, 5), (4, 4), (3, 3), (2, 2),
            (1, 1), (8, 6), (9, 5), (8, 8), (9, 9)))
          testEntireBoard("9/9/3b5/9/9/9/9/9/9 W -", (3, 6), Set((2, 7), (1, 8), (2, 5), (1, 4), (4, 7), (5, 8), (6, 9),
            (4, 5), (5, 4), (6, 3), (7, 2), (8, 1)))
        }

        it("can promote by moving within or out of the zone") {
          testEntireBoard("9/9/3B5/9/9/9/9/9/9 B -", (3, 6), Set((2, 7), (1, 8), (2, 5), (1, 4), (4, 7), (5, 8), (6, 9),
            (4, 5), (5, 4), (6, 3), (7, 2), (8, 1)), promote = true)
          testEntireBoard("9/9/9/9/9/9/2b6/9/9 W -", (7, 7), Set((6, 8), (5, 9), (6, 6), (5, 5), (4, 4), (3, 3), (2, 2),
            (1, 1), (8, 6), (9, 5), (8, 8), (9, 9)), promote = true)
        }

        it("can promote by moving into the zone") {
          testEntireBoard("9/9/9/9/9/9/2B6/9/9 B -", (7, 7), Set((3, 3), (2, 2), (1, 1)), promote = true)
          testEntireBoard("9/9/3b5/9/9/9/9/9/9 W -", (3, 6), Set((7, 2), (8, 1)), promote = true)
        }
      }

      describe("a promoted bishop") {

        it("moves legally") {
          testEntireBoard("9/9/9/9/9/9/2+B6/9/9 B -", (7, 7), Set((6, 8), (5, 9), (6, 6), (5, 5), (4, 4), (3, 3), (2, 2),
            (1, 1), (8, 6), (9, 5), (8, 8), (9, 9), (6, 7), (7, 6), (8, 7), (7, 8)))
          testEntireBoard("9/9/3+b5/9/9/9/9/9/9 W -", (3, 6), Set((2, 7), (1, 8), (2, 5), (1, 4), (4, 7), (5, 8), (6, 9),
            (4, 5), (5, 4), (6, 3), (7, 2), (8, 1), (2, 6), (3, 5), (4, 6), (3, 7)))
        }

        it("cannot promote again") {
          testEntireBoard("9/9/3+B5/9/9/9/9/9/9 B -", (3, 6), Set(), promote = true)
          testEntireBoard("9/9/9/9/9/9/2+b6/9/9 W -", (7, 7), Set(), promote = true)
        }
      }
    }

    describe("when dropping a piece on the board") {

      it("can drop onto an empty space") {
        testLegalMoves(
          ("black", "9/9/9/9/9/9/9/9/9 B G", Drop(Gold(Player.Black), (1, 1)), "8G/9/9/9/9/9/9/9/9 W -"),
          ("white", "9/9/9/9/9/9/9/9/9 W g", Drop(Gold(Player.White), (5, 5)), "9/9/9/9/4g4/9/9/9/9 B -")
        )
      }

      it("cannot drop into an occupied space") {
        testIllegalMoves(
          ("black", "8l/9/9/9/9/9/9/9/9 B G", Drop(Gold(Player.Black), (1, 1))),
          ("white", "9/9/9/9/4p4/9/9/9/9 W g", Drop(Gold(Player.White), (5, 5)))
        )
      }

      it("prevents the wrong player from moving") {
        testIllegalMoves(
          ("black", "9/9/9/9/9/9/9/9/9 W G", Drop(Gold(Player.Black), (1, 1))),
          ("white", "9/9/9/9/9/9/9/9/9 B g", Drop(Gold(Player.White), (5, 5)))
        )
      }

      it("prevents dropping a piece that is not in hand") {
        testIllegalMoves(
          ("black", "9/9/9/9/9/9/9/9/9 B P", Drop(Gold(Player.Black), (1, 1))),
          ("white", "9/9/9/9/9/9/9/9/9 W p", Drop(Gold(Player.White), (5, 5)))
        )
      }

      describe("a pawn") {

        it("cannot drop into a file that already has a (non-promoted) pawn") {
          testIllegalMoves(
            ("black illegal", "9/9/9/9/9/9/5P3/9/9 B P", Drop(Pawn(Player.Black), (6, 4))),
            ("white illegal", "9/9/9/9/1p7/9/9/9/9 W p", Drop(Pawn(Player.White), (3, 8)))
          )
          testLegalMoves(
            ("black legal", "9/9/9/9/9/9/5+P3/9/9 B P", Drop(Pawn(Player.Black), (6, 4)), "9/9/9/9/9/5P3/5+P3/9/9 W -"),
            ("white legal", "9/9/9/9/1+p7/9/9/9/9 W p", Drop(Pawn(Player.White), (3, 8)), "9/9/1p7/9/1+p7/9/9/9/9 B -")
          )
        }

        it("cannot drop into the last rank") {
          testIllegalMoves(
            ("black", "9/9/9/9/9/9/9/9/9 B P", Drop(Pawn(Player.Black), (1, 3))),
            ("white", "9/9/9/9/9/9/9/9/9 W p", Drop(Pawn(Player.White), (9, 9)))
          )
        }

        it("cannot cause checkmate") (pending) // This may be a bitch to test...
      }

      it("a lance cannot drop into the last rank") {
        testIllegalMoves(
          ("black", "9/9/9/9/9/9/9/9/9 B L", Drop(Lance(Player.Black), (1, 3))),
          ("white", "9/9/9/9/9/9/9/9/9 W l", Drop(Lance(Player.White), (9, 9)))
        )
      }

      it("a knight cannot drop into the last two ranks") {
        testIllegalMoves(
          ("black back rank", "9/9/9/9/9/9/9/9/9 B L", Drop(Knight(Player.Black), (1, 3))),
          ("black second-to-back rank", "9/9/9/9/9/9/9/9/9 B L", Drop(Knight(Player.Black), (2, 3))),
          ("white back rank", "9/9/9/9/9/9/9/9/9 W l", Drop(Knight(Player.White), (9, 4))),
          ("white second-to-back rank", "9/9/9/9/9/9/9/9/9 W l", Drop(Knight(Player.White), (8, 4)))
        )
      }
    }
  }

  describe("when checking for unpromoted pawns on a file") {

    it("finds the same player's pawn") {
      val GameState(_, board, _) = GameState.fromSfen("9/9/2p6/9/9/9/5P3/9/9 B -")
      Shogi.fileHasUnpromotedPawn(board, Player.Black, 4) shouldBe true
      Shogi.fileHasUnpromotedPawn(board, Player.White, 7) shouldBe true
    }

    it("ignores the other player's pawn") {
      val GameState(_, board, _) = GameState.fromSfen("9/9/2p6/9/9/9/5P3/9/9 B -")
      Shogi.fileHasUnpromotedPawn(board, Player.Black, 7) shouldBe false
      Shogi.fileHasUnpromotedPawn(board, Player.White, 4) shouldBe false
    }

    it("handles files with no pawns") {
      val GameState(_, board, _) = GameState.fromSfen("4k4/9/9/9/9/9/9/9/4K4 B -")
      Shogi.fileHasUnpromotedPawn(board, Player.Black, 5) shouldBe false
      Shogi.fileHasUnpromotedPawn(board, Player.Black, 4) shouldBe false
      Shogi.fileHasUnpromotedPawn(board, Player.White, 5) shouldBe false
      Shogi.fileHasUnpromotedPawn(board, Player.White, 3) shouldBe false
    }
  }
  
  private def testLegalMoves(testCases: (String, String, Move, String)*): Unit = {
    testCases.foreach {
      case (clue, beforeSfen, _, afterSfen) =>
        withClue(clue + " (SFEN before)") { SfenParser.gameState.parse(beforeSfen) shouldBe a [Success[_,_,_]] }
        withClue(clue + " (SFEN after)") { SfenParser.gameState.parse(afterSfen) shouldBe a [Success[_,_,_]] }
    }
    testCases.foreach {
      case (clue, beforeSfen, move, afterSfen) =>
        val start = GameState.fromSfen(beforeSfen)
        val newState = Shogi.movePiece(start, move)
        withClue(clue) { newState shouldEqual scala.util.Success(GameState.fromSfen(afterSfen)) }
    }
  }

  private def testIllegalMoves(testCases: (String, String, Move)*): Unit = {
    testCases.foreach {
      case (clue, beforeSfen, _) =>
        withClue(clue + " (sanity check)") { SfenParser.gameState.parse(beforeSfen) shouldBe a [Success[_,_,_]] }
    }
    testCases.foreach {
      case (clue, beforeSfen, move) =>
        val start = GameState.fromSfen(beforeSfen)
        val newState = Shogi.movePiece(start, move)
        withClue(clue) { newState shouldBe a [Failure[_]] }
    }
  }

  private def testEntireBoard(initialSfen: String, from: Location, legal: Set[Location], promote: Boolean = false): Unit = {
    val before = GameState.fromSfen(initialSfen)
    for (rank <- 1 to 9; file <- 1 to 9; to = Location(rank, file); if to != from) {
      if(legal contains to) {
        val after = Shogi.movePiece(before, BoardMove(from, to, promote)) match {
          case scala.util.Success(x) => x
          case _ => fail(to + " should have been a legal move")
        }
        withClue(to + " (legal)") {
          after.board.pieceAt(from) shouldBe empty
          after.board.pieceAt(to).get shouldEqual (if (promote) {
            Piece.promotedPiece(before.board.pieceAt(from).get)
          } else {
            before.board.pieceAt(from).get
          })
        }
      } else {
        val after = Shogi.movePiece(before, BoardMove(from, to, promote))
        withClue(to + " (illegal)") { after shouldBe a [Failure[_]]}
      }
    }
  }

  private def testGoldMoves(symbol: String): Unit = {
    testEntireBoard(s"9/9/9/9/9/7${symbol.toUpperCase}1/9/9/9 B -", (6, 2),
      Set((5, 1), (5, 2), (5, 3), (6, 1), (6, 3), (7, 2)))
    testEntireBoard(s"9/6${symbol.toLowerCase}2/9/9/9/9/9/9/9 W -", (2, 3),
      Set((1, 3), (2, 2), (2, 4), (3, 2), (3, 3), (3, 4)))
  }
}

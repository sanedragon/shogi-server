package models

import scala.util.{Failure, Success, Try}
import Player._

object Shogi {

  def movePiece(state: GameState, move: Move): Try[GameState] = move match {
    case b: BoardMove => movePieceOnBoard(state, b)
    case d: Drop => dropPiece(state, d)
  }

  def movePieceOnBoard(state: GameState, move: BoardMove): Try[GameState] = {
    if (!isMoveValid(state, move)) {
      Failure(new Exception())
    } else {
      val piecesInHand = state.board.pieceAt(move.destination) match {
        case None => state.piecesInHand
        case Some(p @ Piece(_)) => state.piecesInHand + p
      }
      val Some(pieceToMove) = state.board.pieceAt(move.start)
      val piece = pieceToMove match {
        case x @ PromotablePiece(_, false) if move.promote => Piece.promotedPiece(x)
        case _ => pieceToMove
      }
      val board = ShogiBoard(state.board.pieces.filter({ case (loc, _) => loc != move.start }) + (move.destination -> piece))
      val nextPlayer = if (state.playerToMove == Black) White else Black
      Success(GameState(nextPlayer, board, piecesInHand))
    }
  }

  def backRank(player: Player.Value): Int = if (player == Black) 1 else 9

  def backTwoRanks(player: Player.Value): Range = if (player == Black) 1 to 2 else 8 to 9

  def isMoveValid(state: GameState, move: BoardMove): Boolean = {
    val pieceToMove = state.board.pieceAt(move.start)
    val promotionZone = if (state.playerToMove == Black) 1 to 3 else 7 to 9
    pieceToMove match {
      case None => false
      case Some(Piece(p)) if p != state.playerToMove => false
      case Some(PromotablePiece(_, true)) if move.promote => false
      case Some(PromotablePiece(_, false)) if move.promote && !(promotionZone.contains(move.destination.rank) ||
        promotionZone.contains(move.start.rank)) => false
      case Some(Pawn(_, false)) | Some(Lance(_,false)) if move.destination.rank == backRank(state.playerToMove) && !move.promote => false
      case Some(Knight(_, false)) if backTwoRanks(state.playerToMove).contains(move.destination.rank) && !move.promote => false
      case Some(Gold(_)) | Some(King(_))  if move.promote => false
      case _ =>
        if (validMovesFrom(move.start, state.board).contains(move.destination)) {
          state.board.pieceAt(move.destination) match {
            case Some(Piece(p)) if p == state.playerToMove => false
            case _ => true
          }
        } else {
          false
        }
    }
  }

  def validMovesFrom(l: Location, board: ShogiBoard): Set[Location] = {
    val N = (-1, 0)
    val NE = (-1, -1)
    val E = (0, -1)
    val SE = (1, -1)
    val S = (1, 0)
    val SW = (1, 1)
    val W = (0, 1)
    val NW = (-1, 1)

    def goldMoves(player: Player.Value) = {
      singleStep(l, player, board, Set(NW, N, NE, E, S, W))
    }

    board.pieceAt(l) match {
      case Some(Pawn(player, false)) => singleStep(l, player, board, Set(N))
      case Some(Pawn(player, true)) => goldMoves(player)
      case Some(Knight(player, false)) => singleStep(l, player, board, Set((-2, 1), (-2, -1)))
      case Some(Knight(player, true)) => goldMoves(player)
      case Some(Silver(player, false)) => singleStep(l, player, board, Set(NW, N, NE, SE, SW))
      case Some(Silver(player, true)) => goldMoves(player)
      case Some(Gold(player)) => goldMoves(player)
      case Some(King(player)) => singleStep(l, player, board, Set(N, NE, E, SE, S, SW, W, NW))
      case Some(Lance(player, false)) => ranging(l, player, board, Set((-1, 0)))
      case Some(Lance(player, true)) => goldMoves(player)
      case Some(Rook(player, false)) => ranging(l, player, board, Set(N, E, S, W))
      case Some(Rook(player, true)) => ranging(l, player, board, Set(N, E, S, W)) ++
        singleStep(l, player, board, Set(NE, SE, NW, SW))
      case Some(Bishop(player, false)) => ranging(l, player, board, Set(NW, NE, SE, SW))
      case Some(Bishop(player, true)) => ranging(l, player, board, Set(NW, NE, SE, SW)) ++
        singleStep(l, player, board, Set(N, E, S, W))
      case _ => Set()
    }
  }

  def singleStep(from: Location,
                 p: Player.Value,
                 board: ShogiBoard,
                 directionsForBlack: Set[(Int, Int)]): Set[Location] = {
    for {
      l <- directionsForBlack.flatMap(from + reflectDirection(_, p))
      if !hasFriendlyPiece(board, l, p)
    } yield l
  }

  def ranging(from: Location,
              p: Player.Value,
              board: ShogiBoard,
              directionsForBlack: Set[(Int, Int)]): Set[Location] = {
    val moves = for {
      d <- directionsForBlack.map(reflectDirection(_, p))
    } yield ranging(from + d, p, board, d)
    moves.flatten
  }

  def ranging(from: Option[Location],
              p: Player.Value,
              board: ShogiBoard,
              direction: (Int, Int)): Set[Location] = {
    from match {
      case None => Set()
      case Some(l) => if (hasFriendlyPiece(board, l, p)) Set() else Set(l) ++ ranging(l + direction, p, board, direction)
    }
  }

  def reflectDirection(direction: (Int, Int), player: Player.Value): (Int, Int) = {
    val (dr, df) = direction
    if (player == Black) (dr, df) else (dr * -1, df)
  }

  def hasFriendlyPiece(board: ShogiBoard, location: Location, player: Player.Value): Boolean =
    board.pieceAt(location) match {
      case Some(Piece(p)) if p == player => true
      case _ => false
    }

  def dropPiece(state: GameState, move: Drop): Try[GameState] = {
    if (move.piece.player == state.playerToMove && state.piecesInHand.hasInHand(move.piece))
      if(state.board.pieceAt(move.destination).isEmpty {
        move.piece match {
          case _: Pawn if fileHasUnpromotedPawn(state.board, state.playerToMove, move.destination.file) => Failure(new Exception())
          case _: Pawn if move.destination.rank == backRank(state.playerToMove) => Failure(new Exception())
          case _: Lance if move.destination.rank == backRank(state.playerToMove) => Failure(new Exception())
          case _: Knight if backTwoRanks(state.playerToMove).contains(move.destination.rank) => Failure(new Exception())
          case _ =>
            val newBoard = ShogiBoard(state.board.pieces + (move.destination -> move.piece))
            val nextPlayer = if (state.playerToMove == Black) White else Black
            val newPiecesInHand = state.piecesInHand - move.piece
            Success(GameState(nextPlayer, newBoard, newPiecesInHand))
        }
      } else {
        Failure(new Exception())
      }
    else
      Failure(new Exception())
  }

  def fileHasUnpromotedPawn(board: ShogiBoard, player: Player.Value, file: Int): Boolean = {
    val pieces = (1 to 9).flatMap(board.pieceAt(_, file))
    if (pieces.isEmpty) false else pieces.forall {
      case Pawn(p, false) => p == player
      case _ => false
    }
  }
}

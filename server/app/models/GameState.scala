package models

import fastparse.core.Parsed

case class GameState(playerToMove: Player.Value, board: ShogiBoard, piecesInHand: PiecesInHand)

object GameState {
  def fromSfen(sfen: String): GameState = {
    SfenParser.gameState.parse(sfen) match {
      case Parsed.Success(state, _) => state
      case f @ Parsed.Failure(_, _, _) => throw new Exception("Failure: " + f)
    }
  }
}

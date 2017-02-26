package models

case class GameState(playerToMove: Player.Value, board: ShogiBoard, piecesInHand: PiecesInHand)

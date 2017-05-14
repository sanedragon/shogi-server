package models

sealed trait Move
case class BoardMove(start: Location, destination: Location, promote: Boolean = false) extends Move
case class Drop(piece: Piece, destination: Location) extends Move
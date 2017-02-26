package models

sealed trait Piece {
  val player: Player.Value
}
sealed trait PromotablePiece extends Piece {
  val promoted: Boolean
}
case class King(player: Player.Value) extends Piece
case class Rook(player: Player.Value, promoted: Boolean = false) extends PromotablePiece
case class Bishop(player: Player.Value, promoted: Boolean = false) extends PromotablePiece
case class Gold(player: Player.Value) extends Piece
case class Silver(player: Player.Value, promoted: Boolean = false) extends PromotablePiece
case class Knight(player: Player.Value, promoted: Boolean = false) extends PromotablePiece
case class Lance(player: Player.Value, promoted: Boolean = false) extends PromotablePiece
case class Pawn(player: Player.Value, promoted: Boolean = false) extends PromotablePiece

object Piece {
  def pieceName(p: Piece): String = p match {
    case Pawn(_, _) => "pawn"
    case Bishop(_, _) => "bishop"
    case Rook(_, _) => "rook"
    case Lance(_, _) => "lance"
    case Knight(_, _) => "knight"
    case Silver(_, _) => "silver"
    case Gold(_) => "gold"
    case King(_) => "king"
  }

  def unpromotedPiece(p: Piece): Piece = p match {
    case Pawn(player, true) => Pawn(player)
    case Rook(player, true) => Rook(player)
    case Bishop(player, true) => Bishop(player)
    case Lance(player, true) => Lance(player)
    case Knight(player, true) => Knight(player)
    case Silver(player, true) => Silver(player)
    case _ => p
  }
}

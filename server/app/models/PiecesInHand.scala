package models

case class PiecesInHand(pieces: Map[Piece,Int] = Map()) {
  require(pieces.values forall (_ >= 1))

  import Piece.unpromotedPiece

  def hasInHand(p: Piece): Boolean = pieces get p match {
    case Some(num) => num >= 1
    case None => false
  }

  def +(p: Piece): PiecesInHand = {
    val un = unpromotedPiece(p)
    PiecesInHand(pieces + (un -> (pieces.getOrElse(un, 0) + 1)))
  }

  def -(p: Piece): PiecesInHand = {
    val un = unpromotedPiece(p)
    val newPieces = pieces get un match {
      case Some(num) if num > 1 => pieces + (un -> (num - 1))
      case Some(num) if num == 1 => pieces - un
      case _ => pieces
    }
    PiecesInHand(newPieces)
  }

  def numberInHand(p: Piece): Int = pieces.getOrElse(unpromotedPiece(p), 0)

  def piecesInHand(player: Player.Value): PiecesInHand = PiecesInHand(pieces filterKeys (_.player == player))
}

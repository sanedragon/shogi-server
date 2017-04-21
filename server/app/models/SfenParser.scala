package models

import fastparse.all._
import Player._

object SfenParser {
  val gameState: Parser[GameState] = P( boardState ~ " " ~ playerToMove ~ " " ~ piecesInHand ~ (" " ~ moveNumber).? ).map({
    case (board, player, inHand, number) => GameState(player, board, inHand)
  })

  lazy val boardState: Parser[ShogiBoard] = P( row.rep(exactly=9, sep="/") ).map({
    pieces =>
      val pairs = allLocations zip pieces.flatten
      val filtered = for ((l, p) <- pairs; if p.isDefined) yield (l, p.get)
      ShogiBoard(filtered.toMap)
  })
  lazy val allLocations: Seq[Location] = for (rank <- 1 to 9; file <- 9 to 1 by -1) yield Location(rank, file)
  lazy val row: Parser[Seq[Option[Piece]]] = P( (spaces | piece).rep(1) ).map(_.flatten)
  lazy val spaces: Parser[Seq[Option[Piece]]] = P( CharIn('1' to '9').!.map(_.toInt) ).map(n => for (_ <- 1 to n) yield None)
  lazy val piece = P( nonPromotable | promotable )
  lazy val nonPromotable: Parser[Seq[Option[Piece]]] = P( CharIn("kKgG").!.map(_(0)) ).map({
    case (code) => Seq(Some(buildUnpromotedPiece(code)))
  })
  private def buildUnpromotedPiece(pieceCode: Char): Piece = {
    val player = if (pieceCode.isUpper) Black else White
    pieceCode.toLower match {
      case 'k' => King(player)
      case 'g' => Gold(player)
      case 's' => Silver(player)
      case 'n' => Knight(player)
      case 'l' => Lance(player)
      case 'r' => Rook(player)
      case 'b' => Bishop(player)
      case 'p' => Pawn(player)
    }
  }
  lazy val promotedFlag: Parser[Boolean] = P( "+".?.! ).map(_ == "+")
  lazy val promotablePieceCode: Parser[Char] = CharIn("sSnNlLrRbBpP").!.map(_(0))
  lazy val promotable: Parser[Seq[Option[PromotablePiece]]] = P( promotedFlag ~ promotablePieceCode ).map({
    case (promoted, pieceCode) =>
      val player = if (pieceCode.isUpper) Black else White
      Seq(Some(pieceCode.toLower match {
        case 's' => Silver(player, promoted)
        case 'n' => Knight(player, promoted)
        case 'l' => Lance(player, promoted)
        case 'r' => Rook(player, promoted)
        case 'b' => Bishop(player, promoted)
        case 'p' => Pawn(player, promoted)
      }))
  })

  lazy val playerToMove: Parser[Player.Value] = P( CharIn("BW").! ).map((p) => if (p == "B") Black else White)

  lazy val piecesInHand = P( emptyPih | inHandPieces )
  lazy val emptyPih: Parser[PiecesInHand] = P( "-".! ).map((_) => PiecesInHand())
  lazy val inHandPieces: Parser[PiecesInHand] = P( inHandPiece.rep(1) ).map(_.flatten.foldLeft(PiecesInHand())(_ + _))
  lazy val inHandPiece: Parser[Seq[Piece]] = P( CharIn('1' to '9').!.? ~ CharIn("gGsSnNlLrRbBpP").!.map(_(0)) ).map({
    case (number, pieceCode) =>
      val count = number match {
        case Some(n) => n.toInt
        case None => 1
      }
      for (_ <- 1 to count) yield buildUnpromotedPiece(pieceCode)
  })

  lazy val moveNumber: Parser[Int] = P( (CharIn('1'to'9') ~ CharIn('0' to '9').rep).! ).map(_.toInt)
}

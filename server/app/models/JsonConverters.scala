package models

import play.api.libs.json._

object JsonConverters {

  implicit val locationWrites = Json.writes[Location]

  implicit val pieceWrites = new Writes[Piece] {
    def writes(p: Piece): JsObject = {
      val writes = Json.obj(
        "type" -> Piece.pieceName(p),
        "player" -> p.player
      )

      p match {
        case pp: PromotablePiece => writes + ("promoted" -> JsBoolean(pp.promoted))
        case _ => writes
      }
    }
  }


  implicit val boardWrites = new Writes[ShogiBoard] {
    def writes(b: ShogiBoard): JsArray = {
      val pieces = for ((loc, piece) <- b.pieces) yield Json.obj(
        "location" -> loc,
        "piece" -> piece
      )
      JsArray(pieces.toSeq)
    }
  }

  implicit val piecesInHandWrites = new Writes[PiecesInHand] {
    def writes(pih: PiecesInHand): JsArray = {
      val pieces = for ((piece, number) <- pih.pieces) yield Json.obj(
        "piece" -> piece,
        "number" -> number
      )
      JsArray(pieces.toSeq)
    }
  }

  implicit val gameStateWrites = Json.writes[GameState]
}

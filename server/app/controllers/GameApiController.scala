package controllers

import models._
import models.Player._
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import models.JsonConverters._

class GameApiController extends Controller {


  // One of each type of piece for each player, promoted and not
  def game(id: Int) = Action {
    lazy val hasAllPromotions = {
      val board = ShogiBoard(Map(
        Location(1,1) -> Lance(PlayerTwo),
        Location(1,2) -> Knight(PlayerTwo),
        Location(1,3) -> Silver(PlayerTwo),
        Location(1,4) -> Gold(PlayerTwo),
        Location(1,5) -> King(PlayerTwo),
        Location(1,7) -> Silver(PlayerTwo, promoted = true),
        Location(1,8) -> Knight(PlayerTwo, promoted = true),
        Location(1,9) -> Lance(PlayerTwo, promoted = true),
        Location(2,2) -> Bishop(PlayerTwo),
        Location(2,3) -> Rook(PlayerTwo),
        Location(2,7) -> Bishop(PlayerTwo, promoted = true),
        Location(2,8) -> Rook(PlayerTwo, promoted = true),
        Location(3,4) -> Pawn(PlayerTwo),
        Location(3,6) -> Pawn(PlayerTwo, promoted = true),
        Location(9,1) -> Lance(PlayerOne),
        Location(9,2) -> Knight(PlayerOne),
        Location(9,3) -> Silver(PlayerOne),
        Location(9,4) -> Gold(PlayerOne),
        Location(9,5) -> King(PlayerOne),
        Location(9,7) -> Silver(PlayerOne, promoted = true),
        Location(9,8) -> Knight(PlayerOne, promoted = true),
        Location(9,9) -> Lance(PlayerOne, promoted = true),
        Location(8,2) -> Bishop(PlayerOne),
        Location(8,3) -> Rook(PlayerOne),
        Location(8,7) -> Bishop(PlayerOne, promoted = true),
        Location(8,8) -> Rook(PlayerOne, promoted = true),
        Location(7,4) -> Pawn(PlayerOne),
        Location(7,6) -> Pawn(PlayerOne, promoted = true)
      ))
      GameState(PlayerOne, board, PiecesInHand())
    }

    // Taken from "Shogi for Beginners", p. 48, Diagram 2
    lazy val midGameState = {
      val board = ShogiBoard(Map(
        Location(1,1) -> Lance(PlayerTwo),
        Location(1,2) -> Knight(PlayerTwo),
        Location(1,5) -> King(PlayerTwo),
        Location(1,9) -> Lance(PlayerTwo),
        Location(3,2) -> Gold(PlayerOne),
        Location(3,4) -> Lance(PlayerOne),
        Location(3,7) -> Silver(PlayerTwo),
        Location(4,1) -> Pawn(PlayerTwo),
        Location(4,5) -> Silver(PlayerOne),
        Location(4,6) -> Pawn(PlayerOne),
        Location(4,7) -> Pawn(PlayerTwo),
        Location(4,8) -> Pawn(PlayerTwo),
        Location(4,9) -> Pawn(PlayerTwo),
        Location(5,5) -> Bishop(PlayerOne),
        Location(5,6) -> Rook(PlayerOne),
        Location(6,1) -> Pawn(PlayerOne),
        Location(6,2) -> Pawn(PlayerOne),
        Location(6,3) -> Pawn(PlayerOne),
        Location(6,9) -> Pawn(PlayerOne),
        Location(7,2) -> King(PlayerOne),
        Location(7,4) -> Silver(PlayerOne),
        Location(7,5) -> Pawn(PlayerTwo, promoted = true),
        Location(7,7) -> Knight(PlayerOne),
        Location(8,3) -> Gold(PlayerOne),
        Location(8,4) -> Gold(PlayerOne),
        Location(8,9) -> Rook(PlayerTwo, promoted = true),
        Location(9,1) -> Lance(PlayerOne),
        Location(9,2) -> Knight(PlayerOne),
        Location(9,5) -> Bishop(PlayerTwo)
      ))
      val inHand = PiecesInHand(Map(
        Gold(PlayerOne) -> 1,
        Pawn(PlayerOne) -> 7,
        Silver(PlayerTwo) -> 1,
        Knight(PlayerTwo) -> 1,
        Pawn(PlayerTwo) -> 1
      ))
      GameState(PlayerOne, board, inHand)
    }

    val state = id match {
      case 0 => GameState(PlayerOne, ShogiBoard.initialSetup(), PiecesInHand())
      case 1 => hasAllPromotions
      case 2 => midGameState
      case _ => GameState(PlayerOne, ShogiBoard(Map()), PiecesInHand())
    }
    Ok(Json.toJson(state))
  }
}

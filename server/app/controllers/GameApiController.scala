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
        Location(1,1) -> Lance(White),
        Location(1,2) -> Knight(White),
        Location(1,3) -> Silver(White),
        Location(1,4) -> Gold(White),
        Location(1,5) -> King(White),
        Location(1,7) -> Silver(White, promoted = true),
        Location(1,8) -> Knight(White, promoted = true),
        Location(1,9) -> Lance(White, promoted = true),
        Location(2,2) -> Bishop(White),
        Location(2,3) -> Rook(White),
        Location(2,7) -> Bishop(White, promoted = true),
        Location(2,8) -> Rook(White, promoted = true),
        Location(3,4) -> Pawn(White),
        Location(3,6) -> Pawn(White, promoted = true),
        Location(9,1) -> Lance(Black),
        Location(9,2) -> Knight(Black),
        Location(9,3) -> Silver(Black),
        Location(9,4) -> Gold(Black),
        Location(9,5) -> King(Black),
        Location(9,7) -> Silver(Black, promoted = true),
        Location(9,8) -> Knight(Black, promoted = true),
        Location(9,9) -> Lance(Black, promoted = true),
        Location(8,2) -> Bishop(Black),
        Location(8,3) -> Rook(Black),
        Location(8,7) -> Bishop(Black, promoted = true),
        Location(8,8) -> Rook(Black, promoted = true),
        Location(7,4) -> Pawn(Black),
        Location(7,6) -> Pawn(Black, promoted = true)
      ))
      GameState(Black, board, PiecesInHand())
    }

    // Taken from "Shogi for Beginners", p. 48, Diagram 2
    lazy val midGameState = {
      val board = ShogiBoard(Map(
        Location(1,1) -> Lance(White),
        Location(1,2) -> Knight(White),
        Location(1,5) -> King(White),
        Location(1,9) -> Lance(White),
        Location(3,2) -> Gold(Black),
        Location(3,4) -> Lance(Black),
        Location(3,7) -> Silver(White),
        Location(4,1) -> Pawn(White),
        Location(4,5) -> Silver(Black),
        Location(4,6) -> Pawn(Black),
        Location(4,7) -> Pawn(White),
        Location(4,8) -> Pawn(White),
        Location(4,9) -> Pawn(White),
        Location(5,5) -> Bishop(Black),
        Location(5,6) -> Rook(Black),
        Location(6,1) -> Pawn(Black),
        Location(6,2) -> Pawn(Black),
        Location(6,3) -> Pawn(Black),
        Location(6,9) -> Pawn(Black),
        Location(7,2) -> King(Black),
        Location(7,4) -> Silver(Black),
        Location(7,5) -> Pawn(White, promoted = true),
        Location(7,7) -> Knight(Black),
        Location(8,3) -> Gold(Black),
        Location(8,4) -> Gold(Black),
        Location(8,9) -> Rook(White, promoted = true),
        Location(9,1) -> Lance(Black),
        Location(9,2) -> Knight(Black),
        Location(9,5) -> Bishop(White)
      ))
      val inHand = PiecesInHand(Map(
        Gold(Black) -> 1,
        Pawn(Black) -> 7,
        Silver(White) -> 1,
        Knight(White) -> 1,
        Pawn(White) -> 1
      ))
      GameState(Black, board, inHand)
    }

    val state = id match {
      case 0 => GameState(Black, ShogiBoard.initialSetup(), PiecesInHand())
      case 1 => hasAllPromotions
      case 2 => midGameState
      case _ => GameState(Black, ShogiBoard(Map()), PiecesInHand())
    }
    Ok(Json.toJson(state))
  }
}

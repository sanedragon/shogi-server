import { Board, Player, PiecesInHand } from 'models';

export interface IGame {
    gameId?: number;
    board?: Board;
    piecesInHand?: PiecesInHand;
    playerToMove?: Player;
}

export interface IGameAction {
    type: string;
    payload?: {
        gameId?: number,
        board?: Board,
        piecesInHand?: PiecesInHand,
        playerToMove?: Player
    };
}

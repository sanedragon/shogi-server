import { IGame, IGameAction } from 'models/game';
import { Board, Player, PiecesInHand, Pawn } from 'models';

/** Action types */
export const REQUEST_GAME = 'game/REQUEST_GAME';
export const RECEIVE_GAME = 'game/RECEIVE_GAME';

const initialState: IGame = {};

/** Reducer */
export function gameReducer(state = initialState, action: IGameAction) {
    switch (action.type) {
        case REQUEST_GAME:
            return {
                ...state,
                gameId: action.payload.gameId
            };

        case RECEIVE_GAME:
            return {
                ...state,
                gameId: action.payload.gameId,
                board: action.payload.board,
                piecesInHand: action.payload.piecesInHand,
                playerToMove: action.payload.playerToMove
            };

        default:
            return state;
    }
}

/** Async action creator */
export function getGame(gameId: number) {
    return (dispatch) => {
        dispatch(gameRequest(gameId));

        return fetch(`/api/game/${gameId}`)
            .then((res) => {
                if (res.ok) {
                    return res.json()
                        .then((res) => {
                            dispatch(gameSuccess(gameId, jsonToBoard(res.board),
                                jsonToPiecesInHand(res.piecesInHand), res.playerToMove));
                        });
                } else {
                    console.log('Bad request: res = ' + JSON.stringify(res));
                }
            });
    };
}

function jsonToBoard(boardJson): Board {
    const board = new Board();
    boardJson.forEach((p) => { board.addPiece(p.piece, p.location.rank, p.location.file); });
    return board;
}

function jsonToPiecesInHand(boardPih): PiecesInHand {
    const piecesInHand = new PiecesInHand();
    boardPih.forEach((p) => {
        console.log(`piece = ${JSON.stringify(p.piece)} -> ${p.number}`);
        console.log(`piece instanceof Pawn = ${p.piece instanceof Pawn}`);
        piecesInHand.add(p.piece, p.number);
    });
    return piecesInHand;
}

/** Action creator */
export function gameRequest(gameId: number) {
    return {
        type: REQUEST_GAME,
        payload: {
            gameId
        }
    };
}

/** Action creator */
export function gameSuccess(gameId: number,
                            board: Board,
                            piecesInHand: PiecesInHand,
                            playerToMove: Player): IGameAction {
    return {
        type: RECEIVE_GAME,
        payload: {
            gameId,
            board,
            piecesInHand,
            playerToMove
        }
    };
}

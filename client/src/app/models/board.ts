import { Piece } from './pieces';

interface IPieceOnBoard {
    piece: Piece;
    rank: number;
    file: number;
}

export class Board {
    private piecesOnBoard: IPieceOnBoard[] = [];

    public addPiece(piece: Piece, rank: number, file: number) {
        this.piecesOnBoard.push({ piece, rank, file });
    }

    public getPieceAt(rank: number, file: number): Piece {
        const p = this.piecesOnBoard.find((p) => p.rank === rank && p.file === file);
        return p ? p.piece : undefined;
    }
}

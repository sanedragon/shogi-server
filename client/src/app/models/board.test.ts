import { Board } from './board';
import { Pawn } from './pieces';
import { expect } from 'chai';

describe('A shogi board model', () => {

    it('should find a piece at a specific location', () => {
        const pawn = new Pawn('PlayerOne', false);
        const board = new Board();
        board.addPiece(pawn, 2, 8);
        expect(board.getPieceAt(2, 8)).to.eql(pawn);
    });

    it('should find nothing at an empty location', () => {
        const pawn = new Pawn('PlayerOne', false);
        const board = new Board();
        board.addPiece(pawn, 2, 8);
        expect(board.getPieceAt(1, 1)).to.be.undefined;
    });
});

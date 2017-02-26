import { Pawn, King, Lance, PiecesInHand } from 'models';

import { expect } from 'chai';

describe('PiecesInHand model', () => {

    it('keeps track of captured pieces', () => {
        const p = new PiecesInHand();
        const pawn = new Pawn('PlayerOne', false);
        p.add(pawn);
        expect(p.numberInHand(pawn)).to.equal(1);
    });

    it('knows when pieces have not been captured', () => {
        const p = new PiecesInHand();
        const pawn = new Pawn('PlayerOne', false);
        expect(p.numberInHand(pawn)).to.equal(0);
    });

    it('keeps track of multiple pieces of the same type', () => {
        const p = new PiecesInHand();
        const pawn = new Pawn('PlayerOne', false);
        p.add(pawn);
        p.add(pawn);
        expect(p.numberInHand(pawn)).to.equal(2);
    });

    it('tracks when a piece is no longer captured', () => {
        const p = new PiecesInHand();
        const pawn = new Pawn('PlayerOne', false);
        p.add(pawn);
        p.remove(pawn);
        expect(p.numberInHand(pawn)).to.equal(0);
    });

    it('tracks each player\'s pieces separately', () => {
        const p = new PiecesInHand();
        const p1Pawn = new Pawn('PlayerOne', false);
        const p2Pawn = new Pawn('PlayerTwo', false);
        p.add(p1Pawn);
        expect(p.numberInHand(p2Pawn)).to.equal(0);
    });

    it('does not remove more pieces than were added', () => {
        const p = new PiecesInHand();
        const pawn = new Pawn('PlayerOne', false);
        p.add(pawn);
        p.remove(pawn);
        p.remove(pawn);
        expect(p.numberInHand(pawn)).to.equal(0);
    });

    it('can add several of the same piece at once', () => {
        const p = new PiecesInHand();
        const pawn = new Pawn('PlayerOne', false);
        p.add(pawn, 3);
        expect(p.numberInHand(pawn)).to.equal(3);
    });

    it('compares pieces by equality', () => {
        const p = new PiecesInHand();
        const pawn1 = new Pawn('PlayerOne', false);
        const pawn2 = new Pawn('PlayerOne', false);
        p.add(pawn1);
        expect(p.numberInHand(pawn2)).to.equal(1);
    });

    it('tracks different kinds of pieces', () => {
        const p = new PiecesInHand();
        const pawn = new Pawn('PlayerOne', false);
        const king = new King('PlayerOne');
        const lance = new Lance('PlayerOne', false);
        p.add(pawn);
        p.add(king);
        p.add(lance);
        expect(p.numberInHand(pawn)).to.equal(1, 'pawns');
        expect(p.numberInHand(king)).to.equal(1, 'kings');
        expect(p.numberInHand(lance)).to.equal(1, 'lances');
    });

    it('can handle both objects and instances', () => {
        const pawn = new Pawn('PlayerOne', false);
        const obj: Pawn = { type: 'pawn', player: 'PlayerOne', promoted: false };
        const p = new PiecesInHand();
        p.add(pawn);
        p.add(obj);
        expect(p.numberInHand(pawn)).to.equal(2, 'compare to Pawn');
        expect(p.numberInHand(obj)).to.equal(2, 'compare to object');
    });

    it('ignores promoted status when adding pieces', () => {
        const p = new PiecesInHand();
        p.add(new Pawn('PlayerOne', false));
        p.add(new Pawn('PlayerOne', true));
        expect(p.numberInHand(new Pawn('PlayerOne', false))).to.equal(2);
        expect(p.numberInHand(new Pawn('PlayerOne', true))).to.equal(2);
    });

    it('ignores promoted status when checking number in hand', () => {
        const p = new PiecesInHand();
        p.add(new Pawn('PlayerOne', false));
        expect(p.numberInHand(new Pawn('PlayerOne', true))).to.equal(1);
    });
});

import { Piece } from 'models/pieces';

export class PiecesInHand {
    private map: Map<string, number>;
    constructor() {
        this.map = new Map();
    }

    public numberInHand(p: Piece): number {
        if (this.map.has(this.keyFromPiece(p))) {
            return this.map.get(this.keyFromPiece(p));
        } else {
            return 0;
        }
    }

    public add(p: Piece, num = 1) {
        const current = this.numberInHand(p);
        console.log(`adding key ${this.keyFromPiece(p)}`);
        this.map.set(this.keyFromPiece(p), current + num);
    }

    public remove(p: Piece) {
        const num = this.numberInHand(p);
        if (num > 0) {
            this.map.set(this.keyFromPiece(p), num - 1);
        }
    }

    // Object comparison in Javascript is by reference only, so we need some
    // way to equate the contents.
    private keyFromPiece(p: Piece): string {
        return `${p.type}-${p.player}`;
    }
}

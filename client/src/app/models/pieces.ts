import { Player } from './player';

export class NonPromotablePiece {
    constructor(readonly player: Player) {};
}

export class PromotablePiece {
    constructor(readonly player: Player, readonly promoted: boolean) {};
}

export class King extends NonPromotablePiece {
    readonly type = 'king';
    constructor(player: Player) {
        super(player);
    }
}

export class Gold extends NonPromotablePiece {
    readonly type = 'gold';
    constructor(player: Player) {
        super(player);
    }
}

export class Silver extends PromotablePiece {
    readonly type = 'silver';
    constructor(player: Player, promoted: boolean) {
        super(player, promoted);
    }
}

export class Knight extends PromotablePiece {
    readonly type = 'knight';
    constructor(player: Player, promoted: boolean) {
        super(player, promoted);
    }
}

export class Lance extends PromotablePiece {
    readonly type = 'lance';
    constructor(player: Player, promoted: boolean) {
        super(player, promoted);
    }
}

export class Rook extends PromotablePiece {
    readonly type = 'rook';
    constructor(player: Player, promoted: boolean) {
        super(player, promoted);
    }
}

export class Bishop extends PromotablePiece {
    readonly type = 'bishop';
    constructor(player: Player, promoted: boolean) {
        super(player, promoted);
    }
}

export class Pawn extends PromotablePiece {
    readonly type = 'pawn';
    constructor(player: Player, promoted: boolean) {
        super(player, promoted);
    }
}

export type Piece = King | Gold | Silver | Knight | Lance | Rook | Bishop | Pawn;

import * as React from 'react';
import * as classNames from 'classnames';
import { Piece } from 'models/pieces';

const style = require('./ShogiBoard.css');

interface IProps {
    piece?: Piece;
}

function pieceStyle(piece: Piece) {
    switch (piece.type) {
        case 'king':
            return style.king;
        case 'gold':
            return style.gold;
        case 'silver':
            return style.silver;
        case 'knight':
            return style.knight;
        case 'lance':
            return style.lance;
        case 'bishop':
            return style.bishop;
        case 'rook':
            return style.rook;
        case 'pawn':
            return style.pawn;
        default:
            return '';
    }
}

export class Square extends React.Component<IProps, {}> {
    public render() {
        const { piece } = this.props;
        if (piece) {
            const classes = classNames(
                pieceStyle(piece),
                {
                    [style.player2]: piece.player === 'PlayerTwo',
                    [style.promoted]: piece.type !== 'king' && piece.type !== 'gold' && piece.promoted
                }
            );
            return <div className={classes}/>;
        } else {
            return <div />;
        }
    }
}

import * as React from 'react';
import { PiecesInHand as PiecesInHandModel, Player, Piece,
    Pawn, Lance, Knight, Silver, Gold, Rook, Bishop } from 'models';

const style = require('./PiecesInHand.css');

interface IPiecesInHandProps {
    piecesInHand: PiecesInHandModel;
    player: Player;
    topToBottom: boolean;
};

export class PiecesInHand extends React.Component<IPiecesInHandProps, {}> {
    public render() {
        const heading = `Pieces in hand for ${this.props.player}`;
        return (
            <div className={style.piecesInHand}>
                {heading}
                <ul>
                    {this.renderItems()}
                </ul>
            </div>
        );
    }

    private renderItems() {
        const { player } = this.props;
        console.log(`player = ${player}`);
        const order: Piece[] = [new Pawn(player, false), new Lance(player, false), new Knight(player, false),
            new Silver(player, false), new Gold(player), new Rook(player, false), new Bishop(player, false)];
        const list = [];

        order.forEach((piece) => {
            const numberInHand = this.props.piecesInHand.numberInHand(piece);
            console.log(`rendering ${JSON.stringify(piece)} ${piece.type} => ${numberInHand}`);
            if (numberInHand > 0) {
                list.push(<li>{piece.type + ': ' + numberInHand}</li>);
            }
        });

        return list;
    }
}

import * as React from 'react';
import { Board } from 'models/board';

import { Square } from './Square';

const style = require('./ShogiBoard.css');

interface IShogiBoardProps {
    board: Board;
};

export class ShogiBoard extends React.Component<IShogiBoardProps, {}> {
    public render() {
        const ranks = [];
        for (let rank = 1; rank <= 9; rank++) {
            const files = [];
            for (let file = 9; file >= 1; file--) {
                files.push(<Square piece={this.props.board.getPieceAt(rank, file)} key={`${rank}-${file}`}/>);
            }
            ranks.push(<div className={style.rank} key={rank}>{files}</div>);
        }
        return <div className={style.shogiBoard}>{ranks}</div>;
    }
}

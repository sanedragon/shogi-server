const appConfig = require('../../../../config/main');

import * as React from 'react';
import * as Helmet from 'react-helmet';

const { connect } = require('react-redux');

import { ShogiBoard } from './components/ShogiBoard';
import { PiecesInHand } from './components/PiecesInHand';
import { IGameAction, Board, Player, PiecesInHand as PiecesInHandModel } from 'models';

import { getGame } from 'modules/game';

const style = require('./style.css');

interface IProps {
    gameId?: number;
    board?: Board;
    piecesInHand?: PiecesInHandModel;
    playerToMove?: Player;
    updateGame: Redux.ActionCreator<IGameAction>;
}

@connect(
(state) => ({
    gameId: state.game.gameId,
    board: state.game.board,
    piecesInHand: state.game.piecesInHand,
    playerToMove: state.game.playerToMove
}),
(dispatch) => ({updateGame: (gameId: number) => dispatch(getGame(gameId))})
)

class App extends React.Component<IProps, any> {
    constructor() {
        super();
        this.handleGetIt = this.handleGetIt.bind(this);
    }

    public render() {
        return (
            <section className={style.AppContainer}>
                <Helmet {...appConfig.app} {...appConfig.app.head}/>
                <div>
                    <h1>Shogi Game</h1>
                    <p>
                        Choose a game:
                        <input id="gameid"/>
                        <button id="doit" type="button" onClick={this.handleGetIt}>Get it</button>
                    </p>
                    {this.props.gameId && <h2>Game # {this.props.gameId}</h2>}
                    {this.renderGameState()}
                </div>
                {this.props.children}
            </section>
        );
    }

    public renderGameState() {
        if (this.props.board && this.props.piecesInHand) {
            return (
                <div className={style.game}>
                    <PiecesInHand piecesInHand={this.props.piecesInHand}
                                    player="PlayerTwo"
                                    topToBottom={false} />
                    <ShogiBoard board={this.props.board} />
                    <PiecesInHand piecesInHand={this.props.piecesInHand}
                                    player="PlayerOne"
                                    topToBottom={true} />
                </div>
            );
        } else {
            return null;
        }
    }

    public handleGetIt() {
        const gameId = (document.getElementById('gameid') as HTMLInputElement).value;
        this.props.updateGame(gameId);
    }

}

export { App }

import { combineReducers } from 'redux';
import { routerReducer } from 'react-router-redux';
import { gameReducer } from './modules/game';
import { IStore } from './IStore';

const { reducer } = require('redux-connect');

const rootReducer: Redux.Reducer<IStore> = combineReducers<IStore>({
  routing: routerReducer,
  game: gameReducer,
  reduxAsyncConnect: reducer,
});

export default rootReducer;

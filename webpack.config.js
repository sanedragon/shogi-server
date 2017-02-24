'use strict';

var webpack = require('webpack');
var jsPath = 'app/assets/javascripts';
var path = require('path');
var srcPath = path.join(__dirname, 'app/assets/javascripts');

var config = {
    target: 'web',
    entry: {
        app: path.join(srcPath, 'app.tsx')
    },
    resolve: {
        alias: {},
        modules: [
            srcPath,
            'node_modules'
        ],
        extensions: ['.ts', '.tsx', '.js', '.jsx']
    },
    output: {
        path: path.join(__dirname, '/public/bundles'),
        publicPath: '/assets/bundles',
        filename: 'javascripts/bundle.js',
        pathinfo: true
    },
    module: {
        rules: [
            {
                test: /\.tsx?$/,
                loaders: ['ts-loader'],
                exclude: [
                    'node_modules'
                ]
            }
        ]
    },
    watchOptions: {
        aggregateTimeout: 500
    }
}

module.exports = config;

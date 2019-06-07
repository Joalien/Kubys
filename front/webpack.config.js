const path = require('path');
const CleanWebpackPlugin = require('clean-webpack-plugin');
const HtmlWebpackPlugin = require('html-webpack-plugin');

module.exports = {
    watch: false,
    devtool: "source-map",
    entry: {
        kubys: './src/js/Game.js'
    },
    plugins: [
        new CleanWebpackPlugin(),
        new HtmlWebpackPlugin({
            filename: 'index.html',
            template: './src/index.html'
        }),
        new HtmlWebpackPlugin({
            files: {
                chunks: {
                    kubys: {
                        entry: './src/js/Game.js',
                    }
                }
            }
        }),
        new HtmlWebpackPlugin({
            filename: 'index.html',
            template: './src/index.html',
            chunks: ['kubys']
        }),
        new HtmlWebpackPlugin({
            filename: 'login.html',
            template: './src/login.html',
        }),


    ],
    output: {
        filename: 'bundle.js',
        path: path.resolve(__dirname, 'dist'),
    },
    module: {
        rules: [
            {
                test: /\.(scss|css)$/,
                use: [
                    'css-loader',
                ]
            },
            {
                test: /\.jpg$/,
                use: [
                    {
                        loader: 'file-loader',
                        options: {},
                    },
                ],
            },
            {
                test: /\.jsx?$/,
                use: 'babel-loader',
                exclude: /node_modules/
            }
        ]
    },
    externals: {
        oimo: 'OIMO', //or true
        cannon: 'CANNON', //or true
        earcut: 'EARCUT' //or true
    }
};

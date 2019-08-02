const path = require('path');
const CleanWebpackPlugin = require('clean-webpack-plugin');
const HtmlWebpackPlugin = require('html-webpack-plugin');

module.exports = {
    watch: false,
    devtool: "source-map",
    // Start of dependency graphs (2 independent graphs)
    entry: {
        kubys: './src/js/Game.js',
        login: './src/login/login.js'
    },
    plugins: [
        new CleanWebpackPlugin(),
        new HtmlWebpackPlugin({
            files: {
                chunks: {
                    kubys: {
                        entry: './src/js/Game.js',
                    },
                    login: {
                        entry: './src/login/login.js',
                    }
                }
            }
        }),
        // 2 entrypoints, linked with associated bundle
        new HtmlWebpackPlugin({
            filename: 'index.html',
            template: './src/index.html',
            favicon: 'kubys_favicon.ico',
            chunks: ['kubys']
        }),
        new HtmlWebpackPlugin({
            filename: 'login.html',
            favicon: 'kubys_favicon.ico',
            template: './src/login/login.html',
            chunks: [ 'login' ]
        })
    ],
    output: {
        path: path.resolve(__dirname, 'dist'),
        filename: '[name].bundle.js'
    },
    module: {
        rules: [
            {
                test: /\.css$|\.scss$/,
                use: ['style-loader', 'css-loader', 'sass-loader']
            },
            {
                test: /\.jpg$|\.ico$/,
                use: [
                    {
                        loader: 'file-loader',
                        options: {},
                    },
                ],
            },
            {
                test: /\.js$/,
                use: 'babel-loader',
                exclude: /node_modules/
            },
            {
                test: /\.(png|svg|woff|woff2|eot|ttf|otf)$/,
                use: [{
                    loader: 'url-loader',
                    options: { limit: 100000 } // in bytes
                }]
            }

        ]
    },
    externals: {
        oimo: 'OIMO', //or true
        cannon: 'CANNON', //or true
        earcut: 'EARCUT' //or true
    }
};

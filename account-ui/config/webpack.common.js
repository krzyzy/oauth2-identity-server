/**
 * @author: @AngularClass
 */

const webpack = require('webpack');
const helpers = require('./helpers');

const ProvidePlugin = require('webpack/lib/ProvidePlugin');
const DefinePlugin = require('webpack/lib/DefinePlugin');
const CommonsChunkPlugin = require('webpack/lib/optimize/CommonsChunkPlugin');

const CopyWebpackPlugin = require('copy-webpack-plugin');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const ForkCheckerPlugin = require('awesome-typescript-loader').ForkCheckerPlugin;
const HtmlElementsPlugin = require('./html-elements-plugin');

const autoprefixer = require('autoprefixer');


const METADATA = {
    title: 'Solidify Account',
    baseUrl: '/account/app/',
    isDevServer: helpers.isWebpackDevServer()
};

module.exports = {
    metadata: METADATA,

    entry: {

        'polyfills': './src/polyfills.browser.ts',
        'vendor': './src/vendor.browser.ts',
        'main': './src/main.browser.ts',
        'style': './src/style.browser.ts',

    },

    resolve: {

        extensions: ['', '.ts', '.js', '.json', '.css', 'less', '.scss', '.html'],
        root: helpers.root('src'),
        modulesDirectories: ['node_modules'],

    },

    module: {
        preLoaders: [
            {
                test: /\.js$/,
                loader: 'source-map-loader',
                exclude: [
                    // these packages have problems with their sourcemaps
                    helpers.root('node_modules/rxjs'),
                    helpers.root('node_modules/@angular'),
                    helpers.root('node_modules/@ngrx'),
                    helpers.root('node_modules/@angular2-material'),
                ]
            }

        ],
        loaders: [
            {
                test: /\.ts$/,
                loaders: ['awesome-typescript-loader', 'angular2-template-loader'],
                exclude: [/\.(spec|e2e)\.ts$/]
            },
            {test: /\.json$/, loader: 'json-loader'},
            {test: /\.css$/, loaders: ['to-string-loader', 'css-loader']},
            {test: /\.less$/, loader: "style!css!less", exclude: /node_modules/,},
            {test: /\.html$/, loader: 'raw-loader', exclude: [helpers.root('src/index.html')]},
            {test: /\.jsp/, loader: 'raw-loader', exclude: [helpers.root('src/login.jsp')]},
            {test: /\.(jpg|png|gif)$/, loader: 'file'},

            {test: /\.scss$/, loaders: ['style', 'css', 'postcss', 'sass']},
            {test: /\.(woff2?|ttf|eot|svg)$/, loader: 'url?limit=10000'},
            {test: /bootstrap-sass[\/\\]assets[\/\\]javascripts[\/\\]/, loader: 'imports?jQuery=jquery'},

            {test: /\.woff(\?v=\d+\.\d+\.\d+)?$/, loader: "url?limit=10000&mimetype=application/font-woff"},
            {test: /\.woff2(\?v=\d+\.\d+\.\d+)?$/, loader: "url?limit=10000&mimetype=application/font-woff"},
            {test: /\.ttf(\?v=\d+\.\d+\.\d+)?$/, loader: "url?limit=10000&mimetype=application/octet-stream"},
            {test: /\.eot(\?v=\d+\.\d+\.\d+)?$/, loader: "file"},
            {test: /\.svg(\?v=\d+\.\d+\.\d+)?$/, loader: "url?limit=10000&mimetype=image/svg+xml"}
        ]

    },

    plugins: [
        new ForkCheckerPlugin(),
        new webpack.optimize.OccurenceOrderPlugin(true),
        new webpack.optimize.CommonsChunkPlugin({name: ['polyfills', 'vendor'].reverse()}),
        new CopyWebpackPlugin([{from: 'src/assets', to: ''}]),
        new HtmlWebpackPlugin({template: 'src/index.html', chunksSortMode: 'dependency'}),
        new HtmlElementsPlugin({headTags: require('./head-config.common')}),
        new ProvidePlugin({
            jQuery: 'jquery',
            $: 'jquery',
            jquery: 'jquery',
            "Tether": 'tether',
            "window.Tether": "tether"
        })
    ],

    node: {
        global: 'window',
        crypto: 'empty',
        process: true,
        module: false,
        clearImmediate: false,
        setImmediate: false
    }

};

// Helper functions

function root(args) {
    args = Array.prototype.slice.call(arguments, 0);
    return path.join.apply(path, [__dirname].concat(args));
}

function rootNode(args) {
    args = Array.prototype.slice.call(arguments, 0);
    return root.apply(path, ['node_modules'].concat(args));
}

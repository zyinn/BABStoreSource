/**
 * @author: @AngularClass
 */

var webpack = require('webpack'),
    util = require("util"),
    path = require('path');

const webpackMerge = require('webpack-merge'); // used to merge webpack configs
const commonConfig = require('./webpack.common.js'); // the settings that are common to prod and dev

const ExtractTextPlugin = require('extract-text-webpack-plugin');

/**
 * Webpack configuration
 *
 * See: http://webpack.github.io/docs/configuration.html#cli
 */
module.exports = function (options) {
    
    var port = process.env.port || 8080;
    
    var outputPath = path.join(__dirname, '../dist');
    
    // console.log(util.format("Setting port: %s in Webpack config.", port));    
    
    return webpackMerge(commonConfig(), {
        
        /**
   * Output
   * Reference: http://webpack.github.io/docs/configuration.html#output
   * Should be an empty object if it's generating a test build
   * Karma will handle setting it up for you when it's a test build
   */
        output: {
            path: outputPath,
            
            // Output path from the view of the page
            // Uses webpack-dev-server in development
            publicPath: util.format('http://localhost:%s/', port),
            
            // Filename for entry points
            // Only adds hash in build mode
            filename: '[name].bundle.js',
            
            // Filename for non-entry points
            // Only adds hash in build mode
            chunkFilename: '[name].bundle.js'
        },
        
        /**
   * Devtool
   * Reference: http://webpack.github.io/docs/configuration.html#devtool
   * Type of sourcemap to use per build type
   */
        devtool: 'eval-source-map',
        
        /**
   * Plugins
   * Reference: http://webpack.github.io/docs/configuration.html#plugins
   * List: http://webpack.github.io/docs/list-of-plugins.html
   */
        plugins: [
            // Reference: https://github.com/webpack/extract-text-webpack-plugin
            // Extract css files
            // Disabled when in test mode or not in build mode
            // new ExtractTextPlugin('[name].[hash].css', { disable: true }),


            new webpack.HotModuleReplacementPlugin(),


            new webpack.NamedModulesPlugin(),
        ],
    });
};



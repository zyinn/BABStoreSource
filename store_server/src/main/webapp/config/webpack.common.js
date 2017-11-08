"use strict";

// Modules
// ReSharper disable UndeclaredGlobalVariableUsing
var webpack = require("webpack"),
  util = require("util"),
  path = require("path");

var autoprefixer = require("autoprefixer");
// var LessPluginCleanCss = require('less-plugin-clean-css');
// ReSharper restore UndeclaredGlobalVariableUsing

const HtmlWebpackPlugin = require("html-webpack-plugin");

module.exports = function makeWebpackConfig() {
    /**
       * Config
       * Reference: http://webpack.github.io/docs/configuration.html
       * This is the object where all configuration gets set
       */
    var config = {};

    /**
       * Entry
       * Reference: http://webpack.github.io/docs/configuration.html#entry
       * Should be an empty object if it's generating a test build
       * Karma will set this when it's a test build
       */
    config.entry = {
        app: ["./src/bab.store.js"]
    };

    /**
       * Loaders
       * Reference: http://webpack.github.io/docs/configuration.html#module-loaders
       * List: http://webpack.github.io/docs/list-of-loaders.html
       * This handles most of the magic responsible for converting modules
       */

    // Initialize module
    config.module = {
        rules: [
          {
              // JS LOADER
              // Reference: https://github.com/babel/babel-loader
              // Transpile .js files using babel-loader
              // Compiles ES6 and ES7 into ES5 code
              test: /\.js$/,
              loader: "babel-loader",
              exclude: /node_modules/
          },
          {
              //    test: /\.css$/,
              //    // Reference: https://github.com/webpack/style-loader
              //    // Use style-loader in development.
              //    use: [
              //        { loader: "style-loader"},
              //        {
              //            loader: 'css-loader', query: { sourceMap: true }
              //        },

              //    ],
              //    exclude: /lib/
              //},
              //{
              //    // CSS LOADER
              //    // Reference: https://github.com/webpack/css-loader
              //    // Allow loading css through js
              //    //
              //    // Reference: https://github.com/postcss/postcss-loader
              //    // Postprocess your css with PostCSS plugins
              //    test: /\.less$/,
              //    // Reference: https://github.com/webpack/style-loader
              //    // Use style-loader in development.
              //    use: [
              //        { loader: "style-loader" },
              //        { loader: 'postcss-loader' },
              //        {
              //            loader: 'css-loader'
              //            // , query: { sourceMap: true }, options: { importLoaders: 1 }
              //        },
              //        {
              //            loader: "less-loader", options: {
              //                strictMath: true,
              //                noIeCompat: true
              //            }
              //        }
              //    ]
              test: /\.less$/,
              loader: "style-loader?sourceMap!css-loader!postcss-loader!less-loader"
          },{
              test: /\.css$/,
              loader: "style-loader?sourceMap!css-loader!postcss-loader"
            },
          {
              // ASSET LOADER
              // Reference: https://github.com/webpack/file-loader
              // Copy png, jpg, jpeg, gif, svg, woff, woff2, ttf, eot files to output
              // Rename the file using the asset hash
              // Pass along the updated reference to your code
              // You can add here any file extension you want to get copied to your output
              test: /\.(png|jpg|jpeg|gif|svg|woff|woff2|ttf|eot)$/,
              loader: "file-loader"
          },
          {
              // HTML LOADER
              // Reference: https://github.com/webpack/raw-loader
              // Allow loading html through js
              test: /\.html$/,
              loader: "raw-loader"
          }
        ]
    };

    var BAB_COMMON_PATH = "D:\\Sumscope\\Bab\\web_framework\\dist\\lib\\bab.common\\".split(path.sep).join('/');

    config.resolve = {
        alias: {
            'angular': "angular/index.js",
            'jquery': 'jquery/dist/jquery.min.js',

            "spin": path.resolve("lib/spin/spin.min.js"),
            "bab.common": path.resolve("lib/bab.common/bab.common.js"),
            "bab.common.css": path.resolve("lib/bab.common/bab.common.css")        
        }
    };

    /**
       * Dev server configuration
       * Reference: http://webpack.github.io/docs/configuration.html#devserver
       * Reference: http://webpack.github.io/docs/webpack-dev-server.html
       */
    config.devServer = {
        contentBase: "./src/public",
        stats: "minimal",
        inline: true,
        hot: true,
        port: 8080
    };

    /**
       * Plugins
       * Reference: http://webpack.github.io/docs/configuration.html#plugins
       * List: http://webpack.github.io/docs/list-of-plugins.html
       */
    config.plugins = [
      new webpack.ProvidePlugin({
          $: "jquery",
          jQuery: "jquery",
          Spinner: "spin"
      }),

      // Reference: https://github.com/ampedandwired/html-webpack-plugin
      // Render index.html
      new HtmlWebpackPlugin({
          template: "./src/public/index.html",
          inject: "body"
      })
    ];

    return config;
};

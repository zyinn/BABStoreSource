(function (require, process) {
    'use strict';
    
    // ReSharper disable InconsistentNaming
    var webpack = require("webpack"),
        WebpackDevServer = require("webpack-dev-server"),
        util = require("util"),
        express = require("express"),
        logger = require('express-log-url'),
        domain = require('express-domain-middleware');
    // ReSharper restore InconsistentNaming
    
    var port = process.env.port || 8080;
    
    console.log(util.format("Setting port: %s in server.", port));
    
    function runDevServer() {
        var config = require("./webpack.config.js");
        
        config.entry.app.unshift("webpack-dev-server/client?http://localhost:8080/");
        
        var compiler = webpack(config);
        
        var server = new WebpackDevServer(compiler, {
            contentBase: "./src/public/",
            stats: { colors: true },
            inline: true,
            hot: true
        });
        
        server.listen(port, "localhost", function () {
            console.log("Listening on " + port);
        });
    };
    
    if (process.argv.length <= 0) {
        console.log("Missing argument. dev or publish." + port);
        return;
    }
    
    if (process.argv[2] === "dev") {
        runDevServer();
        return;
    } 
        
    var app = express();
    
    // 使用该中间件，在下方的处理中捕获异步程序中的异常。
    app.use(domain);
    
    // logger
    app.use(logger);
    
    app.use(/^\/service/, require("./modules/middleware/serviceRouter.js"));
    
    // app.use(express.static('dist'));
    
    app.use(express.static('dist'));
    
    // 一般来说非强制性的错误处理一般被定义在最后
    // 错误处理的中间件和普通的中间件定义是一样的， 只是它必须有4个形参， 这是它的形式： (err, req, res, next):
    app.use(function (err, req, res, next) {
        console.error(err.stack);
        res.send(500, 'Something broke!');
    });
    
    app.listen(port, function () {
        console.log(util.format("Listening on %s", port));
    });

})(require, process);
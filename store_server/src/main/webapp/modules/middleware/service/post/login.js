var colors = require("colors"),
    fs = require("fs");


// ReSharper disable UndeclaredGlobalVariableUsing
module.exports = function () {
    'use strict';
    
    var allowedOrigin = "http://localhost:8080";
    
    var middleware = function (req, res, next) {
        
        var data = '';
        req.addListener('data', function (chunk) {
            data += chunk;
        }).addListener('end', function () {
            data = JSON.parse(data);
            
            res.set({
                "Access-Control-Allow-Origin": allowedOrigin
            });
            
            if (data.password === "LausanneElite") {
                res.status(200).send(JSON.stringify({
                    userName: data.userName,
                    message: 'Login succeed.',
                    token:'superToken'
                }));
            } else {
                res.status(500).send(JSON.stringify({
                    userName: data.userName,
                    message: 'Login failed.'
                }));
            }
            
            // console.log(`Received data $ { JSON.stringify(data) }`)    ;

            res.end();
        });
    }
    
    return middleware;
}();


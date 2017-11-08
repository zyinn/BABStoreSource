'use strict';

const path = require('path'),
  exec = require('child_process').exec;


process.on('uncaughtException', function (err) {
  console.log(err);
});

process.on('unhandledRejection', function (reason, p) {
  console.log("Unhandled Rejection at: Promise ", p, " reason: ", reason);
  // application specific logging, throwing an error, or other logic here
});

const BAB_COMMON_PATH = "D:\\Sumscope\\Bab\\web_framework".split(path.sep).join('/');
const OUTPUTPATH = "D:\\Sumscope\\Bab\\store_sourcecode\\store_server\\src\\main\\webapp\\lib\\bab.common".split(path.sep).join('/');

const COMMAND = `webpack --progress --colors --bail --progress --profile --display-modules`;

var workerProcess = exec(COMMAND, {
  encoding: 'utf8',
  timeout: 0,
  maxBuffer: 200 * 1024,
  killSignal: 'SIGTERM',
  cwd: BAB_COMMON_PATH,
  env: {
    npm_lifecycle_event: 'build',
    output_path: OUTPUTPATH
  }
}, (error, stdout, stderr) => {
  if (error) {
    console.error(`exec error: ${error}`);
    return;
  }
  // console.log(`stdout: ${stdout}`);
  // console.log(`stderr: ${stderr}`);
});

workerProcess.stdout.on('data', function (data) {
  console.log(`stdout: ${data.trim()}`);
});

workerProcess.stderr.on('data', function (data) {
  // console.log(`stderr: ${data.trim()}`);

  var lines = data.split('\n');
  var results = new Array();
  lines.forEach(function (line) {
    // var parts = line.split('=');
    // results[parts[0]] = parts[1];

    console.log(line.trim());
  });
});
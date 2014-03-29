var express = require('express');
var sockjs  = require('sockjs');
var http    = require('http');
var redis   = require('redis');
var path = require('path');

// Initialize system terminal
var sys = require('sys');
var exec = require('child_process').exec;
var term;

// Load configuration
var config = require(path.resolve(__dirname, '../persistence/configuration.json'));

function buildCommand(message) {
	
	
	var manager = config.managers[0];
	var command = manager.cmds.root;
	if (manager.remote)
	{
		command += manager.cmds.connection.host + manager.host + ' ';
		command += manager.cmds.connection.port + manager.port + ' ';
	}
	if (manager.login)
	{
		command += manager.cmds.connection.user + manager.username + ' ';
		command += manager.cmds.connection.pass + manager.password + ' ';
	}
	switch (message.cmd)
	{
		case 'add': 
			command += manager.cmds.add.value;
			if (manager.cmds.add.option)
			{
				command += message.user + ' ';
			}
			command += message.args;
			break;
		case 'status': 
			command += manager.cmds.status.value;
			if (manager.cmds.status.option)
			{
				command += message.user + ' ';
			}
			if (message.args)
			{
				command += message.args;
			}
			break;
	}
	console.log(command);
	return command;
}

function sendCommand(cmd, res) {
	term = exec(cmd, function (error, stdout, stderr) {
        sys.print('stdout: ' + stdout);
        sys.print('stderr: ' + stderr);
        if (error !== null) {
        	console.log('exec error: ' + error);
        	if (res)
        	{
        		res.send({"status": "KO", "comment": "links cannot added to donwload !","stderr":stderr, "error":error});
        	}
        }
        else if (res)
        {
        	res.send({"status": "OK", "comment": "links added to download !", "stdout":stdout});
        }
      });
}

// Redis publisher
var publisher = redis.createClient();

// Sockjs server
var sockjs_opts = {sockjs_url: "http://cdn.sockjs.org/sockjs-0.3.min.js"};
var sockjs_io = sockjs.createServer(sockjs_opts);
sockjs_io.on('connection', function(conn) {
	var browser = redis.createClient();
    browser.subscribe('info_channel');

    // When we see a message on info_channel, send it to the browser
    browser.on("message", function(channel, message){
        conn.write(message);
    });

    // When we receive a message from browser, send it to be published
    conn.on('data', function(data) {
        var message = JSON.parse(data);
        publisher.publish('info_channel', '\\-** New command from user "' + message.user + '" **-/');
        publisher.publish('info_channel', '$ ' + message.cmd + ' ' + message.args);
        // executes client command
        sendCommand(buildCommand(message));
    });
});

// Express server
var app = express();
app.use(express.bodyParser());
var server = http.createServer(app);

sockjs_io.installHandlers(server, {prefix:'/websocket'});

console.log('Listening on '+config.server.ip_addr+':'+config.server.port );
server.listen(config.server.port, config.server.ip_addr);

// Web interface to remote link
app.get('/', function (req, res) {
	res.sendfile(path.resolve(__dirname, '../views/index.html'));
});

// Api to remote link
app.post('/remote', function(req, res) {
	
	var command = req.body;
	command.cmd = "add"; 
	console.log(command);
	sendCommand(buildCommand(command), res);
});

const express = require('express');
const app = express();
var path = require('path');
var admin = require('firebase-admin');
var jwt=require('jsonwebtoken');
var db = require('./controllers/firebaseService').getDb();
var ref = db.ref("lobbies");

const port = process.env.PORT || 5000;

const server = app.listen(port, () => console.log(`Server started on port ${port}`));
const socket = require('socket.io');
var io = socket(server);

var bodyParser = require("body-parser");

app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());

const router=require(path.resolve( __dirname, "./routes"));

app.use('/',router);
app.use(express.json());

// o parte din lobbycontroller(socket-ul). Nu stiu de ce nu merge in lobby controller. Din cauza router-ului cred.

io.on('connection', (socket) => {
	ref.on('child_changed', function(data) {
		socket.emit('changed',JSON.parse(`{"${data.key}": {
	  		"extension": "${data.val().extension}",
			"first": "${data.val().first}",
			"master": "${data.val().master}",
 			"second": "${data.val().second}",
 			"third": "${data.val().third}"
	  		}}`
		));
	});

	ref.on('child_added', function(data) {
		socket.emit('added',JSON.parse(`{"${data.key}": {
	  		"extension": "${data.val().extension}",
	  		"first": "${data.val().first}",
	  		"master": "${data.val().master}",
	 		"second": "${data.val().second}",
	  		"third": "${data.val().third}"
		  	}}`
	  	));
	});
	
	ref.on('child_removed', function(oldChildSnapshot) {
		socket.emit('removed', `{"lobby_key":"${oldChildSnapshot.key}"}`);
	});
});

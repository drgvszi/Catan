var db = require('./firebaseService').getDb();
var ref = db.ref("lobbies");
var path = require('path');
const axios = require('axios');
var all_lobbies = [];

String.prototype.replaceAt=function(index, replacement) {
    return this.substr(0, index) + replacement+ this.substr(index + replacement.length);
}

ref.on("value", function(snapshot) {
	all_lobbies = [];
	snapshot.forEach(function(child) {
		all_lobbies.push({  
			extension: child.val().extension,
			first: child.val().first,
			gameid: child.val().gameid,
			master: child.val().master,
			second: child.val().second,
			third: child.val().third,
			lobbyid : child.key
		});
  	});
});
	

const getLobbies = async(req, res) => {
	res.send({'lobbies' : all_lobbies});
}



function get_user_extension(username) {
    return db.ref('/users/'+username+'/userextension')
    .once('value')
    .then(function(bref) {
        var extension= bref.val();
        return {
            extension: extension
        };
    });
}

function get_lobby_extension(lobbyid) {
    return db.ref('/lobbies/'+lobbyid+'/extension')
    .once('value')
    .then(function(bref) {
        var extension= bref.val();
        return {
            extension: extension
        };
    });
}

function get_game_id(lobbyid) {
	return db.ref('/lobbies/'+lobbyid+'/gameid')
	.once('value')
	.then(function(data) {
		var game_id = data.val();
		return {
			gameid: game_id
		}
	})
}

var lobbies = db.ref("lobbies");

const addLobby = async(req,resp) => {
	var create_game_data = {
	    "username": "catan",
		"password": "catan",
		"command": "newGame",
		"arguments": "{\"scenario\": \"SettlersOfCatan\"}" // sper ca nimeni sa nu faca asa ceva. OMG STRING INSTEAD OF JSON WOOOOOT NICELY DONE
	};
	axios
	  .post('https://catan-engine.herokuapp.com/Catan/managerRequest/', create_game_data)
	  .then(res => {	
	   	if(res.data.code == 200)	{
	   		var game_id = res.data.arguments.slice(11, res.data.arguments.length - 2);
		   	var new_lobby = lobbies.push();
		   	var username = req.body.username;
		   	get_user_extension(req.body.username).then(function(data) {
			   	new_lobby.set({
			   			first : "-",
			   			second: "-",
			   			third: "-",
			   			master : username,
			   			extension : data.extension,
			   			gameid : game_id
			   		});
		   	});
		   
		   	resp.send(`{"lobbyid" : "${new_lobby.key}", "gameid" : "${game_id}"}`);	

		   	var set_maxplayers_data = {
			    "username": "catan",
				"password": "catan",
				"command": "setMaxPlayers",
   				"arguments": "{\"gameId\":\""+game_id+"\",\"maxPlayers\":\"2\"}" 
			};

			axios
				  .post('https://catan-engine.herokuapp.com/Catan/managerRequest/', set_maxplayers_data)
				  .then(res => {	
				   	if(res.data.code == 200)	{
				   		console.log(res.data);
				   		add_player_req_to_ge(game_id, username);
				   	}
				   	else
				   		console.log(res.data);
				  })
				  .catch(error => {
				   	console.error(error);
				  }); 
			
	   	}
	   	else
	   		console.log(res);
	  })
	  .catch(error => {
	    console.error(error)
	  }) 
}

function sleep(ms) {
  return new Promise((resolve) => {
    setTimeout(resolve, ms);
  });
} 


async function add_player_req_to_ge(game_id, username) {
	var add_player_data = {
			    "username": "catan",
			    "password": "catan",
			    "command": "addPlayer",
			    "arguments": "{\"gameId\":\"" + game_id + "\"}"
			};

			axios
				  .post('https://catan-engine.herokuapp.com/Catan/managerRequest/', add_player_data)
				  .then(res => {	
				   	if(res.data.code == 200)	{
				   		console.log(res.data.arguments);
				   		db.ref('users').child(username).update({
				   			"gameengineuserid" : res.data.arguments.slice(13, res.data.arguments.length - 2)
				   		});

				   	}
				   	else
				   		console.log(res.data);
				  })
				  .catch(error => {
				   	console.error(error);
				  });
}

const joinLobby = async(req, res) => {
	var lobby_id = req.body.lobbyid;
	var username = req.body.username;

	var lobby = db.ref("lobbies").child(lobby_id);
	get_user_extension(username).then(function(data) {
		
		var index = 0;
		for(index = 0; index < all_lobbies.length; index++)
			if(lobby_id == all_lobbies[index].lobbyid)
				break;

		if(data.extension == all_lobbies[index].extension) {
			if(all_lobbies[index].first === '-' || all_lobbies[index].second === '-' || all_lobbies[index].third === '-') {
				add_player_req_to_ge(all_lobbies[index].gameid, username);
				if(all_lobbies[index].first === '-') {
					lobby.update({
						"first":username
					});
					res.send(`{"place" : "1", "error" : "-"}`);
					return;
				}
				else if(all_lobbies[index].second === '-'){
					lobby.update({
						"second":username
					});
					res.send(`{"place" : "2", "error" : "-"}`);
					return;
				}
				else {
					lobby.update({
						"third":username
					});
					res.send(`{"place" : "3", "error" : "-"}`);
					return;
				}
			}
			else {
				res.send(`{"place" : "-", "error" : "noplace"}`);
				return;
			}
		}
		res.send(`{"place" : "-", "error" : "diff ext"}`);
		return;
	});
}

const leaveLobby = async(req,res) => {
	var lobby_id = req.body.lobbyid;
	var username = req.body.username;
	var lobby = db.ref("lobbies").child(lobby_id);
	
	if(all_lobbies[lobby_id].master === username) {
		lobby.set(JSON.parse(`{"${lobby_id}": null}`));
	}
	else {
		if(all_lobbies[lobby_id].first === username) {
			lobby.update({
				"first":"-"
			});
		}
		else if(all_lobbies[lobby_id].second === username) {
			lobby.update({
				"second":"-"
			});
		}
		else {
			lobby.update({
				"third":"-"
			});
		}
	}
	res.send("done");
}


const startGame = async(req, response) => {

	await sleep(1000);

	var game_id = req.body.gameid;
	var add_player_data = {
			    "username": "catan",
			    "password": "catan",
			    "command": "startGame",
    			"arguments": "{\"gameId\":\""+ game_id +"\"}"
	};
	axios
		.post('https://catan-engine.herokuapp.com/Catan/managerRequest/', add_player_data)
		.then(res => {	
			console.log(res.data);
			var board = JSON.parse(res.data.arguments.replaceAt(9, " ").replaceAt(res.data.arguments.length - 2, " ").replaceAt(721, " ").replaceAt(731, " ").split("\\").join(""));;
			if(res.data.code == 200) {
				boards = db.ref('boards');
				boards.child(game_id).set({
					data:board
				});
				response.send(board);
		}
			else{
				console.log(res.data);
				response.send(res);
			}
		})
		.catch(error => {
		 	console.error(error);
		 	response.send(error);
		});
}

module.exports={
    getLobbies, addLobby, joinLobby, leaveLobby, startGame
}


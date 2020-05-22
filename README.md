# Catan
# Connectivity

To make Catan a multiplayer game we decided to create an API in Node that will help us to create a layer between C# scripts and Game Engine Module. 


# Node API

[https://github.com/georgiana-ojoc/Catan/tree/master/NodeApi](https://github.com/georgiana-ojoc/Catan/tree/master/NodeApi)
Our Node API has a server.js file that is hosted on heroku. 
### server.js
In server.js we use socketIO to broadcast every request that we receive. 
An example is:
```
socket.on("chat message", (msg) => {

		console.log(msg);

		socket.broadcast.emit("chat message", msg);
});`
```
Here we say that if there is made a request at "chat message" we then take the message and using   const  socket  =  require('socket.io');  broadcast that message at the same route.
We also use var  ref  =  db.ref("lobbies");, that creates a reference to our DB(var  db  =  require('./controllers/firebaseService').getDb();).
Later we use that reference here:
```
io.on('connection', (socket) => {

	ref.on('child_changed', function(data) {

		socket.emit('changed',JSON.parse(`{"lobby": {

					"extension": "${data.val().extension}",

					"first": "${data.val().first}",

					"master": "${data.val().master}",

					"second": "${data.val().second}",

					"third": "${data.val().third}",

					"gameid": "${data.val().gameid}",

					"lobbyid": "${data.key}"}}`
		));

});
```
We use routing and MVC . So in index we require each script that we need for routing.
Each file referes to a type of route. For example we have **lobby.js** that has a lot of other specific routes. An example is  /leaveGame that has attached a function from a specific controller that kicks out user from a game he is in.
```router.post('/leaveGame',  lobbyController.leaveGame);```
So we call a function 
```
const leaveGame = async (req, res) => {

	if (req.body.hasOwnProperty('gameId') && req.body.hasOwnProperty('active') && req.body.hasOwnProperty('playerId')) {

		var request = {

			"username": "catan",

			"password": "catan",

			"command": "changePlayerStatus",

			"arguments": "{\"gameId\":\"" + req.body.gameId + "\",\"active\":\"" + req.body.active + "\",\"playerId\":\"" + req.body.playerId + "\"}"
};

axios.post('https://catan-engine.herokuapp.com/Catan/managerRequest/', request)

.then(function (response) {

	res.send(response.data);

}).catch(function (error) {

		console.log(error);

	});

}

else {

		res.status(400).json({ status: 'error', message: 'Malformed JSON body.Missing required fields' });

	 }

}
```
In this function  we say that if we receive a request that has in its body properties like 'gameId', 'active' and 'playerid' we can then send a request to Game Engine module. We create an JSON object ```request``` with all the properties needed and then using axios we make a POST request at a specific route and then (```.then```) we send the reponse back to the endpoint that made the request in the first place.
If the request comes  with incomplete properties in body, we send back a response that with a specific error (```Malformed JSON body.Missing required fields```).

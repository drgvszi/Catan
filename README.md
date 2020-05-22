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
# C# Scripts
We use C# Scripts to bind our NodeAPI with the Game Interface.
In general this scripts are attached to a button or are called when 
## LobbyHandler.cs
This is one of the most complex scripts. It is used to handle playes activity in the lobby (entering or leaving).
### EmitStartGame()
EmitStartGame() is a function used to handle when a team of players finished waiting in a lobby and a the master presses Start Game. When this happens a board should pe rendered. To do so, we create a request to out NodeApi that then makes a request to Game Engine module.
```
if (ok == false)
{

	ok = true;

	GameIDConnectivityJson gameid = new GameIDConnectivityJson();

	gameid.gameid = LoginScript.CurrentUserGameId;

	RestClient.Post<BoardConnectivityJson>("https://catan-connectivity.herokuapp.com/lobby/startgame", gameid).Then(board =>

	{

		ReceiveBoardScript.ReceivedBoard.ports = board.ports;

		ReceiveBoardScript.ReceivedBoard.board = board.board;

		JSONObject json_message = new JSONObject();

		json_message.AddField("lobbyid", LoginScript.CurrentUserLobbyId);

		socket.Emit("gamestart", json_message);

		SceneChanger n = new SceneChanger();

		n.startGame();

	}).Catch(err => { Debug.Log(err); });

}
```
We use RestClient to make requests. How it works is that you specify what type of request you are making and then manage the response. It uses  a ```Serializable``` class. In this example it is called ```BoardConnectivityJson``` and it looks like this:
```
[Serializable]

public class BoardConnectivityJson

{

	public string[] ports = new string[256];

	public HexagonConnectivityJson[] board = new HexagonConnectivityJson[256];

}
```
So after the request is done, the response is deserialized in a class like that one.
What is important is that the properties a class has should match what comes in request's response. For example a response from NodeAPI comes like this:
```
{ "code" : "200", "arguments" : "{"ports":"["None","None","None","None","None","None","None","None","None","None","None","None","None","None","None","None","None","None","None","None","None","None","None","None","None","None","Brick","Brick","None","Ore","Ore","None","None","Wool","Wool","None","Lumber","Lumber","None","None","ThreeForOne","ThreeForOne","None","ThreeForOne","ThreeForOne","None","Grain","Grain","None,"ThreeForOne","ThreeForOne","None","ThreeForOne","ThreeForOne","None","None","None","None","None","None","None","None","None","None","None","None","None","None","None","None","None","None"]","board":"[{"resource":"grain","number":12},{"resource":"wool","number":4},{"resource":"brick","number":11},{"resource":"ore","number":6},{"resource":"lumber","number":9},{"resource":"ore","number":11},{"resource":"brick","n umber":5},{"resource":"grain","number":8},{"resource":"wool","number":3},{"resource":"brick","number":8},{"resource":"lumber","number":3}, {"resource":"wool","number":9},{"resource":"grain","number":5},{"res ource":"ore","number":4},{"resource":"grain","number":2},{"resource" :"wool","number":6},{"resource":"desert","number":0},{"resource":"lu mber","number":10},{"resource":"lumber","number":10}]"}", "status" : "The game has started successfully." }
```
How it works is that is that the class ```BoardConnectivityJson``` has a list of strings where ports will be deserialized and for arguments is used a list of ```HexagonConnectivityJson``` objects:
```
[Serializable]

public class HexagonConnectivityJson

{

public string resource;

public int number;

}
```
Where we have a field resources in arguments, it will be mapped to HexagonConnectivityJson.resource and so will happen to number.
### SocketIO in C# scripts

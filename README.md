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
In general this scripts are attached to a button or are called when in need.
[https://github.com/georgiana-ojoc/Catan/tree/Connectivity/Code/Scripts](https://github.com/georgiana-ojoc/Catan/tree/Connectivity/Code/Scripts)
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
If we broadcast information in the server side (NodeAPI) we should do that also in C#.
For example a more complex functionality is trade between players. We have a function 
```
public static void acceptTrade(string CurrentUserGame, string CurrentUserId)

{

	GameObject go = GameObject.Find("SocketIO");

	socket = go.GetComponent<SocketIOComponent>();

	TradePlayerJson command = new TradePlayerJson();

	command.gameId = CurrentUserGame;

	command.playerId = CurrentUserId;

	RequestJson req = new RequestJson();

	RestClient.Post<RequestJson>("https://catan-connectivity.herokuapp.com/game/wantToTrade", command).Then(Response =>

	{

		JSONObject json_message = new JSONObject();
		json_message.AddField("lobbyid", LoginScript.CurrentUserLobbyId);

		json_message.AddField("username", LoginScript.CurrentUser);

		json_message.AddField("gameEngineId", LoginScript.CurrentUserGEId);

		json_message.AddField("wantToTrade","true");

		socket.Emit("wantToTrade", json_message);

	}).Catch(err => { Debug.Log(err); });

}

```
What happens is that we create  JSON that will be emited, as I said before, to our server socketIO functionality. 
That JSON contains all the fields that we want to be sent to the rest of the users (an user can't emit an event to himself). 
In a ```SocketIoscript``` script we create an object (```GameObject```) that imports a SocketIO. We set the users that should receive data from emitted events.
Here is an example on how we will receive information about who accepted the trade
```
socket.On("wantToTrade", (E) =>

{

	if (E.data[0].str == LoginScript.CurrentUserLobbyId)

	{

		if(E.data[3].str=="false")

		{

			trade--;

		}

		else

		{

			trade+=10;

		}

		if (trade > 0)

		{

			trade = 0;

			TradePlayerJson command = new TradePlayerJson();

			command.gameId = LoginScript.CurrentUserGameId;

			command.playerId = LoginScript.CurrentUserGEId;

			RequestJson req = new RequestJson();

			RestClient.Post<RequestJson>("https://catan-connectivity.herokuapp.com/game/sendPartners", command).Then(Response =>

			{


				SelectedPartener commandSelect = new SelectedPartener();

				commandSelect.gameId = LoginScript.CurrentUserGameId;

				commandSelect.playerId = LoginScript.CurrentUserGEId;

				commandSelect.player = Response.arguments.player_0;


				RequestJson request = new RequestJson();

				RestClient.Post<RequestJson>("https://catan-connectivity.herokuapp.com/game/selectPartner", commandSelect).Then(Response2 =>

				{

					Text txt = FindTextFiel.find();

					txt.text = Response2.status;

					if(Response2.code==200)

					{

						JSONObject json_message = new JSONObject();

						json_message.AddField("lobbyid", LoginScript.CurrentUserLobbyId);

						socket.Emit("UpdateResource", json_message);

						MakeRequestResponse command1 = new MakeRequestResponse();

						command1.gameId = LoginScript.CurrentUserGameId;

						command1.playerId = LoginScript.CurrentUserGEId;

						RequestJson req1 = new RequestJson();

						RestClient.Post<UpdateJson>("https://catan-connectivity.herokuapp.com/game/update", command1).Then(Response1 =>

						{


							lumber.text = Response1.arguments.lumber.ToString();

							ore.text = Response1.arguments.ore.ToString();

							grain.text = Response1.arguments.grain.ToString();

							brick.text = Response1.arguments.brick.ToString();

							wool.text = Response1.arguments.wool.ToString();

							}).Catch(err => { Debug.Log(err); });

						}

					}).Catch(err => { Debug.Log(err); });

			}).Catch(err => { Debug.Log(err); });

		}

		if(trade==-3)

		{

			trade = 0;

			TradePlayerJson command = new TradePlayerJson();

			command.gameId = LoginScript.CurrentUserGameId;

			command.playerId = LoginScript.CurrentUserGEId;

			RequestJson req = new RequestJson();

			RestClient.Post<RequestJson>("https://catan-connectivity.herokuapp.com/game/sendPartners", command).Then(Response =>

			{

				SelectedPartener commandSelect = new SelectedPartener();

				commandSelect.gameId = LoginScript.CurrentUserGameId;

				commandSelect.playerId = LoginScript.CurrentUserGEId;

				commandSelect.player = null;

				RequestJson request = new RequestJson();

				RestClient.Post<RequestJson>("https://catan-connectivity.herokuapp.com/game/selectPartner", commandSelect).Then(Response2 =>

				{

					Debug.Log("Nobody selected to trade");

				}).Catch(err => { Debug.Log(err); });

		}).Catch(err => { Debug.Log(err); });

		}

	}

});
```
Here we listen when an event comes to "wantToTrade". When an event happens, data sent in JSON can be taken using E.data[i], where i represents an order keys in json have where are added.
Trade is a variable we use to check what kind of request was made. If the field ```wantToTrade``` is set to false, it means that someone declined the trade. Otherwise, if the trade >0 it means that there is someone that wants to trade. We take the first player that accepts and sent him in a request to Game Engine Module via NodeAPI. Both requests are similar. The difference is made by the fact that if we want to send a declined request, we sent a null playerId, otherwise we send  the playerId that first checked the button. After the trade is done we make an update request to render the new resources an user has.
### Tests for NodeAPI
[https://github.com/georgiana-ojoc/Catan/blob/Connectivity/NodeApi/test/unitTests.js](https://github.com/georgiana-ojoc/Catan/blob/Connectivity/NodeApi/test/unitTests.js)

For Tests we used different frameworks like Mocha and an assertion library that is often used alongside Mocha, Chai.
How it works is that we randomize data for fake players.
```
const players=[

	{email:Math.random().toString(36).slice(2),username:Math.random().toString(36).slice(2), password:Math.random().toString(36).slice(2)},

	{email:Math.random().toString(36).slice(2),username:Math.random().toString(36).slice(2), password:Math.random().toString(36).slice(2)},

	{email:Math.random().toString(36).slice(2),username:Math.random().toString(36).slice(2), password:Math.random().toString(36).slice(2)},

	{email:Math.random().toString(36).slice(2),username:Math.random().toString(36).slice(2), password:Math.random().toString(36).slice(2)}

]
```
Mocha uses describe() as a method to say how or what the test is supposed to do.
This is how a register functionality is tested using Mocha:
```
describe('register unit test',function(){
	it('returns OK on succesfull register',function(done){

	for(var index=0;index<players.length;index++)

	{

		request.post({url:baseUrl+'/auth/register/',

		json:{

			email:players[index].email,

			username:players[index].username,

			password:players[index].password,

			confirmpassword:players[index].password

		},

		timeout:300000},

		function(error,response,body){

			expect(response.statusCode).to.equal(200);

		});

	}
	done();});

});
```
To check if the result of the function is the result we are looking for (if the function was successful) we will use Chai.
```
var  expect=require("chai").expect;
```
Its ```expect``` keyword to compare the result of our feature’s implementation and the result we _expect_ to get.
Because we are testing a server's response via HTTP protocol, we can use request.
```
var  request=require("request");
request.post({url:baseUrl+'/auth/register/', json:{ 
	email:players[index].email, 
	username:players[index].username, 
	password:players[index].password, 	
	confirmpassword:players[index].password }, 
	timeout:300000}, 
	function(error,response,body){ 	
			expect(response.statusCode).to.equal(200); 
		});
```
```request``` requires 2 parameters. First is the url we are making the request to. In this case we attach a json to out reques, so we wrap the parameter in {}. The second parameter is a function that will be executed after the request is done. The in the callback function we set our expectations.
```
expect(response.statusCode).to.equal(200)
```
If the statusCode is actually 200, the test will be succesfully.

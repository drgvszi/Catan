# Build First Two Settlements and Roads
``` 
{
 "gameId": "gameId", 
 "playerId": "playerId", 
 "command": "buildSettlement", 
 "arguments":
 {
  "intersection": "number of intersection (integer)" 
 } 
}
```
 - works only in the first two rounds 
```
{ 
 "code": "HttpStatus code", 
 "status": "message (success or error type)", 
 "arguments": null 
}
```
```
{ 
 "gameId": "gameId", 
 "playerId": "playerId", 
 "command": "buildRoad", 
 "arguments":
 { 
  "start": "number of first intersection (integer)", 
  "end": "number of last intersection (integer)" 
 } 
}
```
 - should be after ```buildSettlement```
 - the intersections order does not matter
```
{ 
 "code": "HttpStatus code", 
 "status": "message (success or error type)", 
 "arguments": null 
}
```
# Dice
```
{ 
 "gameId": "gameId", 
 "playerId": "playerId", 
 "command": "rollDice", 
 "arguments": null 
}
```
 - works only after the turn starts (after ```startGame``` or ```endTurn```)
``` 
{ 
 "code": "HttpStatus code", 
 "status": "message (success or error type)", 
 "arguments":
 { 
  "dice_0": "second dice value (integer)", 
  "dice_1": "first dice value (integer)", 
  "player_0": "playerId", 
  "lumber_0": "number of received lumbers (integer)", 
  "wool_0": "number of received wools (integer)", 
  "grain_0": "number of received grains (integer)", 
  "brick_0": "number of received bricks (integer)", 
  "ore_0": "number of received ores (integer)", 
  "resourcesToDiscard_0": "0 or half (rounded down) if rolled seven and has 8 or more resources (integer)", 
  "player_1": "playerId", 
  "lumber_1": "number of received lumbers (integer)", 
  "wool_1": "number of received wools (integer)", 
  "grain_1": "number of received grains (integer)", 
  "brick_1": "number of received bricks (integer)", 
  "ore_1": "number of received ores (integer)", 
  "resourcesToDiscard_1": "0 or half (rounded down) if rolled seven and has 8 or more resources (integer)" 
 }
}
```
 -  ```_0``` (or ```_1```, ```_2```, ```_3```) groups information for each player
 - ```player_0``` contains the player identifier for which the information with ```_0``` will be about (and so on)
 - if the dice sum is not seven, ```resourcesToDiscard``` is 0
 - if the dice sum is seven, ```lumber```, ```wool```, ```grain```, ```brick```, ```ore```  are 0
```
{ 
 "gameId": "gameId", 
 "playerId": "playerId", 
 "command": "discardResources", 
 "arguments":
 { 
  "lumber": "number of lumbers to discard (integer)", 
  "wool": "number of wools to discard (integer)", 
  "grain": "number of grains to discard (integer)", 
  "brick": "number of bricks to discard (integer)", 
  "ore": "number of ores to discard (integer)" 
 } 
}
```
 - for example, if ```resourcesToDiscard_0``` is greater than 0 (the dice sum is seven), the player with the identifier from ```player_0``` must send ```discardResources```, otherwise the game will not continue (available for all players)
``` 
{ 
 "code": "HttpStatus code", 
 "status": "message (success or error type)", 
 "arguments":
 { 
  "sentAll": "true or false (boolean)" 
 } 
}
```
 - if the code is 200, the mentioned resources are moved from the player to the bank
 - if the code is 202, the player must send again ```discardResources```
 - if ```sentAll``` is true, the game can continue
# Robber
``` 
{ 
 "gameId": "gameId", 
 "playerId": "playerId", 
 "command": "moveRobber", 
 "arguments":
 { 
  "tile": "a number between 0 and 18 (integer)" 
 } 
}
```
 - if the dice sum is seven, the current player must send ```moveRobber```, otherwise the game will not continue
```
{ 
 "code": "HttpStatus code", 
 "status": "message (success or error type)", 
 "arguments":
 { 
 "0": "playerId from which to steal a resource", 
 "1": "playerId from which to steal a resource" 
 } 
}
```
 - if the code is 200, the robber is moved on the requested tile
 - the players identified by these players identifier have buildings on the tile where the robber was moved
``` 
{ 
 "gameId": "gameId", 
 "playerId": "playerId", 
 "command": "stealResource", 
 "arguments":
 { 
  "player": "playerId" 
 } 
}
```
 - ```player``` should contain the player identifier from the ```moveRobber``` response arguments
 - if the current player moved the robber or uses the Knight development card, he must send ```stealResource```, otherwise the game will not continue
 - if the code is 200, the stolen resource is moved from the selected player to the current player
```
{ 
 "code": "HttpStatus code", 
 "status": "message (success or error type)", 
 "arguments":
 { 
  "resource": "random stolen resource" 
 } 
}
```
 - if the code is 200, the stolen resource is moved from the selected player to the current player
# Trade
```
{ 
 "gameId": "gameId", 
 "playerId": "playerId", 
 "command": "playerTrade", 
 "arguments":
 { 
  "lumber_o": "number of lumbers to offer (integer)", 
  "wool_o": "number of wools to offer (integer)", 
  "grain_o": "number of grains to offer (integer)", 
  "brick_o": "number of bricks to offer (integer)", 
  "ore_o": "number of ores to offer (integer)", 
  "lumber_r": "number of requested lumbers (integer)", 
  "wool_r": "number of requested wools (integer)", 
  "grain_r": "number of requested grains (integer)", 
  "brick_r": "number of requested bricks (integer)", 
  "ore_r": "number of requested ores (integer)" 
 } 
}
```
 - ```playerTrade``` can be sent whenever between ```rollDice``` and ```endTurn``` (if there is no special case)
```
{ 
 "code": "HttpStatus code", 
 "status": "message (success or error type)", 
 "arguments": null 
}
```
``` 
{ 
 "gameId": "gameId", 
 "playerId": "playerId", 
 "command": "wantToTrade", 
 "arguments": null 
}
```
 - the current player is not allowed to send ```wantToTrade```
```
{ 
  "code": "HttpStatus code", 
 "status": "message (success or error type)", 
 "arguments": null 
}
```
``` 
{ 
 "gameId": "gameId", 
 "playerId": "playerId", 
 "command": "sendPartners", 
 "arguments": null 
}
```
 - the current player must send ```sendPartner``` if he sent ```playerTrade```
``` 
{ 
 "code": "HttpStatus code", 
 "status": "message (success or error type)", 
 "arguments":
 { 
  "player_0": "playerId", 
  "player_1": "playerId" 
 } 
}
```
``` 
{ 
 "gameId": "gameId", 
 "playerId": "playerId", 
 "command": "selectPartner", 
 "arguments":
 { 
  "player": "playerId" 
 } 
}
```
 - the current player must send ```selectPartner``` if he sent ```sendPartners```
 - ```player``` should contain the player identifier from the ```sendPartners``` response arguments
```
{ 
 "code": "HttpStatus code", 
 "status": "message (success or error type)", 
 "arguments": null 
}
```
 - if the code is ```200```, the mentioned resources in ```playerTrade``` are transferred from the current player to the selected player and vice versa
``` 
{ 
 "gameId": "gameId", 
 "playerId": "playerId", 
 "command": "noPlayerTrade", 
 "arguments":
 { 
  "port": "-1 if with bank or the number of the intersection of a port (integer)", 
  "offer": "offered resource", 
  "request": "requested resource" 
 } 
}
```
 - ```noPlayerTrade``` is for bank trade (if sent port is -1) and for port trade otherwise
```
{ 
 "code": "HttpStatus code", 
 "status": "message (success or error type)", 
 "arguments": null 
}
```
 - if the code is ```200```, the mentioned resources are transferred from the current player to the bank and vice versa
# Buy Properties
```
{ 
 "gameId": "gameId", 
 "playerId": "playerId", 
 "command": "buyRoad", 
 "arguments":
 { 
  "start": "number of first intersection (integer)", 
  "end": "number of last intersection (integer)" 
 } 
}
```
 - ```buyRoad``` can be sent whenever between ```rollDice``` and ```endTurn``` (if there is no special case)
```
{ 
 "code": "HttpStatus code", 
 "status": "message (success or error type)", 
 "arguments": null 
}
```
```
{ 
 "gameId": "gameId", 
 "playerId": "playerId", 
 "command": "buySettlement", 
 "arguments":
 { 
  "intersection": "number of intersection (integer)" 
 } 
 }
 ```
 - ```buySettlement``` can be sent whenever between ```rollDice``` and ```endTurn``` (if there is no special case)
```
{ 
 "code": "HttpStatus code", 
 "status": "message (success or error type)", 
 "arguments": null 
}
```
```
{ 
 "gameId": "gameId", 
 "playerId": "playerId", 
 "command": "buyCity", 
 "arguments":
 { 
   "intersection": "number of intersection (integer)" 
 } 
}
```
 - ```buyCity``` can be sent whenever between ```rollDice``` and ```endTurn``` (if there is no special case)
```
{ 
 "code": "HttpStatus code", 
 "status": "message (success or error type)", 
 "arguments": null 
}
```
# Developments
``` 
{ 
 "gameId": "gameId", 
 "playerId": "playerId", 
 "command": "buyDevelopment", 
 "arguments": null 
}
```
 - ```buyDevelopment``` can be sent whenever between ```rollDice``` and ```endTurn``` (if there is no special case)
```
{ 
 "code": "HttpStatus code", 
 "status": "message (success or error type)", 
 "arguments":
 {  
  "development": "random bought development"  
 }
}
```
```
{ 
 "gameId": "gameId", 
 "playerId": "playerId", 
 "command": "useDevelopment", 
 "arguments":
 { 
  "development": "knight or monopoly or roadBuilding or yearOfPlenty (just one per request)" 
 } 
}
```
 - ```useDevelopment``` can be sent whenever between ```rollDice``` and ```endTurn``` (if there is no special case)
 - for ```knight```, the current player must send next ```moveRobber``` and ```stealResource``` requests
 - for ```monopoly```, the current player must send next ```takeResourceFromAll``` request
 - for ```roadBuilding```, the current player must send next two valid ```buildDevelopmentRoad``` requests
 - for ```yearOfPlenty```, the current player must send next ```takeTwoResources``` request
```
{ 
 "code": "HttpStatus code", 
 "status": "message (success or error type)", 
 "arguments": null 
}
```
``` 
{ 
 "gameId": "gameId", 
 "playerId": "playerId", 
 "command": "takeResourceFromAll", 
 "arguments":
 { 
  "resource": "requested resource" 
 } 
}
```
 - ```takeResourceFromAll``` must be sent after a valid ```useDevelopment``` with ```monopoly``` request
``` 
{ 
 "code": "HttpStatus code", 
 "status": "message (success or error type)", 
 "arguments":
 { 
  "player_0": "playerId", 
  "resources_0": "number of resources to steal (integer)", 
  "player_1": "playerId", 
  "resources_1": "number of resources to steal (integer)" 
 } 
}
``` 
 - ```resources_0``` contains the number of resources of the requested type that were stolen from ```player_0```
 - if the code is 200, all the resources of the requested type are moved from all the players who have ```resources``` greater than 0 to the current player
```
{ 
 "gameId": "gameId", 
 "playerId": "playerId", 
 "command": "buildDevelopmentRoad", 
 "arguments":
 { 
  "start": "number of first intersection", 
  "end": "number of last intersection" 
 } 
}
```
 - this request must be sent after a valid ```useDevelopment``` with ```roadBuilding``` request
```
{ 
 "code": "HttpStatus code", 
 "status": "message (success or error type)", 
 "arguments": null 
}
```
```
{ 
 "gameId": "gameId", 
 "playerId": "playerId", 
 "command": "takeTwoResources", 
 "arguments":
 { 
  "resource_0": "second requested resource", 
  "resource_1": "first requested resource" 
 } 
}
```
  - this request must be sent after a valid ```useDevelopment``` with ```yearOfPlenty``` request
 ``` 
{ 
 "code": "HttpStatus code", 
 "status": "message (success or error type)", 
 "arguments": null 
}
```
 - if the code is 200, the requested resources are moved from the bank to the current player
# Update
``` 
{ 
 "gameId": "gameId", 
 "playerId": "playerId", 
 "command": "update", 
 "arguments": null 
}
```
 - ```update``` can be sent whenever
```
{ 
 "code": "HttpStatus code", 
 "status": "message (success or error type)", 
 "arguments":
 { 
  "canBuyRoad": "true or false (boolean)", 
  "canBuySettlement": "true or false (boolean)", 
  "canBuyCity": "true or false (boolean)", 
  "canBuyDevelopment": "true or false (boolean)", 
  "availableSettlementPositions":
  [ 
   "number of available intersection (integer)", 
   "number of available intersection (integer)" 
  ], 
  "availableCityPositions":
  [ 
   "number of available intersection (integer)", 
   "number of available intersection (integer)" 
  ], 
  "availableRoadPositions":
  [ 
   [ 
    "number of start intersection (integer)", 
    "number of end intersection (integer)" 
   ], 
   [ 
    "number of start intersection (integer)", 
    "number of end intersection (integer)" 
   ] 
  ], 
  "hasLargestArmy": "true or false (boolean)", 
  "hasLongestRoad": "true or false (boolean)", 
  "publicScore": "number of public points, without Victory Points development cards (integer)", 
  "hiddenScore": "number of public points, with Victory Points development cards (integer)" 
 } 
}
```
 - ```canBuy``` refers to ```affords```
 - ```available``` refers to positions that respect the game rules
# Turn
``` 
{ 
 "gameId": "gameId", 
 "playerId": "playerId", 
 "command": "endTurn", 
 "arguments": null 
}
```
 -  ```endTurn``` can be sent whenever after ```rollDice```
```
{ 
 "code": "HttpStatus code", 
 "status": "message (success or error type)", 
 "arguments": null 
} 
```
# Dice
## Error messages
 - You do not have more than seven resource cards in order to discard half of them.
## Success messages
 - The dice sum is seven.
 - The dice sum is not seven.
# Player 
## Player Resource Cards
### Error Messages
#### Not Enough Resource Cards
 - You do not have enough Lumber resource cards.
 - You do not have enough Wool resource cards.
 - You do not have enough Grain resource cards.
 - You do not have enough Brick resource cards.
 - You do not have enough Ore resource cards.
#### No Resource Card
 - You do not have Lumber resource cards.
 - You do not have Wool resource cards.
 - You do not have Grain resource cards.
 - You do not have Brick resource cards.
 - You do not have Ore resource cards.
## Player Development Cards
### Error Messages
- You do not have Knight development cards.
- You do not have Monopoly development cards.
- You do not have Road Building development cards.
- You do not have Year of Plenty development cards.
### Success Messages
- The development was bought successfully.
- You can use Knight development card.
- You can use Monopoly development card.
- You can use Road Building development card.
- You can use Year of Plenty development card.
- The resource cards were stolen successfully.
- The road was built successfully.
- The resource cards were taken successfully.
## Player Turn
### Error Messages
- It is not your turn.
### Success Messages
- The turn was changed successfully.
# Bank
## Bank Resource Cards
### Error Messages
#### Not Enough Resource Cards
- The bank does not have enough Lumber resource cards.
- The bank does not have enough Wool resource cards.
- The bank does not have enough Grain resource cards.
- The bank does not have enough Brick resource cards.
- The bank does not have enough Ore resource cards.
#### No Resource Card
- The bank does not have Lumber resource cards.
- The bank does not have Wool resource cards.
- The bank does not have Grain resource cards.
- The bank does not have Brick resource cards.
- The bank does not have Ore resource cards.
## Bank Development Cards
### Error Messages
- The bank does not have any development cards.
- The bank does not have Knight development cards.
- The bank does not have Monopoly development cards.
- The bank does not have Road Building development cards.
- The bank does not have Year of Plenty development cards.
## Bank Roads
### Error Messages
- You do not have roads in bank.
- You do not have settlements in bank.
- You do not have cities in bank.
# Roads
## Error Messages
- Invalid position for road.
- Road already existent.
- You have no more roads to build.
- You do not connect to one of your roads.
## Success Messages
- The road was built successfully.
- The road was bought successfully.
# Settlements
## Error Messages
- Invalid position for settlement.
- Intersection already occupied.
- You have no more settlements to build.
- The two roads distance rule is not satisfied.
- You do not connect to one of your roads.
## Success Messages
- The settlement was built successfully.
- The settlement was bought successfully.
# Cities
## Error Messages
- Invalid position for city.
- You have no more cities to build.
## Success Messages
- The city was bought successfully.
# Robber
## Error Messages
- You can not let the robber on the same tile.
- You can not steal a resource card from yourself.
- The player does not have resource cards.
## Success Messages
- The robber was moved successfully.
- The resource card was stolen successfully.
# Trade
## Player Trade
### Error Messages
- Invalid trade request.
- No trade available.
- You are already in trade.
- The selected player is not in trade.
- The offer does not match the port.
### Success Messages
- The trade has started successfully.
- The trade partners were sent successfully.
- The trade was made successfully.
## No Player Trade
### Success Messages
- The trade was made successfully.
# Game
## Success Messages
- The game has ended successfully.
# Requests
## Error Messages
- Invalid request.
- Forbidden request.
# Automaton
## Error Messages
- The message has no assigned action.

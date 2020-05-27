var path = require('path');
var bodyParser = require("body-parser");
const axios = require('axios');

const buildSettlement = async(req, res) => {

    if(req.body.hasOwnProperty('gameId') && req.body.hasOwnProperty('playerId') && req.body.hasOwnProperty('intersection') ) {
        axios.post('https://catan-engine.herokuapp.com/Catan/userRequest/', {
            gameId: req.body.gameId,
            playerId: req.body.playerId,
            command: 'buildSettlement',
            arguments: {
                intersection: req.body.intersection
            }
          })
          .then(function (response) {
            res.send(response.data);
          })
          .catch(function (error) {
            console.log(error);
          });
    }
    else {
        res.status(400).json({status : 'error', message: 'Malformed JSON body.Missing required fields'});
    }
}


const buySettlement = async(req, res) => {

  if(req.body.hasOwnProperty('gameId') && req.body.hasOwnProperty('playerId') && req.body.hasOwnProperty('intersection') ) {
      axios.post('https://catan-engine.herokuapp.com/Catan/userRequest/', {
          gameId: req.body.gameId,
          playerId: req.body.playerId,
          command: 'buySettlement',
          arguments: {
              intersection: req.body.intersection
          }
        })
        .then(function (response) {
          res.send(response.data);
        })
        .catch(function (error) {
          console.log(error);
        });
  }
  else {
      res.status(400).json({status : 'error', message: 'Malformed JSON body.Missing required fields'});
  }
}


const buildRoad = async(req, res) => {

  if(req.body.hasOwnProperty('gameId') && req.body.hasOwnProperty('playerId') && req.body.hasOwnProperty('start') && req.body.hasOwnProperty('end')) {
      axios.post('https://catan-engine.herokuapp.com/Catan/userRequest/', {
          gameId: req.body.gameId,
          playerId: req.body.playerId,
          command: 'buildRoad',
          arguments: {
              start:req.body.start,
              end:req.body.end
          }
        })
        .then(function (response) {
          res.send(response.data);
        })
        .catch(function (error) {
          console.log(error);
        });
  }
  else {
      res.status(400).json({status : 'error', message: 'Malformed JSON body.Missing required fields'});
  }
}


const buyRoad = async(req, res) => {
  if(req.body.hasOwnProperty('gameId') && req.body.hasOwnProperty('playerId') && req.body.hasOwnProperty('start') && req.body.hasOwnProperty('end')) {
      axios.post('https://catan-engine.herokuapp.com/Catan/userRequest/', {
          gameId: req.body.gameId,
          playerId: req.body.playerId,
          command: 'buyRoad',
          arguments: {
              start:req.body.start,
              end:req.body.end
          }
        })
        .then(function (response) {
          res.send(response.data);
        })
        .catch(function (error) {
          console.log(error);
        });
  }
  else {
      res.status(400).json({status : 'error', message: 'Malformed JSON body.Missing required fields'});
  }
}

const buyCity = async (req, res) => {

  if (req.body.hasOwnProperty('gameId') && req.body.hasOwnProperty('playerId') && req.body.hasOwnProperty('intersection')) {
    axios.post('https://catan-engine.herokuapp.com/Catan/userRequest/', {
      gameId: req.body.gameId,
      playerId: req.body.playerId,
      command: 'buyCity',
      arguments: {
        intersection: req.body.intersection
      }
    })
      .then(function (response) {
        res.send(response.data);
      })
      .catch(function (error) {
        console.log(error);
      });
  }
  else {
    res.status(400).json({ status: 'error', message: 'Malformed JSON body.Missing required fields' });
  }
}

const rollDice = async(req, res) => {
  if(req.body.hasOwnProperty('gameId') && req.body.hasOwnProperty('playerId')) {
      axios.post('https://catan-engine.herokuapp.com/Catan/userRequest/', {
          gameId: req.body.gameId,
          playerId: req.body.playerId,
          command: 'rollDice',
          arguments: {
          }
        })
        .then(function (response) {
          res.send(response.data);
        })
        .catch(function (error) {
          console.log(error);
        });
  }
  else {
      res.status(400).json({status : 'error', message: 'Malformed JSON body.Missing required fields'});
  }
}

const moveRobber = async (req, res) => {

  if (req.body.hasOwnProperty('gameId') && req.body.hasOwnProperty('playerId') && req.body.hasOwnProperty('tile')) {
    axios.post('https://catan-engine.herokuapp.com/Catan/userRequest/', {
      gameId: req.body.gameId,
      playerId: req.body.playerId,
      command: 'moveRobber',
      arguments: {
        tile: req.body.tile
      }
    })
      .then(function (response) {
        res.send(response.data);
      })
      .catch(function (error) {
        console.log(error);
      });
  }
  else {
    res.status(400).json({ status: 'error', message: 'Malformed JSON body.Missing required fields' });
  }
}

const stealResource = async (req, res) => {

  if (req.body.hasOwnProperty('gameId') && req.body.hasOwnProperty('playerId') && req.body.hasOwnProperty('answer') && req.body.hasOwnProperty('player')) {
    axios.post('https://catan-engine.herokuapp.com/Catan/userRequest/', {
      gameId: req.body.gameId,
      playerId: req.body.playerId,
      command: 'stealResource',
      arguments: {
        answer: req.body.answer,
        player: req.body.player
      }
    })
      .then(function (response) {
        res.send(response.data);
      })
      .catch(function (error) {
        console.log(error);
      });
  }
  else {
    res.status(400).json({ status: 'error', message: 'Malformed JSON body.Missing required fields' });
  }
}

const playerTrade = async (req, res) => {

  if (req.body.hasOwnProperty('gameId') && req.body.hasOwnProperty('playerId') && req.body.hasOwnProperty('lumber_o') && req.body.hasOwnProperty('wool_o') && req.body.hasOwnProperty('grain_o') 
  && req.body.hasOwnProperty('brick_o') && req.body.hasOwnProperty('ore_o') && req.body.hasOwnProperty('lumber_r') && req.body.hasOwnProperty('wool_r') && req.body.hasOwnProperty('grain_r')
  && req.body.hasOwnProperty('brick_r') && req.body.hasOwnProperty('ore_r')) {
    axios.post('https://catan-engine.herokuapp.com/Catan/userRequest/', {
      gameId: req.body.gameId,
      playerId: req.body.playerId,
      command: 'playerTrade',
      arguments: {
        lumber_o: req.body.lumber_o,
        wool_o: req.body.wool_o,
        grain_o: req.body.grain_o,
        brick_o: req.body.brick_o,
        ore_o: req.body.ore_o,
        lumber_r: req.body.lumber_r,
        wool_r: req.body.wool_r,
        grain_r: req.body.grain_r,
        brick_r: req.body.brick_r,
        ore_r: req.body.ore_r,
      }
    })
      .then(function (response) {
        res.send(response.data);
      })
      .catch(function (error) {
        console.log(error);
      });
  }
  else {
    res.status(400).json({ status: 'error', message: 'Malformed JSON body.Missing required fields' });
  }
}

const wantToTrade = async(req, res) => {

  if(req.body.hasOwnProperty('gameId') && req.body.hasOwnProperty('playerId')) {
      axios.post('https://catan-engine.herokuapp.com/Catan/userRequest/', {
          gameId: req.body.gameId,
          playerId: req.body.playerId,
          command: 'wantToTrade',
          arguments: {
          }
        })
        .then(function (response) {
          res.send(response.data);
        })
        .catch(function (error) {
          console.log(error);
        });
  }
  else {
      res.status(400).json({status : 'error', message: 'Malformed JSON body.Missing required fields'});
  }
}

const sendPartners = async(req, res) => {

  if(req.body.hasOwnProperty('gameId') && req.body.hasOwnProperty('playerId')) {
      axios.post('https://catan-engine.herokuapp.com/Catan/userRequest/', {
          gameId: req.body.gameId,
          playerId: req.body.playerId,
          command: 'sendPartners',
          arguments: {
          }
        })
        .then(function (response) {
          res.send(response.data);
        })
        .catch(function (error) {
          console.log(error);
        });
  }
  else {
      res.status(400).json({status : 'error', message: 'Malformed JSON body.Missing required fields'});
  }
}

const selectPartner = async(req, res) => {

  if(req.body.hasOwnProperty('gameId') && req.body.hasOwnProperty('playerId') && req.body.hasOwnProperty('player') ) {
      axios.post('https://catan-engine.herokuapp.com/Catan/userRequest/', {
          gameId: req.body.gameId,
          playerId: req.body.playerId,
          command: 'selectPartner',
          arguments: {
             player: req.body.player
          }
        })
        .then(function (response) {
          res.send(response.data);
        })
        .catch(function (error) {
          console.log(error);
        });
  }
  else {
      res.status(400).json({status : 'error', message: 'Malformed JSON body.Missing required fields'});
  }
}

const noPlayerTrade = async (req, res) => {

  if (req.body.hasOwnProperty('gameId') && req.body.hasOwnProperty('playerId') && req.body.hasOwnProperty('port') && req.body.hasOwnProperty('offer') && req.body.hasOwnProperty('request') ) {
    axios.post('https://catan-engine.herokuapp.com/Catan/userRequest/', {
      gameId: req.body.gameId,
      playerId: req.body.playerId,
      command: 'noPlayerTrade',
      arguments: {
        port: req.body.port,
        offer: req.body.offer,
        request: req.body.request
      }
    })
      .then(function (response) {
        res.send(response.data);
      })
      .catch(function (error) {
        console.log(error);
      });
  }
  else {
    res.status(400).json({ status: 'error', message: 'Malformed JSON body.Missing required fields' });
  }
}


const endTurn = async(req, res) => {

  if(req.body.hasOwnProperty('gameId') && req.body.hasOwnProperty('playerId')) {
      axios.post('https://catan-engine.herokuapp.com/Catan/userRequest/', {
          gameId: req.body.gameId,
          playerId: req.body.playerId,
          command: 'endTurn',
          arguments: {
          }
        })
        .then(function (response) {
          res.send(response.data);
        })
        .catch(function (error) {
          console.log(error);
        });
  }
  else {
      res.status(400).json({status : 'error', message: 'Malformed JSON body.Missing required fields'});
  }
}
const update = async (req, res) => {

  if (req.body.hasOwnProperty('gameId') && req.body.hasOwnProperty('playerId')) {
    axios.post('https://catan-engine.herokuapp.com/Catan/userRequest/', {
      gameId: req.body.gameId,
      playerId: req.body.playerId,
      command: 'update',
      arguments: {
      }
    })
      .then(function (response) {
        res.send(response.data);
      })
      .catch(function (error) {
        console.log(error);
      });
  }
  else {
    res.status(400).json({ status: 'error', message: 'Malformed JSON body.Missing required fields' });
  }
}

const discardResources = async (req, res) => {

	if (req.body.hasOwnProperty('gameId') && req.body.hasOwnProperty('playerId') && req.body.hasOwnProperty('lumber') 
	&& req.body.hasOwnProperty('wool') && req.body.hasOwnProperty('grain') && req.body.hasOwnProperty('brick')
	&& req.body.hasOwnProperty('ore'))
	{
	  	axios.post('https://catan-engine.herokuapp.com/Catan/userRequest/', {
			gameId: req.body.gameId,
			playerId: req.body.playerId,
			command: 'discardResources',
			arguments: {
				lumber: req.body.lumber,
				wool: req.body.wool,
				grain: req.body.grain,
				brick: req.body.brick,
				ore: req.body.ore,
			}
	  	})
		.then(function (response) {
			res.send(response.data);
		})
		.catch(function (error) {
			console.log(error);
		});
	}
	else {
		res.status(400).json({ status: 'error', message: 'Malformed JSON body.Missing required fields' });
	}
}

const buyDevelopment = async (req, res) => {

	if (req.body.hasOwnProperty('gameId') && req.body.hasOwnProperty('playerId')) {
		axios.post('https://catan-engine.herokuapp.com/Catan/userRequest/', {
			gameId: req.body.gameId,
			playerId: req.body.playerId,
			command: 'buyDevelopment',
			arguments: {
			}
	  	})
		.then(function (response) {
		  	res.send(response.data);
		})
		.catch(function (error) {
		  	console.log(error);
		});
	}
	else {
	  res.status(400).json({ status: 'error', message: 'Malformed JSON body.Missing required fields' });
	}
}

const useDevelopment = async (req, res) => {

	if (req.body.hasOwnProperty('gameId') && req.body.hasOwnProperty('playerId') && req.body.hasOwnProperty('development') ) {
	  	axios.post('https://catan-engine.herokuapp.com/Catan/userRequest/', {
			gameId: req.body.gameId,
			playerId: req.body.playerId,
			command: 'useDevelopment',
			arguments: {
				development: req.body.development
			}
	  	})
		.then(function (response) {
		  	res.send(response.data);
		})
		.catch(function (error) {
		  	console.log(error);
		});
	}
	else {
	  	res.status(400).json({ status: 'error', message: 'Malformed JSON body.Missing required fields' });
	}
}

const takeResourceFromAll = async (req, res) => {

	if (req.body.hasOwnProperty('gameId') && req.body.hasOwnProperty('playerId') && req.body.hasOwnProperty('resource') ) {
	  	axios.post('https://catan-engine.herokuapp.com/Catan/userRequest/', {
			gameId: req.body.gameId,
			playerId: req.body.playerId,
			command: 'takeResourceFromAll',
			arguments: {
				resource: req.body.resource
			}
	  	})
		.then(function (response) {
		  	res.send(response.data);
		})
		.catch(function (error) {
		  	console.log(error);
		});
	}
	else {
	  	res.status(400).json({ status: 'error', message: 'Malformed JSON body.Missing required fields' });
	}
}

const buildDevelopmentRoad = async(req, res) => {

	if(req.body.hasOwnProperty('gameId') && req.body.hasOwnProperty('playerId') && req.body.hasOwnProperty('start') && req.body.hasOwnProperty('end')) {
		axios.post('https://catan-engine.herokuapp.com/Catan/userRequest/', {
			gameId: req.body.gameId,
			playerId: req.body.playerId,
			command: 'buildDevelopmentRoad',
			arguments: {
				start:req.body.start,
				end:req.body.end
			}
		})
		.then(function (response) {
			res.send(response.data);
		})
		.catch(function (error) {
			console.log(error);
		});
	}
	else {
		res.status(400).json({status : 'error', message: 'Malformed JSON body.Missing required fields'});
	}
}

const takeTwoResources = async(req, res) => {

	if(req.body.hasOwnProperty('gameId') && req.body.hasOwnProperty('playerId') && req.body.hasOwnProperty('resource_0') && req.body.hasOwnProperty('resource_1')) {
		axios.post('https://catan-engine.herokuapp.com/Catan/userRequest/', {
			gameId: req.body.gameId,
			playerId: req.body.playerId,
			command: 'takeTwoResources',
			arguments: {
				resource_0:req.body.resource_0,
				resource_1:req.body.resource_1
			}
		})
		.then(function (response) {
			res.send(response.data);
		})
		.catch(function (error) {
			console.log(error);
		});
	}
	else {
		res.status(400).json({status : 'error', message: 'Malformed JSON body.Missing required fields'});
	}
}

module.exports={
    buildSettlement,
    buildRoad,
	rollDice,
	discardResources,
    moveRobber,
    stealResource,
    playerTrade,
    wantToTrade,
    sendPartners,
    selectPartner,
    noPlayerTrade,
    endTurn,
    buySettlement,
    buyRoad,
    buyCity,
	update,
	buyDevelopment,
	useDevelopment,
	takeResourceFromAll,
	buildDevelopmentRoad,
	takeTwoResources
}
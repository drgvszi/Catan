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

module.exports={
    buildSettlement,
    buildRoad,
    rollDice,
    endTurn,
    buySettlement,
    buyRoad
}
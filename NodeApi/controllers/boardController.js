var path = require('path');
var bodyParser = require("body-parser");
var db = require('./firebaseService').getDb();

const getAllBoards = async(req, res) => {
    return res.status(200).json({boards: boards});
};

function get_board_from_db(gameid) {
    return db.ref('/boards/'+gameid)
    .once('value')
    .then(function(bref) {
        var val= bref.val();
        return {
            board: val.data
        };
    });
}

const getBoard = async(req, res) => {
    // Get Single Table
    console.log('Accepted post Request');
    if(req.body.hasOwnProperty('gameid')) {
    
        console.log(req.body.gameid);
        get_board_from_db(req.body.gameid).then(function(data) {
            res.send(data.board);
        });
    } 
    else {
        console.log(req.body);
        res.status(400).json({status : 'error', message: 'Malformed JSON body. Missing lobbyid.'});
    }
};

// Create Board
const createBoard = async(req, res) => {

    const newBoard = {
        lobbyid: parseInt(req.body.lobbyid),
        board: req.body.board
    }

    // lobbyid obligatoriu specificat
    if(!newBoard.lobbyid) {
        return res.status(400).json({status : 'error', message: 'Please include lobbyid'});
    }

    boards.push(newBoard);
    //console.log(tables);
    res.status(200).json({status : 'succes', message: 'Board successfully created'});
};

// Update Board
const updateBoard = async(req, res) => {

    if(req.body.hasOwnProperty('lobbyid')){
        const found = boards.some(board => board.lobbyid === parseInt(req.body.lobbyid));

        if(found) {
            const updatedBoard = req.body.board; // noul board
            boards.forEach(brd => {
                if(brd.lobbyid === parseInt(req.body.lobbyid)) {
                    brd.board = updatedBoard;
                    return res.status(200).json({status : 'succes', message: 'Board successfully updated', board: brd});
                }
            });
        }
        else {
            res.status(400).json({status : 'error', message: `No board with the id of ${req.body.lobbyid}`});
        }
    }
    else {
        res.status(400).json({status : 'error', message: 'Malformed JSON body. Missing lobbyid.'});
    }
};

// Delete Board
const deleteBoard = async(req, res) => {

    
    if(req.body.hasOwnProperty('lobbyid')){
        const found = boards.some(board => board.lobbyid === parseInt(req.body.lobbyid));

        if(found) {
            boards.forEach(board => {
                if(board.lobbyid === parseInt(req.body.lobbyid)) {
                    let pos = boards.indexOf(board)
                    boards.splice(pos, 1);
                    return res.status(200).json({status : 'succes', message: 'Board successfully deleted'});
                }
            });
        } else {
            res.status(400).json({status : 'error', message: `No board with the id of ${req.body.lobbyid}`});
        }

    }
    else {
        res.status(400).json({status : 'error', message: 'Malformed JSON body. Missing lobbyid.'});
    }
    
};

module.exports={
    getBoard, createBoard, updateBoard, deleteBoard, getAllBoards
}
const router=require('express').Router();
const {lobbyController}=require('../controllers');

router.get('/all', lobbyController.getLobbies);
router.post('/add', lobbyController.addLobby);
router.post('/startgame', lobbyController.startGame);
router.post('/join', lobbyController.joinLobby);
router.post('/leaveLobby', lobbyController.leaveLobby);
router.post('/leaveGame', lobbyController.leaveGame);
router.post('/geid', lobbyController.getGEid);

module.exports=router;
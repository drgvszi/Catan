const router=require('express').Router();
const {gameController}=require('../controllers');

router.post('/buildSettlement',gameController.buildSettlement);
router.post('/buySettlement',gameController.buySettlement);
router.post('/buildRoad',gameController.buildRoad);
router.post('/buyRoad',gameController.buyRoad);
router.post('/buyCity',gameController.buyCity);
router.post('/rollDice',gameController.rollDice);
router.post('/discardResources',gameController.discardResources);
router.post('/moveRobber',gameController.moveRobber);
router.post('/stealResource',gameController.stealResource);
router.post('/playerTrade',gameController.playerTrade);
router.post('/wantToTrade',gameController.wantToTrade);
router.post('/sendPartners',gameController.sendPartners);
router.post('/selectPartner',gameController.selectPartner);
router.post('/noPlayerTrade',gameController.noPlayerTrade);
router.post('/endTurn',gameController.endTurn);
router.post('/update',gameController.update);
router.post('/buyDevelopment',gameController.buyDevelopment);
router.post('/useDevelopment',gameController.useDevelopment);
router.post('/takeResourceFromAll',gameController.takeResourceFromAll);
router.post('/buildDevelopmentRoad',gameController.buildDevelopmentRoad);
router.post('/takeTwoResources',gameController.takeTwoResources);
//router.post('/setExtension',extensionController.setExtensionForUser)
module.exports=router;
const router=require('express').Router();
const {gameController}=require('../controllers');

router.post('/buildSettlement',gameController.buildSettlement);
router.post('/buySettlement',gameController.buySettlement);
router.post('/buildRoad',gameController.buildRoad);
router.post('/buyRoad',gameController.buyRoad);
router.post('/rollDice',gameController.rollDice);
router.post('/endTurn',gameController.endTurn);
// router.post('/setExtension',extensionController.setExtensionForUser)
module.exports=router;
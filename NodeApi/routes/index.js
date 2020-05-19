const router = require('express').Router();

const extension = require('./extension');
router.use('/extension', extension);

const board = require('./board');
router.use('/board', board)

const auth=require('./auth');
router.use('/auth', auth);

const lobby=require('./lobby');
router.use('/lobby', lobby);

const game=require('./game');
router.use('/game', game);

module.exports=router;
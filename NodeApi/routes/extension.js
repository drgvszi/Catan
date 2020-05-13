const router=require('express').Router();
const {extensionController}=require('../controllers');
const validateToken=require('./validateToken')
router.get('/getExtensionByName',extensionController.getExtensionByName);
router.put('/insertExtension',extensionController.insertExtension);
router.post('/setExtension',extensionController.setExtensionForUser)
module.exports=router;


var db = require('./firebaseService').getDb();
var ref = db.ref("extensions");


const getExtensionByName=async(req,res)=>{
    const queryName = req.query.name;
      console.log("HTTP Get Request");
    	var userReference = db.ref("/extensions/");

      ref.orderByKey().equalTo(queryName).on("child_added", (snapshot)=> {
          res.json(snapshot.val());
    });

}
const insertExtension=async(req,res)=>{
    console.log("HTTP Put Request");

var name = req.body.name;
var available=req.body.available
var referencePath = '/extensions/';
var userReference = db.ref(referencePath);
	userReference.child(name).set(
		{
			available:available,
			description:''
		}, 
			function(error) {
				if (error) {
					res.json({status:'error',message:error});
				} 
				else {
					res.json({status:'succes',message:''});
				}
			}
	);
}

const setExtensionForUser = async(req,res)=>{
	var username = req.body.username;
	var extension = req.body.extension;
	var user = db.ref("users").child(username);
	user.update({
		"userextension":extension
	}, function(error) {
		if (error) {
			res.json({status:'error',message:error});
		} 
		else {
			res.json({status:'succes',message:''});
		}
	});

}

module.exports={
    getExtensionByName,
    insertExtension,
    setExtensionForUser
}
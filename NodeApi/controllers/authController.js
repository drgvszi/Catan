var db=require('./firebaseService').getDb();
var ref = db.ref("users");
const jwt=require('jsonwebtoken');
require('dotenv').config();
const crypto = require("crypto");

const login=async(req,res)=>
{
    const username=req.body.username;
    const password=req.body.password;
    console.log(req.body);
    const queryUsername=username;
    console.log(queryUsername);
    var queryRef=ref.orderByKey().equalTo(queryUsername);
    queryRef.once('value')
    .then(function(dataSnapshot){
        const user=dataSnapshot.val();
        if(!user)
        {
            console.log("User not found");
            return res.status(404).json({status : 'error', message: 'User not found'});
        }
        var userCred=Object.values(user)[0];
        console.log(userCred);
        if(userCred.userpassword!=password)
        {
            console.log("Wrong password");
            return res.status(400).json({status:'error',message:'Wrong password'});
        }
        return res.status(200).json({status:'success',message:'Logged in succesfully',user_id:userCred.userId});
    })
    .catch(function(error){
        console.log(error);
    });
}

const register=async(req,res)=>
{
    const email = req.body.email;
    const username = req.body.username;
    const password = req.body.password;
    const confirmpassword = req.body.confirmpassword;

    if(!req.body.hasOwnProperty('email')){
        return res.status(400).json({status : 'error', message: 'Malformed JSON body. Missing email.'});
    }
    else if(!req.body.hasOwnProperty('username')){
        return res.status(400).json({status : 'error', message: 'Malformed JSON body. Missing username.'});
    }
    else if(!req.body.hasOwnProperty('password')){
        return res.status(400).json({status : 'error', message: 'Malformed JSON body. Missing password.'});
    }
    else if(!req.body.hasOwnProperty('confirmpassword')){
        return res.status(400).json({status : 'error', message: 'Malformed JSON body. Missing confirmpassword.'});
    }
    else if(password != confirmpassword){
        return res.status(400).json({status : 'error', message: 'Confirmpassword doesn\'t match the password'});
    }

    const queryUsername=username;
    const queryRef=await ref.orderByKey().equalTo(queryUsername);
    queryRef.once('value')
    .then(function(dataSnapshot){
        const user=dataSnapshot.val();
        if(!user)
        {   
            console.log("HTTP Register POST Request");

            var referencePath = '/users/';
            var userReference = db.ref(referencePath);
            const id = crypto.randomBytes(16).toString("hex");
            userReference.child(username).set(
                {
                    id: id,
                    email: email,
                    extension: '',
                    username: username,
                    password: password
                }, 
                    function(error) {
                        if (error) {
                            console.log(error);
                            res.json({status:'error', message: 'Server error, plase contact your server administrator.'});
                        } 
                        else {
                            res.status(200).json({status : 'success', message: 'User succesfully created!'});
                        }
                    }
            );
    
        } else {
            return res.status(400).json({status : 'error', message: 'The username is not available!'});
        }
    })
    .catch(function(error){
        console.log(error);
    });
}
module.exports={
    login, register
}
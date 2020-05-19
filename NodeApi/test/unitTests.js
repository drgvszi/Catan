var should=require("should");
var request=require("request");
var expect=require("chai").expect;
var util=require("util");
var baseUrl="https://catan-connectivity.herokuapp.com";

// const email=Math.random().toString(36).slice(2);
// const username=Math.random().toString(36).slice(2);
// const password=Math.random().toString(36).slice(2);
// const confirmpassword=password;
var lobby;
var ge;
var geid;
var intersection=(Math.floor(Math.random() * Math.floor(54)));

const players=[
    {email:Math.random().toString(36).slice(2),username:Math.random().toString(36).slice(2), password:Math.random().toString(36).slice(2)},
    {email:Math.random().toString(36).slice(2),username:Math.random().toString(36).slice(2), password:Math.random().toString(36).slice(2)},
    {email:Math.random().toString(36).slice(2),username:Math.random().toString(36).slice(2), password:Math.random().toString(36).slice(2)},
    {email:Math.random().toString(36).slice(2),username:Math.random().toString(36).slice(2), password:Math.random().toString(36).slice(2)}
]

describe('register unit test',function(){
    it('returns OK on succesfull register',function(done){   
        for(var index=0;index<players.length;index++)
        {   
            request.post({url:baseUrl+'/auth/register/',
                        json:{
                                email:players[index].email,
                                username:players[index].username,
                                password:players[index].password,
                                confirmpassword:players[index].password
                            },
                            timeout:300000},
                            function(error,response,body){
                expect(response.statusCode).to.equal(200);
        });   
    }
    done();
    });
});


describe('login unit test',function(){
    it('returns user details at login',function(done){
        request.post({url:baseUrl+'/auth/login/',
                      json:{
                            username:"test2",
                            password:"test2"
                        },
                        timeout:300000},
                        function(error,response,body){
            expect(response.statusCode).to.equal(200);
        done();
        });
    });
});

describe('lobbies unit test',function(){
        it('returns all available lobbies',function(done){
            request.get({url:baseUrl+'/lobby/all',
            timeout:300000},
            function(error,response,body){
        expect(response.body).to.not.equal(null);
            done();
        });
    });
});


describe('create lobby unit test',function(){
    it('returns details about newly created lobby',function(done){
        request.post({url:baseUrl+'/extension/setExtension ',
        json:{
                username:players[0].username,
                extension:"extension1"
            },timeout:300000},
            function(error,response,body){
                request.post({url:baseUrl+'/lobby/add',
                    json:{
                            username:players[0].username,
                        }},
                        function(error,response,body){
                        lobby=response.body.lobbyid;
                        ge=response.body.gameid;
            expect(response.body).to.not.equal(null);
        done();
        });
    });
});
});

describe('join lobby unit test',function(){
    it('makes possible for user to join lobby',function(done){
        for(var index=1;index<players.length;index++)
        {
            request.post({url:baseUrl+'/extension/setExtension ',
                    json:{
                            username:players[index].username,
                            extension:"extension1"
                        },timeout:300000},
                        function(error,response,body){
                           
                        });
        }
        for(var index=1;index<players.length;index++)
        {
            request.post({url:baseUrl+'/lobby/join',
                    json:{
                            username:players[index].username,
                            lobbyid:lobby
                        },timeout:300000},
                    function(error,response,body){
                        expect(response.body.error).to.deep.equal("-");
                    });
        }
        done();
    });
});

describe('get gameid for user unit test',function(){
    it('returns game id for the player created lobby',function(done){
        request.post({url:baseUrl+'/lobby/geid',
                    json:{
                            username:players[0].username,
                        },timeout:300000},
                        function(error,response,body){
                        
            expect(response.body).to.not.equal(null);
            geid=response.body;
        done();
        });
    })
});

describe('start game unit test',function(){
    it('returns board on game start',function(done){
        request.post({url:baseUrl+'/lobby/startgame',
                    json:{
                            gameid:ge,
                        },timeout:300000},
                        function(error,response,body){
            expect(response.body).to.not.equal({});
        done();
        });
    })
});

describe('build settlement unit test',function(){
    it('makes possible for user to build a settlement',function(done){
        request.post({url:baseUrl+'/game/buildSettlement',
        json:{
                gameId:ge,
                playerId:geid,
                intersection:intersection
            },timeout:300000},
            function(error,response,body){
             expect(response.body.code).to.equal(200);
            done();
        });
    });
})

describe('build road unit test',function(){
    it('makes possible for user to build a road',function(done){
        request.post({url:baseUrl+'/game/buildRoad',
        json:{
                gameId:ge,
                playerId:geid,
                start:intersection,
                end:intersection-1
            },timeout:300000},
            function(error,response,body){
             expect(response.body.code).to.equal(200);
            done();
        });
    });
})


describe('leave lobby unit test',function(){
    it('makes possible for user to leave lobby',function(done){
        request.post({url:baseUrl+'/lobby/leaveLobby',
        json:{
                username:players[0].username,
                lobbyid:lobby
            }},
        function(error,response,body){
            expect(response.body).to.deep.equal("done");
            done();
        });
    });
});




var express         = require('express');
var util = require('util');
var path            = require('path'); // модуль для парсинга пути
var libs = process.cwd() + '/libs/';
var log = require(libs + 'log')(module);
var url = require('url');
var UsersList = require('./Model').UsersList;
var app = express();
//var model = require('../bin/Model').Userlist


app.get('/api', function (req, res) {
    res.send('API is running');
});


app.get('/api/wifi/:id', function(req, res) {
    
     res.send(UsersList.get(req.params.id));
    
    //res.send( util.inspect(req));
});

app.post('/api/wifi', function(req, res) {
    
    var url_parts = url.parse(req.url, true);
    
    /*if(!url_parts.query.path){
        res.end(JSON.stringify({"msg" : "Error Please, enter 'path' param"}));
        return;
    }*/
    var path = url_parts.query.path;
    
    
    //var user = new UsersList();
    
    UsersList.add(1,'{"SSID":"DORKA_DOBRO","MAC":"NONE","PASSWORD":"88888887"}');
    UsersList.add(1,'{"SSID":"LOL","MAC":"NONE","PASSWORD":"812887"}');
    UsersList.add(1,'{"SSID":"LOL1","MAC":"NONE1","PASSWORD":"000000"}');
    
    res.send(UsersList.get(1));
});

app.post('/api/wifi/:id', function(req, res) { 
      var url_parts = url.parse(req.url, true);
    
    
    var t="";
    if(!url_parts.query.SSID){
        res.end(JSON.stringify({"msg" : "Error Please, enter 'SSID' param"}));
        return;
    }
    else
        t+='{"SSID":"'+url_parts.query.SSID+'",';
    if(!url_parts.query.MAC){       
       t+=('"MAC":"NONE",');
    }
      else
        t+=('"MAC":"'+url_parts.query.MAC+'",');
        
    if(!url_parts.query.PASSWORD){       
        t+=('"PASSWORD":"NONE"}');
    }
      else
         t+=('"PASSWORD":"'+url_parts.query.PASSWORD+'"}');
    
    UsersList.add(req.params.id,t);
    
    
    res.send(UsersList.get(req.params.id));
});


app.listen(3000, function(){
    console.log('Express server listening on port 3000');
});


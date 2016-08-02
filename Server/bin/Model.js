

var WiFi = function(str) { 
  
    this.SSID,
    this.MAC,
    this.PASSWORD 
    
    for (var key in str)
    {
        this[key] = str[key];
    }
}

//------------------------------------------------------------------
var WiFiList = function(id) {
   
    this.Wifilist  = new Array(),
    this.id = id  
}

WiFiList.prototype = {
    set wifilist (t) {    
        this.WiFiList = t;         
	    },
	get wifilist () {
		return this.Wifilist;
	},
    
    set Id(t){
        this.id = t;
    },
    get Id(){
        return this.id;
    }  
}

WiFiList.prototype.put = function (t)
{
    this.Wifilist.push(t);
}

WiFiList.prototype.get = function () {
    var res="";
    for (var i=0; i<this.Wifilist.length; i++)
    {
        res=JSON.stringify(this.Wifilist) ;
    }
    return res;
}


//-------------------------------------------------------------------------
var UsersList = function(){
    this.userlist = new Array()
    //this.wifi = new WiFi
}

UsersList.prototype.add = function (id,str) { //str - json obj
    
    for (var i=0; i<this.userlist.length ; i++)
    {
        if (this.userlist[i].Id == id)
        {
            this.userlist[i].put(new WiFi(JSON.parse(str)));
            return;
        }
    }
    
    this.userlist.push(new WiFiList(id));
    this.userlist[this.userlist.length-1].put(new WiFi(JSON.parse(str)));
}

UsersList.prototype.get = function (id)
{
    for (var i=0; i<this.userlist.length ; i++)
    {
        if (this.userlist[i].Id == id)
        {
            return this.userlist[i].get();
        }
    }
    return "not found";
}

var usersList = new UsersList();
usersList.add(1,'{"SSID":"DORKA_DOBRO","MAC":"NONE","PASSWORD":"88888887"}');
usersList.add(1,'{"SSID":"LOL","MAC":"24:F2:A1:00:64","PASSWORD":"JHFVHSFDVBV"}');
usersList.add(1,'{"SSID":"PUTIN","MAC":"NONE","PASSWORD":"NONE"}');

module.exports.UsersList = usersList;
module.exports.WiFi = WiFi;
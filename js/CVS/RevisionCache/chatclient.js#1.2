var sel1, messageTable;
var handle, message;
var btnSend, btnConnect, btnDisconnect, btnSendAll;
var selUserList = null;

var messageBox;

var isPrivate;	//..

var room = null;
var selRoom = null;

/* NOTICE myId must exist */
var myID = null;


function setMessageBox(html) {
	if(messageBox1 != null)
		messageBox1.innerHTML = html;
}

function setServerMessageBox(html) {
	if(messageBox2 != null)
		messageBox2.value = html + "\n" + messageBox2.value;
}

function serverNotifyUser(text) {
	if(messagemodel != null) {
		messagemodel.add(new PrivateMessage(window.user_id, "Server", text));
		messagemodel.notifyObservers();
	}
}


function setAsPrivate(id, handle) {
	isPrivate = id;	
	setServerMessageBox("Du skriver til [" + handle + "]");			
}

function unSetPrivate() {
	isPrivate = 0;
	setMessageBox("Du skriver til Alle");
}

/**
*
* Følgende function wrapper til chatApp..
*	--
*	Eventhandlere til events fra browser
*	
*	OnSend						-> setMessage/setPrivateMessage
*	OnChangeRoom				-> setMessage
*	OnConnect 					-> onConnect
*	OnDicconnect				-> onDisconnect
*	--
*	Eventhandlere til events fra chatApp
*
*	OnMessageReceived			<-
*	OnOnlineMessageReceived		<-
*	OnOfflineMessageReceived	<-
*	OnAwayMessageReceived		<-
*	OnPrivateMessageReceived	<-
*	OnChangeRoomReceived		<-
*	OnConnectionUpdated			<-
*	OnUserListReceived			<-
*	OnLoginMessageReceived		<-
*	OnServerMessageReceived		<-

*/

function OnServerMessageReceived() {
	var arr = arguments[3].split(";");
	
	serverNotifyUser(arr);
}

function OnSend() {
	try {
		if(isPrivate == 0)
			chatApp.setMessage("Message", message.value);
		else
			chatApp.setPrivateMessage(parseInt(isPrivate), "PrivateMessage", message.value);
		message.value = "";
		
	} catch(e) {
		//
	}
	message.focus();
}

function OnChangeRoom(sel) {
	try {
		// args: sel string name of room
		unSetPrivate();
		messagemodel.clear();
		//var val = sel.options[sel.selectedIndex].value;				
	
		chatApp.setMessage("ChangeRoom", sel);
	} catch(e) {
		alert("OnChangeRoom: " + e.message);
	}
}

function OnConnect() {
	var user, passwd;
	try {
		user = document.getElementById("username").value;
		passwd = document.getElementById("password").value;

		if(user != "" && passwd != "") {
		
			chatApp.onConnect(user, passwd);
			
			//hideLoginFrame();
			setMessageBox("Checking login...");
		}
		else
			alert("Du skal indtaste et brugernavn og/eller kodeord");
	} catch(e) {
		alert(e + "\nChatten kan ikke anvendes - er java plugin installeret..?");
	}
}

function OnDisconnect() {
	try {
		btnSend.disabled = true;
		btnConnect.disabled = false;
		btnDisconnect.disabled = true;
		//selRoom.disabled = true;
		message.disabled = true;
		usermodel.clear();
		
		chatApp.onDisconnect();
		
	} catch(e) {
		alert(e.message);
	}
}

function OnMessageReceived(id, handle, event, msg) {
	if(messagemodel.count() == 100)
		messagemodel.clear();
	messagemodel.add(new Message(id, handle, msg));				
	messagemodel.notifyObservers();
}

function OnOnlineMessageReceived(id, handle, event, msg) {
	var u = new User(id, handle);

	if(usermodel.find(u) == null) {
		usermodel.add(u);
		usermodel.notifyObservers();		
	}
		
	var eventObj = new Object();
	eventObj["type"] = "Online";
	eventObj["obj"] = u;	
	document.fireOnlineEvent(eventObj);
}

function OnOfflineMessageReceived(id, handle) {
	usermodel.remove(new User(id, handle));
	if(isPrivate == id)
		unSetPrivate();
}

function OnAwayMessageReceived() {
	alert(arguments);
}

function OnPrivateMessageReceived(id, handle, event, msg) {
	messagemodel.add(new PrivateMessage(id, handle, msg));			
	messagemodel.notifyObservers();
}

function OnChangeRoomReceived(id, handle, event, msg) {
	try {
		// message == room name OR "Leave"
		//alert("Change room: " + id + " " + handle + " " + msg);
		var u = new User(id, handle);
						
		if(usermodel.find(u) == null && msg == "Arrive") {
			usermodel.add(u);			
			usermodel.notifyObservers();
		}
		else if(usermodel.find(u) != null && msg == "Leave")
			usermodel.remove(u);
		
		u = null;
	} catch(e) {
		alert("OnChangeRoomReceived: " + e.message);
	}
}

function OnConnectionUpdated() {
	try {
		setServerMessageBox("Connection failure:\n" + arguments[1]);
		usermodel.clear();
		btnSend.disabled = true;
		btnConnect.disabled = false;
		btnDisconnect.disabled = true;
		btnSendAll.disabled = true;
		//handle.disabled = false;
	} catch(e) {
		alert("OnConnectionUpdated: " + e.message);
	}
}
/**
*	@params
*	int id
*	string handle
*	string event
*	string msg
*
*	@return 
*	Invoked when Userlist is received from server.
*	Dataformat [1; [handle], [2; [handle2]]
*/
function OnUserListReceived() {
	//
	//alert("UserList: " + arguments[3]);
	try {
	
		if(usermodel.count() > 0) {
			usermodel.clear();
			
		}
		var str_all = arguments[3].toString();
		// skip []
			
		if(typeof str_all == "object"){								// Mozilla/Firefox 'bug' fix
			str_all = (str_all.substring(1, str_all.length()-1));	// notice length with () .. normally a property			
		}
		else if(typeof str_all == "string") 
			str_all = (str_all.substring(1, str_all.length-1));		// length a property
		
		var splitted = str_all.split(",");							// split str_all by ","
		
		for(var i=0;i<(splitted.length);i++) {
			var userdata = splitted[i].split(";");
			var userhandle = userdata[1];
			if(typeof str_all == "object") {					
				userhandle = userhandle.substring(userhandle.indexOf("[")+1, userhandle.length()-1);
			}
			else if(typeof str_all == "string") {
				userhandle = userhandle.substring(userhandle.indexOf("[")+1, userhandle.length-1);
			}
			var u = new User(parseInt(userdata[0]), userhandle);			
			usermodel.add(u);
		}
		
		usermodel.notifyObservers();
		
	} catch(e) {
		alert("OnUserListReceived: " + e.message);	
	}

}

/**
*	@params
*	int id
*	string handle
*	string event
*	string msg
*/
function OnLoginMessageReceived(id, handle, event, msg) {
	
	if(msg == 0) {
		setMessageBox("Login ikke korrekt!!");
		OnDisconnect();
	}
	else {
		setMessageBox("Du er logget ind som [" + document.getElementById("username").value + "]");
		btnSend.disabled = false;
		btnConnect.disabled = true;
		btnDisconnect.disabled = false;
		btnSendAll.disabled = false; 
		//selRoom.disabled = false;
		message.disabled = false;
		message.focus();
		// Set global user id
		window.user_id = id		
		
	}
						
}
function keyCheck(e) {
	if(window.event && window.event.keyCode == 13) {	// ENTER 
		OnSend();
	} else if(e && e.keyCode == 13) { 					//	FireFox/Mozilla		
		OnSend();
	} 
	return;
}

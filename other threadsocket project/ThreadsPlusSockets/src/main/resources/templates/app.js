var ws;
const url = "http://localhost:8080";
var pokesArray = [];
var requestEnabled = true;
var timeout = 180000;
var contTimeout = 0;

function printMessageFromServer(message){
	$("#postStatus").append(message + "\n");
}

function doDisconnection() {
	console.log("Connection paused");
	$.post(url+"/Pokemon/disable-updates",function(){})
		.fail(function(jqXHR, textStatus, errorThrown){
			console.error(errorThrown);
	});
	setConnected(false);
	requestEnabled = false;
}

async function handleSSE(){
	try{
		if(contTimeout==4){
			console.log("Server error, please check if the server is working");
			doDisconnection();
			contTimeout=1;
			return;
		}
		if(requestEnabled){
			console.log("Starting Connection");
			const eventSource = new EventSource(url + '/Pokemon/pokemon-status');

			eventSource.onmessage = (event) => {
				const messageSse = event.data;
				printMessageFromServer(messageSse);
			};

			eventSource.onerror = (e) => {
				console.log("On Error SSE: "+e.type+" - Status: "+e.target.readyState);
				if(e.readyState === e.CLOSED){
					console.log("Server may be closed");
					eventSource.close();
				}
				handleSSE();
			};

			var timeoutTimer = setTimeout(() => {
				eventSource.close();
				console.log("Connection timed out.");
			}, timeout);

			eventSource.onopen = () => {
				clearTimeout(timeoutTimer);
				console.log("Connection opened");
			};

			if(eventSource.readyState == 0){
				console.log("SSE state: "+eventSource.readyState);
				console.log("Connecting");
				contTimeout++;
			}
			if(eventSource.readyState == 1){
				console.log("SSE state: "+eventSource.readyState);
				console.log("Connected");
			}
			if(eventSource.readyState == 2){
				console.log("SSE state: "+eventSource.readyState);
				console.log("Closed");
				eventSource.close();
				requestEnabled = false;
			}
			setConnected(true);
		}
		$.post(url+"/Pokemon/enable-updates",function(){})
			.fail(function(jqXHR, textStatus, errorThrown){
				console.log("Error on server");
		});
		requestEnabled = true;
		
	}catch(error){
		console.log("handleSSE error: "+error);
	}
}

async function sendIDs(){
	try{
		if(!requestEnabled){
			console.log("Requests are disabled");
			requestEnabled = true;
			return;
		}
		setConnected(true);
		let startID = $("#startID").val();
		let endID = $("#endID").val();
		const response = await fetch(url + "/Pokemon/setPokesByRank?startID="+startID+"&endID="+endID, {
			method: 'POST',
			mode: 'cors'
		}).then((response)=> {
			if(response.ok){
				console.log("response.ok")
				return response.text();
			}	
		}).then((msg)=> {
			printMessageFromServer(msg);
		});
	}catch(error){
		console.log("Error when sending data: "+error);
		console.error(error);
	}
}

async function deleteDB(){
	try{
		const response = await fetch(url + "/Pokemon/emptyTable", {
			method: 'DELETE',
			mode: 'cors'
		}).then((response)=> {
			if(response.ok){
				return response.text();
			}
		}).then((msg) =>{
			$("#dltstatus").html("");
			$("#dltstatus").append(msg);
			$("#infoPokestbl").html("");
			$("#postStatus").html("");
		});
	}catch(error){
		console.log(error);
	}
}

async function getPokes(){
	try{
		const response = await fetch(url + "/Pokemon/getPokes", {
			method: 'GET',
			mode: 'cors'
		});
		if(response.ok){
			pokesArray = await response.json();
			printPokesOnTable();
		}
	}catch(error){
		console.log(error);
	}
}

function printPokesOnTable(){
	var str = "";
	$("#infoPokestbl").html(str);
	for(let i=0;i<pokesArray.length;i++){
		str += 
			"<tr><td colspan=\"1\">" + pokesArray[i].id + "</td>"
			+ "<td colspan=\"1\">" + pokesArray[i].name + "</td>" 
			+ "<td colspan=\"1\">" + pokesArray[i].level + "</td>" 
			+ "<td colspan=\"1\">" + pokesArray[i].type + "</td></tr>" ;
		$("#infoPokestbl").html(str);
	}
}

function setConnected(connected) {
	$("#connect").prop("disabled", connected);
	$("#pause").prop("disabled", !connected);
}

//$(document).ready(function(){
$(function() {
	$("form").on('submit', function(e) {
		e.preventDefault();
	});
	$("#deletebtn").click(function(){
		deleteDB();
	});
	$("#getPokesbtn").click(function(){
		getPokes();
	});
	$("#sendbutton").click(function(){
		sendIDs();
	});
	$("#pause").click(function(){
		doDisconnection();
	});
	$("#connect").click(function(){
		handleSSE();
	});
});

handleSSE();
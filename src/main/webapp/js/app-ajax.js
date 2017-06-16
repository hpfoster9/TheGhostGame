$(document).ready(function() {
		var clock = $('.clock').FlipClock({
			clockFace: 'counter',
			countdown: true
			
		});
		
			
		clock.setTime(0);
		
		var countdown;
		var timerOn = false;
		var hadUpdate = false;
		var maxTime = -1;
		//Used to identify the User's game by ID
		var GAME_HASH;
		//Used to identify the User by ID
		var PLAYER_ID;
		//Used to keep track of Client updates
		var Client_update = 0;
		
		//The user can join through link now too
		var keys = "";
		
		if(window.location.href.indexOf("?") != -1){
			keys= window.location.href.split("?")[1];
		}
		if(keys.length > 1 && keys.substring(0,3) == "id="){
			//console.log("trying to use the fancy url");
			var gameH = keys.split("=")[1];
			//console.log("gameid test: "+gameH);
		
			sendMessage("get", {
        		key: "tryJoinGame",
                gameHash : gameH
            }, tryJoinGameCallback);
		}
		//console.log(window.location.href.split("?")[1]);
		
		
		//Default post request to load the word list
		$.get('AjaxServlet', {
            key: "initialize"
		});
		
		function sendMessage(type, postBody, callbackFunction){
			if(type == "post"){
				if(callbackFunction == null){
					$.post('AjaxServlet', postBody);
				}
				else{
					$.post('AjaxServlet', postBody, callbackFunction);
				}
			}
			else{
				if(callbackFunction == null){
					$.get('AjaxServlet', postBody);
				}
				else{
					$.get('AjaxServlet', postBody, callbackFunction);
				}
			}
			//console.log("sent a "+type+"request: "+postBody.key);
		}
		
		//Runs when user Joins a game through entering a gameID
        $('#joinForm').submit(function(event) {
        		//Sets the users inputs as disabled by default
        		disableTRUE();
        		
        		//Gets users gameID and assigns it to the global variable
                var gameId = $('#idInput').val();
                $('#idInput').val("");
                
	                //Sends getRequest to servlet, to make sure that game exists
	                sendMessage("get", {
	            		key: "tryJoinGame",
	                    gameId : gameId
	                }, tryJoinGameCallback);
               
                
                return false;
            });
        
        //Runs when user Creates new game
        $('#innerCreate').click(function(event) {
        	//Sends postRequest to tell servlet to create new game
            sendMessage("post",{
                    key: "createGame"
            }, createGameCallback);
        });
        
        //Runs when user submits name and password
        $('#namePasswordForm').submit(function(event) {
        	var n = $('#nameInput').val();
        	var pass = $('#passwordInput').val();
        	$('#nameInput').val("");
        	$('#passwordInput').val("");
        	/* Sends postRequest to check if game exists and
        	 *  from there add new player to either new game or existing one */
        	if($('#namePassTitle').text().substring(0,4) == "Join"){
	            sendMessage("post",{
	                key: "namePassword",
	                name: n,
	                password: pass,
	                gameHash: GAME_HASH
	            }, namePasswordCallback);
        	}
        	else{
        		console.log($('#namePassTitle').text().substring(0,4));
        		var lives = $('#livesInput').val();
        		var seconds = $('#secondsInput').val();
        		console.log("l: "+lives);
        		console.log("s: "+seconds);
        		sendMessage("post",{
	                key: "namePassword",
	                name: n,
	                password: pass,
	                gameHash: GAME_HASH,
	                lives: lives,
	                seconds: seconds
	            }, namePasswordCallback);
        		
        	}
            return false;
        });
        
        //Runs at an interval once inside the lobby
        function lobbyPing(){
        	//console.log("lobby ping");
        	sendMessage("get",{
        		key: "lobbyPing",
        		gameHash: GAME_HASH,
        		playerId: PLAYER_ID
        	}, lobbyPingCallback);
        }
        
        $('#readyUp').click(function(event){
        	sendMessage("post",{
        		key: "lobbyReady",
        		gameHash: GAME_HASH,
        		playerId: PLAYER_ID
        	});
        });
        
        //Runs when user submits letter
        $('#letterForm').submit(function(event) {
        	var letter = $('#letterInput').val();
        	$('#letterInput').val("");
        	//Sends letter and gameID
        	sendMessage("post",{
                key: "sendLetter",
                letter: letter.toUpperCase(),
                gameHash: GAME_HASH
        	}, sendLetterCallback);
        	timerOn = false;
			clock.setTime(0);
			clearInterval(countdown);
            return false;
        });
        
        //Runs when user presses "That's a word!"
        $('#checkWord').click(function(event) {
        	sendMessage("get",{
                key: "checkWord",
                gameHash: GAME_HASH
        	});
        	timerOn = false;
			clock.setTime(0);
			clearInterval(countdown);
            
        });
        
        //Runs when user sends challenge request
        $('#challengeButton').click(function(event) {
        	
            sendMessage("post",{
                key: "challengeRequest",
                gameHash: GAME_HASH
        	}, challengeRequestCallback);
            timerOn = false;
			clock.setTime(0);
			clearInterval(countdown);
        });
        
        //Runs when the user responds to the challenge request
        $('#challengeForm').submit(function(event) {
        	var word = $('#challengeInput').val();
        	$('#challengeInput').val("");
        	//Sends word and gameID
        	sendMessage("post",{
                    key: "challengeResponse",
                    word: word.toLowerCase(),
                    gameHash: GAME_HASH
            });
            
            //Close the popup window
            $('#myModal').modal('hide');
            return false;
        });
        
        //if the user presses the 'X' in the modal ******************STILL NEED TO UPDATE THIS SERVERSIDE****************************
        $('#modalX').click(function(event) {
        	
        	$('#challengeInput').val("");
        	//Sends the error and gameID
        	sendMessage("post",{
                key: "challengeResponseError",
                word: "%ERROR%",
                gameHash: GAME_HASH
        	});
            //Closes the popup window
            $('#myModal').modal('hide');
        });
        
        //Every 500ms ping the server to update word, turn, and deathCounter
        function ping(){
        	//Sends postRequest to finds info about game
            sendMessage("get",{
            	key: "ping",
            	gameHash: GAME_HASH,
            	playerId: PLAYER_ID
            }, pingCallback);
            //console.log("gameHash: "+GAME_HASH);
            //console.log("playerId: "+PLAYER_ID);
        }
        
        
        $('#chatForm').submit(function(event){
        	var message = $('#chatInput').val();
        	sendMessage("post",{
        		key: "chat",
        		gameHash: GAME_HASH,
        		playerId: PLAYER_ID,
        		msg: message
        	});
        	$('#chatInput').val("");
        	return false;
        });
        
       
        
        //**CALLBACK FUNCTIONS**//
        function tryJoinGameCallback(responseText) {
    		//If the game couldn't be found by the servlet, notify user
    		if(responseText.length < 1 || parseInt(responseText) == -1){
    			$('#msg').text("Your input was invalid or game didn't exist");
    			GAME_HASH = "";
    		}
    		//If the game is found, let user move to the next screen
    		else{
    			GAME_HASH = responseText;
    			//Change namePass to Join
    			$('#namePassTitle').text("Joined game at ID: "+responseText);
    			//Change screen method
    			from1to2();
    		}
    	}
        
        function createGameCallback(responseText) {
        	//console.log("responseText from create: "+responseText);
        	GAME_HASH = responseText;
        	//Change namePass to Create and move to next screen
        	$('#namePassTitle').text("Created game at ID: "+GAME_HASH);
        	$('#createSettings').removeAttr("hidden");
        	//Change screen method
        	from1to2();
        }
        
        function namePasswordCallback(responseText) {
			//if it is successful, move player to game screen
			if(responseText.length > 0){
			//Assigns the playerID with the newly created ID by the server
			PLAYER_ID = responseText;
			
			//Shows the user what game ID it is
			$('#gameHashLobby').text("Game ID: "+GAME_HASH);
			//Change screen method
			from2to3();
			
			
			}
        }
        
        function lobbyPingCallback(responseText){
        	//console.log("lobbyCallback response: "+responseText);
        	var tempArray = responseText.split("|");
        	var playerList = tempArray[0].split(",");
        	var gameReady = eval(tempArray[1]);
        	
        	$('#lobbyPingTable').html(updateLobby(playerList));
        	
        	if(gameReady){
        		disableTRUE();
        		$('#gameAboutToStart').text("The game is about to start!");
        		//console.log("in lobby ping callback before");
        		setTimeout(function(){from3to4();},1500);
        		//console.log("in lobby ping callback after");
        		clearInterval(lobbyPingInterval);
        		
        	}
        	
        }
			
		function sendLetterCallback(responseText) {
			//If there was some sort of error, notify user
			if(responseText.length < 1){
				$('#errorMsg').text("You must enter a single letter");
			}
			//Otherwise, clear any error messages and disable the users input field
			else{
				$('#errorMsg').text("");
				disableTRUE();
			}
		}
		
		function challengeRequestCallback(responseText) {
			disableTRUE();
    	}
		
		function pingCallback(responseText) {
        	//console.log(responseText);
        	//Splits the response into the word, turn id, and the players, and then the update text
			var temp = responseText.split("|");
        	var wordTurnPlayersArray = temp[0].split(" ");
        	var updateChallengeArray = temp[1].split("*");
        	//console.log(wordTurnPlayersArray);
        	//console.log(updateChallengeArray);
        	
        	//Uses the word, turn, and players to update client
        	var time = wordTurnPlayersArray[0];
    		var word = wordTurnPlayersArray[1];
    		var turnID = wordTurnPlayersArray[2];
    		var turnIndex = wordTurnPlayersArray[3];

    		if(maxTime == -1){
    			maxTime = time;
    		}
    		//get rid of everything but the players in the first array
    		wordTurnPlayersArray.splice(0,4);
    		//console.log(wordTurnPlayersArray);
    		
    		//update the board with the new word
            $('#mainMsg').text(word);
            
            
    		
            //update the table of players on the right
            $('#playerListTable').html(createTable(wordTurnPlayersArray,turnIndex));
            
            
            
            
        	var msg = updateChallengeArray[0];
        	var Server_update = updateChallengeArray[1]; 
        	var challengeID = updateChallengeArray[2];
        	var losingId = updateChallengeArray[3];
        	var winningId = updateChallengeArray[4];
        	
        	
        	
        	//If the client is the one being challenged, show the modal
        	if(challengeID == PLAYER_ID ){
        		$("#myModal").modal({backdrop: "static"});
        	}
            var isTurn = false;
            
        	
        	
        	
            if(turnID == PLAYER_ID && challengeID == "false"){
            	isTurn = true;
            }
            if(!isTurn){
            	console.log("CLEARED THE COUNTDOWN");
            	clearInterval(countdown);
            }
            console.log("made isTurn "+ isTurn);
            
            
        	//If the client is not up to date with the server, show updateMsg or update chat
        	if(Client_update != Server_update){
        		hadUpdate = true;
        		var messages = msg.split("~");
        		//console.log("IN PING");
        		//console.log(msg);
        		
        		if(messages[0] == "&"){
        			//console.log("ran displayChat");
        			displayChat(messages);
        			hadUpdate = false;
        		}
        		else{
        			console.log("ran display messages");
        			displayMessages(messages, isTurn);
        		}
        		
        		Client_update = Server_update;
        	}
        	
        	console.log("hadUpdate = "+hadUpdate);
        	
        	//If it is the client's turn and no one is being challenged, re-enable their inputs 
    		if(turnID == PLAYER_ID && challengeID == "false"){
    			console.log("In the players turn if statement");
    			disableFALSE();
    			if(!hadUpdate){
    				console.log("did not have a update");
    				tryTimer();
    			}
    		}
        	console.log("losing: "+losingId);
        	console.log("winning: "+winningId);
    		
    		if(losingId == PLAYER_ID){
    			$('#lose').removeAttr("hidden");
    			clearInterval(pingInterval);
    			
    		}
    		if(winningId == PLAYER_ID){
    			$('#win').removeAttr("hidden");
    			clearInterval(pingInterval);
    		}
    		
    		
    		console.log("*************************************************************************************************");
    		
        }
		
		
		
		
		
		
        //** HELPER FUNCTIONS **//
		function tryTimer(){
			console.log("timerOn = "+timerOn);
			if(!timerOn){
				timerOn = true;
				//Loooooop
				clock.setTime(maxTime);
				countdown = setInterval(function(){
					console.log("timerOn = "+timerOn);
					console.log("ran interval");
					var Time = clock.getTime();
					if(Time.time <= 0){
						console.log("it stopped!");
						sendMessage("post",{
							key: "expired",
							gameHash: GAME_HASH,
							playerId: PLAYER_ID
						});
						timerOn = false;
						disableTRUE();
						
					}
					else{
						console.log("Flipped it!");
						clock.decrement();
					}
				},1000);
			}
		}
        //Updates the table of players within the lobby
		function updateLobby(nameReady){
			var tableHTML = "<thead><tr><th>Name</th><th>Ready</th></tr></thead> <tbody>";
			for(i = 0; i<nameReady.length; i++){
				//console.log(nameReady);
				if(nameReady[i].length > 0){
					var tempArray = nameReady[i].split(" ");
					var name = tempArray[0];
					var ready = tempArray[1];
					//console.log("Name: "+name);
					//console.log("Ready: "+ready);
					if(ready == "true"){
						var URL = "https://upload.wikimedia.org/wikipedia/commons/thumb/0/0e/Ski_trail_rating_symbol-green_circle.svg/240px-Ski_trail_rating_symbol-green_circle.svg.png";
					}
					else{
						var URL = "https://upload.wikimedia.org/wikipedia/commons/f/f1/Ski_trail_rating_symbol_red_circle.png";
					}
					tableHTML += "<tr><td>"+name+"</td><td style='width: 10%'><img src='" + URL + "' style='width: 50%;'></td></tr>";
				}
			}
			tableHTML += "</tbody>";
			return tableHTML;
		}
		
		
		//Creates the table with playerList
        function createTable(list, turnIndex){
        	//Adds the head of the table by default
        	var tableHTML = "<thead><tr><th>Turn</th><th>Name</th><th>Lives</th></tr></thead> <tbody>";
        	
        	//for every player and name pair, add a row
        	for(var i=0; i<list.length; i+= 2){
        		tableHTML += "<tr>";
        		
        		//Adds the ghost icon if it is the players turn
        		if(turnIndex == i/2){
        			tableHTML += "<td style='width: 20%;'><img src='http://www.freepngimg.com/download/ghost/1-2-ghost-png-pic.png' style='width: 100%;'></td>";
        		}
        		else{
        			tableHTML += "<td></td>";
        		}
        		//Add player's name
        		tableHTML += "<td>"+list[i]+"</td>";
        		//Add player's lives
        		tableHTML += "<td> Lives: "+list[i+1]+"</td>";
        		tableHTML += "</tr>";
        	};
        	tableHTML += "</tbody>";
        	//Return the raw HTML to display within the div
        	return tableHTML;
        };
        
        //Disables users input field 
    	function disableTRUE(){
    		  $('#letterButton').prop("disabled", true);
    		  $('#letterInput').prop("disabled", true);
    		  $('#checkWord').prop("disabled", true);
    		  $('#challengeButton').prop("disabled", true);
	    };
    	//Re-enables users input field
    	function disableFALSE(){
  	          $('#letterButton').prop("disabled", false);
    		  $('#letterInput').prop("disabled", false);
  	          $('#checkWord').prop("disabled", false);
  	          $('#challengeButton').prop("disabled", false);
    	};
        //Moves from Join/Create --> NamePass
        function from1to2(){
        	$("#hostJoin").attr("hidden","true");
        	$("#namePassword").removeAttr("hidden");
        }
        //Moves from NamePass --> Game Lobby
        function from2to3(){
        	$("#namePassword").attr("hidden","true");
        	$("#gameLobby").removeAttr("hidden");
        	lobbyPingInterval = setInterval(lobbyPing, 500 );
        };
        function from3to4(){
        	//console.log("in the 3 to 4");
        	$("#gameLobby").attr("hidden","true");
        	$("#gameBoard").removeAttr("hidden");
        	pingInterval = setInterval( ping, 500 );
        };
        //This function uses recursion so the index keeps track of how many times the function is called
        setTimeoutIndex = 1;
        //Takes array and displays the messages waiting 2 seconds before switching to the next one
        function displayMessages(msgArray, isTurn){
        	//If its the first message, show the first element in the message array
        	if(setTimeoutIndex == 1){
        		$('#updateMsg').text(msgArray[0]);
        	}
        	//Waits 2 seconds and then shows the next function and calls the current function again
        	setTimeout(function() { 
    			$('#updateMsg').text(msgArray[setTimeoutIndex]);
    			//As long as there are still messages to send, call the method again
    			if(setTimeoutIndex < msgArray.length){
    				setTimeoutIndex ++;
    				displayMessages(msgArray);
    			}
    			//Reset the message on screen and reset the counter
    			else{
    				$('#updateMsg').text("");
    				setTimeoutIndex = 1;
    				if(isTurn){
    					console.log("RAN THE TIMER AFTER THE MESSAGES FINISHED LOADING");
    					tryTimer();
    					
    				}
    				hadUpdate = false;
    			}
        	}, 2000);
          	
        };
        
        //Takes the name and message of person, displays it in the chat log
        function displayChat(messageArray){
        	//console.log("In the displayChat Method: "+"<b>"+messageArray[1]+": </b>"+messageArray[2]);
        	$('#innerChat').append("<b>"+messageArray[1]+": </b>"+messageArray[2]+"<br>");
        }
        
        /*$('#rulesContainer').click(function(){
        	$("#myModal2").modal({backdrop: "true"});
        });*/
});
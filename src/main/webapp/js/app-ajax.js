//** This is the js for the client to run **//

$(document).ready(function() {
		//Used to identify the User's game by ID
		var GAME_ID;
		//Used to identify the User by ID
		var PLAYER_ID;
		//Used to keep track of Client updates
		var Client_update = 0;
		
		//Default post request to load the word list
		$.post('AjaxServlet', {
            initialize: "true"
		});
		
		
		
		//Runs when user Joins a game through entering a gameID
        $('#joinForm').submit(function(event) {
        		event.preventDefault();
        		//Sets the users inputs as disabled by default
        		disableTRUE();
        		
        		//Gets users gameID and assigns it to the global variable
                var id = $('#idInput').val();
                GAME_ID = id;
                $('#idInput').val("");
                
                //Sends postRequest to servlet, to make sure that game exists
                $.post('AjaxServlet', {
                		joinGame : "true",
                        gameID : id
                }, function(responseText) {
                		//If the game couldn't be found by the servlet, notify user
                		if(responseText.length < 1 || parseInt(responseText) == -1){
                			$('#msg').text("Your input was invalid or game didn't exist");
                			GAME_ID = "";
                		}
                		//If the game is found, let user move to the next screen
                		else{
                			//Change namePass to Join
                			$('#namePassTitle').text("Joined game at ID: "+responseText);
                			//Change screen method
                			from1to2();
                		}
                	});
                return false;
            });
        
        //Runs when user Creates new game
        $('#innerCreate').click(function(event) {
        	//Sends postRequest to tell servlet to create new game
            $.post('AjaxServlet', {
                    createGame : "true"
            }, function(responseText) {
            	GAME_ID = responseText;
            	//Change namePass to Create and move to next screen
            	$('#namePassTitle').text("Created game at ID: "+GAME_ID);
            	//Change screen method
            	from1to2();
            });
        });
        
        //Runs when user submits name and password
        $('#namePasswordForm').submit(function(event) {
        	var n = $('#nameInput').val();
        	var pass = $('#passwordInput').val();
        	$('#nameInput').val("");
        	$('#passwordInput').val("");
        	/* Sends postRequest to check if game exists and
        	 *  from there add new player to either new game or existing one */
            $.post('AjaxServlet', {
                    namePassword: "true",
                    name: n,
                    password: pass,
                    gameID: GAME_ID
            }, function(responseText) {
            			//if it is successful, move player to game screen
            			if(responseText.length > 0){
            			//Assigns the playerID with the newly created ID by the server
            			PLAYER_ID = responseText;
            			//Change screen method
            			from2to3();
            			}
            });
            return false;
        });
        
        //Runs when user submits letter
        $('#letterForm').submit(function(event) {
        	var letter = $('#letterInput').val();
        	$('#letterInput').val("");
        	//Sends letter and gameID
            $.post('AjaxServlet', {
                    sendLetter: "true",
                    letter: letter.toUpperCase(),
                    gameID: GAME_ID
            }, function(responseText) {
            			//If there was some sort of error, notify user
            			if(responseText.length < 1){
            				$('#errorMsg').text("You must enter a single letter");
            			}
            			//Otherwise, clear any error messages and disable the users input field
            			else{
            				$('#errorMsg').text("");
            				disableTRUE();
            			}
            });
            return false;
        });
        
      //Runs when user presses "That's a word!"
        $('#checkWord').click(function(event) {
            $.post('AjaxServlet', {
                    checkWord: "true",
                    gameID: GAME_ID
            });
        });
        
      //Runs when user sends challenge request
        $('#challengeButton').click(function(event) {
            $.post('AjaxServlet', {
                    challengeRequest: "true",
                    gameID: GAME_ID
            	}, 
            	function(responseText) {
            		disableTRUE();
        	});
        });
        
      //Runs when the user responds to the challenge request
        $('#challengeForm').submit(function(event) {
        	var word = $('#challengeInput').val();
        	$('#challengeInput').val("");
        	//Sends word and gameID
            $.post('AjaxServlet', {
                    challengeResponse: "true",
                    word: word.toLowerCase(),
                    gameID: GAME_ID
            });
            //Close the popup window
            $('#myModal').modal('hide');
            return false;
        });
        
        //if the user presses the 'X' in the modal
        $('#modalX').click(function(event) {
        	
        	$('#challengeInput').val("");
        	//Sends the error and gameID
            $.post('AjaxServlet', {
                    challengeResponse: "true",
                    word: "%ERROR%",
                    gameID: GAME_ID
            });
            //Closes the popup window
            $('#myModal').modal('hide');
        });
        
        
        //Every 500ms ping the server to update word, turn, and deathCounter
        function ping(){
        	//Sends postRequest to finds info about game
            $.post('AjaxServlet', {
            	ping: "true",
            	gameID: GAME_ID,
            	playerID: PLAYER_ID
            	},function(responseText) {
            	console.log(responseText);
            	//Splits the response into the word, turn id, and the players, and then the update text
            	var temp = responseText.split("|");
            	var wordTurnPlayersArray = temp[0].split(" ");
            	var updateChallengeArray = temp[1].split("*");
            	console.log(wordTurnPlayersArray);
            	console.log(updateChallengeArray);
            	
            	//Uses the word, turn, and players to update client
        		var word = wordTurnPlayersArray[0];
        		var turnID = wordTurnPlayersArray[1];
        		var turnIndex = wordTurnPlayersArray[2];

        		//get rid of everything but the players in the first array
        		wordTurnPlayersArray.splice(0,3);
        		console.log(wordTurnPlayersArray);
        		
        		//update the board with the new word
                $('#mainMsg').text(word);
                
                
        		
                //update the table of players on the right
                $('#playerListTable').html(createTable(wordTurnPlayersArray,turnIndex));
                
                
                
                
            	var msg = updateChallengeArray[0];
            	var Server_update = updateChallengeArray[1]; 
            	var challengeID = updateChallengeArray[2];
            	
            	//If the client is not up to date with the server, show updateMsg
            	if(Client_update != Server_update){
            		displayMessages(msg.split("~"));
            		Client_update = Server_update;
            	}
            	
            	
            	//If the client is the one being challenged, show the modal
            	if(challengeID == PLAYER_ID ){
            		$("#myModal").modal({backdrop: "static"});
            	}
                
            	//If it is the client's turn and no one is being challenged, re-enable their inputs 
        		if(turnID == PLAYER_ID && challengeID == "false"){
        			disableFALSE();
        		}
            });
        }
        
        
        //** HELPER FUNCTIONS **//
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
        //Moves from NamePass --> RealGame
        function from2to3(){
        	$("#namePassword").attr("hidden","true");
        	$("#gameBoard").removeAttr("hidden");
        	setInterval( ping, 500 );
        };
        //This function uses recursion so the index keeps track of how many times the function is called
        setTimeoutIndex = 1;
        //Takes array and displays the messages waiting 2 seconds before switching to the next one
        function displayMessages(msgArray){
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
    			}
        	}, 2000);
          	
        }
        
});
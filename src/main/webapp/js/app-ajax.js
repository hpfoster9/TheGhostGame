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
			}, function(responseText) {}
		);
		
		
		
		//Runs when user Joins a game through entering a gameID
        $('#joinForm').submit(function(event) {
        		event.preventDefault();
        		//Sets the users letterInput as disabled by default
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
                			from1to2();
                		}
                	});
                return false;
            });
        
        //Runs when user Creates new game
        $('#create').click(function(event) {
        	//Sends postRequest to tell servlet to create new game
            $.post('AjaxServlet', {
                    createGame : "true"
            }, function(responseText) {
            	GAME_ID = responseText;
            	//Change namePass to Create and move to next screen
            	$('#namePassTitle').text("Created game at ID: "+GAME_ID);
            	
            	from1to2();
            });
        });
        
        //Runs when user submits name and password
        $('#namePasswordForm').submit(function(event) {
        	event.preventDefault();
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
            			PLAYER_ID = responseText;
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
            				$('#errorMsg').val("You must enter a single letter");
            			}
            			//Otherwise, clear any error messages and disable the users input field
            			else{
            				$('#errorMsg').val("");
            				disableTRUE();
            			}
            });
            return false;
        });
        
      //Runs when user submits letter
        $('#checkWord').click(function(event) {
        	console.log("checkWord was clicked");
            $.post('AjaxServlet', {
                    checkWord: "true",
                    gameID: GAME_ID
            }, function(responseText) {});
        });
        
      //Runs when user sends challenge request
        $('#challengeButton').click(function(event) {
        	console.log("challenge was clicked");
            $.post('AjaxServlet', {
                    challengeRequest: "true",
                    gameID: GAME_ID
            }, function(responseText) {
            	disableTRUE();
            });
        });
        
      //Runs when the user responds to the challenge request
        $('#challengeForm').submit(function(event) {
        	console.log("challengeForm was submitted");
        	var word = $('#challengeInput').val();
        	$('#challengeInput').val("");
        	//Sends word and gameID
            $.post('AjaxServlet', {
                    challengeResponse: "true",
                    word: word.toLowerCase(),
                    gameID: GAME_ID
            }, function(responseText) {});
            $('#myModal').modal('hide');
            return false;
        });
        
        //if the user presses the 'X' in the modal
        $('#modalX').click(function(event) {
        	
        	$('#challengeInput').val("");
        	//Sends word and gameID
            $.post('AjaxServlet', {
                    challengeResponse: "true",
                    word: "%ERROR%",
                    gameID: GAME_ID
            }, function(responseText) {});
            $('#myModal').modal('hide');
        });
        
        
        //Every 500ms ping the server to update word, turn, and deathCounter
        function ping()
        {
            $(function() {
            	//Sends postRequest to finds info about game
                $.post('AjaxServlet', {
                	ping:"",
                	gameID: GAME_ID,
                	playerID: PLAYER_ID
                	},function(responseText) {
                	//Splits the response into players and update text
                	var firstArray = responseText.split("|");
                	var secondArray = firstArray[1].split("*");
                
                	var msg = secondArray[0];
                	var Server_update = secondArray[1]; 
                	var challengeID = secondArray[2];
                	
                	//If the client is not up to date with the server, show update
                	if(Client_update != Server_update){
                		displayMessages(msg.split("~"));
                		Client_update = Server_update;
                	}
                	
                	
                	//If the client is the one being challenged, show the modal
                	if(challengeID == PLAYER_ID ){
                		$("#myModal").modal({backdrop: "static"});
                	}
                	
                	
                	
                	//keeps a numbered record, most up to date, if servers number is higher, display message 
                	
                	
                	
                	
                	//Gets info and updates client
            		var inputArray = firstArray[0].split(" ");
            		var word = inputArray[0];
            		var turnID = inputArray[1];
            		var turnIndex = inputArray[2];
            		/*
            		console.log("in response function");
            		console.log(inputArray);
            		console.log("word: "+word);
            		console.log("turnID: "+turnID);
            		console.log("turnIndex" +turnIndex);
            		*/
            		inputArray.splice(0,3);
            		//console.log(inputArray);
            		console.log($('#mainMsg').text());
                    $('#mainMsg').text(word);
                    $('#playerListTable').html(createTable(inputArray,turnIndex));
            		if(turnID == PLAYER_ID && challengeID == "false"){
            			disableFALSE();
            		}
            });
            });
        }
        
        
        //** HELPER FUNCTIONS **//
        //Creates the table with playerList
        function createTable(list, turnIndex){
        	/*console.log("refreshed table");
        	console.log(list);
        	console.log(turnIndex);
        	*/
        	var tableHTML = "<thead><tr><th>Turn</th><th>Name</th><th>Lives</th></tr></thead> <tbody>";
        	for(var i=0; i<list.length; i+= 2){
        		/*
        		console.log(list.length);
        		console.log(i+": "+tableHTML);
        		*/
        		tableHTML += "<tr>";
        		
        		if(turnIndex == i/2){
        			tableHTML += "<td style='width: 20%;'><img src='http://www.freepngimg.com/download/ghost/1-2-ghost-png-pic.png' style='width: 100%;'></td>";
        			//console.log("put ghost at index: "+i/2);
        		}
        		else{
        			tableHTML += "<td></td>";
        			//console.log("didn't put ghost at index: "+(i/2));
        		}
        		tableHTML += "<td>"+list[i]+"</td>";
        		tableHTML += "<td> Lives: "+list[i+1]+"</td>";
        		tableHTML += "</tr>";
        	};
        	tableHTML += "</tbody>";
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
        //Takes array and displays the messages
        setTimeoutIndex = 1;
        function displayMessages(array){
        	console.log("In the display message");
        	console.log(array);
        	if(setTimeoutIndex == 1){
        		$('#updateMsg').text(array[0]);
        	}
        	setTimeout(function() { 
        		console.log("inside setTimeout");
    			$('#updateMsg').text(array[setTimeoutIndex]);
    			if(setTimeoutIndex < array.length){
    				setTimeoutIndex ++;
    				displayMessages(array);
    			}
    			else{
    					$('#updateMsg').text("");
    				
    				setTimeoutIndex = 1;
    			}
        	}, 2000);
          	
        }
        
});
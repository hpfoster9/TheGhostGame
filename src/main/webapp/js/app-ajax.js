$(document).ready(function() {
		//Used to identify the User's game by ID
		var GAME_ID;
		//Used to identify the User by ID
		var PLAYER_ID;
		
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
                	//Gets info and updates client
            		var inputArray = responseText.split(" ");
            		var word = inputArray[0];
            		var turnID = inputArray[1];
            		var turnIndex = inputArray[2];
            		console.log("in response function");
            		console.log(inputArray);
            		console.log("word: "+word);
            		console.log("turnID: "+turnID);
            		console.log("turnIndex" +turnIndex);
            		inputArray.splice(0,3);
            		console.log(inputArray);
                    $('#mainMsg').text(word);
                    $('#playerListTable').html(createTable(inputArray,turnIndex));
            		if(turnID == PLAYER_ID){
            			disableFALSE();
            		}
            });
            });
        }
        
        
        //** HELPER FUNCTIONS **//
        //Creates the table with playerList
        function createTable(list, turnIndex){
        	console.log("refreshed table");
        	console.log(list);
        	console.log(turnIndex);
        	var tableHTML = "<thead><tr><th>Turn</th><th>Name</th><th>Lives</th></tr></thead> <tbody>";
        	for(var i=0; i<list.length; i+= 2){
        		console.log(list.length);
        		console.log(i+": "+tableHTML);
        		tableHTML += "<tr>";
        		
        		if(turnIndex == i/2){
        			tableHTML += "<td style='width: 20%;'><img src='http://www.freepngimg.com/download/ghost/1-2-ghost-png-pic.png' style='width: 100%;'></td>";
        			console.log("put ghost at index: "+i/2);
        		}
        		else{
        			tableHTML += "<td></td>";
        			console.log("didn't put ghost at index: "+(i/2));
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
    		  $('#letterInput').prop("disabled", true);
	    };
    	//Re-enables users input field
    	function disableFALSE(){
  	      $("#letterInput").prop("disabled", false);
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
        
});
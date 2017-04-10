$(document).ready(function() {
		//Used to identify the User's game by ID
		var GAME_ID;
		//Used to identify the User by ID
		var PLAYER_ID;
		
		//Runs when user Joins a game through entering a gameID
        $('#join').focus(function(event) {
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
        $('#submitNamePassword').click(function(event) {
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
        });
        
        //Runs when user submits letter
        $('#letterButton').click(function(event) {
        	var letter = $('#letterInput').val();
        	$('#letterInput').val("");
        	//Sends letter and gameID
            $.post('AjaxServlet', {
                    sendLetter: "true",
                    letter: letter,
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
            		var turnID = inputArray[1]
                    $('#mainMsg').text(word);
            		if(turnID == PLAYER_ID){
            			disableFALSE();
            		}
            });
            });
        }
        
        
        //** HELPER FUNCTIONS **//
        
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
        }
        
});
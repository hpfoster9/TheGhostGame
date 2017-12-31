<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>The Ghost Game</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
        <link rel="stylesheet" href="css/mycss.css">
        
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
        <script src="js/jquery.quickfit.js" type="text/javascript"></script>
        <script src="js/app-ajax.js" type="text/javascript"></script>
        
        <link rel="stylesheet" href="css/flipclock.css" />
        <script src="https://cdnjs.cloudflare.com/ajax/libs/flipclock/0.7.8/flipclock.min.js"></script>
        
      <audio id="ghostSong" loop >
      <source src="ghost.ogg" type="audio/ogg">
      	<source src="ghost.wav" type="audio/wav">
      	
      </audio>
    </head>
    <body>
    
    
    
        <div id="hostJoin" >
            <h1 class="title" id="ghostTitle" onclick="window.location.href= window.location.href.replace('index','admin');")>The Ghost Game</h1>
            <br/>
            
            <form id="joinForm">
                <div class="input-group" id="hostJoinInnerDiv">
                    <input type="text" class="form-control" id="idInput" placeholder="Game ID">
                    <button type="submit" class="btn btn-primary" id="join">Join Game</button>
                </div>
            </form>
            <p id="msg"></p>
            <footer>
                <p id="outerCreate">Create a new game: <a id="innerCreate">Here</a>.</p>
            </footer>
        </div>
        
        
        
        <div id="namePassword" hidden>
            <h1 class="title">The Ghost Game</h1>
            <br/>
            <h1 id="namePassTitle"></h1>
            <br>
            <div id="namePassContainer">
                <form id="namePasswordForm" >
                <div id="createSettings" hidden>
	                <div class="form-group row">
	                        <div class="col-sm-9">
			                    <label for="secondsInput" id="secondsLabel">Seconds per round:</label>
							      <select class="form-control" id="secondsInput">
							        <option>15</option>
							        <option>30</option>
							        <option>45</option>
							        <option>60</option>
							      </select>
							    </div>
	                    	</div>
				      <div class="form-group row">
					      <div class="col-sm-9">
					      <label for="livesInput" id="livesLabel">Lives:</label>
						    <input value=5 class="form-control" type="number" id="livesInput">
						  </div>
					  </div>
				</div>
                    <div class="form-group row" >
                        <div class="col-sm-9">
                            <input type="text" class="form-control" id="nameInput" placeholder="Player Name">
                        </div>
                    </div>
                    <div class="form-group row">
                        <div class="col-sm-9">
                            <input type="password" class="form-control" id="passwordInput" placeholder="Game Password">
                        </div>
                    </div>
                    
				      
                    <div class="form-group row" id="namePassSubmitContainer">
                        <div class="col-sm-7">
                            <button type="submit" class="btn btn-primary" id="submitNamePassword">Join Game</button>
                        </div>
                    </div>
                </form>
            </div>
            <p id="namePassMsg"></p>
        </div>
        
        
        <div id="gameLobby" hidden >
            <h1 class="title">The Ghost Game</h1>
            <br/>
            <div id="timerContainer"> 
            </div>
            <div id="rulesContainer"> RULES
            </div>
            <div id="playersContainerLobby">
                
                <div class="table-responsive" id="playersInnerDivLobby">
                    <table class="table" id="playerListTableLobby">
                    </table>
                </div>
            </div>
            <div  id="ads" > 
            <img hidden id="sound1" class="picSound" src="https://upload.wikimedia.org/wikipedia/commons/thumb/2/21/Speaker_Icon.svg/2000px-Speaker_Icon.svg.png" alt="Smiley face" height="50px" width="50px">
            <img  id="mute1" class="picMute" src="https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/Mute_Icon.svg/2000px-Mute_Icon.svg.png" alt="Smiley face" height="50px" width="50px">
            </div>
            <div id="outerMsgContainerLobby">
                <div id="innerMsgContainerLobby">
                	<div id="updateMsgLobby">
                	WELCOME TO THE GAME LOBBY
                	<div id="gameHashLobby"></div>
                    <div id="mainMsgLobby"></div>
                    <div class="table-responsive" id="readyPlayersDiv" >          
						  <table class="table" id="lobbyPingTable">
						  </table>
  					</div>
  					<div id="gameAboutToStart"></div>
                    <p id="errorMsgLobby"></p>
                    </div>
                </div>
                
                        <button type="submit" class="btn btn-primary" id="readyUp">I'm Ready!</button>

            </div>
        </div>
        
        
        
        
        
        
        <div id="gameBoard" hidden >
            <h1 class="title">The Ghost Game</h1>
            <br/>
            <div id="timerContainer" style="display: table;"> 
           <p id="countdownP" style="width: 100%; height: 100%; font-size: 8vw; display: table-cell;
    vertical-align: middle;
           "></p>
            </div>
            <div id="rulesContainer2" style="display: table;">
            <p id="rulesP" style="width: 100%; height: 100%; font-size: 3vw; display: table-cell;
    vertical-align: middle;
            "> RULES </p>
            
            </div>
            <div id="playersContainer">
                <!-- *****PLAYER LIST***** -->
                <div class="table-responsive" id="playersInnerDiv">
                    <table class="table" id="playerListTable">
                    </table>
                </div>
            </div>
            <div  id="ads" > 
            <img hidden id="sound2" class="picSound" src="https://upload.wikimedia.org/wikipedia/commons/thumb/2/21/Speaker_Icon.svg/2000px-Speaker_Icon.svg.png" alt="Smiley face" height="50px" width="50px">
            <img  id="mute2" class="picMute" src="https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/Mute_Icon.svg/2000px-Mute_Icon.svg.png" alt="Smiley face" height="50px" width="50px">
            
            </div>
            <div id="outerMsgContainer">
                <div id="innerMsgContainer">
                    <div id="mainMsg"></div>
                    <div id="updateMsg">
                    </div>
                    <div id="win" hidden>You won!</div>
                    <div id="lose" hidden>You have lost :(</div>
                    <p id="errorMsg"></p>
                </div>
                <!-- Modal -->
                <div class="modal fade" id="myModal" role="dialog">
                    <div class="modal-dialog">
                        <!-- Modal content-->
                        <div class="modal-content">
                            <div class="modal-header">
                                
                                <h4 class="modal-title">You've been challenged</h4>
                            </div>
                            <div class="modal-body">
                                <p>Please enter the word you were trying to spell</p>
                                <form id="challengeForm">
                                    <div class="input-group" >
                                        <input id="challengeInput" type="text" class="form-control" placeholder="Insert Word">
                                        <span class="input-group-btn">
                                        <button id="challengeSubmit" class="btn btn-default" type="submit">Go</button>
                                        </span>
                                    </div>
                                    <!-- /input-group -->
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
                
                
                
                
                <div style="padding-top: 3vh; clear: both;" >
                    <div  style="margin-left: 10%; width: 20%; float: left;" >
                        <button type="submit" class="btn btn-primary" id="checkWord">That's a word!</button>
                    </div>
                    <div id="letterContainer">
                        <form id="letterForm">
                            <div class="input-group" >
                                <input id="letterInput" type="text" class="form-control" placeholder="Insert letter">
                                <span class="input-group-btn">
                                <button id="letterButton" class="btn btn-default" type="submit">Go!</button>
                                </span>
                            </div>
                        </form>
                    </div>
                    <div id="challengeContainer">
                        <button type="submit" class="btn btn-primary" id="challengeButton">Challenge</button>
                    </div>
                </div>
            </div>
            <div id="outerChat">
                <div id="innerChat">

                </div>
                <form id="chatForm">
                    <div class="input-group" id="chatMsgContainer">
                        <input type="text" id="chatInput" class="form-control" placeholder="Type your message here">
                        <div class="input-group-btn">
                            <button class="btn btn-default"  type="submit">
                            <i>Go</i>
                            </button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
        
                <!-- Modal -->
                <div class="modal fade" id="myModal2" role="dialog">
                    <div class="modal-dialog">
                        <!-- Modal content-->
                        <div class="modal-content">
                            <div class="modal-header">
                                
                                <h4 class="modal-title">Ghost</h4>
                            </div>
                            <div class="modal-body">
                                <b>Objective:</b>
                               <p> The objective of Ghost is to be the last man standing. </p>
	
 
<b>How to Win:</b><br>
	<p>Each round players will take turns adding letters to a word. You should try to add a letter that continues but does not complete a word. For example: If it was your turn and the string of letters so far were “CHAS”, you would lose a life if you put and “E” because it complets the word “CHASE” but would not if you put “I” because “CHASING” could be spelled. If instead you had put “Q” you would also lose a life because no word can be spelled when the first five letters are “CHASQ”. Last man remaining with a life left wins.</p>
 <br>
<b>Rules:</b><br>
<ol>
<li>Before the game starts the creator of the game will enter the settings...This includes the time for each move and the number of lives each player has.
</li><li>When it is a player's turn they have a either 15, 30, or 45 seconds to make a move
</li><li>Each turn a player can either put a letter, click “Challenge”, or click “That’s a word!”
</li><li>Failure to make a move in the given time will result in a lost life
</li><li>If a player puts down a letter it is the next player’s turn
</li><li>If a player clicks “Challenge” the previous player has to spell the word they were thinking of
</li><li>Failure to spell a word will result in a lost life
</li><li>A correct spelling of word that does not start with the letters on the screen will result in a lost life
</li><li>If the player being challenged does spell a word that begins with the letters on the screen the challenger loses a life
</li><li>If a player clicks “That’s a word!”, believing the letter the previous player entered completes a word, the game will either say it is or is not a word. If it is a word the previous player will lose a life. If it is not a word the player who clicked “That’s a word!” will lose a life.
</li><li>Whoever clicks “Challenge” or “That’s a word!” Will go first next round.
</li><li>For a word to be valid it must be at least four letters in length and cannot be an uncommon proper noun
</li><li>Once a player runs out of lives the player is removed from the game
</li><li>This process continues until only one player remains </li>
 </ol>
                                
                            </div>
                        </div>
                    </div>
                </div>
                
                
                <div id="hidden-resizer" style="visibility: hidden"></div>
    </body>
</html>
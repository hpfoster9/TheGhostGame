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
      <script src="js/admin.js" type="text/javascript"></script>
      <link rel="stylesheet" href="css/flipclock.css" />
      <script src="https://cdnjs.cloudflare.com/ajax/libs/flipclock/0.7.8/flipclock.min.js"></script>
      <audio id="ghostSong" loop >
         <source src="ghost.ogg" type="audio/ogg">
         <source src="ghost.wav" type="audio/wav">
      </audio>
   </head>
   <body>
      <h1 class="title">The Ghost Game</h1>
      <br/>
      <div id="timerContainer"> </div>
      <div id="rulesContainer"> RULES</div>
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
               WELCOME TO THE ADMIN PAGE
               <div id="gameHashLobby"></div>
               <div id="mainMsgLobby"></div>
               <div class="table-responsive" id="readyPlayersDiv" >
                  <table class="table" id="adminPingTable">
                  </table>
               </div>
               <p id="errorMsgLobby"></p>
            </div>
         </div>
      </div>
   </body>
</html>
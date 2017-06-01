<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
"http://www.w3.org/TR/html4/loose.dtd">
<html style="height: 100%;">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>The Ghost Game</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<!-- <script src="http://code.jquery.com/jquery-1.10.2.js"
	type="text/javascript"></script> -->
<script src="js/app-ajax.js" type="text/javascript"></script>
</head>
<body  style="background-color: #717181; margin:0; padding:0; height:100%;">

<div id="hostJoin" style="min-height: 100%; position:relative;">

      <h1 style="color: #E1E1E1; font-size: 48px; text-align: center; margin-top: 0; padding-top: 20px;">The Ghost Game</h1><br/>

	                   
      <form id="joinForm">
            <div class="input-group" style="width: 250px;   margin-top: 10px !important; margin-bottom: 10px !important; text-align: center; margin: auto;  ">
                <input type="text" class="form-control" id="idInput" placeholder="Game ID" style="text-align: center; border-radius: 4px;">
                <button type="submit" class="btn btn-primary" style="margin-top: 10px; width: 250px;  text-align: center; background-color: #E1E1E1; color: #4B4B4B; border-color: #4B4B4B; font-weight: 800; font-size: 16px;" id="join">Join Game</button>
            </div>
      </form>
      <p id="msg"></p>
                
       
      <footer style="font-size: 24px; text-align: center; color: #4B4B4B; position:absolute; bottom:0; width:100%; height:60px; background:#E1E1E1;">
          <p style="margin-top: 15px;">Create a new game: <a id="create" style="cursor: pointer; color: #91AAB0;">Here</a>.</p>
      </footer>

</div>

<!-- 
<div id="hostJoin" >
	<form>Game ID: <input type="text" id="idInput"><br/>
	<button type="button" id="join">Join</button>
	<button type="button" id="create">Create</button>
	</form>
	<p id="msg"></p>
</div>
-->




<div id="namePassword" hidden >
        <h1 style="color: #E1E1E1; font-size: 48px; text-align: center; margin-top: 0; padding-top: 20px;">The Ghost Game</h1><br/>
        
	<h1 id="namePassTitle" style="text-align: center; color: #E1E1E1; font-size: 24px;"></h1><br>
	<div style="border-radius: 5px; width: 30%;  margin: auto; background-color: #E1E1E1; color: #4B4B4B; border-color: #4B4B4B; padding: 15px;">
                <form id="namePasswordForm" >
                          <div class="form-group row" >
                            
                            <div class="col-sm-10">
                              <input type="text" class="form-control" id="nameInput" placeholder="Player Name" style="width: 100%; margin-left: 10%; margin-top: 10px;">
                            </div>
                          </div>
                          <div class="form-group row">
                           
                            <div class="col-sm-10">
                              <input type="password" class="form-control" id="passwordInput" placeholder="Game Password" style="width: 100%; margin-left: 10%;">
                            </div>
                          </div>
                          <div class="form-group row" style="margin-bottom: 10px;">
                            <div class="col-sm-10">
                            <button type="submit" class="btn btn-primary" style=" width: 250px;  text-align: center; background-color: #4B4B4B; color: white; border-color: #4B4B4B; font-weight: 800; font-size: 16px; width: 100%; margin-left: 10%;" id="submitNamePassword">Join Game</button>
                            </div>
                          </div>
                </form>
        </div>
        
	<p id="namePassMsg"></p>
</div>


<!-- 
<div id="namePassword" hidden>
	<h1 id="namePassTitle"></h1><br>
	<form>
	Your name: <input type="text" id="nameInput"><br/>
	Game password: <input type="password" id="passwordInput"><br/>
	<button type="button" id="submitNamePassword">Submit</button>
	</form><br/>
	<p id="namePassMsg"></p>
</div>
-->




<div id="gameBoard" style="height: 100%;" hidden >
	        <h1 style="margin-left: 10%; width: 80%; height: 10%; color: #E1E1E1; font-size: 8vmin; text-align: center; margin-top: 0; padding-top: 20px;">The Ghost Game</h1><br/>
        
       <div style="text-align: center; border-radius: 5px; height: 15vmin; width: 15vmin;  position: absolute; left: 5px; top: 5px; background-color: #E1E1E1; color: #4B4B4B; border-color: #4B4B4B; "> 
       
       </div>
       
       <div style="text-align: center; border-radius: 5px; height: 15vmin; width: 15vmin;  position: absolute; float: right; right: 5px; top: 5px; background-color: #E1E1E1; color: #4B4B4B; border-color: #4B4B4B; "> 
       
       </div>
       
       <div style="overflow-y: scroll; overflow-x: hidden;text-align: center; border-radius: 5px; width: 15%; height: 75vh; position:absolute; top: 20vmin; float: right; right: 5px;  background-color: #E1E1E1; color: #4B4B4B; border-color: #4B4B4B; "> 
       <!-- *****PLAYER LIST***** -->
       
        <div class="table-responsive" style="text-align: left;">          
  <table class="table" id="playerListTable">
    
  </table>
  </div>
       
       
       
       
       
       
       </div>
       <div style="text-align: center; border-radius: 5px; width: 15%; height: 75vh; position:absolute; top: 20vmin; float: left; left: 5px;  background-color: #E1E1E1; color: #4B4B4B; border-color: #4B4B4B; "> 
       
       </div>
        
        <div style="border-radius: 5px; width: 65%;  height: 55%; margin: auto; background-color: #E1E1E1; color: #4B4B4B; border-color: #4B4B4B; padding-top: 1vh;">
                 <div id="mainMsgContainer" style="border-radius: 5px; width: 98%; margin: auto; height: 80%;   background-color: white; color: #4B4B4B; font-size: 72px; text-align: center; border-color: #4B4B4B; ">
                 	<div id="mainMsg"></div>
                 
                 	<div id="updateMsg" style="color:grey; font-size: 36px; text-align: center;">
 					</div>
                 
                 </div>
 					
 					
 				
 				
 				
 				
 				
 				
 				
 				
 				
 				
 				<!-- Modal -->
			  <div class="modal fade" id="myModal" role="dialog">
			    <div class="modal-dialog">
			    
			      <!-- Modal content-->
			      <div class="modal-content">
			        <div class="modal-header">
			          <button type="button" class="close" data-dismiss="modal" id="modalX">×</button>
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
					    </div><!-- /input-group -->
					    </form>
			        </div>
			      </div>
			      
			    </div>
			  </div>
 				
 				
 				
 				
 				
 				
 					
                 <div style="padding-top: 3vh; clear: both;" >
                 <div  style="margin-left: 10%; width: 20%; float: left;" >
                 <button type="submit" class="btn btn-primary" style=" width: 100%;  text-align: center; background-color: #4B4B4B; color: white; border-color: #4B4B4B; font-weight: 800; font-size: 1.2vw; border-radius: 4px; display: inline;" id="checkWord">That's a word!</button>
                 </div>
                 
                 <div style="margin-left: 10%; width: 20%; float: left;">
    <form id="letterForm">
    <div class="input-group" >
      <input id="letterInput" type="text" class="form-control" placeholder="Insert letter">
      <span class="input-group-btn">
        <button id="letterButton" class="btn btn-default" type="submit">Go!</button>
      </span>
    </div><!-- /input-group -->
    </form>
  </div><!-- /.col-lg-6 -->
  <div style="margin-left: 10%;  width: 20%; float: left;">
                 <button type="submit" class="btn btn-primary" style=" width: 100%;  text-align: center; background-color: #4B4B4B; color: white; border-color: #4B4B4B; font-weight: 800; font-size: 1.2vw; border-radius: 4px; display: inline;" id="challengeButton">Challenge</button>
                 </div>
                </div>
</div> 
	
        <div style="border-radius: 5px; width: 65%; margin: auto; height: 25vh;  margin-top: 1vh; padding-top: 5px; background-color: #E1E1E1; color: #4B4B4B; border-color: #4B4B4B; "> 
       <div style="overflow-y: scroll; overflow-x: wrap; border-radius: 5px; width: 98%; margin: auto; height: 18vh;    background-color: white; color: #4B4B4B; border-color: #4B4B4B; ">
        <!-- *****CHAT WITHIN GAME***** -->
╔╦╦╦═╦╗╔═╦═╦══╦═╗<br>
║║║║╩╣╚╣═╣║║║║║╩╣<br>
╚══╩═╩═╩═╩═╩╩╩╩═╝<br>
<br>
║║╔║║╔╗ ║<br>
╠╣╠║║║║ ║<br>
║║╚╚╚╚╝ O<br>
 ̿’ ̿’\̵͇̿̿\з=(•̪●)=ε/̵͇̿̿/’̿’̿
<br>
┊　　┊　　┊　　┊<br>
┊　　┊　　┊　　★<br>
┊　　┊　　☆<br>
┊　　★<br>
☆<br>
<br>
╔ღ═╗╔╗<br>
╚╗╔╝║║ღ═╦╦╦═ღ<br>
╔╝╚╗ღ╚╣║║║║╠╣<br>
╚═ღ╝╚═╩═╩ღ╩═╝<br>
<br>
╔╗╔═╦╗<br>
║╚╣║║╚╗<br>
╚═╩═╩═╝<br>
<br>
ن٥ﻻ ﻉ√٥ﺎ ٱ
<br>
ᶫᵒᵛᵉᵧₒᵤ
<br>
╭∩╮( º.º )╭∩╮
<br>
▓▓▓▓<br>
▒▒▒▓▓<br>
▒▒▒▒▒▓<br>
▒▒▒▒▒▒▓<br>
▒▒▒▒▒▒▓<br>
▒▒▒▒▒▒▒▓<br>
▒▒▒▒▒▒▒▓▓▓<br>
▒▓▓▓▓▓▓░░░▓<br>
▒▓░░░░▓░░░░▓<br>
▓░░░░░░▓░▓░▓<br>
▓░░░░░░▓░░░▓<br>
▓░░▓░░░▓▓▓▓<br>
▒▓░░░░▓▒▒▒▒▓<br>
▒▒▓▓▓▓▒▒▒▒▒▓<br>
▒▒▒▒▒▒▒▒▓▓▓▓<br>
▒▒▒▒▒▓▓▓▒▒▒▒▓<br>
▒▒▒▒▓▒▒▒▒▒▒▒▒▓<br>
▒▒▒▓▒▒▒▒▒▒▒▒▒▓<br>
▒▒▓▒▒▒▒▒▒▒▒▒▒▒▓<br>
▒▓▒▓▒▒▒▒▒▒▒▒▒▓<br>
▒▓▒▓▓▓▓▓▓▓▓▓▓<br>
▒▓▒▒▒▒▒▒▒▓<br>
▒▒▓▒▒▒▒▒▓<br>
<br>
<br>
<br>
<br>
<br>
░░░░░░███████ ]▄▄▄▄▄▄▄▄▃<br>
▂▄▅█████████▅▄▃▂<br>
I███████████████████].<br>
◥⊙▲⊙▲⊙▲⊙▲⊙▲⊙▲⊙◤<br>
<br>
……(\__/)<br>
……(=’.’=)<br>
…☆(”)_(”)☆<br>
<br>
░(¯`♥´¯)░░░░░<br>
░░(¯`♥´¯)░░░░<br>
░░░(¯`♥´¯)░░░<br>
░░░░(¯`♥´¯)░░<br>
<br>
╠╣αppy Ѽ ╠╣αlloween♥ ツ ٩(●̮̮̃•̃)۶<br>‌
<br>
♫♪♥♥[̲̅̅H̲̅][̲̅̅A̲̅][̲̅̅P̲̅][̲̅̅P̲̅][̲̅̅Y̲̅] [̲̅̅B̲̅][̲̅̅I̲̅][̲̅̅R̲̅][̲̅̅T̲̅][̲̅̅H̲̅][̲̅̅D̲̅][̲̅̅A̲̅][̲̅̅Y̲̅]♥♥♫♪<br>
<br>
……(\_/)<br>
……( ‘_’)<br>
…./”"”"”"”"”"”"\======░<br>
/”"”"”"”"”"”"”"”"”"”\<br>
\_@_@_@_@_@_/<br>
<br>
<br>
^^^^^^^^^^^|<br>
| TRUCK!!!!!! ‘|”"”;..,___.<br>
|_…_…______===|= _|__|…, ] |<br>
“(@ )’(@ )”"”"*|(@)(@ )*****(@)<br>
<br>
FACES:<br>
<br>
<br>
(✿◠‿◠) (◡‿◡✿) (◕‿◕✿)<br>
<br>
(✖╭╮✖) (≧◡≦) (¬_¬)<br>
<br>
(◑‿◐) (◕‿-) ✖‿✖ (-’_'-)<br>
<br>
(╥_╥) (╯_╰) (╯3╰) (o_-)<br>
<br>
(¬‿¬) (◣_◢) (∪ ◡ ∪)<br>
<br>
(≧ω≦) o(≧o≦)o (⋋▂⋌)<br>
<br>
(॓_॔) (╯ಊ╰) (─‿‿─)<br>
<br>
‹(•¿•)› (╯︵╰,) (︶︹︺)<br>
<br>
(∩︵∩) (｡◕‿◕｡) (⊙_◎)<br>
<br>
(~￣▽￣)~ (︶ω︶) (+_+)<br>
<br>
(◕^^◕) (∩▂∩) (¬▂¬)<br>
<br>
(｡♥‿♥｡) (✿ ♥‿♥) ♥╣[-_-]╠♥<br>
<br>
٩(͡๏̯͡๏)۶ ٩(-̮̮̃•̃)۶ ٩(̾●̮̮̃̾•̃̾)۶ ٩(-̮̮̃-̃)۶<br>
<br>
٩(×̯×)۶ {｡^◕‿◕^｡} (ñ_ñ)<br>
<br>
(u_u) (*_*) (º_º)<br>
<br>
::: (\_(\<br>
*: (=’ :’) :*<br>
•.. (,(”)(”)¤°.¸¸.•´¯`»<br>
<br><br>
<br>
★<br>
(¯`·.·´¯) (¯`·.·´¯)<br>
`·.¸(¯`·.·´¯)¸ .·<br>
×°× ` ·.¸.·´ ×°×<br>
<br>
<br>
╔══╗♫ *´”)<br>
║██║ ¸.•´¸.•*´¨) ¸.•*¨) ♫ ♪<br>
║(O)║ (¸.•´ (¸.• ♫ ♪<br>
╚══╝<br>
<br>
<br>
……….(¯`v´¯)<br>
………..`•.¸.•´<br>
………(●̮̮̃•̃)..(●̮̮̃•̃)<br>
……… /█\ ♥/█\<br>
<br>
<br>
(\ /)<br>
( . .)♥<br>
c(”)(”)<br>
<br>
▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄<br>
████▌▄▌▄▐▐▌█████<br>
████▌▄▌▄▐▐▌▀████<br>
▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀<br>
<br>
<br>
╔═╦══╦═╗<br>
║╩╣║║║║║<br>
╚═╩╩╩╩═╝<br>
<br>
<br>
╠╬╬╬╣<br>
╠╬╬╬╣OK! WHO ATE MY<br>
╠╬╬╬╣CHOCOLATE!!<br>
╚╩╩╩╝<br>
<br>
<br>
(> ” ” <)<br>
( =’o'= )<br>
-(,,)-(,,)-<br>
<br>
… @ @ . .<br>
.. ( ‘ o ‘ ) . .<br>
. º(___)º . <br>
       </div>
       <form>
       
  <div class="input-group" style="padding-top: 5px; width: 98%; margin: auto;">
    <input type="text" class="form-control" id="letterInput" placeholder="Letter" disabled>
    <div class="input-group-btn">
      <button class="btn btn-default" id="letterButton" type="submit" disabled>
        <i>Go</i>
      </button>
    </div>
  </div>
</form>  
       </div>
</div>




<!-- 

<div id="gameBoard" hidden>
	<form>
	
	<p> <font color="red" id="errorMsg"> </font></p>
	Enter letter: <input type="text" id="letterInput"><br/>
	<button type="button" id="letterButton">Submit</button>
	</form><br/>
	<p id="mainMsg"></p>
</div>
-->



</body>
</html>
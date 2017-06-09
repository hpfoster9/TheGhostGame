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
        <script src="js/app-ajax.js" type="text/javascript"></script>
    </head>
    <body>
    
    
    
        <div id="hostJoin" >
            <h1 class="title">The Ghost Game</h1>
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
                    <div class="form-group row" >
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="nameInput" placeholder="Player Name">
                        </div>
                    </div>
                    <div class="form-group row">
                        <div class="col-sm-10">
                            <input type="password" class="form-control" id="passwordInput" placeholder="Game Password">
                        </div>
                    </div>
                    <div class="form-group row" id="namePassSubmitContainer">
                        <div class="col-sm-10">
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
            <div id="rulesContainer"> 
            </div>
            <div id="playersContainerLobby">
                
                <div class="table-responsive" id="playersInnerDivLobby">
                    <table class="table" id="playerListTableLobby">
                    </table>
                </div>
            </div>
            <div  id="ads" > 
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
                
                        <button type="submit" class="btn btn-primary" id="readyUp">Ready Up!</button>

            </div>
        </div>
        
        
        
        
        
        
        <div id="gameBoard" hidden >
            <h1 class="title">The Ghost Game</h1>
            <br/>
            <div id="timerContainer"> 
            </div>
            <div id="rulesContainer"> 
            </div>
            <div id="playersContainer">
                <!-- *****PLAYER LIST***** -->
                <div class="table-responsive" id="playersInnerDiv">
                    <table class="table" id="playerListTable">
                    </table>
                </div>
            </div>
            <div  id="ads" > 
            </div>
            <div id="outerMsgContainer">
                <div id="innerMsgContainer">
                    <div id="mainMsg"></div>
                    <div id="updateMsg">
                    </div>
                    <p id="errorMsg"></p>
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
                    ███████████████████].<br>
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
                    <div class="input-group" id="chatMsgContainer">
                        <input type="text" class="form-control" placeholder="Letter" disabled>
                        <div class="input-group-btn">
                            <button class="btn btn-default"  type="submit" disabled>
                            <i>Go</i>
                            </button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </body>
</html>
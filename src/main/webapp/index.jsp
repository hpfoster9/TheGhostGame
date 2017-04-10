<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>The Ghost Game</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
<script src="http://code.jquery.com/jquery-1.10.2.js"
	type="text/javascript"></script>
<script src="js/app-ajax.js" type="text/javascript"></script>
</head>
<body>
<h1>The Ghost Game</h1><br/>
<div id="hostJoin" >
	<form>Game ID: <input type="text" id="idInput"><br/>
	<button type="button" id="join">Join</button>
	<button type="button" id="create">Create</button>
	</form>
	<p id="msg"></p>
</div>
<div id="namePassword" hidden>
	<h1 id="namePassTitle"></h1><br>
	<form>
	Your name: <input type="text" id="nameInput"><br/>
	Game password: <input type="password" id="passwordInput"><br/>
	<button type="button" id="submitNamePassword">Submit</button>
	</form><br/>
	<p id="namePassMsg"></p>
</div>
<div id="gameBoard" hidden>
	<form>
	
	<p> <font color="red" id="errorMsg"> </font></p>
	Enter letter: <input type="text" id="letterInput"><br/>
	<button type="button" id="letterButton">Submit</button>
	</form><br/>
	<p id="mainMsg"></p>
</div>
</body>
</html>
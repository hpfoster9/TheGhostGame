$(document).ready(function() {
	adminPingInterval = setInterval(adminPing, 500 );
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
			////console.log("sent a "+type+"request: "+postBody.key);
		}
		
		
        //Runs at an interval once inside the admin
        function adminPing(){
        	sendMessage("get",{
        		key: "adminPing"
        	}, adminPingCallback);
        }
        
       
        
        //**CALLBACK FUNCTIONS**//
        
        function adminPingCallback(responseText){
        	$('#adminPingTable').html(updateAdmin(responseText.split(",")));
        	
        }
			
		
        //** HELPER FUNCTIONS **//
		
        //Updates the table of players within the lobby
		function updateAdmin(array){
			var tableHTML = "<thead><tr><th>Game ID</th><th>Num Players</th></tr></thead> <tbody>";
			for(var i = 0; i<array.length; i++){
				////console.log(nameReady);
				if(array[i].length > 0){
					var tempArray = array[i].split(" ");
					var id = tempArray[0];
					var num = tempArray[1];
					tableHTML += "<tr><td>"+id+"</td><td>"+num+"</td></tr>";
				}
			}
			tableHTML += "</tbody>";
			return tableHTML;
		}
		
		
        
        
        $('#rulesContainer').click(function(){
        	$("#myModal2").modal({backdrop: "true"});
        });
        $('#rulesContainer2').click(function(){
        	$("#myModal2").modal({backdrop: "true"});
        });
        
        $('.picSound').click(function(){
        	document.getElementById("ghostSong").pause();
        	$("#sound1").attr("hidden","true");
        	$("#sound2").attr("hidden","true");
        	$("#mute1").removeAttr("hidden");
        	$("#mute2").removeAttr("hidden");
        });
        $('.picMute').click(function(){
        	document.getElementById("ghostSong").play();
        	$("#mute1").attr("hidden","true");
        	$("#mute2").attr("hidden","true");
        	$("#sound1").removeAttr("hidden");
        	$("#sound2").removeAttr("hidden");
        });
});
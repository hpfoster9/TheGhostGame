package servlet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Scanner;
import java.math.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class AjaxServlet
 */
@WebServlet("/AjaxServlet")
public class AjaxServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	//Keeps track of game "serial number" for when creating new ones
    public static int gameCounter = 0;
    
    //List of every English word
    public static ArrayList<String> WordList = new ArrayList<String>();
    
    //Container of active games
    public static ArrayList<Game> gamePool = new ArrayList<Game>();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AjaxServlet(){
        super();
    }
    
    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//Sets content type as default
		response.setContentType("text/plain");
		
		switch(request.getParameter("key")){
			case "initialize": //get
			try {
				initialize(request, response);
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				break;	
			case "tryJoinGame": //get
				tryJoinGame(request, response);
				break;
			case "ping":  //get
    			ping(request, response);
    			break;
			case "checkWord": //get
				checkWord(request, response);
				break;
			case "lobbyPing":
				lobbyPing(request, response);
				break;
			case "adminPing":
				adminPing(request, response);
				break;
		}
	}
    
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Sets content type as default
		response.setContentType("text/plain");
		
		
		switch(request.getParameter("key")){
			case "createGame": //post
						try {
							createGame(request, response);
							
						} catch (NoSuchAlgorithmException e) {
							e.printStackTrace();
						}
						break;
			case "namePassword": //post
						namePassword(request, response);
						break;
			case "sendLetter": //post
						sendLetter(request, response);
						break;
			case "challengeRequest": //post
						challengeRequest(request, response);
						break;
			case "challengeResponse": //post
						challengeResponse(request, response);
						break;
			case "lobbyReady": //post
						lobbyReady(request, response);
						break;
			case "chat":
						chat(request, response);
						break;
			case "expired":
						expired(request, response);
						break;
			default:
						response.getWriter().write("");
						break;
		}
	}
	
	////////////////////////
	//** MAIN FUNCTIONS **//
	////////////////////////
	
	

	//g
	public void initialize(HttpServletRequest request, HttpServletResponse response) throws IOException, NoSuchAlgorithmException{
		//initializes WordList if not already created
		if(WordList.size() == 0){
			compileWordList();
			
			runTest();
		}
	}
	
	
	
	
	
	//p
	public void createGame(HttpServletRequest request, HttpServletResponse response) throws IOException, NoSuchAlgorithmException{
		gamePool.add(new Game(gameCounter));
		gameCounter++;
		
		//Sends the gameID to user for reference
		response.getWriter().write(gamePool.get(gamePool.size()-1).getHash());
	}
	
	//g
	public void tryJoinGame(HttpServletRequest request, HttpServletResponse response) throws NumberFormatException, IOException{
		int gameIndex = -1;
		if(request.getParameter("gameHash") != null){
			gameIndex = findGame(request.getParameter("gameHash"));
		}
		else{
			gameIndex = findGame(request.getParameter("gameId"));
		}
		if(gameIndex != -1){
			response.getWriter().write(gamePool.get(gameIndex).getHash());
		}
		else{
			response.getWriter().write("");
		}
	}
	
	//p
	public void namePassword(HttpServletRequest request, HttpServletResponse response) throws IOException{
		int index = findGame(request.getParameter("gameHash"));
		
		Game game = gamePool.get(index);
		String realPassword = game.getPassword();
		String testPassword = request.getParameter("password");
		String name = request.getParameter("name");
		
		try{
			//If there is not an existing password or both passwords match
			if(realPassword.length() < 1){
				int lives = Integer.parseInt(request.getParameter("lives"));
				int seconds = Integer.parseInt(request.getParameter("seconds"));
				//Sets the new password
				game.setPassword(md5(testPassword));
				
				game.setTime(seconds);
				game.setLives(lives);
				//Add the new player to the game and send the new player's ID to the User
				response.getWriter().write(joinGame(game, name, lives));
			}
			else if (realPassword.equals(md5(testPassword))){
				//Add the new player to the game and send the new player's ID to the User
				response.getWriter().write(joinGame(game, name, game.getLives()));
			}
			else{
				//Otherwise notify the User that there was a problem
				response.getWriter().write("");
			}
		}
		catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
	//p
	private void lobbyReady(HttpServletRequest request, HttpServletResponse response) {
		
		Game game = gamePool.get(findGame(request.getParameter("gameHash")));
		Player p = game.getPlayer(game.findPlayer(request.getParameter("playerId")));
		
		p.makeReady();
	}
	
	//g
	public void lobbyPing(HttpServletRequest request, HttpServletResponse response) throws IOException{
		
		String nameReady = "";
		String gameHash = request.getParameter("gameHash");
		Game game = gamePool.get(findGame(gameHash));
		for(Player p: game.getPlayers()){
			nameReady += p.getName() + " " + p.isReady() +",";
		}
		nameReady +=  "|" + game.gameReady();
		
		response.getWriter().write(nameReady);
		
		//update the Users' deathCounter
		playerUpdateDeath(gameHash, request.getParameter("playerId"));
		
		//update the game's deathCounter
		gameUpdateDeath(gameHash, request.getParameter("playerId"));
		
	}
	public void adminPing(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String output = "";
		for(int i = 0; i < gamePool.size(); i++){
			Game g = gamePool.get(i);
			String id = g.getHash();
			int num = g.getNumPlayers();
			output += id + " " + num + ",";
		}
		response.getWriter().write(output);
	}
	
	//g
	public void ping(HttpServletRequest request, HttpServletResponse response) throws IOException{
		//Gets gameID from params and uses it to find the game
		String gameHash = request.getParameter("gameHash");
		Game game = gamePool.get(findGame(gameHash));

		//Sends the user the current word and turn-player's ID
		response.getWriter().write(getPingOutput(game));
		
		String pId = request.getParameter("playerId");
		if(game.findPlayer(pId) != -1){
			//update the Users' deathCounter
			playerUpdateDeath(gameHash, request.getParameter("playerId"));
			
			//update the game's deathCounter
			gameUpdateDeath(gameHash, request.getParameter("playerId"));
		}
			
	}
	
	//p
	public void sendLetter(HttpServletRequest request, HttpServletResponse response) throws IOException{
		if(isLetter(request.getParameter("letter"))){
			//Gets the game and the letter
			Game game = gamePool.get(findGame(request.getParameter("gameHash")));
			String letter = request.getParameter("letter");
			
			//Sends the letter to the game
			game.addLetter(letter);
			
			game.takeTurn();
			
			//Sends User success msg so they know the letter was added
			response.getWriter().write("Sucess");
		}
	}
	
	//g
	public void checkWord(HttpServletRequest request, HttpServletResponse response){
		//Gets the game
		Game game = gamePool.get(findGame(request.getParameter("gameHash")));
		
		//Get current and previous players
		if(game.getWord().length() > 0){
			Player prev = game.getPreviousPlayer();
			
			Player curr = game.getPlayer(game.getTurnIndex());
			
			
			if(game.checkWord()){
				//The word is a word, so decrement previous players lives
				if(prev.decreaseLives()){
					game.setLosingId(prev.getID());
					game.removePlayer(game.findPlayer(prev.getID()));
					
				}
				game.setUpdateMsg(curr.getName()+ " thinks "+game.getWord()+" is a word~"+game.getWord()+" is a word~"+prev.getName()+" lost a life");
			}
			else{
				//The word isn't a word, so decrement current players lives
				if(curr.decreaseLives()){
					game.setLosingId(curr.getID());
					game.removePlayer(game.findPlayer(curr.getID()));
					game.setTurn(game.findPlayer(game.getPreviousPlayer().getID()));
				}
				game.setUpdateMsg(curr.getName()+ " thinks "+game.getWord()+" is a word~"+game.getWord()+" is not a word~"+curr.getName()+" lost a life");
			}
			checkWin(game);
			//Clear the word
			game.clearWord();
			//Change updateIndex
			game.incrementUpdateIndex();
		}
	}
	
	
	//p
	public void challengeRequest(HttpServletRequest request, HttpServletResponse response){
		//Gets the game
		Game game = gamePool.get(findGame(request.getParameter("gameHash")));
		//Get current and previous players
		Player prev = game.getPreviousPlayer();
		//Assigns the challengeID so the clients know who is being challenged
		game.setChallengeID(prev.getID());
	}
	
	//p
	public void challengeResponse(HttpServletRequest request, HttpServletResponse response){
		//Gets the game
		Game game = gamePool.get(findGame(request.getParameter("gameHash")));
		//Get current and previous players
		Player prev = game.getPreviousPlayer();
		Player curr = game.getPlayer(game.getTurnIndex());
		//Gets the word entered by the person being challenged
		String testWord = request.getParameter("word").toLowerCase();
		//Gets the word currently on the board
		String realWord = game.getWord().toLowerCase();
		
		//If the inputed word is a word and it starts with the word on the board
		if(WordList.contains(testWord)){
			if(testWord.startsWith(realWord)){
				if(curr.decreaseLives()){
					game.setLosingId(curr.getID());
					game.removePlayer(game.findPlayer(curr.getID()));
					game.setTurn(game.findPlayer(game.getPreviousPlayer().getID()));
				}
				game.setUpdateMsg(curr.getName()+ " challenges "+prev.getName()+"~"+testWord.toUpperCase()+" is a word~"+curr.getName()+" lost a life");
			}
			else{
				if(prev.decreaseLives()){
					
					game.setLosingId(prev.getID());
					game.removePlayer(game.findPlayer(prev.getID()));
					
				}
				game.setUpdateMsg(curr.getName()+ " challenges "+prev.getName()+"~Although "+testWord.toUpperCase()+" is a word,~It does not begin with "+game.getWord()+"~"+prev.getName()+" lost a life");
			}
		}
		//The word isn't a word, so decrement previous player's lives
		else{
			if(prev.decreaseLives()){
				
				game.setLosingId(prev.getID());
				game.removePlayer(game.findPlayer(prev.getID()));
				
			}
			
			game.setUpdateMsg(curr.getName()+ " challenges "+prev.getName()+"~"+testWord.toUpperCase()+" is not a word~"+prev.getName()+" lost a life");
		}
		checkWin(game);
		//Clear the word
		game.clearWord();
		//Resets the challengeID so the clients know there is not currently a challenge
		game.resetChallengeID();
		//Change updateIndex
		game.incrementUpdateIndex();
	}
	
	
	//p
	public void chat(HttpServletRequest request, HttpServletResponse response){
		
		//Gets the game
		Game game = gamePool.get(findGame(request.getParameter("gameHash")));
		
		Player player = game.getPlayer(game.findPlayer(request.getParameter("playerId")));
		
		game.setUpdateMsg("&~"+player.getName()+"~"+request.getParameter("msg"));
		
		game.incrementUpdateIndex();
	}

	//p
	public void expired(HttpServletRequest request, HttpServletResponse response){
		Game game = gamePool.get(findGame(request.getParameter("gameHash")));
		Player player = game.getPlayer(game.getTurnIndex());
		
		
		game.clearWord();
		game.setUpdateMsg(player.getName()+" took too long to play~"+player.getName()+" lost a life");
		game.incrementUpdateIndex();
		game.takeTurn();
		if(player.decreaseLives()){
			game.setLosingId(player.getID());
			game.removePlayer(game.findPlayer(game.getPreviousPlayer().getID()));
			
		}
		checkWin(game);
	}
	
	//////////////////////////
	//** HELPER FUNCTIONS **//
	//////////////////////////
	public void checkWin(Game game){
		if(game.getPlayers().size() == 1){
			game.setWinningId(game.getPlayer(0).getID());
		}
	}
	
	
	//Creates the world list
	public static void compileWordList() throws IOException{
		URL url = new URL("https://raw.githubusercontent.com/dwyl/english-words/master/words.txt");
		Scanner fileScanner = new Scanner(url.openStream());
        while (fileScanner.hasNextLine()){
            WordList.add(fileScanner.nextLine().toLowerCase());
            String prev = WordList.get(WordList.size()-1);
            //This if statement filters out words that are fewer than 3 letters, or have apostrophes in it
            if(prev.length() <= 3 || prev.contains("'") || prev.contains(".") || prev.contains("-")|| prev.matches("[0-9]")){
                WordList.remove(WordList.size()-1);
            }
        }
    }
	
	//Adds new player to the game and returns that player's id
	private String joinGame(Game game, String name, int lives) throws NoSuchAlgorithmException {
		game.addPlayer(name, lives);
		return game.getLastPlayer().getID();
	}
	
	//Returns the: game word, turn id, list of players and lives, update message, update index, id of the person being challenged (if applicable)  
	//I have used special characters (   , | , * ) to separate the different bits of information for the client the parse later
	private String getPingOutput(Game game){
		return 
				game.getTime() + " " +
				game.getWord() + " " + 
				game.getTurnID()+" "+
				game.getTurnIndex()+
				game.getPlayersString()+"|"+
				game.getUpdateMsg()+"*"+
				game.getUpdateIndex()+"*"+
				game.getChallengeID()+"*"+
				game.getLosingId()+"*"+
				game.getWinningId();
	}
	
	//Searches gamePool for game with matching id, returns index of the game
	private int findGame(String hash){
		for(int i = 0; i<gamePool.size(); i++){
			if(gamePool.get(i).getHash().equals(hash))
				return i;
		}
		return -1;
	}
	private int findGame(int id){
		for(int i = 0; i<gamePool.size(); i++){
			if(gamePool.get(i).getID() == id)
				return i;
		}
		return -1;
	}
	
	//Creates fingerprint for string, this was a standard md5 hashing method I found on stack overflow
	public static String md5(String input) throws NoSuchAlgorithmException{
		MessageDigest m=MessageDigest.getInstance("MD5");
	    m.update(input.getBytes(),0,input.length());
	    String md5 = new BigInteger(1,m.digest()).toString(16);
	    return md5;
	}
	
	//Checks if the string (should only be one letter long) is a letter
	private boolean isLetter(String letter){
		char c = letter.charAt(0);
		if(letter.length() == 1){
		return Character.isLetter(c);
		}
		return false;
	}
	
	//Checks if the string contains only numbers
	public static boolean isInteger(String str) {
		    try {
		        Integer.parseInt(str);
		        return true;
		    }
		    catch( Exception e ) {
		        return false;
		    }
		
	}
	
	//Updates the deathCounters within the game
	public void playerUpdateDeath(String gID, String pID){
		Game game = gamePool.get(findGame(gID));
		
		//Finds the index for the current player
		int pIndex = game.findPlayer(pID);
		
		
		
		ArrayList<Player> players = game.getPlayers();
		
		//If the player is the last in the ArrayList, add the death counter to the first Player
		if(pIndex == players.size()-1 && players.size() > 1){
			if(players.get(0).addDead()){
				
				game.removePlayer(0);
				pIndex -- ;
			}
			
		}
		//As long as the player isn't the last, add a deathCounter on the next player
		else if (players.size() > 1){
			if(players.get(pIndex + 1).addDead()){
				
				game.removePlayer(pIndex+1);
				
			}
			
		}
		//Refresh the current player's deathCounter
		players.get(pIndex).resetDeath();
		
	}
	
	
	
	
	//Updates the deathCounter within the game
		public void gameUpdateDeath(String gID, String pID){
			Game game = gamePool.get(findGame(gID));
			
			//Finds the index for the current player
			int pIndex = game.findPlayer(pID);
			
			
			
			
			
			ArrayList<Player> players = game.getPlayers();
			
			//finds the index of the game within the game pool
			int gIndex = findGame(gID);
			
			//only run this with the first player in each game
			if(pIndex == 0){
				
				//If the player is the last in the ArrayList, add the death counter to the first Player
				if(gIndex == gamePool.size()-1 && gamePool.size() > 1){
					Game nextGame = gamePool.get(0);
					if(nextGame.addDead() && nextGame.getPlayers().size() == 1 && nextGame.gameReady()){
						
						gamePool.remove(0);
						gIndex--;
					}
				}
				//As long as the player isn't the last, add a deathCounter on the next player
				else if (gamePool.size() > 1 ){
					Game nextGame = gamePool.get(gIndex+1);
					if(nextGame.addDead() && nextGame.getPlayers().size() == 1 && nextGame.gameReady()){
						
						
						gamePool.remove(gIndex+1);
					}
				}
			}
			//Refresh the current player's deathCounter
			game.resetDeath();
			
		}
	
}

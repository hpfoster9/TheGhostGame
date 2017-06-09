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
		System.out.println("Got a get: "+request.getParameter("key"));
		//Sets content type as default
		response.setContentType("text/plain");
		
		switch(request.getParameter("key")){
			case "initialize": //get
				initialize(request, response);
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
		}
	}
    
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Sets content type as default
		response.setContentType("text/plain");
		System.out.println("Got a post: "+request.getParameter("key"));
		
		switch(request.getParameter("key")){
			case "createGame": //post
						try {
							createGame(request, response);
							System.out.println("Successfully made shit");
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
			default:
						response.getWriter().write("");
						break;
		}
	}
	
	////////////////////////
	//** MAIN FUNCTIONS **//
	////////////////////////
	
	

	//g
	public void initialize(HttpServletRequest request, HttpServletResponse response) throws IOException{
		//initializes WordList if not already created
		if(WordList.size() == 0){
			compileWordList();
			System.out.println("Made the wordlist");
			
		}
	}
	
	//Tests duplicates of hashes
	public void runTest() throws NoSuchAlgorithmException{
		String[] pArray = new String[500]; 
		String[] pDup = new String[4];
		String pOutput = "";
		
		String[] gArray = new String[500]; 
		String[] gDup = new String[4];
		String gOutput = "";
		
		for(int i = 0; i<500; i++){
			pArray[i] = md5("p"+i).substring(0,6);
			gArray[i] = md5("g"+i).substring(0, 6);
		}
		
		System.out.println("updated it");
		System.out.println("finished making the test arrays");
		//loop through pArray
		for(int i = 0; i<pArray.length-1; i++){
			pDup[0] = Integer.toString(i);
			pDup[1] = pArray[i];
			for(int j = i+1; j<pArray.length; j++){
				pDup[2] = Integer.toString(j);
				pDup[3] = pArray[j];
				
				if(pDup[1].equals(pDup[3])){
					j = pArray.length;
					i = pArray.length;
					System.out.println("PlayerHash duplicate found at points: ("+pDup[0]+" , "+pDup[1]+") and ("+pDup[2]+" , "+pDup[3]+")");
				}
			}
		}
		
		
		
		//loop through pArray
		for(int i = 0; i<gArray.length-1; i++){
			gDup[0] = Integer.toString(i);
			gDup[1] = gArray[i];
			for(int j = i+1; j<gArray.length; j++){
				gDup[2] = Integer.toString(j);
				gDup[3] = gArray[j];
				
				if(gDup[1].equals(gDup[3])){
					j = gArray.length;
					i = gArray.length;
					System.out.println("GameHash duplicate found at points: ("+gDup[0]+" , "+gDup[1]+") and ("+gDup[2]+" , "+gDup[3]+")");
					
				}
			}
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
	}/////****Make sure the mehtod returns the game hash
	
	//p
	public void namePassword(HttpServletRequest request, HttpServletResponse response) throws IOException{
		int index = findGame(request.getParameter("gameHash"));
		System.out.println("index "+index);
		Game game = gamePool.get(index);
		String realPassword = game.getPassword();
		String testPassword = request.getParameter("password");
		String name = request.getParameter("name");
		
		try{
			//If there is not an existing password or both passwords match
			if(realPassword.length() < 1){
				//Sets the new password
				game.setPassword(md5(testPassword));
				//Add the new player to the game and send the new player's ID to the User
				response.getWriter().write(joinGame(game, testPassword, name));
			}
			else if (realPassword.equals(md5(testPassword))){
				//Add the new player to the game and send the new player's ID to the User
				response.getWriter().write(joinGame(game, testPassword, name));
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
		System.out.println("in lobbyPing function");
		String nameReady = "";
		Game game = gamePool.get(findGame(request.getParameter("gameHash")));
		for(Player p: game.getPlayers()){
			nameReady += p.getName() + " " + p.isReady() +",";
		}
		nameReady +=  "|" + game.gameReady();
		System.out.println("nameReady: "+nameReady);
		response.getWriter().write(nameReady);
	}
	//g
	public void ping(HttpServletRequest request, HttpServletResponse response) throws IOException{
		//Gets gameID from params and uses it to find the game
		String gameHash = request.getParameter("gameHash");
		Game game = gamePool.get(findGame(gameHash));

		//Sends the user the current word and turn-player's ID
		response.getWriter().write(getPingOutput(game));
		
		//update the Users' deathCounter
		updateDeath(gameHash, request.getParameter("playerID"));
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
		System.out.println("checkWord: "+game.getWord());
		//Get current and previous players
		if(game.getWord().length() > 0){
			Player prev = game.getPreviousPlayer();
			
			Player curr = game.getPlayer(game.getTurnIndex());
			System.out.println("prev: "+prev.getName());
			System.out.println("curr: "+curr.getName());
			if(game.checkWord()){
				//The word is a word, so decrement previous players lives
				prev.decreaseLives();
				game.setUpdateMsg(curr.getName()+ " thinks "+game.getWord()+" is a word~"+game.getWord()+" is a word~"+prev.getName()+" lost a life");
			}
			else{
				//The word isn't a word, so decrement current players lives
				curr.decreaseLives();
				game.setUpdateMsg(curr.getName()+ " thinks "+game.getWord()+" is a word~"+game.getWord()+" is not a word~"+curr.getName()+" lost a life");
			}
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
		if(WordList.contains(testWord) && testWord.startsWith(realWord)){
			curr.decreaseLives();
			game.setUpdateMsg(curr.getName()+ " challenges "+prev.getName()+"~"+testWord.toUpperCase()+" is a word~"+curr.getName()+" lost a life");
		}
		//The word isn't a word, so decrement previous player's lives
		else{
			prev.decreaseLives();
			game.setUpdateMsg(curr.getName()+ " challenges "+prev.getName()+"~"+testWord.toUpperCase()+" is not a word~"+prev.getName()+" lost a life");
		}
		//Clear the word
		game.clearWord();
		//Resets the challengeID so the clients know there is not currently a challenge
		game.resetChallengeID();
		//Change updateIndex
		game.incrementUpdateIndex();
	}
	

	
	
	//////////////////////////
	//** HELPER FUNCTIONS **//
	//////////////////////////
	
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
	private String joinGame(Game game, String password, String name) throws NoSuchAlgorithmException {
		game.addPlayer(name);
		return game.getLastPlayer().getID();
	}
	
	//Returns the: game word, turn id, list of players and lives, update message, update index, id of the person being challenged (if applicable)  
	//I have used special characters (   , | , * ) to separate the different bits of information for the client the parse later
	private String getPingOutput(Game game){
		return 
				game.getWord() + " " + 
				game.getTurnID()+" "+
				game.getTurnIndex()+
				game.getPlayersString()+"|"+
				game.getUpdateMsg()+"*"+
				game.getUpdateIndex()+"*"+
				game.getChallengeID();
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
	public void updateDeath(String gID, String pID){
		Game game = gamePool.get(findGame(gID));
		
		//Finds the index for the current player
		int pIndex = game.findPlayer(pID);
		ArrayList<Player> players = game.getPlayers();
		
		//If the player is the last in the ArrayList, add the death counter to the first Player
		if(pIndex == players.size()-1 && players.size() > 1){
			players.get(0).addDead();
		}
		//As long as the player isn't the last, add a deathCounter on the next player
		else if (players.size() > 1){
			players.get(pIndex + 1).addDead();
		}
		//Refresh the current player's deathCounter
		players.get(pIndex).resetDeath();
	}
	
}

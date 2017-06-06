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


/*
READ FIRST

This is the main "server" file which acts to take messages from the clients and responds appropriately

Because the server can only respond and not initiate the messages, the client always sends its requests with three things:
	The game id
	A Player id
	A keyword 
	
The gameid is used the find the specific instance of the game and the playerid is used to find the specific player linked to the client
The keyword is used to tell the server what information is needed to be sent

Once in the game, each player "pings" every .5 seconds to update the client with any information

Other information and keywords are triggered client side on button presses 


Information about how the play the game that this project is based on can be found here: https://en.wikipedia.org/wiki/Ghost_(game)


Also the word list that I used is here: https://github.com/dwyl/english-words/raw/master/words.txt
And is "free and unencumbered software released into the public domain"

*/













/**
 * Servlet implementation class AjaxServlet
 */
@WebServlet("/AjaxServlet")
public class AjaxServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	//Keeps track of game "serial number" for when creating new ones
    private static int gameCounter = 0;
    
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
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Sets content type as default
		response.setContentType("text/plain");
		
		//Runs when users try to create new game
		if(request.getParameter("initialize") != null){
			//initializes WordList if not already created
			if(WordList.size() == 0){
				compileWordList();
			}
		}
		//Runs when users try to create new game
		if(request.getParameter("createGame") != null){
				//Creates game
				createGame();
				//Sends the gameID to user for reference
				response.getWriter().write(Integer.toString(gameCounter-1));
		}
		
		//Runs when users try to join a game with a game id
		if(request.getParameter("joinGame") != null && isInteger(request.getParameter("gameID"))){
			//Sends the index, checks if game exists
			response.getWriter().write(Integer.toString(findGame(Integer.parseInt(request.getParameter("gameID")))));
		}
		
		//Runs when the user submits name and password
		if(request.getParameter("namePassword") != null){
			int index = findGame(Integer.parseInt(request.getParameter("gameID")));
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
		
		//Runs every 500ms to check if for the word and the turn
		if(request.getParameter("ping") != null){
			//Gets gameID from params and uses it to find the game
			int gameID = Integer.valueOf(request.getParameter("gameID"));
			Game game = gamePool.get(findGame(gameID));

			//Sends the user the current word and turn-player's ID
			response.getWriter().write(getPingOutput(game));
			
			//update the Users' deathCounter
			updateDeath(gameID, request.getParameter("playerID"));
		}
		
		//Runs when user submits letter
		if(request.getParameter("sendLetter") != null && isLetter(request.getParameter("letter"))){
			//Gets the game and the letter
			Game game = gamePool.get(Integer.valueOf(request.getParameter("gameID")));
			String letter = request.getParameter("letter");
			
			
			//Sends the letter to the game
			game.addLetter(letter);
			
			
			game.takeTurn();
			
			//Sends User success msg so they know the letter was added
			response.getWriter().write("Sucess");
		}
		
		
		//Runs when user clicks "That's a word"
		if(request.getParameter("checkWord") != null){
			//Gets the game
			Game game = gamePool.get(Integer.valueOf(request.getParameter("gameID")));
			//Get current and previous players
			Player prev = game.getPreviousPlayer();
			Player curr = game.getPlayer(game.getTurnIndex());
			
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
		
		
		//Runs when the user clicks challenge
		if(request.getParameter("challengeRequest") != null){
			//Gets the game
			Game game = gamePool.get(Integer.valueOf(request.getParameter("gameID")));
			//Get current and previous players
			Player prev = game.getPreviousPlayer();
			//Assigns the challengeID so the clients know who is being challenged
			game.setChallengeID(prev.getID());
		}
		
		//Runs when the user submits the modal after being challenged
		if(request.getParameter("challengeResponse") != null){
			//Gets the game
			Game game = gamePool.get(Integer.valueOf(request.getParameter("gameID")));
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
		
		
		//if none of the if statements work, notify user there was a problem with the input
		else{
			response.getWriter().write("");
		}
	}
	
	////////////////////////
	//** MAIN FUNCTIONS **//
	////////////////////////
	
	/*
	Current question I have
	Right now I have almost everything being run out of the DoPost method
	Would it make sense to just have each of those if statements (i.e. if("sendLetter" != null){}) run an external function down here
	
	For instance, I did that with the create game if statement. When a client send a post request with the parameter "createGame" 
	it runs the if statement within the doPost and then calls an external method called "createGame()" down here to actually create the game.
	I don't know to what extent I should compartmentalize my code like this
	 
	*/
	
	//Creates the world list
	public static void compileWordList() throws IOException{
		URL url = new URL("https://github.com/dwyl/english-words/raw/master/words.txt");
		Scanner fileScanner = new Scanner(url.openStream());
        while (fileScanner.hasNextLine()){
            WordList.add(fileScanner.nextLine());
            String prev = WordList.get(WordList.size()-1);
            //This if statement filters out words that are fewer than 3 letters, or have apostrophes in it
            if(prev.length() <= 3 || prev.contains("'") || prev.matches(".*\\d+.*")){
                WordList.remove(WordList.size()-1);
            }
        }
    }
	
	//Add new instance of the game and increment gameCounter
	private void createGame(){
		gamePool.add(new Game(gameCounter));
		gameCounter++;
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


	
	
	//////////////////////////
	//** HELPER FUNCTIONS **//
	//////////////////////////
	
	//Searches gamePool for game with matching id, returns index of the game
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
	public void updateDeath(int gID, String pID){
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

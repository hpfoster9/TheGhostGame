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
    private static int gameCounter = 0;
    
    //WorldList
    public static ArrayList<String> WordList = new ArrayList<String>();
    
    //Container of active games
    public static ArrayList<Game> gamePool = new ArrayList<Game>();
    /**
     * @throws FileNotFoundException 
     * @see HttpServlet#HttpServlet()
     */
    public AjaxServlet() throws FileNotFoundException {
        super();
        
        // TODO Auto-generated constructor stub
    }
    

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Sets content type as default
		response.setContentType("text/plain");
		
		//Runs when users try to create new game
		if(request.getParameter("createGame") != null){
				//initializes WordList if not already created
				if(WordList.size() == 0){
					compileWordList();
				}
				//Creates game
				createGame();
				//Sends the gameID to user for reference
				response.getWriter().write(Integer.toString(gameCounter-1));
		}
		
		//Runs when users try to join a game with an id
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
			
			//If the prior word was completed, clear it before adding to it
			if(game.finishedWord() == true){
				System.out.println("Word was already complete");
				game.clearWord();
			}
			//Sends the letter to the game
			game.addLetter(letter);
			
			
			//check to see if the new word is finished
			if(game.checkWord()){
				System.out.println("new word is complete");
				game.addLetter("*");
				game.loseLife();
			}
			game.takeTurn();
			
			//Sends User success msg so they know the letter was added
			System.out.println("Sucess");
			response.getWriter().write("Sucess");
		}
		
		//if none of the if statements work, notify user there was a problem with the input
		else{
			response.getWriter().write("");
		}
	}
	
	////////////////////////
	//** MAIN FUNCTIONS **//
	////////////////////////
	
	//Creates the world list
	public static void compileWordList() throws IOException{
		URL url = new URL("https://github.com/dwyl/english-words/raw/master/words.txt");
		Scanner fileScanner = new Scanner(url.openStream());
		int count = 0;
        while (fileScanner.hasNextLine()){
            WordList.add(fileScanner.nextLine());
            String prev = WordList.get(WordList.size()-1);
            if(prev.length() <= 3 || prev.contains("'") || prev.matches(".*\\d+.*")){
                WordList.remove(WordList.size()-1);
                count++;
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
	
	//Returns the current word and turn for a game 
	private String getPingOutput(Game game){
		System.out.println("Ran ping output: "+game.getTurnIndex()+" *** "+game.getPlayersString());
		return game.getWord() + " " + game.getTurnID()+" "+game.getTurnIndex()+game.getPlayersString();
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
	
	//Creates fingerprint for string
	public static String md5(String input) throws NoSuchAlgorithmException{
		MessageDigest m=MessageDigest.getInstance("MD5");
	    m.update(input.getBytes(),0,input.length());
	    String md5 = new BigInteger(1,m.digest()).toString(16);
	    return md5;
	}
	
	//Checks if the string contains sort sort of letter
	private boolean isLetter(String letter){
		char c = letter.charAt(0);
		if(letter.length() == 1){
		return Character.isLetter(c);
		}
		System.out.println("Failed isletter: "+letter);
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

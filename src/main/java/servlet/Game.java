package servlet;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class Game {
	//Unique number to reference each game with
	private int gameID;
	
	//Hash of the gameID;
	private String gameHash;
	//A "hashed" password to be set upon game creation
	private String password = "";
	
	//List of players within the game
	private ArrayList<Player> players;
	
	//The word that will used within the game
	private String WORD;
	
	//Keeps track of # players in game, used to creating hashed playerID
	private int COUNTER;
	
	//Keeps track of turn through index in the "players" list
	private int TURN;
	
	private int time;
	private int lives;
	
	//Most recent Update Message
	private String updateMsg;
	
	//Keeps track of how many inactive counters the player has
	private int deadCounter;
	
	
	private String losingId = "false";
	private String winningId = "false";
	
	/*
	     This is an index of the most recent update, it is incremented every time there is a 
	     new update so the client knows when it is out of sync with the server
	*/
	private int updateIndex;
	
	//ID of player that is being challenged
	private String challengeID;
	
	public Game(int id) throws NoSuchAlgorithmException{
		this.gameID = id;
		this.gameHash = AjaxServlet.md5("g"+Integer.toString(id)).substring(0, 6);
		this.players = new ArrayList<Player>();
		this.WORD = "";
		this.COUNTER = 0;
		this.TURN = 0;
		this.updateMsg = "";
		this.updateIndex = 0;
		this.challengeID = "false";
		this.deadCounter = 0;
	}
	
	
	
	/*
	    PASSWORD METHODS
	*/
	public void setPassword(String pass){
		password = pass;
	}
	public String getPassword(){
		return password;
	}
	
	/*
    SETTINGS METHODS
	*/
	public void setTime(int seconds){
		time = seconds;
	}
	public int getTime(){
		return time;
	}
	
	public void setLives(int L){
		lives = L;
	}
	public int getLives(){
		return lives;
	}
	
	public void setTurn(int index){
		TURN = index;
	}
	/*
  	    PLAYER METHODS
	*/
	//Finds the index of the player through playerID
	public int findPlayer(String id){
		for(int i = 0; i<players.size(); i++){
			if(players.get(i).getID().equals(id))
				return i;
		}
		
		return -1;
	}
	//Add a new player by name and then advance the counter by 1
	public void addPlayer(String name, int lives) throws NoSuchAlgorithmException{
		players.add(new Player(name, AjaxServlet.md5("p"+Integer.toString(COUNTER)).substring(0, 6), lives));
		COUNTER++;
	}
	//Returns all players within a game
	public ArrayList<Player> getPlayers(){
		return players;
	}
	//Returns specific player based on index
	public Player getPlayer(int index){
		return players.get(index);
	}
	public Player getLastPlayer(){
		return players.get(players.size()-1);
	}
	public Player getPreviousPlayer(){
		if(TURN == 0){
			return getLastPlayer();
		}
		else{
			return players.get(TURN-1);
		}
	}
	//Generates a string of players and their lives to output to the clients screen
	public String getPlayersString(){
		String output = "";
		for(Player p: players){
			output += " " + p.getName() + " " + p.getLives();
		}
		return output;
	}
	//Checks if all players are ready
	public boolean gameReady(){
		for(Player p: players){
			if(!p.isReady() || players.size() <= 1)
				return false;
		}
		return true;
	}
	//Removes the selected player
	public void removePlayer(int index){
		players.remove(index);
	}
	public int getNumPlayers(){
		return players.size();
	}
	
	
	/*
	    WORD METHODS
	*/
	public void addLetter(String letter){
		WORD += letter;
	}
	public String getWord(){
		return WORD;
	}
	public void clearWord(){
		WORD = "";
	}
	//Returns whether or not the WORD is within the word list
	public boolean checkWord() {
		String lowerWord = WORD.toLowerCase();
		System.out.println("Checkword test: "+lowerWord);
		System.out.println(AjaxServlet.WordList.size());
        if(AjaxServlet.WordList.contains(lowerWord)){
        	return true;
        }
        else{
        	return false;
        }
	}
	
	public void setLosingId(String str){
		losingId = str;
	}
	public String getLosingId(){
		return losingId;
	}
	public void setWinningId(String str){
		winningId = str;
	}
	public String getWinningId(){
		return winningId;
	}
	
	
	
	/*
	    TURN METHODS 
	*/
	//Advance the turn by one, if at the game move turn to the front
	public void takeTurn(){
		if(TURN == players.size()-1 )
		TURN = 0;
		else{
			TURN++;
		}
	}
	//Moves the turn index back by 1
	public void takeBackTurn(){
		if(TURN == 0){
			TURN = players.size()-1;
		}
		else{
			TURN--;
		}
	}
	//Returns the playerID of the person whose turn it is
	public String getTurnID(){
		return players.get(TURN).getID();
	}
	public int getTurnIndex(){
		return TURN;
	}
	
	
	
	/*
	    UPDATE METHODS 
	*/
	public String getUpdateMsg(){
		return updateMsg;
	}
	public int getUpdateIndex(){
		return updateIndex;
	}
	public void setUpdateMsg(String text){
		updateMsg = text;
	}
	public void incrementUpdateIndex(){
		updateIndex++;
	}
	
	
	
	/*
	    CHALLENGE METHODS
	*/
	public void setChallengeID(String id){
		challengeID = id;
	}
	public void resetChallengeID(){
		challengeID = "false";
	}
	public String getChallengeID(){
		return challengeID;
	}
	
	
	
	/*
	    GENERAL METHODS
	*/
	public void loseLife(){
		players.get(TURN).decreaseLives();
	}
	public int getID(){
		return gameID;
	}
	public String getHash(){
		return gameHash;
	}
	
	
	/*
    GAME INACTIVITY METHODS
	*/
	//Increase the death counter by one, if the user has been unresponsive remove them from the game
	public boolean addDead(){
		deadCounter++;
		System.out.println("IN ADD DEATH");
		System.out.println("In game "+gameID+" deathcounter went from "+(deadCounter-1)+" to "+deadCounter);
		if(deadCounter > 10){
			return true;
		}
		return false;
	}
	//If the player shows signs of being active, it will reset the death counter
	public void resetDeath(){
		deadCounter = 0;
	}
	public int getDeath(){
		return deadCounter;
	}
}

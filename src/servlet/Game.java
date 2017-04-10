package servlet;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class Game {
	//Unique number to reference each game with
	private int gameID;
	
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
	
	public Game(int id){
		this.gameID = id;
		this.players = new ArrayList<Player>();
		this.WORD = "";
		this.COUNTER = 0;
		this.TURN = 0;
	}
	public void setPassword(String pass){
		password = pass;
	}
	public String getPassword(){
		return password;
	}
	//Add a new player by name and then advance the counter by 1
	public void addPlayer(String name) throws NoSuchAlgorithmException{
		players.add(new Player(name, AjaxServlet.md5(Integer.toString(COUNTER))));
		COUNTER++;
	}
	//Finds the index of the player through playerID
	public int findPlayer(String id){
		for(int i = 0; i<players.size(); i++){
			if(players.get(i).getID().equals(id))
				return i;
			else{
				System.out.println("actualID: "+players.get(i).getID());
				System.out.println("test: "+id);
			}
		}
		
		return -1;
	}
	public int getID(){
		return gameID;
	}
	public ArrayList<Player> getPlayers(){
		return players;
	}
	public Player getPlayer(int index){
		return players.get(index);
	}
	public Player getLastPlayer(){
		return players.get(players.size()-1);
	}
	public String getWord(){
		return WORD;
	}
	public void addLetter(String l){
		WORD += l;
	}
	private static String md5(String input) throws NoSuchAlgorithmException{
		MessageDigest m=MessageDigest.getInstance("MD5");
	    m.update(input.getBytes(),0,input.length());
	    String md5 = new BigInteger(1,m.digest()).toString(16);
	    System.out.println("MD5: "+md5);
	    return md5;
	}
	
	//Advance the turn by one, if at the game move turn to the front
	public void takeTurn(){
		if(TURN == players.size()-1 )
		TURN = 0;
		else{
			TURN++;
		}
		
	}
	public String getTurnID(){
		return players.get(TURN).getID();
	}
}

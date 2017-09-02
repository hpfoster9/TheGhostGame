package servlet;

public class Player {
	
	private int lives;
	
	//A hashed string based on what number the user is in the game, used to locate the user
	private String playerID;
	
	private String name;
	
	//Keeps track of how many inactive counters the player has
	private int deadCounter;
	
	//Keeps track of if the player is ready
	private boolean ready;
	
	public Player(String n, String hash, int lives){
		this.lives = lives;
		this.playerID = hash;
		this.name = n;
		this.deadCounter = 0;
		this.ready = false;
	}
	
	/*
	    STANDARD METHODS
	*/
	public String getName(){
		return name;
	}
	public int getLives(){
		return lives;
	}
	public String getID(){
		return playerID;
	}
	public boolean decreaseLives() {
		lives --;
		if(lives <= 0){
			return true;
		}
		return false;
	}
	public boolean isReady(){
		return ready;
	}
	public void makeReady(){
		ready = true;
	}

	
	/*
	    PLAYER INACTIVITY METHODS
	*/
	//Increase the death counter by one, if the user has been unresponsive remove them from the game
	public boolean addDead(){
		deadCounter++;
		if(deadCounter > 6){
			System.out.println(name+" IS DEAD");
			return true;
		}
		return false;
	}
	//If the player shows signs of being active, it will reset the death counter
	public void resetDeath(){
		deadCounter = 0;
	}
	
}

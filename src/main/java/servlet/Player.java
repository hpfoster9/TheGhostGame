package servlet;

public class Player {
	private int lives;
	private String playerID;
	private String name;
	private int deadCounter;
	
	public Player(String n, String hash){
		this.lives = 10;
		this.playerID = hash;
		this.name = n;
		this.deadCounter = 0;
	}
	public String getName(){
		return name;
	}
	public int getLives(){
		return lives;
	}
	public String getID(){
		return playerID;
	}
	//Increase the death counter by one, if the user has been unresponsive remove them from the game
	public void addDead(){
		deadCounter++;
		if(deadCounter > 3){
			System.out.println(name+" is DEAD");
			//return true;
		}
		//return false;
	}
	public void resetDeath(){
		deadCounter = 0;
	}
	public void decreaseLives() {
		lives --;
		
	}
}

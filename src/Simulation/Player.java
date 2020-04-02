package Simulation;

public class Player {
	String player_name = "";
	String player_id = "";
	double make1 = 0;
	double miss1 = 0;
	double make2 = 0;
	double miss2 = 0;
	double make3 = 0;
	double miss3 = 0;
	double fouled_rate = 0;
	
	public Player(String id){
		player_id = id;
	}
	
	public int Shoot(Team defense){
		
		// determine odds of shooting a 2 pointer
		double num2 = this.miss2 + this.make2;
		double num3 = this.miss3 + this.make3;
		double percent2 = num2/(num2 + num3);
		
		// find a random number
		double randnum = Math.random();
		
		// determine what type of shot 
		if(percent2 > randnum) {
			
			// determine odds of making a 2 pointer
			double make = this.make2/(this.make2 + this.miss2);
			randnum = Math.random();
			
			if(randnum < make){
				System.out.println(this.player_name + " made a 2");
				return 2;
			
			} else {
				System.out.println(this.player_name + " missed a 2");
				return 0;
			}
			
		} else {
			
			// determine odds of making a 3 pointer
			double make = this.make3/(this.make3 + this.miss3);
			randnum = Math.random();
		
			if(randnum < make){
				System.out.println(this.player_name + " made a 3");
				return 3;
				
			} else {
				System.out.println(this.player_name + " missed a 3");
				return 0;
				
			}
		}
	}
	
	public void PrettyPrint(){
		System.out.println(player_name + " " + make1 + " " + make2 + " " + make3 + " " + miss1 + " " + miss2 + " " + miss3);
	}
	
}


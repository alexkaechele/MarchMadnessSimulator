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
	
	/**
	 * shot attempted and points resulting from a player shooting a shot
	 * @param defense: team that is defending the shot
	 * @return int array [made points, attempted points]
	 */
	public int[] Shoot(Team defense){
		
		// determine odds of shooting a 2 pointer
		double num2 = this.miss2 + this.make2;
		double num3 = this.miss3 + this.make3;
		double percent2 = num2/(num2 + num3);
		
		// find a random number
		double randnum = Math.random();
		
		// determine what type of shot 
		if(percent2 > randnum){
			
			// determine odds of making a 2 pointer
			double make = this.make2/(this.make2 + this.miss2) + defense.defense_strength*2;
			randnum = Math.random();
			
			if(randnum < make){
				System.out.println(this.player_name + " made a 2");
				return new int[] {2, 2};
			
			} else {
				System.out.println(this.player_name + " missed a 2");
				return new int[] {0, 2};
			}
			
		} else {
			
			// determine odds of making a 3 pointer
			double make = this.make3/(this.make3 + this.miss3)+ defense.defense_strength*3;
			randnum = Math.random();
		
			if(randnum < make){
				System.out.println(this.player_name + " made a 3");
				return new int[] {3, 3};
				
			} else {
				System.out.println(this.player_name + " missed a 3");
				return new int[] {0, 3};
				
			}
		}
	}
	
	/**
	 * Prints out a player and some season stats
	 */
	public void PrettyPrint(){
		System.out.println(player_name + " " + make1 + " " + make2 + " " + make3 + " " + miss1 + " " + miss2 + " " + miss3);
	}
	
	/**
	 * Shoots a number of free throws determined by ft_type and adds the points to game
	 * @param game: game where the player is shooting free throws
	 * @param ft_type: String that determines what sort of shots to take
	 *          "0" : 0 free throws needed
	 * 			"1" : 1 free throw needed
	 * 			"2" : 2 free throws needed
	 * 			"3" : 3 free throws needed
	 * 			"1:1" : a one and one 
	 * @param offense: team the free throw shooter is on
	 * @param defense: team the free throw shooter is facing
	 */
	public void ShootFT(Game game, String ft_type, Team offense, Team defense){
		
		if(ft_type.equals("1")) {
			
			// make one free throw
			DetMakeFT(game, offense, defense, true);
			
		} else if(ft_type.equals("2")){
			
			// make 2 free throws
			DetMakeFT(game, offense, defense, false);
			DetMakeFT(game, offense, defense, true);
			
		} else if(ft_type.equals("3")){
			
			// make 3 free throws
			DetMakeFT(game, offense, defense, false);
			DetMakeFT(game, offense, defense, false);
			DetMakeFT(game, offense, defense, true);

			
		} else if(ft_type.equals("1:1")){
			
			// make first free throw
			boolean make_first = DetMakeFT(game, offense, defense, true);


			// if made the first free throw shoot the second
			if(make_first){
				DetMakeFT(game, offense, defense, true);
			}
			
		}
	}
	
	/**
	 * Makes a single free throw shot
	 * @param game: game in which the player is shooting the free throw
	 * @param offense: team the ft shooter plays for
	 * @param defense: team the ft shooter is facing
	 * @param rebound: determine if players should rebound the missed shot
	 * @return boolean on whether the shot was made
	 */
	public boolean DetMakeFT(Game game, Team offense, Team defense, boolean rebound){
		
		// odds the player can make the free throw
		double ft_odds = this.make1/(this.make1 + this.miss1);
		
		// random number
		double rand_num = Math.random();
		
		// if the shot was made
		if(ft_odds > rand_num){
			
			// report made free throw
			System.out.println(this.player_name + " made a free throw");
			
			// add points to score
			game.AddScore(offense, 1);
			
			// set flip possession to true
			game.flip_poss = true;
			
			return true;
		
		// Otherwise
		} else {
			
			// report made free throw
			System.out.println(this.player_name + " missed a free throw");
			
			// rebound to determine next possession
			if(rebound) {
				game.flip_poss = game.DetermineRebound(offense, defense);
			}
			
			return false;
		}
		
	}
	
}


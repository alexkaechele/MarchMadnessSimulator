package Simulation;

import java.util.Random;

public class Game {
	Team team1 = null;
	Team team2 = null;
	int team1_score = 0;
	int team2_score = 0;
	int team1_fouls = 0;
	int team2_fouls = 0;

	// Game constructor 
	public Game(){}

	/**
	 * Plays a game of basketball
	 * @param team_name_1: String of the first team that is playing the game
	 * @param team_name_2: String of the second team that is playing the game
	 * @param teams: array of ncaa basketball teams
	 */
	public void PlayGame(String team_name_1, String team_name_2, Team[] teams){

		// locate teams
		this.FindTeams(team_name_1, team_name_2, teams);

		// determine number of possessions
		int possessions = DeterminePossNum();
		
		// Determine who goes first
		Team[] pairing = DetermineFirst(team1, team2);
		
		// simulate half game
		// loop though the possessions
		for(int i =1; i < possessions/2; i++){
			
			// play the possession
			pairing = PlayPossession(pairing);
			
			// wait to play the next possession
			Delays.Possession(1);
		}

		// show box score
		StatCalculator.BoxScoreDisplay(this);
		
		// wait to continue
		Delays.WaitForIt("second half");

		// sim other half of game
		for(int i = possessions/2; i <= possessions; i++){
			
			// play the possession
			pairing = PlayPossession(pairing);
			
			// wait to play the next possession
			Delays.Possession(1);
		}
		
		// display results
		StatCalculator.DisplayFinalScore(this);

	}

	/**
	 * Plays a possession of basketball
	 * @param pairing: Array of 2 teams (first is offense, second is defense)
	 * @return the team who plays next possession
	 */
	public Team[] PlayPossession(Team[] pairing) {

		// extract offense and defense
		Team offense = pairing[0];
		Team defense = pairing[1];
		
		boolean flip_poss = true;
		
		// if a turnover
		if(DetermineTurnover(offense, defense)) {
						
			// announce the turnover
			System.out.println(offense.team_name + " turned the ball over");
			
		// if not a turnover
		} else {
			
			// determine the shooter for the offense
			Player shooter = DetermineShooter(offense);

			// determine if the shooter made the shot
			int makes = shooter.Shoot(defense);
			
			this.AddScore(offense, makes);

			// TODO: determine if defensive foul
			boolean foul = DetermineFoul(offense, defense, makes);
			
			// TODO: if not makes or not defensive foul, determine who rebounds
			if(makes == 0 && !foul) {
				
				flip_poss = DetermineRebound(offense, defense);
			}
		}
		
		// Determine the next pairing
		if(flip_poss){
			// flip the possession
			pairing[0] = defense;
			pairing[1] = offense;
			
		} else {
			// don't flip the possession
			pairing[0] = offense;
			pairing[1] = defense;
			
		}
		
		// Return the pairing to be used for next possession
		return pairing;
	}
	
	/**
	 * Determine the number of possessions in the game
	 * @return int of number of possessions
	 */
	public int DeterminePossNum(){
		return (int) (team1.poss + team2.poss)/2;
	}

	/**
	 * Determine if the offense turned the ball over
	 * @param offense: the offensive team that could potentially turn the ball over
	 * @return boolean true if turned the ball over, false otherwise
	 */
	public static boolean DetermineTurnover(Team offense, Team defense){
		
		// percent of times the team turns if over
		double turnover_odds = (offense.turnover_propensity/offense.poss + defense.turnover_causing/defense.poss)/2;

		// random number between 0 and 1
		double random_num = Math.random();

		// if the turnover odds is less than the random number
		if(turnover_odds > random_num){
			
			// it caused the turnover
			return true;
			
		} else {
			// there was no turnover
			return false;
		}
	}
	
	/**
	 * Determine the shooter for the offense team
	 * @param offense: team that is shooting the ball
	 * @return the player that is going to shoot
	 */
	public static Player DetermineShooter(Team offense) {		
		
		int total_shots = 0;
		
		// loop through the players on the shooting team
		for(int i=0; i < offense.players.length; i++){
			
			// if there is a player there
			if(offense.players[i] != null) {
				
				// find the total shots a team makes
				total_shots += offense.players[i].make2;
				total_shots += offense.players[i].miss2;
				total_shots += offense.players[i].make3;
				total_shots += offense.players[i].miss3;
				
			}
		}
		
		// choose a random number
		Random rand = new Random();
		int shot_num = rand.nextInt(total_shots);
		
		
		// find the shooter
		for(int j=0; j < offense.players.length; j++) {
			
			
			shot_num -= offense.players[j].make2;
			shot_num -= offense.players[j].miss2;
			shot_num -= offense.players[j].miss3;
			shot_num -= offense.players[j].make3;

			if(shot_num < 0) {
				return offense.players[j];
			}
			
		}
		
		return null;
	}
	
	public static boolean DetermineRebound(Team offense, Team defense) {
		return false;
	}
	
	public static boolean DetermineFoul(Team offense, Team defense, int makes){
		
		double foul_odds = (offense.fouled_rate/offense.poss + defense.foul_rate/defense.poss)/2;
		
		double rand_num = Math.random();
		
		if(foul_odds > rand_num){
			
			// add to the total fouls
			
			// determine if it was on the shot
			
			// determine if need free throws
			
			// determine possession of ball
			
			
			
			System.out.println(defense.team_name + " committed a foul");
			return true;
		} else {
			return false;
		}
	}
	
	
	/**
	 * Method to determine which team is going first
	 * @param team1: one team in the game
	 * @param team2: the other team in the game
	 * @return array of the pairings, 0th team is offense, 1st team is defense
	 */
	public Team[] DetermineFirst(Team team1, Team team2){
		
		// setting up pairing array
		Team[] pairing = new Team[2];
		
		// get a random number between 1 and 0
		int random = (int) Math.round(Math.random());
		
		// setting the pairing
		pairing[random] = team1;
		pairing[1-random] = team2;		
		
		// announcing the jump ball winner
		System.out.println(pairing[0].team_name + " won the jump ball");
		
		return pairing;
	}

	
	public void AddScore(Team offense, int makes){
		
		// if team1 is the offense that scored
		if(this.team1.team_name.equals(offense.team_name)){
			
			// add points the the score
			this.team1_score += makes;
		
		} else {
			
			// otherwise add points to team2 score
			this.team2_score += makes;
		}
		
		System.out.println(team1.team_name + ": " + this.team1_score + ", " + team2.team_name + ": " + this.team2_score);
		
	}
	
	/**
	 * Uses strings to find teams (assigns the teams to the game)
	 * @param team_name_1: string of first team
	 * @param team_name_2: string of second team
	 * @param teams: array of teams in the year
	 */
	public void FindTeams(String team_name_1, String team_name_2, Team[] teams) {
		
		// loop though the teams
		for(int i= 0; i < teams.length; i++) {

			// if the teams are not null
			if(teams[i] != null) {

				// determine if matches team 1 name
				if(teams[i].team_name.equals(team_name_1)) {

					// assign team to game
					this.team1 = teams[i];

					// determine if matches team 2 name
				} else if(teams[i].team_name.equals(team_name_2)) {

					// assign team to game
					this.team2 = teams[i];
				}

			} else {

				break;
			}
		}
	}

}

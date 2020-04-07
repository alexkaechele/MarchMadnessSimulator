package Simulation;

import java.util.Random;

public class Game {
	Team team1 = null;
	Team team2 = null;
	int team1_score = 0;
	int team2_score = 0;
	int team1_fouls = 0;
	int team2_fouls = 0;
	boolean flip_poss = true;

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
			
		}

		// show box score
		StatCalculator.BoxScoreDisplay(this);
		
		// reset foul counts
		this.team1_fouls=0;
		this.team2_fouls=0;
		
		// wait to continue
		Delays.WaitForIt("second half");

		// sim other half of game
		for(int i = possessions/2; i <= possessions; i++){
			
			// play the possession
			pairing = PlayPossession(pairing);
			
		}
		
		// display results
		StatCalculator.DisplayFinalScore(this);

		// TODO: determine if need overtime
		
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
		
		this.flip_poss = true;
		
		// if a turnover
		if(DetermineTurnover(offense, defense)) {
						
			// announce the turnover
			System.out.println(offense.team_name + " turned the ball over");
			
		// if not a turnover
		} else {
			
			// determine the shooter for the offense
			Player shooter = DetermineShooter(offense);

			// determine if the shooter made the shot
			int[] makes = shooter.Shoot(defense);
			
			// add the score to the scoreboard and print the current score
			this.AddScore(offense, makes[0]);

			// Determine if defensive foul
			boolean foul = DetermineFoul(offense, defense, makes, shooter);
			
			// the shot isn't made and a defensive foul didn't occur, determine who rebounds
			if(makes[0] == 0 && !foul) {
				
				this.flip_poss = DetermineRebound(offense, defense);
			}
		}
		
		// Determine the next pairing
		if(this.flip_poss){
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
	public boolean DetermineTurnover(Team offense, Team defense){
		
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
	
	/**
	 * Used to determine which team gets the rebound
	 * @param offense: team on offense
	 * @param defense: team on defense
	 * @return boolean on whether the possession switches
	 */
	public boolean DetermineRebound(Team offense, Team defense){
		
		double odds_def_rebound = defense.def_rebound_rate/(defense.def_rebound_rate + offense.off_rebound_rate);
		
		double rand_num = Math.random();
		
		if(odds_def_rebound > rand_num){
			System.out.println("Defensive rebound made by " + defense.team_name);
			return true;
		} else {
			System.out.println("Offense rebound made by " + offense.team_name);
			return false;
		}
	}
	
	/**
	 * Determine if there is a defensive foul on this possession
	 * @param offense
	 * @param defense
	 * @param makes
	 * @return
	 */
	public boolean DetermineFoul(Team offense, Team defense, int makes[], Player shooter){
		
		double foul_odds = (offense.fouled_rate/offense.poss + defense.foul_rate/defense.poss)/2;
		
		double rand_num = Math.random();
		
		if(foul_odds > rand_num){
			
			// add to the total fouls and player fouls
			this.AddFoul(defense);
			
			// determine if it was on the shot
			boolean on_shot = DetermineOnShot(shooter);
			
			// determine if need free throws
			String ft_type = DetFT(defense, makes, on_shot);
			
			// shoot free throws if applicable 
			shooter.ShootFT(this, ft_type, offense, defense);			
			
			// report/indicate that there was a foul
			System.out.println(defense.team_name + " committed a foul");
			return true;
			
		} else {
			
			// indicate that there was no foul
			return false;
		}
	}
	
	/**
	 * Determines if/what type of free throws that need to occur
	 * @param defense: team that committed the foul
	 * @param makes: points that 
	 * @param on_shot: whether the foul was on the shot
	 * @return: String of foul type needed. Possible values include:
	 *          "0" : 0 free throws needed
	 * 			"1" : 1 free throw needed
	 * 			"2" : 2 free throws needed
	 * 			"3" : 3 free throws needed
	 * 			"1:1" : a one and one 
	 */
	public String DetFT(Team defense, int[] makes, boolean on_shot){
		
		int fouls =0;
		// determine num fouls committed by defense
		if(this.team1.team_name.equals(defense.team_name)){
			fouls = this.team1_fouls;
		} else {
			fouls = this.team2_fouls;
		}
		
		// if the shot was made and foul was on the shot
		if(makes[0] > 0 && on_shot){
			return "1";
		} else if(makes[0]==0 && !on_shot && fouls >= 10){
			return "2";
		} else if(makes[0]==0 && !on_shot && fouls >=7){
			return "1:1";
		} else if(makes[0]==0 && makes[1]==2 && on_shot){
			return "2";
		} else if(makes[0]==0 && makes[1]==2 && on_shot){
			return "3";
		} else {
			return "0";
		}
	}
	
	
	/**
	 * Determine if the foul that was committed was on the shot
	 * @param shooter: player that was fouled
	 * @return boolean: true if fouled on the shot, false if fouled off the shot
	 */
	public boolean DetermineOnShot(Player shooter){
		
		// percent of time player usually gets fouled on shot
		double percent_fouled = shooter.fouled_rate/(shooter.make2+ shooter.miss2 + shooter.make3 + shooter.miss3);
		
		// finding a random number
		double rand_num = Math.random();
		
		// if a foul occurred
		if(percent_fouled > rand_num){
			return true;
		} else {
			return false;
		}		
	}
	
	/**
	 * Adds the committed foul to the Game instance
	 * @param defense: team that committed the foul
	 */
	public void AddFoul(Team defense){
		
		// if the defense is team 1
		if(this.team1.team_name.equals(defense.team_name)) {
			
			// add the foul to their total
			this.team1_fouls++;
		
		// otherwise add to team 2
		} else {
			
			this.team2_fouls++;
		}
		
		// TODO: add foul to player that committed the foul
		// TODO: if player foul is equal to 5, remove from team array
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

	/**
	 * Adds the points of a shot to the total score
	 * @param offense: team that attempeted the shot
	 * @param makes: points made after the shot
	 */
	public void AddScore(Team offense, int makes){
		
		// if team1 is the offense that scored
		if(this.team1.team_name.equals(offense.team_name)){
			
			// add points the the score
			this.team1_score += makes;
		
		} else {
			
			// otherwise add points to team2 score
			this.team2_score += makes;
		}
		
		// print out current score
		System.out.println(team1.team_name + ": " + this.team1_score + ", " + team2.team_name + ": " + this.team2_score + "\n");
		
		// Pause after displaying score
		Delays.Possession(5);

		
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

package Simulation;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Alex Kaechele
 * This class is used to read in csv basketball data
 */

public class DataReader{

	/**
	 *  Constructor for reading in documents
	 */
	public DataReader(){
	}

	/**
	 * General method for reading in a csv file.
	 * @param fileName: Filepath string of data location (intended for team name and team seed)
	 * @param teams: array of teams to add data to
	 * @param type: String containing type of information adding to teamList. Types confirmed are:
	 *		        Team: Brings in fouls, rebounds, points, games played, turnovers of the team
	 *				Player: Brings in make/miss percentage for all shots
	 *              TeamName: Adds team name from team id
	 *              PlayerName: Add player name from player/team id
	 * @return Array of teams
	 */
	public static Team[] ReadData(String fileName, String type, Team[] teams){
		// initializing some settings
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";

		// adding team functions
		if(teams == null){
			teams = new Team[1000];
		}

		try {

			// importing filereader
			br = new BufferedReader(new FileReader(fileName));

			// keep reading in lines if the next line exists
			while ((line = br.readLine()) != null) {

				// use comma as separator
				String[] newInfo = line.split(cvsSplitBy);

				try {

					// add the information to the teams
					if(type.equals("Player")){
						
						teams = AddPlayerInfo(teams, newInfo);
					
					} else if(type.equals("Team")){
						
						teams = AddTeamInfo(teams, newInfo);
					
					} else if(type.equals("PlayerName")){
						
						teams = AddPlayerNames(teams, newInfo);
						
					} else if(type.equals("TeamName")){
						
						teams = AddTeamNames(teams, newInfo);
					}


				} catch(NumberFormatException nfe) {}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}

		// return team array
		return teams;
	}


	/**
	 * Add a player or team if not in the teams array, then data to array
	 * @param teams: array of teams to add data to
	 * @param info: line of data being read in
	 * @return an array of teams
	 */
	public static Team[] AddPlayerInfo(Team[] teams, String[] info){

		// Add team if not in array
		teams = AddTeam(teams, info);

		// Add player if not in array
		teams = AddPlayer(teams, info);

		// list of player attributes
		ArrayList<String> player_char = new ArrayList<String>(Arrays.asList("made2", "made3", "miss2", "miss3", "made1", "miss1", "fouled"));

		// add the player information
		if(player_char.contains(info[12])) {

			teams = AddPlayerShot(teams, info);

		}

		return teams;
	}

	/**
	 * Helper function to add a team to an array
	 * @param teams: array of ncaa teams
	 * @param info: information being read in from event data
	 * @return the array of teams
	 */
	public static Team[] AddTeam(Team[] teams, String[] info){

		// Loop though the teams in the array
		for(int i=0; i<teams.length; i++){

			// skipping header row
			if(info[10].equals("EventTeamID") || info[10].equals("0")){
				break;
			}

			// if the space is empty
			if(teams[i] != null) {

				// and the new team is in that space already
				if(teams[i].team_id.equals(info[10])){

					// exit the loop
					break;
				}
			}

			// if there is a null entry
			if(teams[i] == null){		

				// add the team
				teams[i] = new Team(info[10]);

				// and then exit the loop
				break;
			}
		}

		// return the array of teams
		return teams;
	}


	public static Team[] AddTeamInfo(Team[] teams, String[] info){

		// if the year is correct
		if(info[0].equals("2020")) {

			// loop though the teams
			for(int i=0; i < teams.length; i++){

				// check if teams are null
				if(teams[i] != null) {

					// check if team matches winning team
					if(teams[i].team_id.equals(info[2])){

						// add one to game count
						teams[i].games_played++;

						// add offensive rebound rate
						teams[i].off_rebound_rate += Integer.parseInt(info[14]);

						// add defensive rebound rate
						teams[i].def_rebound_rate += Integer.parseInt(info[15]);
						
						// add foul rate
						teams[i].foul_rate += Integer.parseInt(info[20]);

						// add fouled rate
						teams[i].fouled_rate += Integer.parseInt(info[33]);

						// add tournover causing
						teams[i].turnover_causing += Integer.parseInt(info[30]);

						// add turnover propensity
						teams[i].turnover_propensity += Integer.parseInt(info[17]);

						// add defensive strength
						teams[i].typical_scoring += Integer.parseInt(info[3]);
						teams[i].typical_allowed.add(Integer.parseInt(info[5]));
						
						// add possessions
						teams[i].poss += Integer.parseInt(info[8]) + 
								         Integer.parseInt(info[9]) + 
								         Integer.parseInt(info[21]) +
								         Integer.parseInt(info[22]);
						
						// add opponant
						// loop though teams played
						for(int k=0; k < teams[i].teams_played.length; k++){
							
							// until there is an empty slot to put new opponant
							if(teams[i].teams_played[k] == null) {
								
								// loop though teams to find opponant team
								for(int l=0; l < teams.length;l++) {
									
									// if there is a non empty team
									if(teams[l] != null) {
										
										// if the team id's match
										if(teams[l].team_id.equals(info[4])){
											
											// add the team to the opponant list
											teams[i].teams_played[k] = teams[l];
										}
									}
								}
								break;
							}
						}
						

						// check if team matches losing team
					} else if(teams[i].team_id.equals(info[4])){

						// add one to game count
						teams[i].games_played++;

						// add offensive rebound rate
						teams[i].off_rebound_rate += Integer.parseInt(info[27]);

						// add defensive rebound rate
						teams[i].def_rebound_rate += Integer.parseInt(info[28]);
						
						// add foul rate
						teams[i].foul_rate += Integer.parseInt(info[33]);

						// add fouled rate
						teams[i].fouled_rate += Integer.parseInt(info[20]);

						// add tournover causing
						teams[i].turnover_causing += Integer.parseInt(info[17]);

						// add turnover propensity
						teams[i].turnover_propensity += Integer.parseInt(info[30]);

						// add defensive strength
						teams[i].typical_scoring += Integer.parseInt(info[5]);
						teams[i].typical_allowed.add(Integer.parseInt(info[3]));
						
						// add possessions
						teams[i].poss += Integer.parseInt(info[8]) + 
								         Integer.parseInt(info[9]) + 
								         Integer.parseInt(info[21]) +
								         Integer.parseInt(info[22]);
						
						// add opponant
						// loop though teams played
						for(int k=0; k < teams[i].teams_played.length; k++){
							
							// until there is an empty slot to put new opponant
							if(teams[i].teams_played[k] == null) {
								
								// loop though teams to find opponant team
								for(int l=0; l < teams.length;l++) {
									
									// if there is a non empty team
									if(teams[l] != null) {
										
										// if the team id's match
										if(teams[l].team_id.equals(info[2])){
											
											// add the team to the opponant list
											teams[i].teams_played[k] = teams[l];
										}
									}
								}
								break;
							}
						}
						
					}
					
				} else {
					
					// if it is null get out of the loop
					break;
					
				}
			}
		}

		return teams;
	}

	/**
	 * Helper function to add a player to a team array
	 * @param teams: array of ncaa teams
	 * @param info: information being read in from event data
	 * @return the array of teams
	 */
	public static Team[] AddPlayer(Team[] teams, String[] info){

		if(!info[11].equals("0")) {

			// loop though teams in an array
			for(int i=0; i < teams.length; i++){

				// if the space has a team
				if(teams[i] != null){

					// if the teams match
					if(teams[i].team_id.equals(info[10])){

						// loop though the team's players
						for(int j =0; j < teams[i].players.length; j++) {

							// if the value is null
							if(teams[i].players[j] == null) {

								// add the player
								teams[i].players[j] = new Player(info[11]);

								// exit the loop
								break;
							}

							// if the players match
							if(teams[i].players[j].player_id.equals(info[11])) {

								// exit the loop
								break; 
							}
						}
					}
				}
			}
		}

		return teams;
	}


	public static Team[] AddPlayerShot(Team[] teams, String[] info){

		// loop though teams
		for(int i=0; i < teams.length; i++) {

			// if team is not null
			if(teams[i] != null){

				// if team id matches
				if(teams[i].team_id.equals(info[10])) {

					// loop though players
					for(int j=0;j < teams[i].players.length; j++) {

						// if players are not null
						if(teams[i].players[j] != null){

							// if players match
							if(teams[i].players[j].player_id.equals(info[11])){

								if(info[12].equals("fouled")){
									teams[i].players[j].fouled_rate++;
									break;

								} else if(info[12].equals("made2")){
									teams[i].players[j].make2++;
									break;

								} else if(info[12].equals("made3")){
									teams[i].players[j].make3++;
									break;

								} else if(info[12].equals("miss2")){
									teams[i].players[j].miss2++;
									break;

								} else if(info[12].equals("miss3")){
									teams[i].players[j].miss3++;
									break;

								} else if(info[12].equals("made1")){
									teams[i].players[j].make1++;
									break;

								} else if(info[12].equals("miss1")){
									teams[i].players[j].miss1++;
									break;

								}
							}
						}
					}
				}
			}
		}

		return teams;
	}

	public static Team[] AddTeamNames(Team[] teams, String[] info){
		
		// loop through the teams
		for(int i=0; i < teams.length; i++) {
			
			// if the team is not empty
			if(teams[i] != null) {
				
				// if the team id's match
				if(teams[i].team_id.equals(info[0])) {
					
					// add the team name
					teams[i].team_name = info[1];
				}
				
			// if team is empty
			} else {
				
				// exit loop
				break;
				
			}
			
		}
		
		return teams;
	}
	
	public static Team[] AddPlayerNames(Team[] teams, String[] info){

		// loop through the teams
		for(int i=0; i < teams.length; i++) {
			
			// if the team is not empty
			if(teams[i] != null) {
				
				// if the team id's match
				if(teams[i].team_id.equals(info[3])) {
					
					// loop though the players
					for(int j =0; j < teams[i].players.length; j++) {
						
						// check if player is null
						if(teams[i].players[j] != null) {
							
						
							// check if player ids match
							if(teams[i].players[j].player_id.equals(info[0])) {
						
								// add player name
								teams[i].players[j].player_name = info[2] + " " + info[1];
							}
							
						} else {
							
							// exit loop
							break;
							
						}
					}
				}
				
			// if team is empty
			} else {
				
				// exit loop
				break;
				
			}
			
		}
		
		return teams;
	
	}
	
}




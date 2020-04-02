package Simulation;

import java.util.ArrayList;

public class Team {
	String team_name = "";
	String team_id = "";
	
	Player[] players = new Player[20];
	Team[] teams_played = new Team[40];
	int games_played = 0;
	double poss =0;
	
	double defense_strength = 0; // modifies opponant shot making
	double typical_scoring = 0;
	ArrayList<Integer> typical_allowed = new ArrayList<Integer>();
	
	double turnover_propensity = 0;
	double turnover_causing = 0;
	double foul_rate = 0;
	double fouled_rate = 0;
	double off_rebound_rate = 0;
	double def_rebound_rate = 0;

	
	public Team(String id){
		team_id = id;
	}
	
	public Player DetermineShooter(){
		return null;
	}
	
	public int CountPlayers(){
		int count =0;
		for(int i =0; i < players.length; i++) {
			if(players[i] != null) {
				count++;
			} else {
				break;
			}
		}
		return count;
	}
	
	public void PrettyPrint(){
		
		String opponants = "";
		
		for(Team t : teams_played){
     	   if(t != null) {
     		   opponants = "\n\t" + t.team_name + " " + opponants;
     	   } else {
     		   break;
     	   }
        }
		
		System.out.println("\nTeam Name: " + team_name +  
				           "\nNum of Players: " + CountPlayers() + 
				           "\nNum of Games Played: " + games_played + 
				           "\nDefensive Strength: " + defense_strength + 
				           "\nTurnover Caused: " + turnover_causing + 
				           "\nTurnover Average: " + turnover_propensity + 
				           "\nFoul Rate: " + foul_rate + 
				           "\nFouled Rate: " + fouled_rate + 
				           "\nOff Rebound Rate: " + off_rebound_rate + 
				           "\nDef Rebound Rate: " + def_rebound_rate +
				           "\nOpponants: " + opponants +
				           "\n");
	}

}

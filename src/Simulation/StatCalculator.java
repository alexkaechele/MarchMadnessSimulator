package Simulation;

public class StatCalculator {

	public static void BoxScoreDisplay(Game game){
		
	}
	
	public static void DisplayFinalScore(Game game){
		
	}
	
	public static void DescribeTeam(String selected_team, Team[] teams){		

		// Print out team
		for(int i =0; i < teams.length; i++){
			if(teams[i] != null) {
				if(teams[i].team_name.equals(selected_team)){
					teams[i].PrettyPrint();
					break;
				}
			}
		}

		// Print out players
		for(int i =0; i < teams.length; i++){
			if(teams[i] != null) {
				if(teams[i].team_name.equals(selected_team)){
					for(int j =0; j < teams[i].players.length; j++) {
						if(teams[i].players[j] != null) {
							teams[i].players[j].PrettyPrint();
						}
					}
				}
			} else {
				break;
			}
		}
	}
	
}

package Simulation;

public class DetermineStrength {

	/**
	 * Add 
	 * @param teams
	 * @return
	 */
	public static Team[] SOS(Team[] teams){
		
		// normalize values
		teams = NormalizeValues(teams);
		
		// get defensive strength
		teams = GetDefStrength(teams);
		
		return teams;
	}
		
	public static Team[] NormalizeValues(Team[] teams){
		
		// loop though the teams
		for(int i=0; i < teams.length; i++) {
			
			// if the spot is not empty
			if(teams[i] != null){
				
				// normalize the values
				teams[i].poss /= teams[i].games_played;
				
				teams[i].typical_scoring /= teams[i].games_played;
				
				teams[i].turnover_propensity /= teams[i].games_played;

				teams[i].turnover_causing /= teams[i].games_played;

				teams[i].foul_rate /= teams[i].games_played;

				teams[i].fouled_rate /= teams[i].games_played;

				teams[i].off_rebound_rate /= teams[i].games_played;

				teams[i].def_rebound_rate /= teams[i].games_played;
				
				
			// otherwise
			} else {
				
				// exit the loop
				break;
			}
			
		}
		
		return teams;
	}
	
	public static Team[] GetDefStrength(Team[] teams){
		
		// loop though teams
		for(int i=0; i < teams.length; i++){
			
			// if the teams spot is not empty
			if(teams[i] != null){
				
				// loop though the teams played
				for(int j=0; j < teams[i].teams_played.length; j++) {
					
					// if there is an opponant to calculate
					if(teams[i].teams_played[j] != null) {
						
						// add difference between opponant's typically allowed and team's typical scoring
						teams[i].defense_strength += teams[i].typical_allowed.get(j) - teams[i].teams_played[j].typical_scoring;
						
					// if there are no more opponants
					} else {
						
						// normalize defensive strength
						teams[i].defense_strength /= teams[i].games_played;
						
						// exit the loop
						break;
					}
				}
				
			// if at the end of the teams list
			} else {
				
				// exit the loop
				break;
			}
		}
		
		return teams;
	}


}

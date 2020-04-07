package Simulation;

public class GameSimulation {

	// Game simulation function
	public static void SimulateGame(String team1, String team2){

		System.out.println("Setting up game");

		// Load in player data
		Team[] teams = DataReader.ReadData("C:\\Users\\Alex Kaechele\\eclipse-workspace\\MarchMadnessSimulator\\MEvents2020.csv", "Player", null);

		// Add team data
		teams = DataReader.ReadData("C:\\Users\\Alex Kaechele\\eclipse-workspace\\MarchMadnessSimulator\\MRegularSeasonDetailedResults.csv", "Team", teams);

		// Add team names
		teams = DataReader.ReadData("C:\\Users\\Alex Kaechele\\eclipse-workspace\\MarchMadnessSimulator\\MTeams.csv", "TeamName", teams);

		// Add player names
		teams = DataReader.ReadData("C:\\Users\\Alex Kaechele\\eclipse-workspace\\MarchMadnessSimulator\\MPlayers.csv", "PlayerName", teams);

		// Add strength of schedule
		teams = DetermineStrength.SOS(teams);
		
		
		for(int i=0; i < teams.length;i++) {
			if(teams[i]!=null) {
				System.out.println(teams[i].team_name);
			}
		}
		
		// Ask to continue
		System.out.println("Setting up game is complete");
		Delays.WaitForIt("game");
		
		// Play the game
		Game game = new Game();
		game.PlayGame(team1, team2, teams);

	}


	// Specify teams that are playing here
	public static void main(String[] args) {
		
		SimulateGame("Kansas", "Kansas St");
		
	}


	


}

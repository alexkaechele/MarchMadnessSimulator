package Simulation;

public class GameSimulation {

	// Game simulation function
	public static void SimulateGame(String team1, String team2){

		// read in data

		// determine team stats

		// determine number of possessions

		// loop though half of possessions

		// show halftime box score

		// ask to continue to next half

		// loop though rest of possessions


		// print final score
		System.out.println(team1 + " beat " + team2);

	}


	// Play a game
	public static void main(String[] args) {
		
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
		
		// Ask to continue
		System.out.println("Setting up game is complete");
		Delays.WaitForIt("game");
		
		// Play the game
		Game game = new Game();
		game.PlayGame("Kansas", "Duke", teams);
		
		//StatCalculator.DescribeTeam("Kansas", teams);
		
		
	}


	


}

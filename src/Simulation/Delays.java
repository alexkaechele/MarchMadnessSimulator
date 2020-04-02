package Simulation;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Delays {
	
	public static void WaitForIt(String message){
		Scanner scan = new Scanner(System.in);
		System.out.print("Press any key to play " + message);
	    scan.nextLine();
		
	}
	
	public static void Possession(int delay){
		try {
			TimeUnit.SECONDS.sleep(delay);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

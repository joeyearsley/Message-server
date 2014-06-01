import java.util.Scanner;
import java.lang.Integer;
import java.io.*;  
import java.net.*;  
public class MultiAgentTest extends Thread {
   
    
    public static void main(String[] argv) {
	Thread x;
	String line1;
	int y;
	try {
	    System.out.println("How many threads?  I recommend 1 to start off with, then slowly increase");
	    System.out.println("Run with > 1 threads to test multi-threading, outputs list of topics at end.");
	    Scanner s = new Scanner(System.in);
	    y = s.nextInt();
	    //change i the 2 for more agents
	    for (int i = 0; i < y; i++) {
		x = new Thread(new Agent());
		x.start();
    
	    }
	    
	    
	} catch (Exception e) {
	    e.printStackTrace();
	}
	
	
	    
    }
}

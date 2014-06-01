import java.io.*;  
import java.net.*;  

public class Agent implements Runnable 
{  
    public static void main(String [] args)   
    {  }
    public void run(){ 
	/**
	   Simple test cases adding 3 topics to test start middle and end, as well as the retrieval and posting of objects,
	   Includes error checking at the bottom, whilst some errors only need checking once as I've used functional decomposition,
	   hence if they work once they are working.
	   Further comments can be found in the print statements, as this is to represent output and checking nicely.
	   More tests may be added in the general format below. To implement a client, use the general format below but include
	   your own outputs etc.. to make it user friendly. 
	**/
	try
	    {
		System.out.println("Follows generally same path as maude tests, with exceptions for the error cases.");
		System.out.println("Put 3 topics in first");
		Socket client = new Socket("localhost", 12111);
		BufferedReader in = new BufferedReader
		    (new InputStreamReader(client.getInputStream()));
		PrintWriter out = new PrintWriter
		    (new OutputStreamWriter(client.getOutputStream()));
                  
		out.println("3");
		out.println("Wallpaper");
		out.println("Oskar Wild");
		out.println("1Hello, Message Board!\n1Either that wallpaper goes\n1or I do.\n.\n");
		out.flush();
		System.out.println("Should output a 3: ");
		System.out.println(in.readLine());
		String line1;
		while ((line1 = in.readLine()) != null) {
		    System.out.println(line1);
		}
		client.close();
	
		//TOPIC 2
		client = new Socket("localhost", 12111);
		in = new BufferedReader
		    (new InputStreamReader(client.getInputStream()));
		out = new PrintWriter
		    (new OutputStreamWriter(client.getOutputStream()));
                  
		out.println("3");
		out.println("Wall");
		out.println("N  Wild");
		out.println("1Hello, Message Board!\n1Love the wallpaper\n.\n");
		out.flush();
		System.out.println("Should output a 3: ");
		System.out.println(in.readLine());
		while ((line1 = in.readLine()) != null) {
		    System.out.println(line1);
		}
		client.close();
	
		//TOPIC 3
		client = new Socket("localhost", 12111);
		in = new BufferedReader
		    (new InputStreamReader(client.getInputStream()));
		out = new PrintWriter
		    (new OutputStreamWriter(client.getOutputStream()));
                  
		out.println("3");
		out.println("Greeting");
		out.println("John");
		out.println("1Hello, everyone\n1how r u?\n.\n");
		out.flush();
		System.out.println("Should output a 3: ");
		System.out.println(in.readLine());
		while ((line1 = in.readLine()) != null) {
		    System.out.println(line1);
		}
		client.close();
	
		//GET TOPIC 0
		System.out.println("Now get all the topics individually:");
		client = new Socket("localhost", 12111);
		in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		out = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
		out.println("1");	
		out.println("0");
		out.flush();
		System.out.println("Should output first topic input: ");
		System.out.println(in.readLine());
		while ((line1 = in.readLine()) != null) {
		    System.out.println(line1);
		}
		client.close();

		//GET TOPIC 1
		client = new Socket("localhost", 12111);
		in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		out = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
		out.println("1");
		out.println("1");
		out.flush();
		System.out.println("Should output second topic input: ");
		System.out.println(in.readLine());
		while ((line1 = in.readLine()) != null) {
		    System.out.println(line1);
		}
		client.close();
	
		//GET TOPIC 2
		client = new Socket("localhost", 12111);
		in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		out = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
		out.println("1");
		out.println("2");
		out.flush();
		System.out.println("Should output a third topic input: ");
		System.out.println(in.readLine());
		while ((line1 = in.readLine()) != null) {
		    System.out.println(line1);
		}
		client.close();
  
		//PRINT OUT TOPIC LIST
		System.out.println("Now print out topic list as a string");
		client = new Socket("localhost", 12111);
		in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		out = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
		out.println("0");
		out.flush();
		System.out.println("Should output list of topics, Id then title on a newline: ");
		System.out.println(in.readLine());
		while ((line1 = in.readLine()) != null) {
		    System.out.println(line1);
		}
		client.close();
  
		//ADD A NEW MESSAGE TO TOPIC 0
		System.out.println("Add a new message to first topic in list.");
		client = new Socket("localhost", 12111);
		in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		out = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
		out.println("2");
		out.println("0\nBegbie\n1I thought the art of conversation was dead\n1\n1\n1and I was right!\n.\n");
		out.flush();
		System.out.println("Should output a 2: ");
		System.out.println(in.readLine());
		while ((line1 = in.readLine()) != null) {
		    System.out.println(line1);
		}
		client.close();
  
		//PRINT TOPIC LIST
		System.out.println("Get all topics to see the order change.");
		client = new Socket("localhost", 12111);
		in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		out = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
		out.println("0");
		out.flush();
		System.out.println("Should output list of topics: ");
		System.out.println(in.readLine());
		while ((line1 = in.readLine()) != null) {
		    System.out.println(line1);
		}
		client.close();

		//RETIREVE MESSAGES FOR TOPIC 0
		System.out.println("Output all messages in first topic to see its been added.");
		client = new Socket("localhost", 12111);
		in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		out = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
		out.println("1");
		out.println("0");
		out.flush();
		System.out.println("Should output all messages in first topic, with periods inbetween. : ");
		System.out.println(in.readLine());
		while ((line1 = in.readLine()) != null) {
		    System.out.println(line1);
		}
		client.close();

		//ADD A NEW MESSAGE TO TOPIC 0, NOW ITS AT START OF LIST
		System.out.println("Add a new message to first topic in list.");
		client = new Socket("localhost", 12111);
		in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		out = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
		out.println("2");
		out.println("0\nJoe\n1Test 2\n.\n");
		out.flush();
		System.out.println("Should output a 2: ");
		System.out.println(in.readLine());
		while ((line1 = in.readLine()) != null) {
		    System.out.println(line1);
		}
		client.close();
  
		//PRINT TOPIC LIST
		System.out.println("Get all topics to see the order change.");
		client = new Socket("localhost", 12111);
		in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		out = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
		out.println("0");
		out.flush();
		System.out.println("Should output list of topics: ");
		System.out.println(in.readLine());
		while ((line1 = in.readLine()) != null) {
		    System.out.println(line1);
		}
		client.close();

		//RETIREVE MESSAGES FOR TOPIC 0
		System.out.println("Output all messages in first topic to see its been added.");
		client = new Socket("localhost", 12111);
		in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		out = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
		out.println("1");
		out.println("0");
		out.flush();
		System.out.println("Should output all messages in first topic, with periods inbetween. : ");
		System.out.println(in.readLine());
		while ((line1 = in.readLine()) != null) {
		    System.out.println(line1);
		}
		client.close();
    
		//ADD A NEW MESSAGE TO TOPIC 2, NOW ITS IN MIDDLE OF LIST
		System.out.println("Add a new message to first topic in list.");
		client = new Socket("localhost", 12111);
		in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		out = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
		out.println("2");
		out.println("2\nJoe\n1Test 1\n.\n");
		out.flush();
		System.out.println("Should output a 2: ");
		System.out.println(in.readLine());
		while ((line1 = in.readLine()) != null) {
		    System.out.println(line1);
		}
		client.close();
  
		//PRINT TOPIC LIST
		System.out.println("Get all topics to see the order change.");
		client = new Socket("localhost", 12111);
		in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		out = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
		out.println("0");
		out.flush();
		System.out.println("Should output list of topics: ");
		System.out.println(in.readLine());
		while ((line1 = in.readLine()) != null) {
		    System.out.println(line1);
		}
		client.close();

		//RETIREVE MESSAGES FOR TOPIC 2
		System.out.println("Output all messages in first topic to see its been added.");
		client = new Socket("localhost", 12111);
		in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		out = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
		out.println("1");
		out.println("2");
		out.flush();
		System.out.println("Should output all messages in first topic, with periods inbetween. : ");
		System.out.println(in.readLine());
		while ((line1 = in.readLine()) != null) {
		    System.out.println(line1);
		}
		client.close();
    
		/**
		   Finished MAUDE Tests, 
		   
		   Time To Test Errors
		**/




		System.out.println("Time To Test Errors!");
		/** test null error after asking to add new topic, not needed to test for add message as 
		    by function decomposition uses same method. should return 8
		**/
		System.out.println("Test to see if nulls make the server throw an error.");
		client = new Socket("localhost", 12111);
		in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		out = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
		out.println("3");
		out.println("");
		out.flush();
		System.out.println("Should output a 8: ");
		System.out.println(in.readLine());
		while ((line1 = in.readLine()) != null) {
		    System.out.println(line1);
		}
		client.close();

		// TEST NULL ID
		System.out.println("Test to see if nulls make the server throw an error.");
		client = new Socket("localhost", 12111);
		in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		out = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
		out.println("1");
		out.println("");
		out.flush();
		System.out.println("Should output a 8: ");
		System.out.println(in.readLine());
		while ((line1 = in.readLine()) != null) {
		    System.out.println(line1);
		}
		client.close();

		// TEST INVALID OPTION NUMBER
		System.out.println("Test to see if invalid numbers make the server throw an error");
		client = new Socket("localhost", 12111);
		in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		out = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
		out.println("");
		out.flush();
		System.out.println("Should output a 9: ");
		System.out.println(in.readLine());
		while ((line1 = in.readLine()) != null) {
		    System.out.println(line1);
		}
		client.close();
  
		// TEST NO INVALID TOPIC ID
		System.out.println("Test to see if invalid topic ID makes the server throw an error");
		client = new Socket("localhost", 12111);
		in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		out = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
		out.println("1");
		out.println("27");
		out.flush();
		System.out.println("Should output a 4: ");
		System.out.println(in.readLine());
		while ((line1 = in.readLine()) != null) {
		    System.out.println(line1);
		}
		client.close();
  
		// TEST INVALID TOPIC ID
		System.out.println("Test to see if invalid Topid ID to add to makes the server throw an error.");
		client = new Socket("localhost", 12111);
		in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		out = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
		out.println("2");
		out.println("227");
		out.println("hey");
		out.println("1hi\n.\n");
		out.flush();
		System.out.println("Should output a 5: ");
		System.out.println(in.readLine());
		while ((line1 = in.readLine()) != null) {
		    System.out.println(line1);
		}
		client.close();
  
		// TEST MESSAGES START WITH 1s 
		// USE OF FUNCTIONAL DECOMPOSITION, HENCE ONLY NEED ONE TEST
		System.out.println("Test to see if wrong format off 1s at start make the server throw an error");
		client = new Socket("localhost", 12111);
		in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		out = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
		out.println("3");
		out.println("1");
		out.println("joe");
		out.println("1hi\n1jy\nbye\n.\n");
		out.flush();
		System.out.println("Should output a 6: ");
		System.out.println(in.readLine());
		while ((line1 = in.readLine()) != null) {
		    System.out.println(line1);
		}
		client.close();

		System.out.println("Just to see multi-threading works, all IDs in order," +
				   "Apart from ones with messages added");
		//PRINT TOPIC LIST
		System.out.println("Get all topics to see the order change.");
		client = new Socket("localhost", 12111);
		in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		out = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
		out.println("0");
		out.flush();
		System.out.println("Should output list of topics: ");
		System.out.println(in.readLine());
		while ((line1 = in.readLine()) != null) {
		    System.out.println(line1);
		}
		client.close();
	    }   
	catch(IOException e){
	    System.err.println(e);
    
	}

    }  
}

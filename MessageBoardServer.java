/*
  Import necessary networking and data packages
*/
import java.io.*;
import java.net.*;
import java.util.ArrayList;

/**
 *  Simple server class to act as an API for {@link SortedTopicList} .
 *   Clients are accepted and then treated like {@link java.lang.Thread threads} .
 *   Once finished the threads die.
 *   The server only shuts down for system errors, or if an 
 *   administrator  stops the process locally. This prevents security threats.
 * @author josephyearsley
 */
public class MessageBoardServer
{
    /**
     * Port Number on which communication will occur, set to 12111
     */ 
    private static final int PORT_NUM = 12111;
    /**
     * The <code>ServerSocket</code> to communicate on 
     */
    private static ServerSocket ss;
    /**
     * Creates a <code>synchronized</code> class to keep all 
     * <code>synchronized</code> methods in.
     *  <code>static</code> as there's only one instance to be shared between
     *  clients.
     *   
     */
    private static final Sync SYNC = new Sync();
       
    /**
     * Creates the <code>ServerSocket</code> with the predefined 
     * <code>PORT_NUM</code>. Also creates and starts the thread for the
     * client, if anything goes wrong it clears them up with exceptions.
     * It also closes down to be safe and not cause further damage.
     * 
     * @param args not used
     * @exception SocketException An error occured with the sockets, 
     *               print error and shuts down.
     * @exception IOException An IO error occured, shuts down and print error 
     *               to be safe.
     * @exception Exception  Catch any remaining errors after closing sockets.
     */
    public static void main(String[] args ) 
    {
	/**
	 * A new socket for the incoming communication.
	 */ 
	Socket incoming;

	/**
	 * Creates a thread t.
	 */
	Thread t;

	try 
	    {
		// instantiate the previously created socket
		ss = new ServerSocket(PORT_NUM);
             
		//whilst theres a client trying to connect
		while (true)
		    {
			// accept the client
			incoming = ss.accept();

			// start handler in new thread, giving it a reference to 
			// the synchronized methods.
			t = new Thread(new Handler(incoming,SYNC));
			t.start();
		    }
	    }
	catch (SocketException se)
	    {
		/*
		 * something has happend shutdown.
		 */
		System.err.println("socket problem:");
                System.err.println(se.getMessage());
                // trust the JVM to clean up
                System.exit(1);
             
	    }
	catch (IOException ioe)
	    {
		/*
		 * something has happend shutdown.
		 */ 
		System.err.println("I/O error:");
		System.err.println(ioe.getMessage());
		//trust the JVM to clean up
		System.exit(1);
	    }
	finally
	    {  //if the sockets still open, close it.
		if (ss != null)
		    {
			try 
			    {  //close the socket 
				ss.close();
			    }
			catch (Exception e)
			    {
				//closing down anyway 
				System.err.println("closing: " + e.getMessage());
			    }
		    }
	    }
    }
}

/**
 * A Class to handle remote clients, implementing {@link java.lang.Runnable}
 * @author josephyearsley
 */
class Handler implements Runnable
{
    /**
     * The socket to which the client is communicating on.
     */ 
    private Socket client;
    /**
     * The <code>synchronized</code> methods class to ensure shared classes.
     * Makes use of functional decomposition, as all threads use one method.
     */
    private final Sync SYNC;
    /**
     * Creates a new <code>Handler</code> initiated with the socket to 
     * communicate on and the <code>synchronized</code> class to use.
     * By passing the class of methods to use, it makes it easier to swap
     * better methods into place as you can just call it the same.
     * @param s The socket the client is on.
     * @param sy The <code>synchronized</code> class to use for operations.
     */
    Handler(Socket s, final Sync SY)
    {
	client = s;
	//pass the synchronized class with the static list in it
	SYNC = SY;
    }

    /**
     *  Handle session for one remote client:
     *  set up I/O, and decide which method to use based
     *   upon the initial line, outputting the response then closing the 
     *   connection.
     *  Handles any exceptions which occur, and closes connections 
     *   once complete. 
     * 
     *   @exception StartOneException Thrown if messages don't start with 1's,
     *               returns a 6.
     *   @exception NumberFormatException Thrown if the initial input 
     *               isn't a number, or if number isn't an option,
     *                   returns a 9.
     *   @exception NullPointerException Thrown if any null line is sent to 
     *               the server, returns a 8.
     *   @exception IOException Thrown if anything goes wrong with IO,
     *               closes down to be safe, after printing the error.
     *   
     */
    public void run()
    {
	// for I/O
	BufferedReader in = null;
	PrintWriter out = null;

	//Variables
	String text,returnValue = "";
	int iD;


	try 
	    {
		// set up I/O
		in = new BufferedReader
		    (new InputStreamReader(client.getInputStream()));
		out = new PrintWriter
		    (new OutputStreamWriter(client.getOutputStream()));

		// for client input
		String line;
		line = in.readLine();

                //first line should be an int, trim and parse
                final int INT_LINE = Integer.parseInt(line.trim());

                //use method to do with option
                switch(INT_LINE)
		    {
		    case(0):
			//return all topics
			returnValue = SYNC.getTopics();
			break;

		    case(1): 
			//ensure not null
			if(!(text = in.readLine()).isEmpty())
			    {
				iD = Integer.parseInt(text);
			    }
			else
			    {
				throw new NullPointerException();
			    }
			//return messages in topic
			returnValue = SYNC.getTopicString(iD);
			break;

		    case(2):
			//ensure not null
			ArrayList<String> postList = SYNC.nullReaderAdd(in);
			//formatted already, add the post
			returnValue = SYNC.addPost(postList.get(1),
						   postList.get(2),
						   Integer.parseInt(
								    postList.get(0)));
			break;

		    case(3): 
			//ensure not null
			ArrayList<String> topicList = SYNC.nullReaderAdd(in);
			//already formatted so add the topic
			returnValue =  SYNC.addNewTopic(topicList.get(0),
							topicList.get(1),
							topicList.get(2));
			break;

		    default:
			//the number isn't in the options hence exception
			throw new NumberFormatException();

		    }
		//return based upon users inputs 
		out.write(returnValue + "\n");  
		out.flush();  
	    }
	catch (IOException e)
	    {
		// fatal error for this session
		System.err.println(e.getMessage());
	    }
	catch (StartOneException e)
	    {
		//doesn't start with ones
		out.write("6\n");  
		out.flush();  
	    }
	catch (NullPointerException e)
	    {
		//no need for error output to system as doesn't matter, 
		//we've closed and moved on.
		//don't like nulls
		out.write("8\n");  
		out.flush();  
	    }
	catch (NumberFormatException e)
	    { 
		//no need for error output to system as doesn't matter, 
		//we've closed and moved on.
		//9 to represent no number input
		out.write("9\n");  
		out.flush();  

	    }
	finally
	    {  // close connections
		try 
		    {
			in.close();
		    }
		catch(IOException e)
		    {
		    }
		if (out != null)
		    {
			//close output buffer
			out.close();
		    }
		if (client != null)
		    {
			try 
			    {
				//close client connection 
				client.close();
			    }
			catch (IOException e)
			    {
			    }
		    }
	    }
    }
   
}

/**
 * Created an exception to throw if any messages input doesn't
 * begin with a 1.
 * @author josephyearsley
 */
class StartOneException extends Exception
{
    //Parameterless Constructor
    public StartOneException() {}

    //Constructor that accepts a message
    public StartOneException(String message)
    {
	super(message);
    }
}

/**
 * A class to ensure no deadlock and only one update at a time, whilst
 * interacting with the {@link SortedTopicList list}.
 * Includes methods to {@link #getTopics get Topics} ,
 * {@link #getTopicString get all messages in a topic},
 *  {@link #addPost add messages},
 *  {@link #addNewTopic & add new Topics}.
 *  Not public as its a nested class.
 * @see SortedTopicList
 * 
 * @author josephyearsley
 */
class Sync{

    /**
     * The Topic list to use.
     * Final as so it can't be edited.
     * Static as there's only one instance to every be running.
     */
    private static final SortedTopicList MB = new SortedTopicList();
        
    /**
     * A Method to return all topics in the list, <code>synchronized</code>
     * so only one thread at a time can access it.
     * @return The string containing ID and Title on all topics
     *  e.g. 0\nWallpaper\n
     */
    public synchronized String getTopics(){
        return MB.listToString();
    }

    /**
     * A Method to return all messages in the topic, <code>synchronized</code>
     * so only one thread at a time can access it.
     * @param iD The ID of the topic to return
     * @return A string of a Topic's {@link Message messages}, or a 4 if the 
     *       topic isn't found. e.g. N Wild\n1I love the wallpaper\n.\n
     */
    public synchronized String getTopicString(int iD){
        return MB.getTopicString(iD);
    }

    /**
     * A Method to add a post to a topic with a specific ID,
     * <code>synchronized</code>
     * so only one thread at a time can access it,
     * as otherwise data would be corrupted.
     * @param sender The sender of the message.
     * @param text  The text of the message.
     * @param iD The ID of the topic of which to add the message.
     * @return Either a 2 (if added) or a 5 (if not a valid ID for a topic)
     */
    public synchronized String addPost(String sender, String text, int iD){

        String addedP;

        //send a purpose as we are checking
        if(MB.publicTopicExists(iD)){
	    MB.addPost(sender,text,iD);
	    addedP = "2";
        }
        else{ addedP = "5";}

        return addedP;
    }

    /**
     *  A Method to add  a topic to the {@link SortedTopicList list} ,
     * <code>synchronized</code>
     * so only one thread at a time can access it, as otherwise data would be 
     * corrupted.
     * @param title The title of the <code>Topic</code> to add
     * @param sender The sender of the message
     * @param text The text of the message 
     * @return A 3 to show its completed.
     */
    public synchronized String addNewTopic(String title, String sender, String text){

        MB.addNewTopic(sender,text, title);
        return "3";
    }

    /**
     * A method to check if the input by the user is correct, i.e. starts with a 
     * 1. 
     * In a different class for functional decompostion.
     * <code>synchronized</code> as not to corrupt data between threads.
     * Possibly not needed as only called from a <code>synchronized</code>
     * method anyway, that wouldn't keep the lock on the other method.
     * @param text the line of a message just input.
     * @return The text input without the initial 1.
     * @throws StartOneException if the text doesn't have a 1 after each newline.
     */
    public synchronized String correctFormat(String text) throws StartOneException{

        StringBuilder sb = new StringBuilder();

        if(text.substring(0).charAt(0) != '\n')
	    {
		sb.append("\n");
	    }
        sb.append(text);
        //add newline so can use only 2 loops.
        if(sb.substring(1).charAt(0) != '1')
	    {
		throw new StartOneException();
	    }
        return sb.toString().replace("\n1", "\n");
    }

    // faster to share the resource by making used of same memory space than
    // by allowing every thread to have the same code.
    /**
     * A method to read and format/validate inputs on the fly. 
     * Faster way than each thread having their own method, plus takes use of 
     * functional decomposition. Shaves 0.3s off having this and
     * {@link #correctFormat} in the Handler class. Also by reading inputs and 
     * validating straight away saves any unnecessary storing of data as it says
     * if its bad data straight away, hence killing the thread and freeing 
     * resources. 
     * <p>
     * Only exception is an incorrect ID as that would of taken the
     * functional decomposition advantages out of the method.
     * Could integrate in future with more time to re-jig code to allow for this
     * </p>
     * @param input The input reader, for the thread to get what the client has
     *              input.
     * @return An ArrayList of size 3, with data the user has typed in.
     * @throws IOException In case there's any socket errors, Catched from parent
     *          who called method.
     * @throws StartOneException Pass the exception from {@link #correctFormat}
     *                              back to the parent thread.
     */
    public synchronized ArrayList<String> nullReaderAdd(BufferedReader input) throws IOException,StartOneException{

        BufferedReader in = input;
        Boolean done = false;
        int count = 0;
        String textIn;
        StringBuilder temp = new StringBuilder();
        ArrayList<String> list = new ArrayList<>();

        try{
            // 1 loop to allow for automation of input, very efficient.
            
            while(!done){
                textIn = in.readLine();
                //readline only ever returns an empty string if null
                if(textIn.isEmpty()){
                    //parent catches the exception
                    throw new NullPointerException();
                }
                //not null so check for end
                else{
                    //if end/period stop
                    if(textIn.equals(".")){
                        done = true;
                    }
                    //else add the text to the list
                    else{
                        count++;
                        /*  2 usual input, i.e. id/title, sender 
                            then validation needs to occur
                        */
                        if(count > 2){
                            /** build string with string builder as its more efficient
                             *  than a loop which was used before in top class
                             */
                            temp.append(correctFormat(textIn));
                        }
                        else
			    {
				list.add(textIn);
			    }
                    }
                }
            }
            /*  nearly finished, just add the string buffer for 
                text of message now 
                inputs stopped.
            */
            list.add(temp.toString());
        }
	catch(IOException e){
            System.err.println(e);
        }
        //no need to close reader as it keeps being used and refreshed.
        return  list;
    }
}

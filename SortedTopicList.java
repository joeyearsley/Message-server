

  /*
  * copyright Grant Malcolm
  *
  *   This source code may be freely used, modified, or distributed
  *   provided due credit is given.
  *
  *   Modified by Joe Yearsley Adding:
  *     - topicToString();
  *     - serverToString();
  *     - topicExists();
  *     - publicTopicExists();
  *     - getTopicString();
  *   The above are to help with the API Server, used so employees 
  *   can communicate off site, as well as on site.
  */


/**
 *  List of topics for a simple Message Board server.
 *  This class represents a list of {@link SortedTopicList.Topic Topics}
 *  that might be stored by a Message Board server.
 *  Topics are stored in the order
 *  of their most-recent update, the most-recently updated topics being
 *  at the start of the list.
 *  Topics are {@link #addNewTopic(String,String,String) created}
 *  by providing a title and a {@link SortedTopicList.Message Message}
 *  to initiate the topic;
 *  the topic is given a unique identifier (the length of the list),
 *  and stored at the start of the list.
 *  Topics may be updated by {@link #addPost(String,String,int) adding}
 *  a message to a given topic
 *  (specified by its {@link SortedTopicList.Topic#getID() identifier});
 *  the updated topic is moved to the start of the list.
 *
 *  @author <a href="mailto:grant@liverpool.ac.uk">Grant Malcolm</a>
 *  @version 1.0
 *
 */
public class SortedTopicList
{
    /**
     *  The list of topics.
     *  Set to <code>null</code> when the list is empty.
     *
     */
    private List<Topic> topics = new List<Topic>();

    /**
     *  The number of topics in the list.
     *
     */
    private int length = 0;


    /**
     *  Creates a new <code>SortedTopicList</code> instance.
     *  The new instance has an empty list of topics.
     *  Implements <code>[] : -&gt; SortedTopicList</code>.
     *
     */
    public SortedTopicList()
    {
    }

    /**
     *  The length of the list; i.e., the number of {@link Topic topics}
     *  in the list.
     *  Implements <code>length : SortedTopicList -&gt; Int</code>.
     *
     *  @return the number of topics in the list
     *
     */
    private int length()
    {
	return length;
    }

    /**
     *  Add a new Topic to the list.
     *  The {@link SortedTopicList.Topic Topic} contains
     *  the {@link SortedTopicList.Message message} formed from
     *  the first two parameter strings and has the third string as title.
     *  and is assigned a unique identifier
     *  (the {@link #length() length} of the list).  The new topic is
     *  placed at the start of the list.
     *
     *  @param ms the sender of the first message in the topic
     *  @param mt the text of the first message in the topic
     *  @param t the title of the topic
     *
     */
    public void addNewTopic(String ms, String mt, String t)
    { 
	topics.add(new Topic(new Message(ms, mt), t, length));
	// list is one topic longer
	length++;
    }

    /**
     *  Add a message to a topic.
     *  The first two string parameters give the
     *  {@link SortedTopicList.Message message} that will be
     *  added to the {@link SortedTopicList.Topic topic} with the given ID;
     *  this topic will be moved to the start of the list,
     *  as it is the most-recently updated.
     *  If there is no topic in the list with the given ID,
     *  this method has no effect.
     *
     *  @param ms the sender of the message to be added
     *  @param mt the text of the message to be added
     *  @param i the id of the topic to add the message to
     *
     */
    public void addPost(String ms, String mt, int i)
    {
        // if list is empty, do nothing
        if (topics.theList == null)
	    {
		return;
	    }
        /*
         *  else:
         *  if the first element has the requested id, add the message
         */
        if (topics.theList.value.getID() == i)
	    {
		topics.theList.value.addMessage(new Message(ms, mt));
		return;
	    }
        /*
         *  else:
         *  (the list is not empty and the first topic is not the one we want):
         *  traverse through the list to find the topic with ID i.
         *
         *  variable curr is used to traverse the list;
         *  variable currPrev is always set to the preceding node
         */
        List.Node<Topic> curr = topics.theList.next;
        List.Node<Topic> currPrev = topics.theList;

        while (curr != null) // stop at end of list
	    {
		if (curr.value.getID() == i)
		    {
			/*
			 * found topic with ID i:
			 * add the message and move to start of list
			 *
			 */
			curr.value.addMessage(new Message(ms, mt));
			/*
			 * remove curr from list:
			 */
			// set currPrev to point to node after curr (can be null):
			currPrev.next = curr.next;
			// curr should be first;
			// it should point to what was the start of the list
			curr.next = topics.theList;
			// start of the list should be curr
			topics.theList = curr;
			return;  // done
		    }
		// else (haven't found the topic): move on to tail of list
		currPrev = curr;
		curr = curr.next;
	    }
        // got through without finding i: do nothing
    }


    /**
     *  Format the list of topics as a string.
     *
     *  @return the string containing data on all topics
     *
     *  @see Message#toString Message#toString
     */
    public String toString()
    {
	String topicData = "";
	List.Node<Topic> node = topics.theList;
	while (node != null)
	    {
		topicData += node.value;
		node = node.next;
	    }
	return topicData;
    }
    /**
     *  A server helper class which
     *  formats the list of topics as a string, without the {@link Topic topic's}
     *  {@link Message messages}.
     *
     *  @return the string containing ID and Title on all topics
     *  e.g. 0\nWallpaper\n
     *
     *  
     */
    public String listToString()
    {
	String topicData = "";
	List.Node<Topic> node = topics.theList;
	while (node != null)
	    {
		topicData += node.value.getID() + "\n" + node.value.getTitle() + "\n";
		node = node.next;
	    }
	return topicData;
    }
   
    /** 
     *  Finds the Topic with use of {@link #topicExists}, passing it
     *  the ID of the topic. 
     *  If it returns a topic,
     *  then make the topic into a string, otherwise topic not found.
     *  
     *  @param ID The ID of the topic to return.
     *  @return A string of a Topic's {@link Message messages}, or a 4 if the 
     *       topic isn't found. e.g. N Wild\n1I love the wallpaper\n.\n
     * 
     * 
     */
    public String getTopicString(int ID)
    {
	String topicData = "";
	// Make use of functional decomp, plus only one loop, as it passes a topic
	// back
	Topic topic = topicExists(ID);
	if(topic != null) {
	    topicData += topic.topicToString();
	}
	else
            {
                topicData = "4";
            }
	return topicData;
    }
   
    /**
     * A public method to see if a {@link Topic} exists.
     * Not implemented the list as a inner class as I can see it being
     * more of a business forum for employees, to use both internally and 
     * externally.
     * @param ID The ID of the Topic, to see if it exists.
     * @return A boolean saying if the topic exists or not.
     */
    public boolean publicTopicExists(int ID){
	boolean bool;
	if(topicExists(ID) != null)
	    {
		bool = true;
	    }
	else
	    {
		bool = false;
	    }
	return bool;
    }
    /**
     * Checks to see if a topic exists or not.
     * @param ID The ID of the Topic to see if exists.
     * @return The Topic if it exists otherwise returns nothing.
     */
    private Topic topicExists(int ID){
	List.Node<Topic> node = topics.theList;
	Topic returnNode = null;
	while (node != null)
	    {  
		if(node.value.getID() == ID)
		    {
			returnNode = node.value;
		    }
		node = node.next;
	    }
	return returnNode;
    }
   
    /**
     *  This class represents messages for a simple Message Board server,
     *  implementing sort Message in
     *  <a href="MessageBoard.maude">MESSAGE_BOARD</a>.
     *  Messages consist of two strings:
     *  one representing the name of the poster;
     *  the other being the text of the message.
     *
     */
    private static class Message 
    {

	/**
	 *  The name of the poster who sent the message.
	 *
	 */
	private final String sender;

	/**
	 *  The text of the message.
	 *
	 */
	private final String text;

	/**
	 *  Creates a new <code>Message</code> instance with the given data.
	 *  Implements newMessage : String String -&gt; Message.
	 *
	 *  @param s the name of the poster
	 *  @param t the text of the message
	 *
	 */
	public Message(String s, String t)
	{
	    sender = s;
	    text = t;
	}

	/**
	 *  Return the name of the sender of the message.
	 *  Implements getSender : Message -&gt; String.
	 *
	 *  @return the name of the poster who sent the message
	 *
	 */
	public String getSender()
	{
	    return sender;
	}

	/**
	 *  Return the text of the message.
	 *  Implements getText : Message -&gt; String.
	 *
	 *  @return the text of the message
	 *
	 */
	public String getText()
	{
	    return text;
	}

	/**
	 *  Give a string showing the sender and the message text.
	 *  <p>To agree with the Maude tests, it might be better to have
	 *      <code>return "newMessage(" + sender + ", " + text + ")";</code>,
	 *  but the implementation here tries to give a slightly more readable
	 *  format.</p>
	 * 
	 *  @return a string in the format
	 *     <code>From: <i>sender</i></code> followed by
	 *     the message text on the next line, and ended by a newline.
	 *
	 */
	public String toString()
	{
	    return "   From: " + sender + "\n      " + text + "\n";
	}
      
	/**
	 * Give a string showing the sender and the text on different lines.
	 * Matches the maude return also.
	 * @return A string consisting of the sender and the text, where the text
	 *  is preceded by a 1, end of a message is shown by a period.
	 * e.g. N Wild\n1I love the wallpaper\n.\n
	 */
	public String serverToString()
	{
	    String textStyle = text.replace("\n", "\n1");
	    return sender + textStyle + "\n.\n";
	}
    }


    /**
     *  Very basic singly-linked lists (no need for two pointers)
     *  with a method to add an element to the list.
     *
     */
    private static class List<E>
    {
	/**
	 *  The first node in the list.
	 *  <code>null</code> if the list is empty.
	 *
	 */
	private Node<E> theList;

	/**
	 *  Creates a new, empty, List.
	 *
	 */
	public List()
	    {
	    }

	/**
	 *  Add an element to the list.
	 *
	 *  @param e the element to add
	 *
	 */
	public void add(E e)
	{
	    theList = new Node<E>(e, theList);
	}

	/**
	 *  Nodes in the singly-linked list.
	 *
	 */
	private static class Node<F>
	{
	    /**
	     *  The value stored in the node.
	     *
	     */
	    private final F value;

	    /**
	     *  Pointer to the next node in the list.
	     *
	     */
	    private Node<F> next;

	    /**
	     *  Conduct a node with given value and pointing to
	     *  a given successor node.
	     *
	     */
	    public Node(F e, Node<F> t)
		{
		    value = e;
		    next = t;
		}
	}
    }


    /**
     *  A class of Topics for a simple Message Board server,
     *  implementing sort Topic in <a href="Topics.maude">MESSAGE_BOARD</a>.
     *  Each topic consists of a list of
     *  {@link SortedTopicList.Message Messages}
     *  that have been posted to the server.  Each topic also has an ID,
     *  which should be assigned by the server in such a way that it
     *  uniquely identifies the topic
     *  (this is done in class {@link SortedTopicList SortedTopicList}).
     *  Topics also have a title, which should be provided by the 
     *  Message Board user, along with an initial message to start
     *  the topic (see {@link #Topic(Message,String,int) the constructor}).
     *
     */
    private static class Topic 
    {
	/**
	 *  The list of {@link SortedTopicList.Message messages} in the topic.
	 *
	 */
	private final List<Message> messages = new List<Message>();

	/**
	 *  The unique identifier for this topic.
	 *
	 */
	private final int id;

	/**
	 *  The title of this topic.
	 *
	 */
	private final String title;

	/**
	 *  Creates a new <code>Topic</code> instance with a given ID, title,
	 *  and {@link Message message} to initiate the topic.
	 *  Implements newTopic : Message Int String -&gt; Topic.
	 *
	 *  @param m the {@link Message Message} that initiates the topic
	 *  @param t the title of the topic
	 *  @param i the unique identifier for this topic
	 *
	 */
	public Topic(Message m, String t, int i)
	{
	    id = i;
	    title = t;
	    messages.add(m);
	}

	/**
	 *  Return the unique identifier for the topic.
	 *  Implements getID : Topic -&gt; Int.
	 *
	 *  @return the topic's identifier
	 *
	 */
	public int getID()
	{
	    return id;
	}

	/**
	 *  Return the title of the topic.
	 *  Implements getTitle : Topic -&gt; String.
	 *
	 *  @return the topic's title
	 *
	 */
	public String getTitle()
	{
	    return title;
	}

	/**
	 *  Add a {@link Message Message} to the topic}.
	 *  Implements addMessage : Message Topic -&gt; Topic.
	 *
	 *  @param m the message to be added.
	 *
	 */
	public void addMessage(Message m)
	{
	    messages.add(m);
	}

	/**
	 *  Formats the topic as a string.
	 *  <p>Gives the title an ID of the topic,
	 *  and all the messages in the topic in a readable format.
	 *
	 *  @return a string consisting of the topic title and ID,
	 *    followed by all the messages in the topic
	 *
	 *  @see Message#toString Message#toString
	 *
	 */
	public String toString()
	{
	    // title and ID
	    String topicInfo = "Topic: " + title + "\nID: " + id + "\n";

	    // all the messages in the topic
	    String msgs = "";
	    List.Node<Message> node = messages.theList;
	    while (node != null)
		{
		    msgs = node.value + msgs;
		    node = node.next;
		}

	    // return title + ID + all messages
	    return topicInfo + msgs;
	}
      
	/**
	 * A server helper method to return all messages in a topic.
	 * Used helper methods instead as I can imagine the implementation being
	 * used in a business enviroment, hence returning different items to 
	 * different clients and formats.
	 * @return All Messages in a topic in the format of:
	 *        e.g. N Wild\n1I love the wallpaper\n.\n
	 */
	public String topicToString()
	{
	    // all the messages in the topic
	    String msgs = "";
	    List.Node<Message> node = messages.theList;
	    while (node != null)
		{
		    msgs = node.value.serverToString() + msgs;
		    node = node.next;
		}

	    // return title + ID + all messages
	    return msgs;
	}
    }

    /* ----------------------------------------------------------------------

       Testing

       ----------------------------------------------------------------------
    */
    /**
     *  Tests the implementation by creating a sorted topic list
     *  and adding topics and messages.
     *  See <a href="MessageBoardTest.maude>the Maude tests</a>
     *  for the expected results.
     *
     *  @param args not used
     */
    public static void main(String[] args)
    {
	SortedTopicList mb = new SortedTopicList();
	System.out.println(mb);
	mb.addNewTopic("Polonius", "What reads my lord?", "Book Recommendation");
	System.out.println(mb);
	mb.addNewTopic("121.110.34.2", "ping", "Is there anybody out there?");
	System.out.println(mb);
	mb.addPost("Hamlet", "Words, words, words", 0);
	System.out.println(mb);
	mb.addPost("Polonius", "lolz :)", 0);
	System.out.println(mb);
	mb.addNewTopic("Oscar Wilde", "What's brown and sticky?", "Joke");
	System.out.println(mb);
	mb.addNewTopic("Devo", "We are Devo", "Are we not men?");
	System.out.println(mb);
	mb.addPost("Jordan", "A stick", 2);
	System.out.println(mb);
	mb.addPost("localhost", "ssh", 1);
	System.out.println(mb);
    }
}

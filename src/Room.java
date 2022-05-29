import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Room {
	private String message;
	private ArrayList<Passageway> passageways;
	
	public Room(String message, ArrayList<Passageway> passageways) {
		this.message = message;
		this.passageways = passageways;
	}
	
	public Room(BufferedReader reader, ArrayList<String> roomFiles) 
			throws IOException, Passageway.ParsingException {
		// Read in message
		//first line that isnt a comment or empty is message variable
		String message;
        while ((message = reader.readLine()) != null) {
        	// Ignore empty lines and comments
        	if (message.isEmpty() || message.charAt(0) == '#') {
        		continue;
        	}
        	this.message = message;
        	this.message = this.message.replace("\\n", "\n");
        	break;
        }

        // Read in passageways
        this.passageways = new ArrayList<Passageway>();
    	String firstLineOfSet;
        while (true) {
            while ((firstLineOfSet = reader.readLine()) != null) {
            	// Ignore empty lines and comments
            	if (firstLineOfSet.isEmpty() || firstLineOfSet.charAt(0) == '#') {
            		continue;
            	}
            	break;
            }
        	if (firstLineOfSet == null) {
        		return;
        	}
        	Passageway a = new Passageway(firstLineOfSet, reader, roomFiles);
        	passageways.add(a);
        }
        
	}
	
	public String getMessage() {
		return message;
	}
	
	public boolean hasPassageways() {
		return passageways.size() > 0;
	}
	
	public Passageway determinePassageway(String keyword) {
		for (Passageway passageway: passageways) {
			if (passageway.getKeyword().equals(keyword)) {
				return passageway;
			}
		}
		return null;
	}
	
	public void display() {
		System.out.println(message);
		if (hasPassageways()) {
			for (Passageway passageway: passageways) {
				System.out.println("-> " + passageway.getExplanation());
			}
		}
		System.out.println();
	}
}
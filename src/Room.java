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
		String message;
        while ((message = reader.readLine()) != null) {
        	// Ignore empty lines and comments
        	if (message.isEmpty() || message.charAt(0) == '#') {
        		continue;
        	}
        	this.message = message;
        	break;
        }

        // Read in passageways
        this.passageways = new ArrayList<Passageway>();
        Passageway lastPassageway;
        while (reader.ready()) {
        	lastPassageway = new Passageway(reader, roomFiles);
        	if (lastPassageway != null) {
        		this.passageways.add(lastPassageway);
        	}
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
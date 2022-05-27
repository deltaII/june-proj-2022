import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.IOException;

public class Passageway {
	public class ParsingException extends Exception {
		public ParsingException(String message) {
			super(message);
		}
	}
	
	private String explanation;
	private String keyword;
	private int destination;
	
	public Passageway(String explanation, String keyword, int destination) {
		this.explanation = explanation;
		this.keyword = keyword;
		this.destination = destination;
	}
	
	public Passageway(BufferedReader reader, ArrayList<String> roomFiles) 
			throws IOException, ParsingException {
		// Read in properties
		String line;
		int state = 0;
        while ((line = reader.readLine()) != null) {
        	// Ignore empty lines and comments
        	if (line.isEmpty() || line.charAt(0) == '#') {
        		continue;
        	}
        	
        	switch (state) {
        	case 0:
        		this.explanation = line;
            	state++;
        		break;
        	case 1:
        		this.keyword = line;
            	state++;
        		break;
        	case 2:
        		boolean foundRoomIndex = false;
        		for (int i = 0; i < roomFiles.size(); i++) {
        			String roomFile = roomFiles.get(i);
        			if (roomFile.equals(line)) {
                		this.destination = i;
                		foundRoomIndex = true;
                		break;
        			}
        		}
        		if (!foundRoomIndex) {
        			throw new ParsingException("Invalid room file '" + line + "'");
        		}
            	state++;
        		break;
        	default:
        		break;
        	}
        }
        
        if (state < 3) {
        	String[] pointAtFail = { "explanation", "keyword", "destination" };
        	throw new ParsingException("Passageway entry missing " + pointAtFail[state]);
        } else if (state > 3) {
        	throw new ParsingException("Something went horribly wrong parsing passageway entry");

        }
	}
	
	public String getExplanation() {
		return explanation;
	}
	
	public String getKeyword() {
		return keyword;
	}
	
	public int getDestination() {
		return destination;
	}
}
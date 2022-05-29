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
	
	public Passageway(String firstLineOfSet, BufferedReader reader, ArrayList<String> roomFiles) 
			throws IOException, ParsingException {
		this.explanation = firstLineOfSet;
		
		// Read in properties
		String line;
		int state = 0;
        while ((line = reader.readLine()) != null && state != 2) {
        	// Ignore empty lines and comments
        	if (line.isEmpty() || line.charAt(0) == '#') {
        		continue;
        	}
        	switch (state) {
        	case 0:
        		this.keyword = line;
            	state++;
        		break;
        	case 1:
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
        
        if (state == 0) {
        	throw new ParsingException("Extra empty lines/comments at the end of file: should end right after room message if no passageways wanted");
        } else if (state < 2) {
        	String[] pointAtFail = { "keyword", "destination" };
        	throw new ParsingException("Passageway entry missing " + pointAtFail[state]);
        } else if (state > 2) {
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
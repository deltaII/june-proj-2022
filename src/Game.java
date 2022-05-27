// Help with I/O from:
// - https://www.geeksforgeeks.org/fast-io-in-java-in-competitive-programming/
// - https://stackoverflow.com/questions/71577446/how-to-create-one-bufferedreader-for-several-text-files
// - https://stackoverflow.com/questions/17678862/reading-lines-with-bufferedreader-and-checking-for-end-of-file

import java.util.ArrayList;
import java.util.Arrays;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;
import java.util.Scanner;

public class Game {
	private Player player;
	private ArrayList<Room> rooms;
	private int currentRoom;
	private Scanner in;
	
	public Game(Player player, ArrayList<Room> rooms) {
		this.player = player;
		this.rooms = rooms;
		
		setup();
	}
	
	public Game(Player player, String manifest, String roomFolder)
			throws IOException, Passageway.ParsingException {
		this.player = player;
		this.rooms = new ArrayList<Room>();
		
		// Read in the manifest
        BufferedReader manifestReader = new BufferedReader(new FileReader(manifest));  
        String line;
        ArrayList<String> roomFiles = new ArrayList<String>();
        while ((line = manifestReader.readLine()) != null) {
        	// Ignore empty lines and comments
        	if (line.isEmpty() || line.charAt(0) == '#') {
        		continue;
        	}
        	roomFiles.add(line);
        }
        manifestReader.close();

        // Deserialize the rooms
        for (String roomFile: roomFiles) {
        	// Read in each room
        	String roomFilePath = roomFolder + "/" + roomFile;
            BufferedReader roomReader = new BufferedReader(new FileReader(roomFilePath));
            this.rooms.add(new Room(roomReader, roomFiles));
            roomReader.close();
        }

        setup();
	}
	
	public void play() {
		this.currentRoom = 0;
		Room room;
		Passageway passage;
		while (true) {
			room = rooms.get(currentRoom);
			room.display();
			if (!room.hasPassageways()) {
				break;
			}
			passage = null;
			while (passage == null) {
				String keyword = prompt("> ");
				passage = room.determinePassageway(keyword);
				if (passage == null) {
					System.out.println("Please choose one of the avaliable paths.");
				}
			}
			currentRoom = passage.getDestination();
		}
		in.close();
	}
	
	private String prompt(String msg) {
		System.out.print(msg);
		return in.nextLine();
	}
	
	private void setup() {
		in = new Scanner(System.in);
	}
}
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
	private ArrayList<Integer> journey;
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
            this.rooms.add(new Room(roomFile, roomReader, roomFiles));
            roomReader.close();
        }

        setup();
	}
	
	private void setup() {
		in = new Scanner(System.in);
		journey = new ArrayList<Integer>();
	}
	
	public void play() {
		this.currentRoom = 0;
		Room room;
		Passageway passage;

		room = rooms.get(currentRoom);

		while (true) {
			journey.add(currentRoom);
			room.display();
			if (!room.hasPassageways()) {
				break;
			}
			passage = null;
			while (passage == null) {
				String keyword = prompt("[" + room.getFile() + "] $ ");
				
				if (keyword.equals("JOURNEY")) {
					displayJourney();
				} else {
					passage = room.determinePassageway(keyword);
					if (passage == null) {
						ArrayList<String> possibilities = room.findCorrectSpelling(keyword);
						System.out.println("Sorry, '" + keyword + "' isn't not a path you can take here.");
						if (!possibilities.isEmpty()) {
							System.out.println("Did you mean:");
							for (String possibility: possibilities) {
								System.out.println(" -> " + possibility);
							}
						}
					}
				}
			}
			currentRoom = passage.getDestination();
			room = rooms.get(currentRoom);
			displayWander(room.getWander());
		}
		in.close();
	}
	
	private void displayJourney() {
		int n = 1;
		for (int room: journey) {
			System.out.println("" + n + ". " + rooms.get(room).getFile());
			n++;
		}
	}
	
	private void displayDots(int min, int n) {
		try {
			for (int i = 0; i < n + min; i++) {
				Thread.sleep(50);
				System.out.print(".");
			}
			System.out.println();
		} catch (InterruptedException e) {
			System.err.println("You lost your way and stumbled into a locked room. Please tell us about this glitch in the code.");
		}
	}
	
	private void displayWander(String wander) {
		try {
			System.out.println();
			displayDots(3, 5);
			Thread.sleep((long)(Math.random() * 1000));
			for (int i = 0; i < wander.length(); i++) {
				System.out.print(wander.charAt(i));
				switch (wander.charAt(i)) {
				case '.':
				case ':':
				case ';':
				case '?':
				case '!':
					Thread.sleep(200);
					break;
				case ',':
					Thread.sleep(125);
					break;
				default:
					Thread.sleep(50);
					break;
				}
			}
			System.out.println();
			displayDots(3, 5);
			Thread.sleep(500);
			System.out.println();
		} catch (InterruptedException e) {
			System.err.println("You lost your way and stumbled into a locked room. Please tell us about this glitch in the code.");
		}
	}
	
	private String prompt(String msg) {
		System.out.print(msg);
		return in.nextLine().strip().toUpperCase();
	}
}

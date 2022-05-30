import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.io.BufferedReader;
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
			this.message = message.replace("\\n", "\n");
			break;
		}

		while ((message = reader.readLine()) != null) {
			// Ignore empty lines and comments
			if (message.startsWith("\\n")) {
				this.message += message.replace("\\n", "\n");
			} else {
				break;
			}
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

	private int calculateSpellingCloseness(String a, String b) {
		HashSet<Character> aUniques = new HashSet<Character>();
		HashSet<Character> bUniques = new HashSet<Character>();

		for (int i = 0; i < a.length(); i++) {
			aUniques.add(a.charAt(i));
		}


		for (int i = 0; i < b.length(); i++) {
			bUniques.add(b.charAt(i));
		}

		int intersections = 0;
		for (Character c: aUniques) {
			if (bUniques.contains(c)) {
				intersections++;
			}
		}

		return intersections;
	}

	public ArrayList<String> findCorrectSpelling(String input) {
		class Pair implements Comparable<Pair> {
			public int close;
			public int idx;

			@Override
			public int compareTo(Pair other) {
				return close - other.close;
			}
			
			public String toString() {
				return "{close=" + close + ", idx=" + idx + "}";
			}
		}

		ArrayList<String> options = new ArrayList<String>();
		Pair[] spellingClosenesses = new Pair[passageways.size()];

		int i = 0;
		for (Passageway passageway: passageways) {
			Pair pair = new Pair();
			pair.close = calculateSpellingCloseness(input, passageway.getKeyword());
			pair.idx = i;
			spellingClosenesses[i] = pair;
			i++;
		}
		Arrays.sort(spellingClosenesses);
		
		for (Pair spellingCloseness: spellingClosenesses) {
			if (spellingCloseness.close >= Math.ceil(input.length() * 0.8)) {
				options.add(passageways.get(spellingCloseness.idx).getKeyword());
			}
			if (options.size() >= 4) {
				break;
			}
		}

		return options;
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
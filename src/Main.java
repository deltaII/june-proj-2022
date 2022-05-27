import java.io.IOException;

public class Main {
	public static void main(String[] args) {
		Player player = new Player("Alethanx");
		
		try {
			Game game = new Game(player, "data/rooms.txt", "data/rooms");
			game.play();
		} catch (IOException exception) {
			System.err.println(exception);
		} catch (Passageway.ParsingException exception) {
			System.err.println(exception);
		}
	}
}
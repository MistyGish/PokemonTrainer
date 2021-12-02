package trainer;

import java.util.Random;

public class Pokemon {

	int level;
	boolean fighter;
	PokemonType type;
	
	public String getPokemonInfo() {
		return "Type: " + type + ", " + "Level: " + level + ", " + "Fighter: " + fighter;
	}
	
	public int getAttackValue() {
		int max = level;
		int min = 1;
		Random random = new Random();
		
		return random.nextInt(max - min + 1) + min;
	}
}


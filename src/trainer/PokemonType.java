package trainer;

import java.util.Random;

public enum PokemonType {
	
	DITTO,
	ABRA,
	PICHU,
	CELEBI,
	BULBASAUR,
	GASTLY,
	CUBONE,
	EEVEE,
	MEWTWO,
	PSYDUCK,
	ODDISH,
	DIGLETT,
	POLIWAG,
	SQUIRTLE,
	CHARMANDER,
	RATTATA,
	VULPIX,
	GEODUDE,
	SLOWPOKE,
	VOLTORB;
	
	private static final PokemonType[] VALUES = values();
	private static final int SIZE = VALUES.length;
	private static final Random RANDOM = new Random();
	
	public static PokemonType getRandom() {
		return VALUES[RANDOM.nextInt(SIZE)];
	}

}

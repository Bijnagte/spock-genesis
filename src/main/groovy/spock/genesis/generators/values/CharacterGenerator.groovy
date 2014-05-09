package spock.genesis.generators.values

class CharacterGenerator extends RandomElementGenerator<Character> {
	static final String DEFAULT_CHARACTERS = (' '..'~').join()

	CharacterGenerator() {
		super(DEFAULT_CHARACTERS.toList())
	}
	
	CharacterGenerator(List<Character> potentialCharacters) {
		super(potentialCharacters)
	}
	
	CharacterGenerator(String potentialCharacters) {
		super(potentialCharacters.toList())
	}

}

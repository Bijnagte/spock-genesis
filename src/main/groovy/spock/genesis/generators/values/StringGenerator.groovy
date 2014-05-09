package spock.genesis.generators.values

import spock.genesis.generators.InfiniteGenerator

class StringGenerator extends InfiniteGenerator<String> {

	static final int DEFAULT_LENGTH_LIMIT = 1024 * 10
	
	final CharacterGenerator charGenerator
	final WholeNumberGenerator lengthSource
	
	StringGenerator() {
		this.lengthSource = new WholeNumberGenerator(DEFAULT_LENGTH_LIMIT)
		this.charGenerator =  new CharacterGenerator()
	}
	
	StringGenerator(int maxLength) {
		this.lengthSource = new WholeNumberGenerator(maxLength)
		this.charGenerator =  new CharacterGenerator()
	}

	StringGenerator(int minLength, int maxLength) {
		this.lengthSource = new WholeNumberGenerator(minLength, maxLength)
		this.charGenerator =  new CharacterGenerator()
	}
	
	StringGenerator(String potentialCharacters) {
		this.lengthSource = new WholeNumberGenerator(DEFAULT_LENGTH_LIMIT)
		this.charGenerator = new CharacterGenerator(potentialCharacters)
	}
	
	StringGenerator(int maxLength, String potentialCharacters) {
		this.lengthSource = new WholeNumberGenerator(maxLength)
		this.charGenerator = new CharacterGenerator(potentialCharacters)
	}
	
	StringGenerator(int minLength, int maxLength, String potentialCharacters) {
		this.lengthSource = new WholeNumberGenerator(minLength, maxLength)
		this.charGenerator = new CharacterGenerator(potentialCharacters)
	}
	
	StringGenerator(Collection<Character> potentialCharacters) {
		this.lengthSource = new WholeNumberGenerator(DEFAULT_LENGTH_LIMIT)
		this.charGenerator = new CharacterGenerator(potentialCharacters)
	}
	
	StringGenerator(int maxLength, Collection<Character> potentialCharacters) {
		this.lengthSource = new WholeNumberGenerator(maxLength)
		this.charGenerator = new CharacterGenerator(potentialCharacters)
	}
	
	StringGenerator(int minLength, int maxLength, Collection<Character> potentialCharacters) {
		this.lengthSource = new WholeNumberGenerator(minLength, maxLength)
		this.charGenerator = new CharacterGenerator(potentialCharacters)
	}


	@Override
	String next() {
		makeString(lengthSource.next())
	}

	String makeString(int length) {
		def chars = new char[length]
		length.times { index ->
			chars[index] = charGenerator.next()
		}
		chars.toString()
	}
}
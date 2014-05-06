package spock.genesis

class StringGenerator extends RandomGenerator<String> {

	static final int DEFAULT_LENGTH_LIMIT = 1024 * 10
	final int lengthLimit
	final CharacterGenerator charGenerator
	
	StringGenerator() {
		super()
		this.lengthLimit = DEFAULT_LENGTH_LIMIT
		this.charGenerator =  new CharacterGenerator()
	}
	
	StringGenerator(int lengthLimit) {
		super()
		this.lengthLimit = lengthLimit
		this.charGenerator =  new CharacterGenerator()
	}

	StringGenerator(String potentialCharacters) {
		super()
		this.lengthLimit = DEFAULT_LENGTH_LIMIT
		this.charGenerator = new CharacterGenerator(potentialCharacters)
	}
	
	StringGenerator(Collection<Character> potentialCharacters) {
		super()
		this.lengthLimit = DEFAULT_LENGTH_LIMIT
		this.charGenerator = new CharacterGenerator(potentialCharacters)
	}

	StringGenerator(int lengthLimit, String potentialCharacters) {
		super()
		this.lengthLimit =  lengthLimit
		this.charGenerator = new CharacterGenerator(potentialCharacters)
	}
	
	StringGenerator(int lengthLimit, Collection<Character> potentialCharacters) {
		super()
		this.lengthLimit =  lengthLimit
		this.charGenerator = new CharacterGenerator(potentialCharacters)
	}

	@Override
	String next() {
		if (lengthLimit > 0) {
			makeString(random.nextInt(lengthLimit))
		} else {
			''
		}
	}

	String makeString(int length) {
		def chars = new char[length]
		length.times { index ->
			chars[index] = charGenerator.next()
		}
		chars.toString()
	}
}
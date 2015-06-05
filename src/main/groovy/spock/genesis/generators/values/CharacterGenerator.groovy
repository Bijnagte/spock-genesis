package spock.genesis.generators.values

class CharacterGenerator extends RandomElementGenerator<Character> {
    static final String DEFAULT_CHARACTERS = (' '..'~').join()

    CharacterGenerator() {
        super(convertToCharacters(DEFAULT_CHARACTERS))
    }

    CharacterGenerator(List<Character> potentialCharacters) {
        super(convertToCharacters(potentialCharacters))
    }

    CharacterGenerator(String potentialCharacters) {
        super(convertToCharacters(potentialCharacters))
    }

    static List<Character> convertToCharacters(chars) {
        chars.collect { it as char }
    }
}

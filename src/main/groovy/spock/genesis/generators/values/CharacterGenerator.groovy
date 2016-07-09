package spock.genesis.generators.values

import groovy.transform.CompileStatic

@CompileStatic
class CharacterGenerator extends RandomElementGenerator<Character> {
    static final String DEFAULT_CHARACTERS = (' '..'~').join('')

    CharacterGenerator() {
        super(DEFAULT_CHARACTERS.collect { it as char })
    }

    CharacterGenerator(Collection<Character> potentialCharacters) {
        super(potentialCharacters)
    }

    CharacterGenerator(String potentialCharacters) {
        super(potentialCharacters.collect { it as char })
    }
}

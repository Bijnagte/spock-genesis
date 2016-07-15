package spock.genesis.generators.values

import com.mifmif.common.regex.Generex
import groovy.transform.CompileStatic
import spock.genesis.generators.InfiniteGenerator
import spock.genesis.generators.InfiniteIterator

import java.util.regex.Pattern

/**
 * lazy infinite {@link java.lang.String} generator
 */
@CompileStatic
class StringGenerator extends InfiniteGenerator<String> {

    static final int DEFAULT_LENGTH_LIMIT = 1024

    private final CharacterGenerator charGenerator
    private final WholeNumberGenerator lengthSource
    private final Generex generex

    StringGenerator() {
        this.lengthSource = new WholeNumberGenerator(DEFAULT_LENGTH_LIMIT)
        this.charGenerator = new CharacterGenerator()
    }

    StringGenerator(int maxLength) {
        this.lengthSource = new WholeNumberGenerator(maxLength)
        this.charGenerator = new CharacterGenerator()
    }

    StringGenerator(int minLength, int maxLength) {
        this.lengthSource = new WholeNumberGenerator(minLength, maxLength)
        this.charGenerator = new CharacterGenerator()
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

    StringGenerator(Pattern regex) {
        generex = new Generex(regex.pattern())
    }

    InfiniteIterator<String> iterator() {
        new InfiniteIterator<String>() {
            private final InfiniteIterator<Character> charIterator = charGenerator?.iterator()
            private final InfiniteIterator<Integer> length = lengthSource?.iterator()

            @Override
            String next() {
                if (charIterator) {
                    makeString(length.next())
                } else {
                    generex.random()
                }
            }

            private String makeString(int length) {
                def sb = new StringBuffer(length)
                for (int i = 0; i < length; i++) {
                    sb.append(charIterator.next())
                }
                sb.toString()
            }
        }
    }

    @Override
    StringGenerator seed(Long seed) {
        super.seed(seed)
        charGenerator?.seed(seed)
        lengthSource?.seed(seed)
        generex?.setSeed(seed)
        this
    }
}

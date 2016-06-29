package spock.genesis.generators.values

import groovy.transform.CompileStatic
import spock.genesis.generators.InfiniteGenerator
import spock.genesis.generators.InfiniteIterator

@CompileStatic
class ByteArrayGenerator extends InfiniteGenerator<byte[]> {

    static final int DEFAULT_LENGTH_LIMIT = 1024 * 10
    final WholeNumberGenerator lengthSource
    final Random random = new Random()

    ByteArrayGenerator() {
        this.lengthSource = new WholeNumberGenerator(DEFAULT_LENGTH_LIMIT)
    }

    ByteArrayGenerator(int maxLength) {
        this.lengthSource = new WholeNumberGenerator(maxLength)
    }

    ByteArrayGenerator(int minLength, int maxLength) {
        this.lengthSource = new WholeNumberGenerator(minLength, maxLength)
    }

    ByteArrayGenerator(IntRange range) {
        this.lengthSource = new WholeNumberGenerator(range)
    }

    @Override
    InfiniteIterator<byte[]> iterator() {
        new InfiniteIterator<byte[]>() {
            private final InfiniteIterator<Integer> length = lengthSource.iterator()
            @Override
            byte[] next() {
                byte[] bytes = new byte[length.next()]
                random.nextBytes(bytes)
                bytes
            }
        }
    }
}

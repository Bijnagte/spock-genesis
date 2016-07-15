package spock.genesis.generators.composites

import groovy.transform.CompileStatic
import spock.genesis.generators.Generator
import spock.genesis.generators.UnmodifiableIterator
import spock.genesis.generators.values.WholeNumberGenerator

@CompileStatic
class ListGenerator<E> extends Generator<List<E>> {

    static final int DEFAULT_LENGTH_LIMIT = 1000

    private final Generator<E> valueSource
    private final WholeNumberGenerator lengthSource

    ListGenerator(Generator<E> generator) {
        this.valueSource = generator
        this.lengthSource = new WholeNumberGenerator(DEFAULT_LENGTH_LIMIT)
    }

    ListGenerator(Generator<E> generator, int maxLength) {
        this.valueSource = generator
        this.lengthSource = new WholeNumberGenerator(maxLength)
    }

    ListGenerator(Generator<E> generator, int minLength, int maxLength) {
        this.valueSource = generator
        this.lengthSource = new WholeNumberGenerator(minLength, maxLength)
    }

    ListGenerator(Generator<E> generator, IntRange range) {
        this.valueSource = generator
        this.lengthSource = new WholeNumberGenerator(range)
    }

    @Override
    UnmodifiableIterator<List<E>> iterator() {
        new UnmodifiableIterator<List<E>>() {
            private final Iterator<E> source = valueSource.iterator()
            private final Iterator<Integer> length = lengthSource.iterator()

            @Override
            boolean hasNext() {
                source.hasNext()
            }

            @Override
            List<E> next() {
                Integer size = length.next()
                source.take(size).toList()
            }
        }
    }

    @Override
    ListGenerator<E> seed(Long seed) {
        super.seed(seed)
        lengthSource.seed(seed)
        valueSource.seed(seed)
        this
    }
}

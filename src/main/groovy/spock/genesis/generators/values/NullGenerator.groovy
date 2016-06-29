package spock.genesis.generators.values

import spock.genesis.generators.InfiniteIterator

class NullGenerator<E> extends ValueGenerator<E> {

    NullGenerator() {
        super(null)
    }

    InfiniteIterator<E> iterator() {
        new InfiniteIterator() {
            @Override
            E next() {
                null
            }
        }
    }
}

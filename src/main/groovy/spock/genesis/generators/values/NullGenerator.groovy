package spock.genesis.generators.values

import groovy.transform.CompileStatic
import spock.genesis.generators.InfiniteIterator

@CompileStatic
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

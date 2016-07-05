package spock.genesis.generators

import groovy.transform.CompileStatic

@CompileStatic
abstract class UnmodifiableIterator<E> implements Iterator<E> {
    abstract boolean hasNext()
    abstract E next()

    @Override
    void remove() {
        throw new UnsupportedOperationException()
    }
}

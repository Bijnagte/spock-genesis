package spock.genesis.generators

import groovy.transform.CompileStatic

@CompileStatic
abstract class InfiniteIterator<E> extends UnmodifiableIterator<E> {
    boolean hasNext() {
        true
    }
    abstract E next()
}

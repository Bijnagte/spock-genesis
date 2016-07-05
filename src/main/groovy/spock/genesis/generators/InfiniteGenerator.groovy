package spock.genesis.generators

import groovy.transform.CompileStatic

/**
 * A generator whose iterator is infinite.
 *
 * @param < E >   the generated type
 */
@CompileStatic
abstract class InfiniteGenerator<E> extends Generator<E> {

    abstract InfiniteIterator<E> iterator()

    boolean isFinite() {
        false
    }
}

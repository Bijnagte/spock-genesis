package spock.genesis.generators

import groovy.transform.CompileStatic

/**
 * A infinite generator that returns the results of invoking a Closure
 * @param < T >   the generated type
 */
@CompileStatic
class FactoryGenerator<T> extends InfiniteGenerator<T> {

    final Closure<T> factory

    FactoryGenerator(Closure<T> factory) {
        this.factory = factory
    }

    @Override
    InfiniteIterator<T> iterator() {
        new InfiniteIterator<T>() {
            @Override
            T next() {
                factory.call()
            }
        }
    }
}

package spock.genesis.generators

import groovy.transform.CompileStatic
import spock.genesis.extension.ExtensionMethods

@CompileStatic
class LimitedGenerator<E> extends GeneratorDecorator<E> {

    final int iterationLimit

    LimitedGenerator(Iterable<E> iterable, int iterationLimit) {
        super(ExtensionMethods.toGenerator(iterable))
        this.iterationLimit = iterationLimit
    }

    UnmodifiableIterator<E> iterator() {
        final Iterator ITERATOR = super.iterator()
        new UnmodifiableIterator<E>() {
            private int iteration = 0

            @Override
            boolean hasNext() {
                ITERATOR.hasNext() && iteration < iterationLimit
            }

            @Override
            E next() {
                iteration++
                ITERATOR.next()
            }
        }
    }

    @Override
    boolean isFinite() {
        true
    }
}

package spock.genesis.generators

import groovy.transform.CompileStatic

@CompileStatic
class LimitedGenerator<E> extends GeneratorDecorator<E> {

    final int iterationLimit

    LimitedGenerator(Iterable<E> iterable, int iterationLimit) {
        super(iterable)
        this.iterationLimit = iterationLimit
    }

    LimitedGenerator(Collection<E> collection) {
        super(collection)
        this.iterationLimit = collection.size()
    }

    LimitedGenerator(E... array) {
        super(Arrays.asList(array))
        this.iterationLimit = array.length
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

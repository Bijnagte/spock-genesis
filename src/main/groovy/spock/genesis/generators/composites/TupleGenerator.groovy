package spock.genesis.generators.composites

import spock.genesis.generators.Generator

class TupleGenerator<T> extends Generator<List<T>> {

    final List<Iterator<T>> iterators
    final boolean finite

    TupleGenerator(List<Iterator<T>> iterators) {
        this.iterators = iterators.asImmutable()
        finite = iterators.every { it.respondsTo('isFinite') && it.finite }
    }

    TupleGenerator(Iterator<T>... iterators) {
        this(iterators.toList())
    }

    @Override
    boolean hasNext() {
        iterators.every { it.hasNext() }
    }

    @Override
    List<T> next() {
        iterators*.next()
    }
}

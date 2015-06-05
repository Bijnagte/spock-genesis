package spock.genesis.generators.composites

import spock.genesis.generators.Generator

class TupleGenerator<T> extends Generator<List<T>> {

    final List<Iterator<T>> iterators

    TupleGenerator(List<Iterator<T>> iterators) {
        this.iterators = iterators
    }

    TupleGenerator(Iterator<T>... iterators) {
        this.iterators = iterators.toList()
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

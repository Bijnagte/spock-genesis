package spock.genesis.generators.composites

import spock.genesis.generators.Generator
import spock.genesis.generators.GeneratorUtils

class TupleGenerator<T> extends Generator<List<T>> {

    final List<Iterator<T>> iterators

    TupleGenerator(List<Iterator<T>> iterators) {
        this.iterators = iterators.asImmutable()
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

    @Override
    boolean isFinite() {
        GeneratorUtils.anyFinite(iterators)
    }
}

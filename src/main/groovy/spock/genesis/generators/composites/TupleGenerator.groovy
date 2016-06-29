package spock.genesis.generators.composites

import spock.genesis.generators.Generator
import spock.genesis.generators.GeneratorUtils
import spock.genesis.generators.UnmodifiableIterator

class TupleGenerator<T> extends Generator<List<T>> {

    private final List<Generator<T>> generators

    TupleGenerator(List<Iterable<T>> iterables) {
        this.generators = iterables.collect { it.toGenerator() }.asImmutable()
    }

    TupleGenerator(Iterable<T>... iterables) {
        this(iterables.toList())
    }

    UnmodifiableIterator<List<T>> iterator() {
        new UnmodifiableIterator<List<T>>() {
            List<Iterator> iterators = generators*.iterator()
            @Override
            boolean hasNext() {
                iterators.every { it.hasNext() }
            }

            @Override
            List<T> next() {
                iterators*.next()
            }
        }
    }

    @Override
    boolean isFinite() {
        GeneratorUtils.anyFinite(iterators)
    }
}

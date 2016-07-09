package spock.genesis.generators.composites

import groovy.transform.CompileStatic
import spock.genesis.extension.ExtensionMethods
import spock.genesis.generators.Generator
import spock.genesis.generators.GeneratorUtils
import spock.genesis.generators.UnmodifiableIterator

@CompileStatic
class TupleGenerator<T> extends Generator<List<T>> {

    private final List<Generator<T>> generators

    TupleGenerator(List<Iterable<T>> iterables) {
        List<Generator<T>> collector = []
        iterables.collect(collector) { ExtensionMethods.toGenerator(it) }
        this.generators = collector.asImmutable()
    }

    TupleGenerator(Iterable<T>... iterables) {
        this(iterables.toList())
    }

    UnmodifiableIterator<List<T>> iterator() {
        new UnmodifiableIterator<List<T>>() {
            List<UnmodifiableIterator> iterators = generators*.iterator()

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
        GeneratorUtils.anyFinite(generators)
    }
}

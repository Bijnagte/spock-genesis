package spock.genesis.generators

import spock.genesis.generators.values.NullGenerator

/**
 * An Iterator that generates a type (usually lazily)
 * @param < E >   the generated type
 */
@SuppressWarnings('SpaceAroundMapEntryColon')
abstract class Generator<E> implements Iterable<E> {

    /**
     * Wraps this generator in a generator that returns values that matches the supplied predicate
     * @param predicate
     * @return a FilteredGenerator
     */
    FilteredGenerator<E> filter(Closure predicate) {
        new FilteredGenerator<E>(this, predicate)
    }

    TransformingGenerator<E, ?> map(Closure<?> transform) {
        new TransformingGenerator<E, ?>(this, transform)
    }

    TransformingGenerator<E, E> with(Closure<?> transform) {
        Closure withClosure = { generatedValue ->
            generatedValue.with(transform)
            generatedValue
        }
        new TransformingGenerator<E, E>(this, withClosure)
    }

    LimitedGenerator<E> take(int qty) {
        new LimitedGenerator<E>(this, qty)
    }

    SequentialMultisourceGenerator<E> then(Iterable<E>... iterables) {
        new SequentialMultisourceGenerator<E>(this, *iterables)
    }

    CyclicGenerator<E> repeat() {
        new CyclicGenerator<E>(this)
    }

    LimitedGenerator<E> multiply(int qty) {
        take(qty)
    }

    CyclicGenerator<E> getRepeat() {
        repeat()
    }

    MultiSourceGenerator<E> getWithNulls() {
        withNulls(100)
    }

    /**Wraps this generator in a {@link spock.genesis.generators.MultiSourceGenerator} that randomly returns nulls
     * @param resultsPerNull the average number of results from this generator per null result
     * @return {@link spock.genesis.generators.MultiSourceGenerator}
     */
    MultiSourceGenerator<E> withNulls(int resultsPerNull) {
        Map weightedGenerators = [(this): resultsPerNull, (new NullGenerator<E>()): 1]
        new MultiSourceGenerator<E>(weightedGenerators)
    }

    MultiSourceGenerator and(Iterable iterator) {
        if (MultiSourceGenerator.isAssignableFrom(this.getClass())) {
            MultiSourceGenerator gen = this
            new MultiSourceGenerator(gen.iterables + iterator)
        } else {
            new MultiSourceGenerator([this, iterator])
        }
    }

    abstract UnmodifiableIterator<E> iterator()

    /**
     * If false then the generator may still terminate when iterated
     * @return true if the Generator will terminate
     */
    abstract boolean isFinite()

    List<E> getRealized() {
        this.collect()
    }
}

package spock.genesis.generators

import groovy.transform.CompileStatic
import spock.genesis.extension.ExtensionMethods

/**
 * A generator that returns the next value from one of its source generators at random.
 * @param < E >   the generated type
 */
@CompileStatic
class MultiSourceGenerator<E> extends Generator<E> implements Closeable {

    private final List<Generator<E>> generators

    MultiSourceGenerator(Collection<Iterable<E>> iterables) {
        this.generators = iterables.collect { ExtensionMethods.toGenerator(it) }
    }

    MultiSourceGenerator(Map<Iterable<E>, Integer> weightedIterators) {
        List i = []
        weightedIterators.each { iterable, qty ->
            def generator = ExtensionMethods.toGenerator(iterable)
            qty.times { i << generator }
        }
        this.generators = i
    }

    UnmodifiableIterator<E> iterator() {
        new UnmodifiableIterator<E>() {
            private final List<UnmodifiableIterator<E>> iterators = generators*.iterator()
            /**
             * @return true if any of the source generators has next
             */
            @Override
            boolean hasNext() {
                iterators.any { it.hasNext() }
            }

            /** generates a value from one of the source generators
             * @return the generated value
             */
            @Override
            E next() {
                boolean search = hasNext()

                while (search) {
                    int i = random.nextInt(iterators.size())
                    def generator = iterators[i]
                    if (generator.hasNext()) {
                        return generator.next()
                    }
                }
            }
        }
    }

    @SuppressWarnings('ExplicitCallToPlusMethod')
    MultiSourceGenerator plus(Iterable additional) {
        plus([additional])
    }

    /**
     * Adds additional source generators to this generators source generators.
     * This method does not mutate but does reuse the source generators.
     * @param additional
     * @return a new MultiSourceGenerator
     */
    MultiSourceGenerator<E> plus(Collection<Iterable<E>> additional) {
        new MultiSourceGenerator(additional + generators)
    }

    void close() {
        generators.each { it.close() }
    }

    boolean isFinite() {
        generators.every { it.finite }
    }

    @Override
    MultiSourceGenerator<E> seed(Long seed) {
        generators.each { it.seed(seed) }
        super.seed(seed)
        this
    }
}

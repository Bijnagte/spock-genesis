package spock.genesis.generators

import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic

/**
 * A generator that returns the next value from one of its source generators at random.
 * @param < E >   the generated type
 */
@CompileStatic
class MultiSourceGenerator<E> extends Generator<E> implements Closeable {

    final List<Iterable<E>> iterables
    final Random random = new Random()

    MultiSourceGenerator(Collection<Iterable<E>> iterables) {
        this.iterables = iterables.toList()
    }

    MultiSourceGenerator(Map<Iterable<E>, Integer> weightedIterators) {
        List i = []
        weightedIterators.each { iterable, qty ->
            qty.times { i << iterable }
        }
        this.iterables = i
    }

    UnmodifiableIterator<E> iterator() {
        new UnmodifiableIterator<E>() {
            private final List<Iterator<E>> iterators = iterables*.iterator()
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
    MultiSourceGenerator plus(Collection<Iterable<E>> additional) {
        new MultiSourceGenerator(additional + iterables)
    }

    void close() {
        iterables.each { close(it) }
    }

    @CompileDynamic
    void close(Iterable generator) {
        if (generator.respondsTo('close')) {
            generator.close()
        }
    }

    boolean isFinite() {
        GeneratorUtils.allFinite(iterables)
    }
}

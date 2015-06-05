package spock.genesis.generators

/**
 * A generator that returns the next value from one of its source generators at random.
 * @param < E >   the generated type
 */
class MultiSourceGenerator<E> extends Generator<E> implements Closeable {

    final List<Iterator<E>> iterators
    final Random random = new Random()

    MultiSourceGenerator(Collection<Iterator<E>> iterators) {
        this.iterators = iterators.toList()
    }

    MultiSourceGenerator(Map<Iterator<E>, Integer> weightedIterators) {
        List i = []
        weightedIterators.each { iterator, qty ->
            qty.times { i << iterator }
        }
        this.iterators = i
    }

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

    @SuppressWarnings('ExplicitCallToPlusMethod')
    MultiSourceGenerator plus(Iterator additional) {
        plus([additional])
    }

    /**
     * Adds additional source generators to this generators source generators.
     * This method does not mutate but does reuse the source generators.
     * @param additional
     * @return a new MultiSourceGenerator
     */
    MultiSourceGenerator plus(Collection<Iterator> additional) {
        new MultiSourceGenerator(additional + iterators)
    }

    void close() {
        iterators.each { close(it) }
    }

    void close(Iterator generator) {
        if (generator.respondsTo('close')) {
            generator.close()
        }
    }
}

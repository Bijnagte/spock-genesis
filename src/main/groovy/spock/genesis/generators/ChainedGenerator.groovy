package spock.genesis.generators

class ChainedGenerator<E, T> extends GeneratorDecorator<E> implements Closeable {

    private Closure<Generator<E>> transform
    private Map<T, Generator<E>> chained = new HashMap()

    ChainedGenerator(Generator<T> first, Closure<Generator<E>> transform) {
        super(first)
        this.transform = transform
    }

    UnmodifiableIterator<E> iterator() {
        final Iterator<T> selector = generator.iterator()

        new UnmodifiableIterator<E>() {
            /**
             * @return true if any of the source generators has next
             */
            @Override
            boolean hasNext() {
                return selector.hasNext()
            }

            /** generates a value from one of the source generators
             * @return the generated value
             */
            @Override
            E next() {
                final Iterator<E> iterator = chained.computeIfAbsent(selector.next(), { transform(it).iterator() })
                return iterator.next()
            }
        }
    }

    void close() {
        super.close()
        chained.values().each { it.close() }
    }
}

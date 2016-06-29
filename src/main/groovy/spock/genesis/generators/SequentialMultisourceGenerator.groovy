package spock.genesis.generators

class SequentialMultisourceGenerator<E> extends Generator<E> implements Closeable {

    private final List<Iterable<E>> iterables
    final boolean finite

    SequentialMultisourceGenerator(Iterable<E>... iterables) {
        this.iterables = iterables.toList()
        finite = GeneratorUtils.allFinite(iterables)
    }

    UnmodifiableIterator<E> iterator() {
        new UnmodifiableIterator<E>() {
            private Iterator<E> current
            private final Iterator<Iterator<E>> iterators = iterables.collect { it.iterator() }.iterator()

            boolean hasNext() {
                setupIterator()
                current.hasNext()
            }

            private void setupIterator() {
                while (!current?.hasNext() && iterators.hasNext()) {
                    current = iterators.next()
                }
            }

            E next() {
                setupIterator()
                current.next()
            }
        }
    }

    void close() {
        iterables.each { close(it) }
    }

    void close(Iterable generator) {
        if (generator.respondsTo('close')) {
            generator.close()
        }
    }
}

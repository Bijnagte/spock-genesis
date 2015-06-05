package spock.genesis.generators

class SequentialMultisourceGenerator<E> extends Generator<E> implements Closeable {

    final Iterator<Iterator<E>> iterators
    private Iterator<E> current

    SequentialMultisourceGenerator(Iterator<E>... iterators) {
        this.iterators = iterators.iterator()
    }

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

    void close() {
        iterators.each { close(it) }
    }

    void close(Iterator generator) {
        if (generator.respondsTo('close')) {
            generator.close()
        }
    }
}

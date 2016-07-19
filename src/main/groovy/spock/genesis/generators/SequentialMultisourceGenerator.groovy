package spock.genesis.generators

import groovy.transform.CompileStatic

@CompileStatic
class SequentialMultisourceGenerator<E> extends Generator<E> implements Closeable {

    private final List<Generator<E>> generators

    SequentialMultisourceGenerator(Generator<E>... iterables) {
        this.generators = iterables.toList()
    }

    UnmodifiableIterator<E> iterator() {
        new UnmodifiableIterator<E>() {
            private Iterator current
            private final Iterator iterators = generators.collect { it.iterator() }.iterator()

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
        generators.each { it.close() }
    }

    boolean isFinite() {
        generators.every { it.finite }
    }

    SequentialMultisourceGenerator<E> seed(Long seed) {
        generators.each { it.seed(seed) }
        super.seed(seed)
        this
    }
}

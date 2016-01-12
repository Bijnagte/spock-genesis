package spock.genesis.generators

/**
 * A generator that wraps an iterator to provide {@link Generator} methods.
 * @param < E >   the generated type
 */
class GeneratorDecorator<E> extends Generator<E> implements Closeable {
    protected Iterator<E> generator
    final boolean finite

    GeneratorDecorator(Iterator<E> generator, boolean finite = false) {
        this.generator = generator
        this.finite =  !generator.hasNext() || finite || (generator.respondsTo('isFinite') && generator.finite)
    }

    boolean hasNext() {
        generator.hasNext()
    }

    @Override
    E next() {
        generator.next()
    }

    void close() {
        if (generator.respondsTo('close')) {
            generator.close()
        }
    }

    boolean isFinite() {
        this.finite
    }
}

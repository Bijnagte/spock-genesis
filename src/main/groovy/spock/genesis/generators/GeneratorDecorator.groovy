package spock.genesis.generators

/**
 * A generator that wraps an iterator to provide {@link Generator} methods.
 * @param < E >   the generated type
 */
class GeneratorDecorator<E> extends Generator<E> implements Closeable {
    protected Iterator<E> generator

    GeneratorDecorator(Iterator<E> generator) {
        this.generator = generator
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
}

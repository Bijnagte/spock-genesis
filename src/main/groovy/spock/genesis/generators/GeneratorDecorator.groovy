package spock.genesis.generators

/**
 * A generator that wraps an iterator to provide {@link Generator} methods.
 * @param < E >   the generated type
 */
class GeneratorDecorator<E> extends Generator<E> implements Closeable {
    protected Iterator<E> generator
    final boolean finiteOverride

    GeneratorDecorator(Iterator<E> generator, boolean finite = false) {
        this.generator = generator
        this.finiteOverride = finite
    }

    boolean hasNext() {
        generator.hasNext()
    }

    @Override
    E next() {
        generator.next()
    }

    @Override
    boolean isFinite() {
        finiteOverride || GeneratorUtils.isFinite(generator)
    }

    void close() {
        if (generator.respondsTo('close')) {
            generator.close()
        }
    }


}

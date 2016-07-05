package spock.genesis.generators

/**
 * A generator that wraps an iterator to provide {@link Generator} methods.
 * @param < E >   the generated type
 */
class GeneratorDecorator<E> extends Generator<E> implements Closeable {
    protected Iterable<E> generator
    final boolean finiteOverride

    GeneratorDecorator(Iterable<E> iterable) {
        this.generator = iterable
        this.finiteOverride = false
    }

    GeneratorDecorator(Iterable<E> iterable, boolean finite) {
        this.generator = iterable
        this.finiteOverride = finite
    }

    @Override
    UnmodifiableIterator<E> iterator() {
        new UnmodifiableIterator<E>() {
            final private Iterator iterator = generator.iterator()

            @Override
            boolean hasNext() {
                iterator.hasNext()
            }

            @Override
            E next() {
                iterator.next()
            }
        }
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

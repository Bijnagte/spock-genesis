package spock.genesis.generators

import groovy.transform.CompileStatic

/**
 * Base {@link Generator} class for functionality that modifies a stream from the wrapped generator.
 * @param < E >   the generated type
 */
@CompileStatic
class GeneratorDecorator<E> extends Generator<E> implements Closeable {
    protected Generator<E> generator
    final boolean finiteOverride

    GeneratorDecorator(Generator<E> generator) {
        this.generator = generator
        this.finiteOverride = generator.finite
    }

    GeneratorDecorator(Generator<E> generator, boolean finite) {
        this.generator = generator
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
        finiteOverride || !generator.iterator().hasNext()
    }

    void close() {
        generator.close()
    }
}

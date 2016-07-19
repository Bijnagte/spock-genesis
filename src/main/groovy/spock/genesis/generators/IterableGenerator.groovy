package spock.genesis.generators

import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic

/**
 * A generator that wraps an {@link Iterable} to provide {@link Generator} methods.
 * @param < E >   the generated type
 */
@CompileStatic
class IterableGenerator<E> extends Generator<E> implements Closeable {

    private final Iterable<E> iterable
    private final boolean finite

    IterableGenerator(Iterable<E> iterable) {
        this.iterable = iterable
        this.finite = Generator.isInstance(iterable) ? false : true
    }

    IterableGenerator(Iterable<E> iterable, boolean finite) {
        this.iterable = iterable
        this.finite = finite
    }

    IterableGenerator(E... array) {
        this.iterable = Arrays.asList(array)
        this.finite = true
    }

    @Override
    UnmodifiableIterator<E> iterator() {
        new UnmodifiableIterator<E>() {
            private final Iterator<E> iterator = iterable.iterator()
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
        finite || GeneratorUtils.isFinite(iterable)
    }

    @CompileDynamic
    @Override
    void close() {
        if (iterable.respondsTo('close')) {
            iterable.close()
        }
    }

    @CompileDynamic
    @Override
    IterableGenerator<E> seed(Long seed) {
        if (iterable.respondsTo('seed')) {
            iterable.seed(seed)
        }
        this
    }
}

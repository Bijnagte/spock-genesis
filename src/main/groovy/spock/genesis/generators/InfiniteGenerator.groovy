package spock.genesis.generators

/**
 * A generator where {@link #hasNext()} always returns true.
 *
 * @param < E >   the generated type
 */
abstract class InfiniteGenerator<E> extends Generator<E> {

    boolean hasNext() {
        true
    }

    abstract E next()
}

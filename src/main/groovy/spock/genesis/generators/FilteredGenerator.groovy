package spock.genesis.generators

/**
 * A lazy generator that returns the next value from the wrapped iterator that satisfies a predicate Closure.
 * @param < E >   the generated type
 */
class FilteredGenerator<E> extends GeneratorDecorator<E> {

    private final Closure predicate

    FilteredGenerator(Iterable<E> iterable, Closure predicate) {
        super(iterable)
        this.predicate = predicate
    }

    UnmodifiableIterator<E> iterator() {
        new UnmodifiableIterator<E>() {
            private E nextVal
            boolean hasNext() {
                if (nextVal == null) {
                    nextVal = findNext()
                }
                nextVal != null
            }

            E next() {
                if (nextVal == null) {
                    findNext()
                } else {
                    E val = nextVal
                    nextVal = null
                    val
                }
            }

            private E findNext() {
                generator.find(predicate)
            }
        }
    }
}

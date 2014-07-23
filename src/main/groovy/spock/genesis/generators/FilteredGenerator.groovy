package spock.genesis.generators

/**
 * A lazy generator that returns the next value from the wrapped iterator that satisfies a predicate Closure.
 * @param <E> the generated type
 */
class FilteredGenerator<E> extends GeneratorDecorator<E> {

	private final Closure predicate
	private E nextVal

	FilteredGenerator(Iterator<E> iterator, Closure predicate) {
		super(iterator)
		this.predicate = predicate
	}

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
			def val = nextVal
			nextVal = null
			val
		}
	}

	E findNext() {
		generator.find(predicate)
	}
}

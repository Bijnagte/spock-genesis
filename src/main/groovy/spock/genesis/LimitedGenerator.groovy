package spock.genesis


class LimitedGenerator<E> extends Generator<E> {
	final int iterationLimit
	private int iteration = 0
	private final Iterator<E> iterator

	LimitedGenerator( Iterator<E> iterator, int iterationLimit) {
		this.iterationLimit = iterationLimit
		this.iterator = iterator
	}

	@Override
	boolean hasNext() {
		iterator.hasNext() && iteration < iterationLimit
	}

	@Override
	E next() {
		iteration ++
		iterator.next()
	}
}

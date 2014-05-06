package spock.genesis

class NullGenerator<E> extends Generator<E> {

	@Override
	boolean hasNext() {
		true
	}

	@Override
	E next() {
		null
	}
}

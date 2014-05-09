package spock.genesis.generators

abstract class InfiniteGenerator<E> extends Generator<E> {

	boolean hasNext() {
		true
	}
	
	abstract E next()
}

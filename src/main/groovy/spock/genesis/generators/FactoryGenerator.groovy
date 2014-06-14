package spock.genesis.generators

class FactoryGenerator<T> extends InfiniteGenerator<T> {

	final Closure<T> factory

	FactoryGenerator(Closure<T> factory) {
		this.factory = factory
	}
	@Override
	T next() {
		factory.call()
	}
}

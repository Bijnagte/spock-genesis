package spock.genesis.generators

/**
 * A infinite generator that returns the results of invoking a Closure
 * @param <T> the generated type
 */
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

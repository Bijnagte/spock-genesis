package spock.genesis.generators.values

import spock.genesis.generators.InfiniteGenerator

class RandomElementGenerator<E> extends InfiniteGenerator<E> {

	final List<E> elementSource
	final WholeNumberGenerator indexSource

	RandomElementGenerator(Collection<E> elementSource) {
		this.elementSource = elementSource.toList().asImmutable()
		this.indexSource = new WholeNumberGenerator(this.elementSource.size() - 1)
	}
	
	@Override
	E next() {
		elementSource[indexSource.next()]
	}
}

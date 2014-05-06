package spock.genesis

abstract class RandomGenerator<E> extends Generator<E> {

	final Random random

	RandomGenerator() {
		this.random = new Random()
	}

	RandomGenerator(Random random) {
		this.random = random
	}

	@Override
	boolean hasNext() {
		true
	}
}

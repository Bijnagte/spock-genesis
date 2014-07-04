package spock.genesis.generators.values

import spock.genesis.generators.InfiniteGenerator

class WholeNumberGenerator extends InfiniteGenerator<Integer> {

	final Random random = new Random()
	final int min
	final int magnitude

	WholeNumberGenerator(int max = Integer.MAX_VALUE) {
		this(0, max)
	}

	WholeNumberGenerator(int min, int max) {
		assert min >= 0
		assert max >= min
		this.min = min
		this.magnitude = max - min + 1
	}

	WholeNumberGenerator(IntRange range) {
		this(range.from, range.to)
	}

	@Override
	Integer next() {
		min + random.nextInt(magnitude)
	}
}

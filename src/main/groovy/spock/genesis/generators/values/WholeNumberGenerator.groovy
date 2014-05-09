package spock.genesis.generators.values

import spock.genesis.generators.InfiniteGenerator

class WholeNumberGenerator extends InfiniteGenerator<Integer> {

	final Random random = new Random()
	final int min
	final int magnitude

	WholeNumberGenerator() {
		this.min = 0
		this.magnitude = Integer.MAX_VALUE
	}

	WholeNumberGenerator(int max) {
		assert max >= 0
		this.min = 0
		this.magnitude = max + 1
	}

	WholeNumberGenerator(int min, int max) {
		assert min >= 0
		assert max >= min
		this.min = min
		this.magnitude = max - min + 1
	}

	WholeNumberGenerator(IntRange range) {
		assert range
		assert range.from >= 0
		assert range.to > range.from
		this.min = range.from
		this.range = range.to - min
	}

	@Override
	Integer next() {
		min + random.nextInt(magnitude)
	}
}

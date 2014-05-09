package spock.genesis.generators.values

import spock.genesis.generators.InfiniteGenerator

class IntegerGenerator extends InfiniteGenerator<Integer> {

	final long min
	final long max
	final Random random = new Random()

	IntegerGenerator() {
		this.min = Integer.MIN_VALUE
		this.max = Integer.MAX_VALUE
	}
	
	IntegerGenerator(int min, int max) {
		assert min < max
		this.min = min
		this.max = max
	}
	
	IntegerGenerator(IntRange range) {
		this.min = range.from
		this.max = range.to
	}

	@Override
	Integer next() {
		long magnitude = max - min
		if (magnitude <= Integer.MAX_VALUE) {
			int val = random.nextInt(magnitude as int)
			val + min
		} else {
			while (true) {
				int val = random.nextInt()
				if (val >= min && val <= max) {
					return val 
				}
			}
		}
	}
}

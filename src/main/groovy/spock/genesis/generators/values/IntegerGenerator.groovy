package spock.genesis.generators.values

import spock.genesis.generators.InfiniteGenerator

class IntegerGenerator extends InfiniteGenerator<Integer> {

	final long min
	final long max
	final Random random = new Random()
	
	IntegerGenerator(int min = Integer.MIN_VALUE, int max = Integer.MAX_VALUE) {
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
		long magnitude = max - min + 1
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

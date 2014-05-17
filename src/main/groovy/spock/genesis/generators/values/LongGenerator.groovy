package spock.genesis.generators.values

import spock.genesis.generators.InfiniteGenerator

class LongGenerator extends InfiniteGenerator<Long> {

	final Random random = new Random()
	
	final BigInteger min
	final BigInteger max
	
	LongGenerator() {
		this.min = Long.MIN_VALUE
		this.max = Long.MAX_VALUE
	}
	
	LongGenerator(long min, long max) {
		assert min < max
		this.min = min
		this.max = max
	}
	
	LongGenerator(IntRange range) {
		this.min = range.from
		this.max = range.to
	}
	
	
	@Override
	Long next() {
		//def magnitude = max - min
		
		while (true) {
			def val = random.nextLong()
			if (val >= min && val <= max) {
				return val
			}
		}
	}
}

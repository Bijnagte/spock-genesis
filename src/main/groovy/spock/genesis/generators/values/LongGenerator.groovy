package spock.genesis.generators.values

import spock.genesis.generators.Generator
import spock.genesis.generators.InfiniteGenerator

class LongGenerator extends InfiniteGenerator<Long> {

	final Generator candidateProvider
	final long min
	final long max

	LongGenerator() {
		this.min = Long.MIN_VALUE
		this.max = Long.MAX_VALUE
		this.candidateProvider = chooseProvider(min, max)
	}

	LongGenerator(long min, long max) {
		assert min < max
		this.min = min
		this.max = max
		this.candidateProvider = chooseProvider(min, max)
	}

	Generator chooseProvider(BigInteger min, BigInteger max) {
		def magnitude = max - min
		if (magnitude <= Integer.MAX_VALUE) {
			new ShiftedIntegerGenerator(magnitude as int, min as long)
		} else {
			new RandomLongGenerator()
		}
	}

	@Override
	Long next() {
		while (true) {
			def val = candidateProvider.next()
			if (val >= min && val <= max) {
				return val
			}
		}
	}

	private class RandomLongGenerator extends InfiniteGenerator<Long> {
		final Random random = new Random()

		Long next() {
			random.nextLong()
		}
	}

	private class ShiftedIntegerGenerator extends InfiniteGenerator<Long> {
		final IntegerGenerator generator
		final long shift

		ShiftedIntegerGenerator(int magnitude, long shift) {
			generator = new IntegerGenerator(0, magnitude)
			this.shift = shift
		}
		Long next() {
			shift + generator.next()
		}
	}
}
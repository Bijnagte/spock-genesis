package spock.genesis.generators.values

import spock.genesis.generators.InfiniteGenerator

class ByteArrayGenerator extends InfiniteGenerator<byte[]> {
	
	static final int DEFAULT_LENGTH_LIMIT = 1024 * 10
	final WholeNumberGenerator lengthSource
	final Random random

	ByteArrayGenerator() {
		this.random = new Random()
		this.lengthSource = new WholeNumberGenerator(DEFAULT_LENGTH_LIMIT)
	}

	ByteArrayGenerator(int maxLength) {
		this.random = new Random()
		this.lengthSource = new WholeNumberGenerator(maxLength)
	}
	
	ByteArrayGenerator(int minLength, int maxLength) {
		this.random = new Random()
		this.lengthSource = new WholeNumberGenerator(minLength, maxLength)
	}
	
	ByteArrayGenerator(IntRange range) {
		this.random = new Random()
		this.lengthSource = new WholeNumberGenerator(range)
	}

	@Override
	byte[] next() {
		def bytes = new byte[lengthSource.next()]
		random.nextBytes(bytes)
		bytes
	}
}

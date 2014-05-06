package spock.genesis

class ByteArrayGenerator extends RandomGenerator<byte[]> {

	static final int DEFAULT_LENGTH_LIMIT = 1024 * 10
	final int lengthLimit

	ByteArrayGenerator() {
		super()
		this.lengthLimit = DEFAULT_LENGTH_LIMIT
	}

	ByteArrayGenerator(int lengthLimit) {
		super()
		this.lengthLimit = lengthLimit
	}

	@Override
	byte[] next() {
		int length = random.nextInt(lengthLimit)
		def bytes = new byte[length]
		random.nextBytes(bytes)
		bytes
	}
}

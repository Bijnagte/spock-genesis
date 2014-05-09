package spock.genesis.generators

class CyclicGenerator<E> extends GeneratorDecorator<E> {
	private final List repeatSource = []
	private boolean hasRepeated = false
	private boolean started = false

	CyclicGenerator(Iterator<E> iterator) {
		super(iterator)
	}

	@Override
	boolean hasNext() {
		if (started) {
			true
		} else {
			generator.hasNext()
		}
	}

	@Override
	E next() {
		started = true
		if (!generator.hasNext()) {
			hasRepeated = true
			super.generator = repeatSource.iterator()
		}
		def val = generator.next()
		if (!hasRepeated) {
			repeatSource << val
		}
		val
	}
}

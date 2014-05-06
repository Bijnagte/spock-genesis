package spock.genesis

class CyclicGenerator<E> extends GeneratorDecorator<E> {
	private final List repeatSource = []
	private boolean hasRepeated = false
	private boolean started = false

	CyclicGenerator(Iterator<E> iterator) {
		super(iterator)
	}

	@Override
	boolean hasNext() {
		if (!started) {
			iterator.hasNext()
		} else {
			true
		}
	}

	@Override
	E next() {
		started = true
		if (!iterator.hasNext()) {
			hasRepeated = true
			super.iterator = repeatSource.iterator()
		}
		def val = iterator.next()
		if (!hasRepeated) {
			repeatSource << val
		}
		val
	}
}

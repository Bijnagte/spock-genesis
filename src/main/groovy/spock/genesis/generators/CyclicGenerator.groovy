package spock.genesis.generators

/**
 * A lazy infinite generator that repeats an iterator.
 * This generator keeps track of 1 iterator worth of data so infinite sources could lead to exesive memory usage.
 * @param <E> the generated type
 */
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
			repeatSource.add(val)
		}
		val
	}
}

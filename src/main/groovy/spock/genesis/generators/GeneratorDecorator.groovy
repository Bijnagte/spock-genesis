package spock.genesis.generators

class GeneratorDecorator<E> extends Generator<E> implements Closeable {
	protected Iterator<E> generator

	GeneratorDecorator(Iterator<E> generator) {
		this.generator = generator
	}

	boolean hasNext() {
		generator.hasNext()
	}

	@Override
	E next() {
		generator.next()
	}
	
	void close() {
		if (generator.respondsTo('close')) {
			generator.close()
		}
	}
}

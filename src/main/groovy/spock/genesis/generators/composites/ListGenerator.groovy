package spock.genesis.generators.composites

import spock.genesis.generators.Generator
import spock.genesis.generators.GeneratorDecorator
import spock.genesis.generators.values.WholeNumberGenerator

class ListGenerator<E> extends GeneratorDecorator<List<E>> {

	static final int DEFAULT_LENGTH_LIMIT = 1000
	
	
	final WholeNumberGenerator lengthSource

	ListGenerator(Generator<E> generator) {
		super(generator)
		this.lengthSource = new WholeNumberGenerator(DEFAULT_LENGTH_LIMIT)
	}
	
	ListGenerator(Generator<E> generator, int maxLength) {
		super(generator)
		this.lengthSource = new WholeNumberGenerator(maxLength)
	}
	
	ListGenerator(Generator<E> generator, int minLength, int maxLength) {
		super(generator)
		this.lengthSource = new WholeNumberGenerator(minLength, maxLength)
	}
	
	ListGenerator(Generator<E> generator, IntRange range) {
		super(generator)
		this.lengthSource = new WholeNumberGenerator(range)
	}

	@Override
	List<E> next() {
		def gen = generator as Generator
		gen.take(lengthSource.next()).realized
	}
}

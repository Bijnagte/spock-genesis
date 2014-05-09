package spock.genesis.generators

import spock.genesis.generators.values.NullGenerator

abstract class Generator<E> implements Iterator<E> {

	FilteredGenerator<E> filter(Closure predicate) {
		new FilteredGenerator<E>(this, predicate)
	}

	TransformingGenerator<E, ?> map(Closure<?> transform) {
		new TransformingGenerator<E, ?>(this, transform)
	}

	LimitedGenerator<E> take(int qty) {
		new LimitedGenerator<E>(this, qty)
	}

	CyclicGenerator<E> repeat() {
		new CyclicGenerator<E>(this)
	}

	CyclicGenerator<E> getRepeat() {
		repeat()
	}

	MultiSourceGenerator<E> getWithNulls() {
		withNulls(100)
	}

	MultiSourceGenerator<E> withNulls(int resultsPerNull) {
		Map weightedGenerators = [(this): resultsPerNull, (new NullGenerator<E>()): 1]
		new MultiSourceGenerator<E>(weightedGenerators)
	}

	abstract boolean hasNext()

	abstract E next()

	@Override
	void remove() {
		throw new UnsupportedOperationException()
	}

	List<E> getRealized() {
		this.collect()
	}
}

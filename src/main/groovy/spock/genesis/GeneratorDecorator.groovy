package spock.genesis

abstract class GeneratorDecorator<E> extends Generator<E> {
	protected Iterator<E> iterator
	
	GeneratorDecorator(Iterator<E> iterator) {
		this.iterator = iterator
	}
	
	boolean hasNext() {
		iterator.hasNext()
	}
	
	void close() {
		if (iterator.respondsTo('close')) {
			iterator.close()
		}
	}
}

package spock.genesis.generators

class MultiSourceGenerator<E> extends Generator<E> implements Closeable {

	final List<Iterator<E>> iterators
	final Random random = new Random()
	
	MultiSourceGenerator(Collection<Iterator<E>> iterators) {
		this.iterators = iterators.toList()
	}
	
	MultiSourceGenerator(Map<Iterator<E>,Integer> weightedIterators) {
		def i = []
		weightedIterators.each { iterator, qty ->
			qty.times {  i << iterator }
		}
		this.iterators = i
	}

	@Override
	boolean hasNext() {
		iterators.any { it.hasNext() }
	}

	@Override
	E next() {
		boolean search = hasNext()
		
		while (search) {
			int i = random.nextInt(iterators.size())
			def generator = iterators[i]
			if (generator.hasNext()) {
				return generator.next()
			}
		}
	}
	
	@SuppressWarnings('ExplicitCallToPlusMethod')
	MultiSourceGenerator plus(Iterator additional) {
		plus([additional])
	}
	
	MultiSourceGenerator plus(Collection<Iterator> additional) {
		new MultiSourceGenerator(additional + iterators)
	}
	

	void close() {
		iterators.each { close(it) }
	}

	void close(Iterator generator) {
		if (generator.respondsTo('close')) {
			generator.close()
		}
	}
}

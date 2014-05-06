package spock.genesis

class MultiSourceGenerator<E> extends RandomGenerator {

	private final List<Iterator<E>> iterators

	MultiSourceGenerator(Collection<Iterator<E>> iterators) {
		super()
		this.iterators = iterators.toList()
	}
	
	MultiSourceGenerator(Map<Iterator<E>,Integer> weightedIterators) {
		super()
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
			} else {
				close(generator)
				iterators.remove(i)
			}
		}
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

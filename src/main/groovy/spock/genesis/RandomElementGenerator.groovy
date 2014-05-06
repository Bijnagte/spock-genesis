package spock.genesis

class RandomElementGenerator<E> extends RandomGenerator<E> {

	final List<E> elementSource

	RandomElementGenerator(Collection<E> elementSource) {
		super()
		this.elementSource = elementSource.toList().asImmutable()
	}

	@Override
	E next() {
		int limit = elementSource.size()
		int sourceIndex = random.nextInt(limit)
		elementSource[sourceIndex]
	}
}

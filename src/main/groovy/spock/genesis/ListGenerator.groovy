package spock.genesis

class ListGenerator<E> extends RandomGenerator<List<E>> {

	private final Generator<E> iterator
	final int lengthLimit	
	
	@Override
	List<E> next() {
		int length = random.nextInt(lengthLimit)
		iterator.take(length).realized
	}

	void close() {
		iterator.close()
	}
}

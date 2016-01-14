package spock.genesis.generators

class LimitedGenerator<E> extends GeneratorDecorator<E> {

    final int iterationLimit
    private int iteration = 0

    LimitedGenerator(Iterator<E> generator, int iterationLimit) {
        super(generator)
        this.iterationLimit = iterationLimit
    }

    LimitedGenerator(Collection<E> collection) {
        super(collection.iterator())
        this.iterationLimit = collection.size()
    }

    LimitedGenerator(E... array) {
        super(array.iterator())
        this.iterationLimit = array.length
    }

    @Override
    boolean hasNext() {
        generator.hasNext() && iteration < iterationLimit
    }

    @Override
    E next() {
        iteration++
        generator.next()
    }

    @Override
    boolean isFinite() {
        true
    }
}

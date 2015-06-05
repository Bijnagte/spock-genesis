package spock.genesis.generators

class LimitedGenerator<E> extends GeneratorDecorator<E> {

    final int iterationLimit
    private int iteration = 0

    LimitedGenerator(Iterator<E> generator, int iterationLimit) {
        super(generator)
        this.iterationLimit = iterationLimit
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
}

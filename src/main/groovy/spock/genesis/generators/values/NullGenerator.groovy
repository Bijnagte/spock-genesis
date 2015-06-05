package spock.genesis.generators.values

class NullGenerator<E> extends ValueGenerator<E> {

    NullGenerator() {
        super(null)
    }

    @Override
    E next() {
        null
    }
}

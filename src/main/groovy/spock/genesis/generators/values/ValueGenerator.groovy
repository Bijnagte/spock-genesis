package spock.genesis.generators.values

import spock.genesis.generators.InfiniteGenerator

class ValueGenerator<T> extends InfiniteGenerator<T> {

    final T value

    ValueGenerator(T value) {
        this.value = value
    }

    @Override
    T next() {
        value
    }
}

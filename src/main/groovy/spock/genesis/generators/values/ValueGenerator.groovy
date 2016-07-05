package spock.genesis.generators.values

import spock.genesis.generators.InfiniteGenerator
import spock.genesis.generators.InfiniteIterator

class ValueGenerator<T> extends InfiniteGenerator<T> {

    final T value

    ValueGenerator(T value) {
        this.value = value
    }

    InfiniteIterator<T> iterator() {
        new InfiniteIterator<T>() {
            @Override
            T next() {
                value
            }
        }
    }
}

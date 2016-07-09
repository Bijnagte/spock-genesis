package spock.genesis.generators.values

import groovy.transform.CompileStatic
import spock.genesis.generators.InfiniteGenerator
import spock.genesis.generators.InfiniteIterator

@CompileStatic
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

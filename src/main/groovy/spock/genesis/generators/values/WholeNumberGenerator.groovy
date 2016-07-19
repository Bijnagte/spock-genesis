package spock.genesis.generators.values

import groovy.transform.CompileStatic
import spock.genesis.generators.InfiniteGenerator
import spock.genesis.generators.InfiniteIterator

@CompileStatic
class WholeNumberGenerator extends InfiniteGenerator<Integer> {

    final int min
    final int magnitude

    WholeNumberGenerator(int max = Integer.MAX_VALUE) {
        this(0, max)
    }

    WholeNumberGenerator(int min, int max) {
        if (min < 0 || max < min) {
            throw new IllegalArgumentException("min $min must be 0 or greater and max $max must be greater than min")
        }
        this.min = min
        int magnitude = max - min
        this.magnitude = magnitude == Integer.MAX_VALUE ? magnitude : magnitude + 1
    }

    WholeNumberGenerator(IntRange range) {
        this(range.from, range.to)
    }

    InfiniteIterator<Integer> iterator() {
        new InfiniteIterator<Integer>() {
            @Override
            Integer next() {
                min + random.nextInt(magnitude)
            }
        }
    }
}

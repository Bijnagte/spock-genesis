package spock.genesis.generators.values

import groovy.transform.CompileStatic
import spock.genesis.generators.Generator
import spock.genesis.generators.InfiniteGenerator
import spock.genesis.generators.InfiniteIterator

@CompileStatic
class LongGenerator extends InfiniteGenerator<Long> {

    final Long min
    final Long max
    private final IntegerGenerator integerGenerator

    LongGenerator() {
        this.min = Long.MIN_VALUE
        this.max = Long.MAX_VALUE
    }

    LongGenerator(long min, long max) {
        assert min < max
        this.min = min
        this.max = max
        def magnitude = magnitude(min, max)
        if (magnitude <= Integer.MAX_VALUE) {
            integerGenerator = new IntegerGenerator(0, magnitude as int)
        }
    }

    InfiniteIterator<Long> chooseProvider(long min, long max) {
        BigInteger magnitude = magnitude(min, max)
        if (magnitude <= Integer.MAX_VALUE) {
            new ShiftedIntegerIterator(min as long)
        } else if (magnitude <= Long.MAX_VALUE) {
            new ShiftedLongIterator(magnitude as long, min as long)
        } else {
            new RandomLongIterator()
        }
    }

    private static BigInteger magnitude(long min, long max) {
        max.toBigInteger() - min.toBigInteger()
    }

    InfiniteIterator<Long> iterator() {
        final InfiniteIterator<Long> CANDIDATE_PROVIDER = chooseProvider(min, max)
        new InfiniteIterator<Long>() {
            @Override
            Long next() {
                while (true) {
                    Long val = CANDIDATE_PROVIDER.next()
                    if (val >= min && val <= max) {
                        return val
                    }
                }
            }
        }
    }

    class RandomLongIterator extends InfiniteIterator<Long> {
        Long next() {
            random.nextLong()
        }
    }

    class ShiftedLongIterator extends InfiniteIterator<Long> {
        final long magnitude
        final long shift

        ShiftedLongIterator(long magnitude, long shift) {
            this.magnitude = magnitude
            this.shift = shift
        }

        Long next() {
            // error checking and 2^x checking removed for simplicity.
            long bits
            long val

            boolean seek = true
            while (seek) {
                bits = (random.nextLong() << 1) >>> 1
                val = bits % magnitude
                seek = bits - val + (magnitude - 1) < 0L
            }
            shift + val
        }
    }

    class ShiftedIntegerIterator extends InfiniteIterator<Long> {
        final InfiniteIterator<Integer> iterator
        final long shift

        ShiftedIntegerIterator(long shift) {
            iterator = integerGenerator.iterator()
            this.shift = shift
        }

        Long next() {
            shift + iterator.next()
        }
    }

    @Override
    Generator<Long> seed(Long seed) {
        super.seed(seed)
        integerGenerator?.seed(seed)
        this
    }
}

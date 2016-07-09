package spock.genesis.generators.values

import groovy.transform.CompileStatic
import spock.genesis.generators.InfiniteGenerator
import spock.genesis.generators.InfiniteIterator

@CompileStatic
class LongGenerator extends InfiniteGenerator<Long> {

    final Long min
    final Long max
    final Random random = new Random()

    LongGenerator() {
        this.min = Long.MIN_VALUE
        this.max = Long.MAX_VALUE
    }

    LongGenerator(long min, long max) {
        assert min < max
        this.min = min
        this.max = max
    }

    InfiniteIterator<Long> chooseProvider(BigInteger min, BigInteger max) {
        BigInteger magnitude = max - min
        if (magnitude <= Integer.MAX_VALUE) {
            new ShiftedIntegerIterator(magnitude as int, min as long)
        } else if (magnitude <= Long.MAX_VALUE) {
            new ShiftedLongIterator(magnitude as long, min as long)
        } else {
            new RandomLongIterator()
        }
    }

    InfiniteIterator<Long> iterator() {
        final InfiniteIterator<Long> CANDIDATE_PROVIDER = chooseProvider(min.toBigInteger(), max.toBigInteger())
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

    private class RandomLongIterator extends InfiniteIterator<Long> {
        Long next() {
            random.nextLong()
        }
    }

    private class ShiftedLongIterator extends InfiniteIterator<Long> {
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

    private class ShiftedIntegerIterator extends InfiniteIterator<Long> {
        final InfiniteIterator<Integer> iterator
        final long shift

        ShiftedIntegerIterator(int magnitude, long shift) {
            iterator = new IntegerGenerator(0, magnitude).iterator()
            this.shift = shift
        }

        Long next() {
            shift + iterator.next()
        }
    }
}

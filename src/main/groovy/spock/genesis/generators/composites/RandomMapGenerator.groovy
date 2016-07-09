package spock.genesis.generators.composites

import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import spock.genesis.generators.Generator
import spock.genesis.generators.GeneratorUtils
import spock.genesis.generators.UnmodifiableIterator
import spock.genesis.generators.values.WholeNumberGenerator

@CompileStatic
class RandomMapGenerator<K, V> extends Generator<Map<K, V>> implements Closeable {

    static final int DEFAULT_ENTRY_LIMIT = 100
    final WholeNumberGenerator sizeSource
    final Iterable<K> keyGenerator
    final Iterable<V> valueGenerator

    RandomMapGenerator(Iterable<K> keyGenerator, Iterable valueGenerator) {
        this.sizeSource = new WholeNumberGenerator(DEFAULT_ENTRY_LIMIT)
        this.keyGenerator = keyGenerator
        this.valueGenerator = valueGenerator
    }

    RandomMapGenerator(Iterable<K> keyGenerator, Iterable<V> valueGenerator, int maxSize) {
        this.sizeSource = new WholeNumberGenerator(maxSize)
        this.keyGenerator = keyGenerator
        this.valueGenerator = valueGenerator
    }

    RandomMapGenerator(Iterable<K> keyGenerator, Iterable<V> valueGenerator, int minSize, int maxSize) {
        this.sizeSource = new WholeNumberGenerator(minSize, maxSize)
        this.keyGenerator = keyGenerator
        this.valueGenerator = valueGenerator
    }

    RandomMapGenerator(Iterable<K> keyGenerator, Iterable<V> valueGenerator, IntRange sizeRange) {
        this.sizeSource = new WholeNumberGenerator(sizeRange)
        this.keyGenerator = keyGenerator
        this.valueGenerator = valueGenerator
    }

    UnmodifiableIterator<Map<K, V>> iterator() {
        new UnmodifiableIterator<Map<K, V>>() {
            Iterator keys = keyGenerator.iterator()
            Iterator values = valueGenerator.iterator()
            Iterator sizes = sizeSource.iterator()

            @Override
            boolean hasNext() {
                keys.hasNext() && values.hasNext()
            }

            @Override
            Map<K, V> next() {
                int targetSize = sizes.next()
                int i = 0
                Map result = [:]
                while (result.size() < targetSize && hasNext()) {
                    result[keys.next()] = values.next()
                    i++
                }
                result
            }
        }
    }

    @CompileDynamic
    void close() {
        [keyGenerator, valueGenerator].each {
            if (it.respondsTo('close')) {
                it.close()
            }
        }
    }

    boolean isFinite() {
        GeneratorUtils.anyFinite(keyGenerator, valueGenerator)
    }
}

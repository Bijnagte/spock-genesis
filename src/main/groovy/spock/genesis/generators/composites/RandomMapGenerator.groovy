package spock.genesis.generators.composites

import groovy.transform.CompileStatic
import spock.genesis.extension.ExtensionMethods
import spock.genesis.generators.Generator
import spock.genesis.generators.UnmodifiableIterator
import spock.genesis.generators.values.WholeNumberGenerator

@CompileStatic
class RandomMapGenerator<K, V> extends Generator<Map<K, V>> implements Closeable {

    static final int DEFAULT_ENTRY_LIMIT = 100
    final WholeNumberGenerator sizeSource
    final Generator<K> keyGenerator
    final Generator<V> valueGenerator

    RandomMapGenerator(Iterable<K> keyGenerator, Iterable valueGenerator) {
        this(keyGenerator, valueGenerator, new WholeNumberGenerator(DEFAULT_ENTRY_LIMIT))
    }

    RandomMapGenerator(Iterable<K> keyGenerator, Iterable<V> valueGenerator, int maxSize) {
        this(keyGenerator, valueGenerator, new WholeNumberGenerator(maxSize))
    }

    RandomMapGenerator(Iterable<K> keyGenerator, Iterable<V> valueGenerator, int minSize, int maxSize) {
        this(keyGenerator, valueGenerator, new WholeNumberGenerator(minSize, maxSize))
    }

    RandomMapGenerator(Iterable<K> keyGenerator, Iterable<V> valueGenerator, IntRange sizeRange) {
        this(keyGenerator, valueGenerator, new WholeNumberGenerator(sizeRange))
    }

    RandomMapGenerator(Iterable<K> keyGenerator, Iterable<V> valueGenerator, WholeNumberGenerator sizeSource) {
        this.sizeSource = sizeSource
        this.keyGenerator = ExtensionMethods.toGenerator(keyGenerator)
        this.valueGenerator = ExtensionMethods.toGenerator(valueGenerator)
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

    @Override
    void close() {
        keyGenerator.close()
        valueGenerator.close()
    }

    @Override
    boolean isFinite() {
        keyGenerator.finite || valueGenerator.finite
    }

    @Override
    RandomMapGenerator<K, V> seed(Long seed) {
        keyGenerator.seed(seed)
        valueGenerator.seed(seed)
        sizeSource.seed(seed)
        super.seed(seed)
        this
    }
}

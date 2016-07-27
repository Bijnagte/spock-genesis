package spock.genesis.generators.composites

import groovy.transform.CompileStatic
import spock.genesis.generators.Generator
import spock.genesis.generators.Permutable
import spock.genesis.generators.UnmodifiableIterator

@CompileStatic
class DefinedMapGenerator<K, V> extends Generator<Map<K, V>> implements Permutable {

    private final List<K> keys
    private final TupleGenerator<V> valuesGenerator

    DefinedMapGenerator(Map<K, Iterable<V>> keysToValueGenerators) {
        List keys = []
        List iterables = []
        keysToValueGenerators.each { key, iterable ->
            keys << key
            iterables << iterable
        }
        this.keys = keys
        this.valuesGenerator = new TupleGenerator(iterables)
    }

    DefinedMapGenerator(List<K> keys,  TupleGenerator<V> valuesGenerator) {
        int values = valuesGenerator.generators.size()
        if (keys.size() != values) {
            throw new IllegalArgumentException("Keys size (${keys.size()}) does not match values size ($values")
        }
        this.keys = keys.collect() //defensive copy
        this.valuesGenerator = valuesGenerator
    }

    @Override
    UnmodifiableIterator<Map<K, V>> iterator() {

        new UnmodifiableIterator<Map<K, V>>() {
            final private Iterator<List<V>> iterator = valuesGenerator.iterator()

            @Override
            boolean hasNext() {
                iterator.hasNext()
            }

            @Override
            Map<K, V> next() {
                List values = iterator.next()
                Map<K,V> result = [:]
                keys.eachWithIndex { K key, int i ->
                    result[key] = values[i]
                }
                result
            }
        }
    }

    DefinedMapGenerator<K,V> permute() {
        new DefinedMapGenerator<K, V>(keys, valuesGenerator.permute())
    }

    DefinedMapGenerator<K,V> permute(int maxDepth) {
        new DefinedMapGenerator<K, V>(keys, valuesGenerator.permute(maxDepth))
    }

    boolean isFinite() {
        valuesGenerator.finite
    }

    @Override
    DefinedMapGenerator<K, V> seed(Long seed) {
        valuesGenerator.seed(seed)
        super.seed(seed)
        this
    }
}

package spock.genesis.generators.composites

import groovy.transform.CompileStatic
import spock.genesis.extension.ExtensionMethods
import spock.genesis.generators.Generator
import spock.genesis.generators.GeneratorUtils
import spock.genesis.generators.UnmodifiableIterator

@CompileStatic
class DefinedMapGenerator<K, V> extends Generator<Map<K, V>> {

    final Map<K, Generator<V>> keysToValueGenerators

    DefinedMapGenerator(Map<K, Iterable<V>> keysToValueGenerators) {
        Map<K, Generator<V>> collector = [:]
        this.keysToValueGenerators = keysToValueGenerators.collectEntries(collector) { key, generator ->
            [key, ExtensionMethods.toGenerator(generator)]
        }.asImmutable()
    }

    @Override
    UnmodifiableIterator<Map<K, V>> iterator() {
        final Map<K, Iterator<V>> COLLECTOR = [:]

        new UnmodifiableIterator<Map<K, V>>() {
            final private Map<K, Iterator<V>> iteratorMap = keysToValueGenerators.collectEntries(COLLECTOR) { k, v ->
                [k, v.iterator()]
            }

            @Override
            boolean hasNext() {
                iteratorMap.every { key, generator ->
                    generator.hasNext()
                }
            }

            @Override
            Map<K, V> next() {
                iteratorMap.collectEntries { key, generator ->
                    [key, generator.next()]
                }
            }
        }
    }

    boolean isFinite() {
        GeneratorUtils.anyFinite(keysToValueGenerators.values())
    }
}

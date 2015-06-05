package spock.genesis.generators.composites

import spock.genesis.generators.Generator

class DefinedMapGenerator<K, V> extends Generator<Map<K, V>> {

    final Map<K, Iterator<V>> keysToValueGenerators

    DefinedMapGenerator(Map<K, Iterator> keysToValueGenerators) {
        this.keysToValueGenerators = keysToValueGenerators.asImmutable()
    }

    @Override
    boolean hasNext() {
        keysToValueGenerators.every { key, generator ->
            generator.hasNext()
        }
    }

    @Override
    Map<K, V> next() {
        keysToValueGenerators.collectEntries { key, generator ->
            [key, generator.next()]
        }
    }
}

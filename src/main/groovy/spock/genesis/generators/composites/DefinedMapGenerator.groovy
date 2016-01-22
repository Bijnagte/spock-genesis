package spock.genesis.generators.composites

import spock.genesis.extension.ExtensionMethods
import spock.genesis.generators.Generator
import spock.genesis.generators.GeneratorUtils

class DefinedMapGenerator<K, V> extends Generator<Map<K, V>> {

    final Map<K, Generator<V>> keysToValueGenerators

    DefinedMapGenerator(Map<K, Iterator> keysToValueGenerators) {
        this.keysToValueGenerators = keysToValueGenerators.collectEntries { key, generator ->
            [key, ExtensionMethods.toGenerator(generator)]
        }.asImmutable()
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

    boolean isFinite() {
         GeneratorUtils.anyFinite(keysToValueGenerators.values())
    }
}

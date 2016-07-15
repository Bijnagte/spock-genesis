package spock.genesis.generators.composites

import spock.genesis.Gen
import spock.genesis.generators.Generator
import spock.genesis.generators.test.CloseableIterable
import spock.genesis.generators.values.IntegerGenerator
import spock.genesis.generators.values.StringGenerator
import spock.genesis.transform.Iterations
import spock.lang.Specification

class RandomMapGeneratorSpec extends Specification {

    def 'generates keys and values of the expected types'() {
        setup:
            def generator = new RandomMapGenerator(new StringGenerator(10), new IntegerGenerator())
        when:
            Map result = generator.iterator().next()
        then:
            result.each { key, value ->
                key instanceof String
                value instanceof Integer
            }
    }

    def 'calling close does nothing if the generators have no close method'() {
        setup:
            Iterable keyGenerator = Mock()
            Iterable valueGenerator = Mock()

            def generator = new RandomMapGenerator(keyGenerator, valueGenerator)
        when:
            generator.close()
        then:
            0 * keyGenerator._
            0 * valueGenerator._
    }

    def 'calling close calls close on key and value generators'() {
        setup:
            CloseableIterable keyGenerator = Mock()
            CloseableIterable valueGenerator = Mock()
            def generator = new RandomMapGenerator(keyGenerator, valueGenerator)
        when:
            generator.close()
        then:
            1 * keyGenerator.close()
            1 * valueGenerator.close()
    }

    @Iterations
    def 'generates map that is limited to max size'() {
        expect:
            result instanceof Map
            result.size() <= 100
            result.size() >= 0
            result.every { key, value ->
                key instanceof String && value instanceof Integer
            }
        where:
            result << new RandomMapGenerator(Gen.string(10), Gen.integer, 100)
    }

    @Iterations
    def 'generates map that is limited to min and max size'() {
        expect:
            result instanceof Map
            result.size() <= 100
            result.size() >= 90
            result.every { key, value ->
                key instanceof String && value instanceof Integer
            }
        where:
            result << new RandomMapGenerator(Gen.string(10), Gen.integer, 90, 100)
    }

    @Iterations
    def 'generates map that is limited to size range'() {
        expect:
            result instanceof Map
            result.size() <= 100
            result.size() >= 90
            result.every { key, value ->
                key instanceof String && value instanceof Integer
            }
        where:
            result << new RandomMapGenerator(Gen.string(10), Gen.integer, 90..100)
    }

    def 'close closes sources'() {
        given:
            Generator keys = Mock()
            Generator values = Mock()
            def generator = new RandomMapGenerator(keys, values, 10)
        when:
            generator.close()
        then:
            1 * keys.close()
            1 * values.close()
            0 * _
    }

    @Iterations
    def 'setting seed returns the same values with 2 generators configured the same'() {
        given:
            def generatorA = new RandomMapGenerator(Gen.string(10), Gen.integer, 10..20).seed(seed).take(10).realized
            def generatorB = new RandomMapGenerator(Gen.string(10), Gen.integer, 10..20).seed(seed).take(10).realized
        expect:
            generatorA == generatorB
        where:
            seed = 100L
    }
}

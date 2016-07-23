package spock.genesis.generators.composites

import spock.genesis.Gen
import spock.genesis.generators.values.IntegerGenerator
import spock.genesis.generators.values.NullGenerator
import spock.genesis.generators.values.StringGenerator
import spock.lang.Specification

class DefinedMapGeneratorSpec extends Specification {

    def 'generate map with generator per key'() {
        setup:
            def date = new Date()
            def generators = [
                    1     : new IntegerGenerator(),
                    string: new StringGenerator(20),
                    (date): new NullGenerator()
            ]
            def generator = new DefinedMapGenerator(generators)
        when:
            def result = generator.iterator().next()
        then:
            result.keySet() == [1, date, 'string'].toSet()
            result[1] instanceof Integer
            result['string'] instanceof String
            result[date] == null
    }

    def 'has next is false if one field generator does not have next'() {
        setup:
            def generators = [
                    1     : new IntegerGenerator(),
                    string: new StringGenerator(20),
                    empty : []
            ]
            def generator = new DefinedMapGenerator(generators)
        expect:
            generator.iterator().hasNext() == false
    }

    def 'isFinite'() {
        expect:
            new DefinedMapGenerator(generators).finite == expected

        where:
            generators                          || expected
            [a: new IntegerGenerator()]         || false
            [a: []]                             || true
            [a: new IntegerGenerator().take(1)] || true
    }

    def 'setting seed returns the same values with 2 generators configured the same'() {
        given:
            def generatorA = new DefinedMapGenerator(a: Gen.string(10), b: Gen.integer).seed(seed).take(10).realized
            def generatorB = new DefinedMapGenerator(a: Gen.string(10), b: Gen.integer).seed(seed).take(10).realized
        expect:
            generatorA == generatorB
        where:
            seed = 100L
    }

    def 'generates all permutations'() {
        given:
            def mapDefinition = [int: [1, 2, 3], string:['a', 'b', 'c']]
            def generator = new DefinedMapGenerator(mapDefinition).permute()
        expect:
            generator.collect() == [
                    [int: 1, string: 'a'],
                    [int: 2, string: 'a'],
                    [int: 3, string: 'a'],
                    [int: 1, string: 'b'],
                    [int: 2, string: 'b'],
                    [int: 3, string: 'b'],
                    [int: 1, string: 'c'],
                    [int: 2, string: 'c'],
                    [int: 3, string: 'c']]
    }

    def 'generates permutations'() {
        given:
            def mapDefinition = [int: [1, 2, 3], string: ['a']]
            def generator = new DefinedMapGenerator(mapDefinition).permute()
        expect:
            generator.collect() == [
                    [int: 1, string: 'a'],
                    [int: 2, string: 'a'],
                    [int: 3, string: 'a'],
            ]
    }
}

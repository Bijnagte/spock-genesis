package spock.genesis.generators.composites

import spock.genesis.Gen

import spock.lang.Specification

class PermutationGeneratorSpec extends Specification {

    def 'generates all permutations'() {
        given:
            def generators = [[1, 2, 3].toGenerator(), ['a', 'b', 'c'].toGenerator()]
            def generator = new PermutationGenerator(generators)
        expect:
            generator.collect() == [
                    [1, 'a'],
                    [2, 'a'],
                    [3, 'a'],
                    [1, 'b'],
                    [2, 'b'],
                    [3, 'b'],
                    [1, 'c'],
                    [2, 'c'],
                    [3, 'c']]
    }

    def 'generates permutations'() {
        given:
            def generators = [[1, 2, 3].toGenerator(), ['a'].toGenerator()]
            def generator = new PermutationGenerator(generators)
        expect:
            generator.collect() == [
                    [1, 'a'],
                    [2, 'a'],
                    [3, 'a'],
                    ]
    }

    def 'permutations are capped at the generator count root of 10000'() {
        given:
            def generators = []
            generatorCount.times { generators.add(Gen.integer) }
            def generator = new PermutationGenerator(generators)
        expect:
            generator.collect().size() == permutations
        where:
            generatorCount | permutations
            1              | 10000
            2              | 10000
            3              | 9261
            4              | 10000
            5              | 7776
            6              | 4096
            7              | 2187
            8              | 6561
    }
}

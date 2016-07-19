package spock.genesis.generators

import spock.genesis.Gen
import spock.lang.Specification

class MultiSourceGeneratorSpec extends Specification {

    def 'multiple iterables run until all are empty'() {
        setup:
            def generator = new MultiSourceGenerator(iterables).seed(9809)
            def expectedCount = iterables*.size().sum()
        when:
            def result = generator.realized

        then: 'all of the elements were returned'
            result.size() == expectedCount
            iterables.every { list ->
                list.every { it in result }
            }
        and: 'the order does not match'
            result != iterables.flatten()
        where:
            iterables << [
                    [['a', 'b', 'c', 'd', 'e'], (1..100).collect()],
                    [[null, 'b', 'c', [key: 'value']], [1, 2, 3, new Date()], (1..100).collect()]
            ]
    }

    def 'setting seed returns the same values with 2 generators configured the same'() {
        given:
            def generatorA = new MultiSourceGenerator([Gen.string(10), Gen.integer]).seed(seed).take(10).realized
            def generatorB = new MultiSourceGenerator([Gen.string(10), Gen.integer]).seed(seed).take(10).realized
        expect:
            generatorA == generatorB
        where:
            seed = 100L
    }
}

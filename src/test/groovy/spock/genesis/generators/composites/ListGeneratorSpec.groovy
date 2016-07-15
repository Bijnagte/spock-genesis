package spock.genesis.generators.composites

import spock.genesis.Gen
import spock.genesis.generators.values.IntegerGenerator
import spock.genesis.generators.values.NullGenerator
import spock.genesis.generators.values.StringGenerator
import spock.genesis.transform.Iterations
import spock.lang.Specification

class ListGeneratorSpec extends Specification {

    def 'list generator next'() {
        setup:
            def generator = new ListGenerator(gen, limit)
        when:
            List result = generator.iterator().next()
        then:
            result.size() <= limit
        where:
            gen                     | limit
            new IntegerGenerator()  | 40
            new StringGenerator(20) | 40
            new NullGenerator()     | 1
            new IntegerGenerator()  | 0
    }

    @Iterations
    def 'setting seed returns the same values with 2 generators configured the same'() {
        given:
            def generatorA = new ListGenerator(Gen.string(10)).seed(seed).take(10).realized
            def generatorB = new ListGenerator(Gen.string(10)).seed(seed).take(10).realized
        expect:
            generatorA == generatorB
        where:
            seed = 100L
    }
}

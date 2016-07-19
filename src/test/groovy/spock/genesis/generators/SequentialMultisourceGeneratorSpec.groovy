package spock.genesis.generators

import spock.genesis.Gen
import spock.lang.Specification


class SequentialMultisourceGeneratorSpec extends Specification {

    def 'setting seed returns the same values with 2 generators configured the same'() {
        given:
            def generatorA = new SequentialMultisourceGenerator(Gen.string(10), Gen.integer).seed(seed).take(10).realized
            def generatorB = new SequentialMultisourceGenerator(Gen.string(10), Gen.integer).seed(seed).take(10).realized
        expect:
            generatorA == generatorB
        where:
            seed = 100L
    }
}

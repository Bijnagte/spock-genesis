package spock.genesis.generators.values

import spock.lang.Specification


class RandomElementGeneratorSpec extends Specification {
    def'setting seed produces the same sequences for different generators' () {
        given:
            def elements = ['a', 'b', 'c', 'd']
            def a = new RandomElementGenerator(elements).seed(seed).take(100).realized
            def b = new RandomElementGenerator(elements).seed(seed).take(100).realized
        expect:
            a == b
        where:
            seed << [Long.MIN_VALUE, 100, Long.MAX_VALUE]
    }
}

package spock.genesis.generators.values

import spock.lang.Specification
import spock.lang.Unroll

class WholeNumberGeneratorSpec extends Specification {
    def 'whole number generator for range'() {
        when:
            def numbers = new WholeNumberGenerator(min..max).take(1000)
        then:
            numbers.every { it >= min && it <= max }
        where:
            min | max
            0   | Integer.MAX_VALUE
            10  | 20
    }

    def 'whole number generator for min and max'() {
        when:
            def numbers = new WholeNumberGenerator(min, max).take(1000)
        then:
            numbers.every { it >= min && it <= max }
        where:
            min | max
            0   | Integer.MAX_VALUE
            10  | 20
    }

    def 'whole number generator for max'() {
        when:
            def numbers = new WholeNumberGenerator(max).take(1000)
        then:
            numbers.every { it >= 0 && it <= max }
        where:
            max << [1, 10, Integer.MAX_VALUE]
    }

    @Unroll
    def 'arguments must be positive and the correct order'() {
        when:
            new WholeNumberGenerator(*args)
        then:
            thrown(IllegalArgumentException)
        where:
            args  << [
                    [2, 1],
                    [-1, 10],
                    [-1..10],
                    [-1],
            ]
    }
}
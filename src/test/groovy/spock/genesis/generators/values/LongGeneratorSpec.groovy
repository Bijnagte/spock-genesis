package spock.genesis.generators.values

import spock.lang.Specification

class LongGeneratorSpec extends Specification {


    def 'generate includes boundaries'() {
        setup:
            def generator = new LongGenerator(low, high)
            int sample = 1000 * (high - low)
        when:
            Set results = generator.take(sample).realized as Set
        then:
            results.find { it == low }
            results.find { it == high }
            results.every { it <= high && it >= low }
        where:
            low                | high
            Long.MIN_VALUE     | Long.MIN_VALUE + 1
            Long.MAX_VALUE - 1 | Long.MAX_VALUE
            -10                | 10
    }
}

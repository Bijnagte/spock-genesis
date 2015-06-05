package spock.genesis.generators.values

import spock.lang.Specification

class DateGeneratorSpec extends Specification {

    def 'generate in range'() {
        setup:
            final sample = 10
        when:
            def generator = new DateGenerator(low, high)
            def results = generator.take(sample).realized

        then:
            results.size() == sample
            results.every { it <= high && it >= low }
        where:
            low        | high
            new Date() | new Date() + 10
    }
}

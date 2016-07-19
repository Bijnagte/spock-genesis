package spock.genesis.generators.values

import spock.lang.Specification
import spock.lang.Unroll

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

    @Unroll
    def 'setting seed produces the same sequences for different generators'() {
        given:
            def generatorA = new LongGenerator(low, high).seed(1).take(100).realized
            def generatorB = new LongGenerator(low, high).seed(1).take(100).realized
        expect:
            generatorA == generatorB
        where:
            low            | high           | iterator
            Long.MIN_VALUE | Long.MAX_VALUE | LongGenerator.RandomLongIterator
            0              | Long.MAX_VALUE | LongGenerator.RandomLongIterator
            -10            | 10             | LongGenerator.RandomLongIterator
            -10            | Long.MAX_VALUE | LongGenerator.RandomLongIterator
    }

    def 'chose correct iterator'() {
        given:
            def generator = new LongGenerator(low, high)
        expect:
            generator.chooseProvider(low, high).getClass() == iterator
        where:
            low            | high           | iterator
            Long.MIN_VALUE | Long.MAX_VALUE | LongGenerator.RandomLongIterator
            0              | Long.MAX_VALUE | LongGenerator.ShiftedLongIterator
            -10            | 10             | LongGenerator.ShiftedIntegerIterator
            -10            | Long.MAX_VALUE | LongGenerator.RandomLongIterator

    }
}

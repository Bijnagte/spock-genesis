package spock.genesis.generators

import spock.lang.Specification

class CyclicGeneratorSpec extends Specification {

    def 'generate repeats'() {
        setup:
            def generator = new CyclicGenerator(iterable).take(limit)
        when:
            def result = generator.realized
        then:
            result == expected

        where:
            limit | iterable     || expected
            5     | [1, 2, 3]    || [1, 2, 3, 1, 2]
            3     | [null]       || [null, null, null]
            4     | [a: 1, b: 2] || [[a: 1], [b: 2], [a: 1], [b: 2]].collect { it.entrySet().first() }
            10    | 'hi!'        || ['h', 'i', '!', 'h', 'i', '!', 'h', 'i', '!', 'h']
    }

    def 'has next is false if the iterator has no entries'() {
        setup:
            def iterable = []
            def generator = new CyclicGenerator(iterable)
        expect:
            generator.iterator().hasNext() == false
    }

    def 'generator is not finite'() {
        expect:
            new CyclicGenerator([1]).isFinite() == false
            new CyclicGenerator([]).isFinite() == true
    }

}

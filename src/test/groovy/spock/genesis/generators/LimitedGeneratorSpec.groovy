package spock.genesis.generators

import spock.lang.Specification

class LimitedGeneratorSpec extends Specification {

    Iterator wrapped = Mock()

    def 'limits the wrapped iterator to the length specified'() {
        setup:
            def generator = new LimitedGenerator(wrapped, limit)
            wrapped.hasNext() >> true
        when:
            def result = generator.collect()
        then:
            limit * wrapped.next() >> 'a'
            result.size() == limit
            result.every { it == 'a' }
        where:
            limit << [0, 10, 20]
    }

    def 'if the wrapped iterator runs out before the length then stop'() {
        setup:
            def generator = new LimitedGenerator(wrapped, 20)
        when:
            def result = generator.collect()
        then:
            1 * wrapped.hasNext() >> false
            0 * wrapped.next()
            result == []
    }

    def 'is finite'() {
        expect:
            new LimitedGenerator(wrapped, 20).isFinite() == true
    }

    def 'collection and array constructors iterate complete source'() {
        expect:
            new LimitedGenerator([1, 2, 3]).realized == [1, 2, 3]
            new LimitedGenerator([1, 2, 3].toSet()).realized == [1, 2, 3]
            new LimitedGenerator([1, 2, 3].toArray()).realized == [1, 2, 3]
    }
}

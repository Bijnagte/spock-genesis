package spock.genesis.generators

import spock.lang.Specification

class LimitedGeneratorSpec extends Specification {

    Generator wrapped = Mock()
    UnmodifiableIterator iterator = Mock()

    def setup() {
        wrapped.iterator() >> iterator
    }

    def 'limits the wrapped iterator to the length specified'() {
        setup:

            def generator = new LimitedGenerator(wrapped, limit)
            iterator.hasNext() >> true
        when:
            def result = generator.collect()
        then:
            limit * iterator.next() >> 'a'
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
            1 * iterator.hasNext() >> false
            0 * iterator.next()
            result == []
    }

    def 'is finite'() {
        expect:
            new LimitedGenerator(wrapped, 20).isFinite() == true
    }
}

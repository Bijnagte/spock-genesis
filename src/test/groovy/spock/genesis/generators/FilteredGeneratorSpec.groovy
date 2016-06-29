package spock.genesis.generators

import spock.lang.Specification

class FilteredGeneratorSpec extends Specification {

    Iterator supplier = Mock()
    Iterable iterable = Stub {
        iterator() >> supplier
    }

    def 'calls supplier until a result that matches the predicate is found'() {
        setup:
            def predicate = { it == 7 }
            def generator = new FilteredGenerator(iterable, predicate)
            supplier.hasNext() >> true
        when:
            def result = generator.iterator().next()
        then:
            result == 7
            3 * supplier.next() >>> [2, 4, 7, 8]
    }

    def 'all values are returned even with extra calls to has next'() {
        setup:
            def predicate = { arg -> true }
            def generator = new FilteredGenerator(iterable, predicate)
            supplier.hasNext() >> true
            supplier.next() >>> [2, 4, 7]
            def iterator = generator.iterator()
        when:
            iterator.hasNext()
            def result = iterator.next()
        then:
            result == 2
        when:
            iterator.hasNext()
            iterator.hasNext()
            result = iterator.next()
        then:
            result == 4
    }

    def 'has next is false if no match can be found before supplier is empty'() {
        setup:
            def predicate = { it == 1 }
            def supplier = [2, 3, 4]
            def generator = new FilteredGenerator(supplier, predicate)
        expect:
            generator.iterator().hasNext() == false
    }

}

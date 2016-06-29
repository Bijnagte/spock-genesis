package spock.genesis.generators

import spock.lang.Specification

class TransformingGeneratorSpec extends Specification {

    Iterator iterator = Mock()
    Iterable supplier = Stub {
        iterator() >> iterator
    }

    def 'transforming generator calls closure with next value'() {
        setup:
            def transform = { val -> val + 1 }
            def generator = new TransformingGenerator(supplier, transform)
        when:
            def result = generator.iterator().next()
        then:
            1 * iterator.next() >> 20
            result == 21
    }
}

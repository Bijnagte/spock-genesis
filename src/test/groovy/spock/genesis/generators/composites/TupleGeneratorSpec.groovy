package spock.genesis.generators.composites

import spock.lang.Specification
import spock.lang.Unroll

class TupleGeneratorSpec extends Specification {

    @Unroll
    def 'tuple generator makes lists from source generators'() {
        setup:
            def generator = new TupleGenerator(sources*.iterator())
        when:
            def results = generator.realized
        then:
            results == expected
        where:
            sources                         || expected
            [['a', 'b'], [1, 2], [5, 6, 7]] || [['a', 1, 5], ['b', 2, 6]]
            ['hello!', [1, 2, 3, 4, 5]]     || [['h', 1], ['e', 2], ['l', 3], ['l', 4], ['o', 5]]
    }
}

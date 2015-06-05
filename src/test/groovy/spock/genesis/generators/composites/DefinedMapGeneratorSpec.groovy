package spock.genesis.generators.composites

import spock.genesis.generators.values.IntegerGenerator
import spock.genesis.generators.values.NullGenerator
import spock.genesis.generators.values.StringGenerator
import spock.lang.Specification

class DefinedMapGeneratorSpec extends Specification {

    def 'generate map with generator per key'() {
        setup:
            def date = new Date()
            def generators = [
                    1     : new IntegerGenerator(),
                    string: new StringGenerator(20),
                    (date): new NullGenerator()
            ]
            def generator = new DefinedMapGenerator(generators)
        when:
            def result = generator.next()
        then:
            result.keySet() == [1, date, 'string'].toSet()
            result[1] instanceof Integer
            result['string'] instanceof String
            result[date] == null
    }

    def 'has next is false if one field generator does not have next'() {
        setup:
            def generators = [
                    1     : new IntegerGenerator(),
                    string: new StringGenerator(20),
                    empty : [].iterator()
            ]
            def generator = new DefinedMapGenerator(generators)
        expect:
            generator.hasNext() == false
    }
}

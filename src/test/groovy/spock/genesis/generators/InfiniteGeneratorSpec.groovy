package spock.genesis.generators

import spock.lang.Specification

class InfiniteGeneratorSpec extends Specification {

    def 'is infinite'() {
        setup:
            def generator = new InfiniteGenerator() {
                InfiniteIterator iterator() {
                    new InfiniteIterator() {
                        def next() {}
                    }
                }
                def next() {}
            }
        expect:
            generator.iterator().hasNext() == true
            generator.isFinite() == false
    }
}
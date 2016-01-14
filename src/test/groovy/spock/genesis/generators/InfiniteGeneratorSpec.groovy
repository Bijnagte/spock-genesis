package spock.genesis.generators;

import spock.lang.Specification

class InfiniteGeneratorSpec extends Specification {

    def 'is infinite'() {
        setup:
            def generator = new InfiniteGenerator() {
                def next() {}
            }
        expect:
            generator.hasNext() == true
            generator.isFinite() == false
    }
}
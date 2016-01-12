package spock.genesis.generators

import spock.genesis.generators.test.CloseableIterator
import spock.lang.Specification
import spock.lang.Unroll

class GeneratorDecoratorSpec extends Specification {

    def 'calling close does nothing if wrapped generator does not have a close'() {
        setup:
            Iterator wrapped = Mock()
            def generator = new GeneratorDecorator(wrapped)
        when:
            generator.close()
        then:
            0 * wrapped._
    }

    def 'calling close closes wrapped generator'() {
        setup:
            CloseableIterator wrapped = Mock()
            def generator = new GeneratorDecorator(wrapped)
        when:
            generator.close()
        then:
            1 * wrapped.close()
    }

    def 'has next delegates to wrapped generator'() {
        setup:
            Iterator wrapped = Mock()
            def generator = new GeneratorDecorator(wrapped)
        when:
            generator.hasNext()
        then:
            1 * wrapped.hasNext()
            0 * wrapped._
    }

    def 'next delegates to wrapped generator'() {
        setup:
            Iterator wrapped = Mock()
            def generator = new GeneratorDecorator(wrapped)
        when:
            generator.next()
        then:
            1 * wrapped.next()
            0 * wrapped._
    }

    @Unroll
    def 'isFinite if specified as such or wrapped iterator is finite '() {
        setup:
            def generator = overrideFinite ? new GeneratorDecorator(wrapped, overrideFinite) : new GeneratorDecorator(wrapped)
        expect:
            generator.isFinite() == expected
        where:
            overrideFinite | wrapped                                                    || expected
            false          | [].iterator()                                              || true
            false          | [1].iterator()                                             || false
            true           | [1].iterator()                                             || true
            true           | new TestGenerator(finiteValue: false, hasNextValue: true)  || true
            false          | new TestGenerator(finiteValue: false, hasNextValue: true)  || false
            false          | new TestGenerator(finiteValue: false, hasNextValue: false) || true
            false          | new TestGenerator(finiteValue: true, hasNextValue: true)   || true
    }


    class TestGenerator implements Iterator {
        boolean finiteValue
        boolean hasNextValue

        boolean hasNext() { hasNextValue }
        def next() { assert false }
        void remove() { assert false }
        boolean isFinite() { finiteValue }
    }
}

package spock.genesis.generators

import spock.lang.Specification
import spock.lang.Unroll

class GeneratorDecoratorSpec extends Specification {

    def 'calling close closes wrapped generator'() {
        setup:
            Generator wrapped = Mock()
            def generator = new GeneratorDecorator(wrapped)
        when:
            generator.close()
        then:
            1 * wrapped.close()
    }

    def 'has next delegates to wrapped generator'() {
        setup:
            UnmodifiableIterator iterator = Mock()
            Generator wrapped = Mock()
            def generator = new GeneratorDecorator(wrapped)
        when:
            generator.iterator().hasNext()
        then:
            1 * wrapped.iterator() >> iterator
            1 * iterator.hasNext()
            0 * _
    }

    def 'next delegates to wrapped generator'() {
        setup:
            UnmodifiableIterator iterator = Mock()
            Generator wrapped = Mock()
            def generator = new GeneratorDecorator(wrapped)
        when:
            generator.iterator().next()
        then:
            1 * wrapped.iterator() >> iterator
            1 * iterator.next()
            0 * _
    }

    @Unroll
    def 'isFinite if specified as such or wrapped iterator is finite '() {
        setup:
            def generator = overrideFinite != null ? new GeneratorDecorator<>(wrapped, overrideFinite) : new GeneratorDecorator<>(wrapped)
        expect:
            generator.isFinite() == expected
        where:
            overrideFinite | wrapped                                                    || expected
            true           | new TestGenerator(finiteValue: false, hasNextValue: true)  || true
            null           | new TestGenerator(finiteValue: false, hasNextValue: true)  || false
            null           | new TestGenerator(finiteValue: false, hasNextValue: false) || true
            null           | new TestGenerator(finiteValue: true, hasNextValue: true)   || true
    }

    class TestGenerator extends Generator {
        boolean finiteValue
        boolean hasNextValue

        UnmodifiableIterator iterator() {
            new UnmodifiableIterator() {
                boolean hasNext() { hasNextValue }
                def next() { assert false }
            }
        }
        boolean isFinite() { finiteValue }
    }
}

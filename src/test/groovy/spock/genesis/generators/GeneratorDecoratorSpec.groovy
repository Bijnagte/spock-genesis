package spock.genesis.generators

import spock.genesis.generators.test.CloseableIterable
import spock.lang.Specification
import spock.lang.Unroll

class GeneratorDecoratorSpec extends Specification {

    def 'calling close does nothing if wrapped generator does not have a close'() {
        setup:
            Iterable wrapped = Mock()
            def generator = new GeneratorDecorator(wrapped)
        when:
            generator.close()
        then:
            0 * wrapped._
    }

    def 'calling close closes wrapped generator'() {
        setup:
            CloseableIterable wrapped = Mock()
            def generator = new GeneratorDecorator(wrapped)
        when:
            generator.close()
        then:
            1 * wrapped.close()
    }

    def 'has next delegates to wrapped generator'() {
        setup:
            Iterator iterator = Mock()
            Iterable wrapped = Mock()
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
            Iterator iterator = Mock()
            Iterable wrapped = Mock()
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
            def generator = overrideFinite ? new GeneratorDecorator(wrapped, overrideFinite) : new GeneratorDecorator(wrapped)
        expect:
            generator.isFinite() == expected
        where:
            overrideFinite | wrapped                                                    || expected
            false          | []                                                         || true
            false          | [1]                                                        || false
            true           | [1]                                                        || true
            true           | new TestGenerator(finiteValue: false, hasNextValue: true)  || true
            false          | new TestGenerator(finiteValue: false, hasNextValue: true)  || false
            false          | new TestGenerator(finiteValue: false, hasNextValue: false) || true
            false          | new TestGenerator(finiteValue: true, hasNextValue: true)   || true
    }

    class TestGenerator implements Iterable {
        boolean finiteValue
        boolean hasNextValue

        Iterator iterator() {
            new Iterator() {
                boolean hasNext() { hasNextValue }
                def next() { assert false }
                void remove() { assert false }
            }
        }
        boolean isFinite() { finiteValue }
    }
}

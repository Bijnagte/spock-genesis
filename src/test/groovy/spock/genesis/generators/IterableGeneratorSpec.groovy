package spock.genesis.generators

import spock.genesis.generators.test.CloseableIterable
import spock.lang.Specification
import spock.lang.Unroll

class IterableGeneratorSpec extends Specification {
    def 'Iterable and array constructors iterate complete source'() {
        expect:
            new IterableGenerator([1, 2, 3]).realized == [1, 2, 3]
            new IterableGenerator([1, 2, 3].toSet()).realized == [1, 2, 3]
            new IterableGenerator([1, 2, 3].toArray()).realized == [1, 2, 3]
    }

    def 'calling close does nothing if wrapped generator does not have a close'() {
        setup:
            Iterable wrapped = Mock()
            def generator = new IterableGenerator(wrapped)
        when:
            generator.close()
        then:
            0 * wrapped._
    }

    def 'calling close closes wrapped iterable'() {
        setup:
            CloseableIterable wrapped = Mock()
            def generator = new IterableGenerator(wrapped)
        when:
            generator.close()
        then:
            1 * wrapped.close()
    }

    @Unroll
    def 'isFinite if specified as such or wrapped iterator is finite '() {
        setup:
            def generator = overrideFinite != null ? new IterableGenerator(wrapped, overrideFinite) : new IterableGenerator(wrapped)
        expect:
            generator.isFinite() == expected
        where:
            overrideFinite | wrapped                                                    || expected
            false          | []                                                         || true
            null           | [1]                                                        || true
            true           | [1]                                                        || true
            false          | [1]                                                        || false
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

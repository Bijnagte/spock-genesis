package spock.genesis.generators

import spock.genesis.generators.values.IntegerGenerator
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by dylan on 13/01/16.
 */
class GeneratorUtilsSpec extends Specification {
    def "AnyFinite"() {


    }

    @Unroll
    def 'all iterator is finite or empty'() {
        expect:
            GeneratorUtils.allFinite(iterables) == expected
        where:
            iterables                                   || expected
            [['a', 'b'], [1, 2], [5, 6, 7]]             || false
            [[]]                                        || true
            [[], [1, 2, 3, 4, 5]]                       || false
            [new IterableGenerator([1, 2, 3, 4, 5])]     || true
            [new IterableGenerator([1, 2, 3, 4, 5]), []] || true
            [new IntegerGenerator(), []]                || false
    }

    @Unroll
    def 'any iterator is finite or empty'() {
        expect:
            GeneratorUtils.anyFinite(iterators) == expected
        where:
            iterators                                   || expected
            [['a', 'b'], [1, 2], [5, 6, 7]]             || false
            [[]]                                        || true
            [[], [1, 2, 3, 4, 5]]                       || true
            [new IterableGenerator([1, 2, 3, 4, 5])]     || true
            [new IterableGenerator([1, 2, 3, 4, 5]), []] || true
            [new IntegerGenerator(), []]                || true
    }

    @Unroll
    def 'is finite iterator'() {
        expect:
            GeneratorUtils.isFinite([]) == true
            GeneratorUtils.isFinite([1]) == false
    }

    @Unroll
    def 'is finite implements Generator'() {
        setup:
            //anonymous class issue with where variables
            def has = hasNext
            def finite = isFinite

            def generator = new Generator() {
                UnmodifiableIterator iterator() {
                    new UnmodifiableIterator() {
                        boolean hasNext() { has }
                        def next() {}
                    }
                }
                boolean isFinite() { finite }
            }
        expect:
            GeneratorUtils.isFinite(generator) == expected
        where:
            hasNext | isFinite || expected
            true    | true     || true
            false   | true     || true
            true    | false    || false
            false   | false    || true
    }
}

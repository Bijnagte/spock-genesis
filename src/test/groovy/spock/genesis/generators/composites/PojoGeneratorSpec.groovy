package spock.genesis.generators.composites

import spock.genesis.Gen
import spock.genesis.generators.test.Pojo
import spock.lang.Specification

class PojoGeneratorSpec extends Specification {

    def 'has next true if param generator has next'() {
        setup:
            def generator = new PojoGenerator(null, iterable)
        expect:
            iterable.iterator().hasNext() == generator.iterator().hasNext()
        where:
            iterable << [[1], []]
    }

    def 'make tuple constructor object'() {
        setup:
            def tuple = [string, integer]
            def generator = new PojoGenerator(TupleConstructorObj, [tuple])

        when:
            def result = generator.iterator().next()
        then:
            result instanceof TupleConstructorObj
            result.string == string
            result.integer == integer
        where:
            string | integer
            'A'    | 0
            null   | 0
            'B'    | null
    }

    def 'make map constructor object'() {
        setup:
            def map = [string: string, integer: integer]
            def generator = new PojoGenerator(MapConstructorObj, [map])
        when:
            def result = generator.iterator().next()
        then:
            result instanceof MapConstructorObj
            result.string == string
            result.integer == integer
        where:
            string | integer
            'A'    | 0
            null   | 0
            'B'    | null

    }

    def 'make default constructor object'() {
        setup:
            def map = [string: string, integer: integer]
            def generator = new PojoGenerator(DefaultConstructorObj, [map])
        when:
            def result = generator.iterator().next()
        then:
            result instanceof DefaultConstructorObj
            result.string == string
            result.integer == integer
        where:
            string | integer
            'A'    | 0
            null   | 0
            'B'    | null
    }

    def 'make default constructor final java object'() {
        setup:
            def map = [a: 'A']
            def generator = new PojoGenerator(Pojo, [map])
        when:
            Pojo result = generator.iterator().next()
        then:
            result.a == 'A'
    }

    def 'make default constructor object with extra arg in map'() {
        setup:
            def map = [a: 'A', i: 1]
            def generator = new PojoGenerator(Pojo, [map])
        when:
            generator.iterator().next()
        then:
            def ex = thrown(MissingPropertyException)
            ex.message.contains('No such property: i for class')
    }

    def 'make single arg constructor object'() {
        setup:
            def generator = new PojoGenerator(SingleArgConstructorObj, [arg])
        when:
            def result = generator.iterator().next()
        then:
            result instanceof SingleArgConstructorObj
            if (arg instanceof String) {
                result.string == arg
            } else if (arg instanceof Integer) {
                result.integer == arg
            } else {
                assert false
            }
        where:
            arg << ['A', 1, 'Test', 200]
    }

    def 'make var args constructor object succeeds'() {
        setup:
            Integer[] args = [10, 1, 77, 200]
            def generator = new PojoGenerator(VarArgsConstructorObj, [args])
        when:
            def result = generator.iterator().next()
        then:
            result instanceof VarArgsConstructorObj
            result.args == args
    }

    def 'make var args constructor object succeeds if array wrapped in a list'() {
        setup:
            Integer[] args = [10, 1, 77, 200]
            def argList = [args]
            def generator = new PojoGenerator(VarArgsConstructorObj, [argList])
        when:
            def result = generator.iterator().next()
        then:
            result instanceof VarArgsConstructorObj
            result.args == args
    }

    def 'setting seed returns the same values with 2 generators configured the same'() {
        setup:
            def generatorA = new PojoGenerator(Pojo, Gen.map(a: Gen.string)).seed(seed).take(10).realized
            def generatorB = new PojoGenerator(Pojo, Gen.map(a: Gen.string)).seed(seed).take(10).realized
        expect:
            generatorA == generatorB
        where:
            seed = 100L

    }

    static class TupleConstructorObj {
        String string
        Integer integer

        TupleConstructorObj(String string, Integer integer) {
            this.string = string
            this.integer = integer
        }
    }

    static class MapConstructorObj {
        String string
        Integer integer

        MapConstructorObj(Map args) {
            string = args.string
            integer = args.integer
        }
    }

    static class VarArgsConstructorObj {
        Integer[] args

        VarArgsConstructorObj(Integer... args) {
            this.args = args
        }
    }

    static class DefaultConstructorObj {
        String string
        Integer integer
    }

    static class SingleArgConstructorObj {
        String string
        Integer integer

        SingleArgConstructorObj(String string) {
            this.string = string
        }

        SingleArgConstructorObj(Integer integer) {
            this.integer = integer
        }
    }
}
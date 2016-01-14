package spock.genesis

import groovy.transform.Immutable
import spock.genesis.extension.ExtensionMethods
import spock.genesis.generators.Generator
import spock.genesis.generators.MultiSourceGenerator
import spock.lang.Specification
import spock.lang.Unroll
import spock.util.mop.Use

class SamplesSpec extends Specification {

    def 'using static factory methods'() {
        expect:
            Gen.string.next() instanceof String
            Gen.bytes.next() instanceof byte[]
            Gen.double.next() instanceof Double
            Gen.integer.next() instanceof Integer
            Gen.long.next() instanceof Long
            Gen.character.next() instanceof Character
            Gen.date.next() instanceof Date
    }

    def 'create multi source generator with & operator'() {
        setup:
            def gen = Gen.string(100) & Gen.integer
        expect:
            gen instanceof MultiSourceGenerator
            gen.any { it instanceof Integer }
            gen.any { it instanceof String }

    }

    def 'multiply by int limits the quantity generated'() {
        setup:
            def gen = Gen.string * 3
        when:
            def results = gen.collect()
        then:
            results.size() == 3
    }

    @Use(ExtensionMethods)
    def 'multiply int by generator limits the quantity generated'() {
        setup:
            def gen = 3 * Gen.string
        when:
            def results = gen.collect()
        then:
            results.size() == 3
    }

    @Unroll('convert #source.class to generator')
    @Use(ExtensionMethods) //this Extension will be added by the groovy runtime
    def 'convert Collection, Array, Iterator, and Iterable to generator using method added by extension'() {
        setup:
            def gen = source.toGenerator()
        expect:
            Generator.isAssignableFrom(gen.class)
        when:
            def results = gen.collect()
        then:
            results == [1, 2, 3]
        where:
            source << [
                    [1, 2, 3],
                    [1, 2, 3].toArray(),
                    [1, 2, 3].iterator(),
                    new Iterable() { Iterator iterator() { [1, 2, 3].iterator() } }
            ]
    }

    @Use(ExtensionMethods)
    def 'convert iterable to generator'() {
        setup:
            def source = [1, 2, 3]
            def iterable = new Iterable() {
                Iterator iterator() { source.iterator() }
            }

            def gen = iterable.toGenerator()
        expect:
            iterable instanceof Collection == false
            Generator.isAssignableFrom(gen.class)
        when:
            def results = gen.collect()
        then:
            results == source
    }

    static class Data {
        String s
        Integer i
        Date d
    }

    def 'generate type with map'() {
        setup:
            def gen = Gen.type(Data, s: Gen.string, i: Gen.integer, d: Gen.date)
        when:
            Data result = gen.next()
        then:
            result.d
            result.i
            result.s
    }

    def 'generate type then call method on instance'() {
        setup:
            def gen = Gen.type(Data, i: Gen.integer, d: Gen.date).map { it.s = it.toString(); it }
        when:
            Data result = gen.next()
        then:
            result.d
            result.i
            result.s == result.toString()
    }

    static class TupleData {
        String s
        Integer i
        Date d

        TupleData(String s, Integer i, Date d) {
            this.s = s
            this.i = i
            this.d = d
        }
    }

    def 'generate type with tuple'() {
        setup:
            def gen = Gen.type(TupleData, Gen.string, Gen.value(42), Gen.date)
        when:
            def result = gen.next()
        then:
            result instanceof TupleData
            result.d
            result.i == 42
            result.s
    }

    def 'generate with factory'() {
        setup:
            def gen = Gen.using { new Date(42) }
        expect:
            gen.next() == new Date(42)
    }

    def 'call methods on generated value using with'() {
        setup:
            def gen = Gen.date.with { setTime(1400) }

        expect:
            gen.next().getTime() == 1400
    }

    def 'generate from multiple iterators in sequence'() {
        setup:
            def gen = Gen.these(1, 2, 3).then([4, 5])
        expect:
            gen.collect() == [1, 2, 3, 4, 5]
    }

    def 'generate a value once'() {
        setup:
            def gen = Gen.once(value)
        expect:
            gen.collect() == [value]
        where:
            value << [null, 1, 'b', [1,2]]
    }

    def 'generate a value repeatedly'() {
        setup:
            def gen = Gen.value(null).take(100)
        when:
            def result = gen.collect()
        then:
            result.size() == 100
            result.every { it == null }
    }

    def 'generate a random value from specified values'() {
        setup:
            def range = 1..100
            def gen = Gen.any(range)
        when: 'generate a list for each value until it is generated'
            def results = range.collect { num ->
                gen.takeWhile { it != num }.collect()
            }
        then: 'at least one should have gotten multiple results before finding the value'
            results.any { it.size() > 1 }
        and: 'all results should be from the supplied values'
            results.flatten().every { it in range }
    }

    def 'generate a string using a regular expression'() {
        setup:
            String regex = '(https?|ftp|file)://[-A-Z0-9+&@#/%?=~_|!:,.;]*[-A-Z0-9+&@#/%=~_|]\\d'
        expect:
            Gen.string(~regex).next() ==~ regex
    }

    @Immutable
    static class Person {
        int id
        String name
        String title
        Date birthDate
        char gender
    }

    def 'complex pogo'() {
        expect:
            person instanceof Person
            person.gender in ['M', 'F', 'T', 'U'].collect { it as char }
            person.id > 199
            person.id < 10001
            person.birthDate >= Date.parse('MM/dd/yyyy', '01/01/1940')
            person.birthDate <= new Date()

        where:
            person << Gen.type(Person,
                    id: Gen.integer(200..10000),
                    name: Gen.string(~/[A-Z][a-z]+( [A-Z][a-z]+)?/),
                    birthDate: Gen.date(Date.parse('MM/dd/yyyy', '01/01/1940'), new Date()),
                    title: Gen.these('', null).then(Gen.any('Dr.', 'Mr.', 'Ms.', 'Mrs.')),
                    gender: Gen.character('MFTU')
            ).take(3)
    }
}

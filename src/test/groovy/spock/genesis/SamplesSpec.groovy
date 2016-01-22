package spock.genesis

import groovy.transform.Immutable
import spock.genesis.extension.ExtensionMethods
import spock.genesis.generators.Generator
import spock.genesis.generators.MultiSourceGenerator
import spock.lang.Specification
import spock.lang.Unroll
import spock.util.mop.Use


//static import generator factory methods
import static spock.genesis.Gen.*

class SamplesSpec extends Specification {

    def 'using static factory methods'() {
        expect:
            string.next() instanceof String
            bytes.next() instanceof byte[]
            getDouble().next() instanceof Double
            integer.next() instanceof Integer
            getLong().next() instanceof Long
            character.next() instanceof Character
            date.next() instanceof Date
    }

    def 'create multi source generator with & operator'() {
        setup:
            def gen = string(100) & integer
        expect:
            gen instanceof MultiSourceGenerator
            gen.any { it instanceof Integer }
            gen.any { it instanceof String }
    }

    def 'multiply by int limits the quantity generated'() {
        setup:
            def gen = string * 3
        when:
            def results = gen.collect()
        then:
            results.size() == 3
    }

    @Use(ExtensionMethods)
    def 'multiply int by generator limits the quantity generated'() {
        setup:
            def gen = 3 * string
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
            def gen = type(Data, s: string, i: integer, d: date)
        when:
            Data result = gen.next()
        then:
            result.d
            result.i
            result.s
    }

    def 'generate type then call method on instance'() {
        setup:
            def gen = type(Data, i: integer, d: date).map { it.s = it.toString(); it }
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
            def gen = type(TupleData, string, value(42), date)
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
            def gen = using { new Date(42) }
        expect:
            gen.next() == new Date(42)
    }

    def 'call methods on generated value using with'() {
        setup:
            def gen = date.with { setTime(1400) }

        expect:
            gen.next().getTime() == 1400
    }

    def 'generate from multiple iterators in sequence'() {
        setup:
            def gen = these(1, 2, 3).then([4, 5])
        expect:
            gen.collect() == [1, 2, 3, 4, 5]
    }

    enum Days {
        SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY
    }

    def 'generate from an enum'() {
        setup:
            def gen = these Days
        expect:
            gen.collect() == Days.collect()
    }

    def 'generate a value once'() {
        setup:
            def gen = once value
        expect:
            gen.collect() == [value]
        where:
            value << [null, 1, 'b', [1,2]]
    }

    def 'generate a value repeatedly'() {
        setup:
            def gen = value(null).take(100)
        when:
            def result = gen.collect()
        then:
            result.size() == 100
            result.every { it == null }
    }

    def 'generate a random value from specified values'() {
        setup:
            def range = 1..100
            def gen = any range
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
            string(~regex).next() ==~ regex
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
            person << type(Person,
                    id: integer(200..10000),
                    name: string(~/[A-Z][a-z]+( [A-Z][a-z]+)?/),
                    birthDate: date(Date.parse('MM/dd/yyyy', '01/01/1940'), new Date()),
                    title: these('', null).then(Gen.any('Dr.', 'Mr.', 'Ms.', 'Mrs.')),
                    gender: character('MFTU')
            ).take(3)
    }
}

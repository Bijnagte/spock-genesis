package spock.genesis

import groovy.transform.Immutable
import spock.genesis.extension.ExtensionMethods
import spock.genesis.generators.Generator
import spock.genesis.generators.MultiSourceGenerator
import spock.lang.Specification
import spock.lang.Unroll
import spock.util.mop.Use


// tag::genimport[]
//static import generator factory methods
import static spock.genesis.Gen.*
// end::genimport[]

class SamplesSpec extends Specification {

    // tag::factorymethods[]
    def 'using static factory methods'() {
        expect:
            string.iterator().next() instanceof String
            bytes.iterator().next() instanceof byte[]
            getDouble().iterator().next() instanceof Double
            integer.iterator().next() instanceof Integer
            getLong().iterator().next() instanceof Long
            character.iterator().next() instanceof Character
            date.iterator().next() instanceof Date
    }
    // end::factorymethods[]

    // tag::stringlength[]
    def 'create a string by length'() {
        when: 'establishing max string length'
            def shortWord = string(5).iterator().next() // <1>

        then: 'word size should be less equal than max'
            shortWord.size() <= 5

        when: 'establishing min and max word size'
            def largerWord = string(5,10).iterator().next() // <2>

        then: 'word should be larger equal min'
            largerWord.size() >= 5

        and: 'word should be less equal max'
            largerWord.size() <= 10
    }
    // end::stringlength[]

    // tag::numbers[]
    def 'generate numbers'() {
        expect:
            getDouble().iterator().next() instanceof Double
            integer.iterator().next() instanceof Integer
            getLong().iterator().next() instanceof Long
            bytes.iterator().next() instanceof byte[]
    }
    // end::numbers[]

    // tag::integerlength[]
    def 'create an integer with min and max'() {
        when: 'establishing max possible number'
            def firstNumber = integer(5..10).iterator().next() // <1>

        then: 'generated number will be less equals than max'
            firstNumber >= 5
            firstNumber <= 10

        when: 'establishing min and max valid numbers'
            def secondNumber = integer(5,10).iterator().next() // <2>

        then: 'generated number must be between both numbers'
            secondNumber >= 5
            secondNumber <= 10
    }
    // end::integerlength[]

    // tag::value[]
    def 'create a value using the value() method'() {
        expect: 'to get several copies of a value'
        value(0).take(2).collect() == [0,0]

        and: 'to get just one'
        value(0).iterator().next() == 0
    }
    // end::value[]

    // tag::dates[]
    def 'create a new date value range'() {
        given: "yesterday's reference and tomorrow's"
        def yesterday = new Date() - 1
        def tomorrow  = new Date() + 1

        when: 'getting a new date'
        def newDate = date(yesterday, tomorrow).iterator().next()

        then: 'new date should be between boundaries'
        tomorrow.after(newDate)
        newDate.after(yesterday)
    }
    // end::dates[]

    // tag::ampersand[]
    def 'create multi source generator with & operator'() {
        setup:
            def gen = string(100) & integer
        expect:
            gen instanceof MultiSourceGenerator
            gen.any { it instanceof Integer }
            gen.any { it instanceof String }
    }
    // end::ampersand[]

    // tag::multiplyby[]
    def 'multiply by int limits the quantity generated'() {
        setup:
            def gen = string * 3
        when:
            def results = gen.collect()
        then:
            results.size() == 3
    }
    // end::multiplyby[]

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

    // tag::tuple[]
    def 'generate a tuple'() {
        when: 'generating a tuple of numbers'
            def tuple = tuple(integer, integer, string).iterator().next()

        then: 'make sure we get a list of the expected size'
            tuple.size() == 3

        and: 'the type of the members are the expected'
            tuple.first() instanceof Integer
            tuple.get(1) instanceof Integer
            tuple.last() instanceof String
    }
    // end::tuple[]

    // tag::simplelist[]
    def 'generate a simple list'() {
        when: 'generating a simple list'
            def list = list(integer).iterator().next() // <1>

        then: 'we only can be sure about the type of the list'
            list instanceof List

        and: 'the type of elements due to the value generator used'
            list.every { it instanceof Integer }
    }
    // end::simplelist[]

    // tag::sizedlist[]
    def 'generate a list with size boundaries'() {
        when: 'establishing the list definition'
            def list = list(integer, 1, 5).iterator().next() // <1>

        then: 'it should obey the following assertions'
            list instanceof List                  // <2>
            list.size() >= 1                      // <3>
            list.size() <= 5                      // <4>
            list.every { it instanceof Integer }  // <5>

    }
    // end::sizedlist[]

    // tag::mapgenerator[]
    def 'generate a map'() {
        when: 'defining a map with different fields'
            def myMap = map(                            // <1>
                id: getLong(),                          // <2>
                name: string,                           // <3>
                age: integer(0, 120)).iterator().next() // <4>

        then: 'we should get instances of map'
            myMap instanceof Map

        and: 'the fields should follow the generators rules'
            myMap.id instanceof Long
            myMap.name instanceof String
            myMap.age instanceof Integer
    }
    // end::mapgenerator[]

    // tag::data[]
    static class Data {
        String s
        Integer i
        Date d
    }
    // end::data[]

    // tag::typegenerator[]
    def 'generate type with map'() {
        setup:
            def gen = type(Data, s: string, i: integer, d: date) // <1>
        when:
            Data result = gen.iterator().next() // <2>
        then:
            result.d
            result.i
            result.s
    }
    // end::typegenerator[]

    def 'generate type then call method on instance'() {
        expect:
            result instanceof Data
            result.d
            result.i
            result.s == result.toString()
        where:
            result << type(Data, i: integer, d: date).map { it.s = it.toString(); it }.take(10)
    }

    // tag::tupledata[]
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
    // end::tupledata[]

    // tag::typegenerator2[]
    def 'generate type with tuple'() {
        expect:
            result instanceof TupleData
            result.d
            result.i == 42
            result.s

        where:
            result << type(TupleData, string, value(42), date).take(5)
    }
    // end::typegenerator2[]

    def 'generate with factory'() {
        expect:
            result == new Date(42)
        where:
            result << using { new Date(42) }.take(1)
    }

    // tag::datewith[]
    def 'call methods on generated value using with'() {
        setup:
            def gen = date.with { setTime(1400) }

        expect:
            gen.iterator().next().getTime() == 1400
    }
    // end::datewith[]

    // tag::these[]
    def 'generate from a specific set of values'() {
        expect: 'to get numbers from a varargs'
            these(1,2,3).take(3).collect() == [1,2,3]

        and: 'to get values from an iterable object such as a list'
            these([1,2,3]).take(2).collect() == [1,2]

        and: 'to get values from a given class'
            these(String).iterator().next() == String

        and: 'to stop producing numbers if the source is exhausted'
            these(1..3).take(10).collect() == [1,2,3]
    }
    // end::these[]

    // tag::then[]
    def 'generate from multiple iterators in sequence'() {
        setup:
            def gen = these(1, 2, 3).then([4, 5])
        expect:
            gen.collect() == [1, 2, 3, 4, 5]
    }
    // end::then[]

    // tag::fromenum[]
    enum Days {
        SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY
    }

    def 'generate from an enum'() {
        setup:
            def gen = these Days
        expect:
            gen.collect() == Days.collect()
    }
    // end::fromenum[]

    // tag::once[]
    def 'generate a value once'() {
        setup:
            def gen = once value
        expect:
            gen.collect() == [value]
        where:
            value << [null, 1, 'b', [1,2]]
    }
    // end::once[]

    // tag::take[]
    def 'generate a value repeatedly'() {
        setup:
            def gen = value(null).take(100)
        when:
            def result = gen.collect()
        then:
            result.size() == 100
            result.every { it == null }
    }
    // end::take[]

    // tag::any[]
    def 'generate any value from a given source'() {
        given: 'a source'
            def source = [1,2,null,3]

        expect: 'only that the generated value is any of the elements'
            Gen.any(source).take(2).every { n -> n in source }
    }
    // end::any[]

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

    // tag::stringregex[]
    def 'generate a string using a regular expression'() {
        expect:
            generatedString ==~ '(https?|ftp|file)://[-A-Z0-9+&@#/%?=~_|!:,.;]*[-A-Z0-9+&@#/%=~_|]\\d'
        where:
            generatedString << string(~'(https?|ftp|file)://[-A-Z0-9+&@#/%?=~_|!:,.;]*[-A-Z0-9+&@#/%=~_|]\\d').take(10)
    }
    // end::stringregex[]

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
            person.birthDate >= Date.parse('MM/dd/yyyy', '01/01/1980')
            person.birthDate <= new Date()

        where:
            person << type(Person,
                    id: integer(200..10000),
                    name: string(~/[A-Z][a-z]+( [A-Z][a-z]+)?/),
                    birthDate: date(Date.parse('MM/dd/yyyy', '01/01/1980'), new Date()),
                    title: these('', null).then(Gen.any('Dr.', 'Mr.', 'Ms.', 'Mrs.')),
                    gender: character('MFTU')
            ).take(3)
    }
}

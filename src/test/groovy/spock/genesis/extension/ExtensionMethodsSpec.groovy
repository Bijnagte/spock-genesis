package spock.genesis.extension

import groovy.transform.CompileStatic
import spock.genesis.Gen
import spock.genesis.generators.Generator
import spock.lang.Specification
import spock.lang.Unroll

class ExtensionMethodsSpec extends Specification {

    @Unroll
    def 'to generator method on #type returns a generator'() {
        when:
            Generator gen = source.toGenerator()
        then:
            gen.realized == expected
        and: 'calling to generator is an identity method'
            gen.toGenerator().is(gen)
        where:
            type             | source               || expected
            'List'           | [1, 2, 3]            || [1, 2, 3]
            'Array'          | [1, 2, 3].toArray()  || [1, 2, 3]
            'Set'            | [1, 2, 3].toSet()    || [1, 2, 3]
            'Iterator'       | [1, 2, 3].iterator() || [1, 2, 3]
            'Enum'           | Option               || [Option.YES, Option.NO]
            'non-enum Class' | Map                  || [Map]
    }

    enum Option {
        YES, NO
    }

    def 'to generator on generator returns the original generator through static method' () {
        expect:
            toGenerator(generator).is(generator)
        where:
            generator << [Gen.string, Gen.bytes, Gen.once('foo'), Gen.double, Gen.list(Gen.integer)]
    }

    @CompileStatic
    static Generator toGenerator(Iterable iterable) {
        iterable.toGenerator()
    }

}

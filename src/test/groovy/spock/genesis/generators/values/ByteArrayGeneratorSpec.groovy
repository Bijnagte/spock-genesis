package spock.genesis.generators.values

import spock.lang.Specification

class ByteArrayGeneratorSpec extends Specification {

    def 'setting seed produces the same sequences for different generators' () {
        given:
            def a = new ByteArrayGenerator().seed(seed).take(100).realized
            def b = new ByteArrayGenerator(ByteArrayGenerator.DEFAULT_LENGTH_LIMIT).seed(seed).take(100).realized
            def c = new ByteArrayGenerator(0, ByteArrayGenerator.DEFAULT_LENGTH_LIMIT).seed(seed).take(100).realized
            def d = new ByteArrayGenerator((0..ByteArrayGenerator.DEFAULT_LENGTH_LIMIT) as IntRange).seed(seed).take(100).realized
        expect:
            a == b
            b == c
            c == d
        where:
            seed << [Long.MIN_VALUE, 100, Long.MAX_VALUE]
    }
}

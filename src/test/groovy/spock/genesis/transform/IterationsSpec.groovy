package spock.genesis.transform

import spock.genesis.Gen
import spock.lang.Specification
import spock.lang.Stepwise


@Stepwise
// tag::class[]
@Iterations(5)
class IterationsSpec extends Specification {

    static List NUMBERS = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11]

    static List<Integer> VALUES = []

    def 'method iterations is limited by class annotation'() {
        expect:
            value < 6
        where:
            value << NUMBERS
    }
// end::class[]

    @Iterations(2)
    def 'method annotation less than class annotation'() {
        expect:
            value < 3
        where:
            value << NUMBERS
    }

    @Iterations(7)
    def 'method annotation more than class annotation'() {
        given:
            VALUES.add(value)
        expect:
            value < 8
        where:
            value << NUMBERS
    }

    def 'VALUES has 7 entries at this point'() {
        expect:
            VALUES.size() == 7
        when: 'reset'
            VALUES.clear()
        then:
            VALUES.size() == 0
    }

    @Iterations(2)
    def 'annotation works for data tables'() {
        expect:
            value == expected
        where:
            value | expected
            true  | true
            true  | true
            true  | false //not reachable
    }

    @Iterations(2)
    def 'annotation works for multiple assignment'() {
        expect:
            value == expected
            value < 3
        where:
            [value, expected] << Gen.tuple(NUMBERS, NUMBERS)
    }

    @Iterations(2)
    def 'annotation works for multiple data providers'() {
        expect:
            value == expected
            value < 3
        where:
            value << NUMBERS
            expected << NUMBERS
    }

    @Iterations(4)
    def 'fixed value in where does not affect the iterations'() {
        given:
            VALUES.add(value)
        expect:
            value < 5
        where:
            other = 9
            value << NUMBERS
    }

    def 'VALUES has 4 entries at this point'() {
        expect:
            VALUES.size() == 4
        when: 'reset'
            VALUES.clear()
        then:
            VALUES.size() == 0
    }
}

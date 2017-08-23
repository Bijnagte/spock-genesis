package spock.genesis.generators

import spock.lang.Specification
import static spock.genesis.Gen.*

class ChainedGeneratorSpec extends Specification {

    def "The output of the first generator is used to create the second"() {
        setup:
        final chained = any(10, 20).flatMap { string(it, it) }

        when:
        final result = chained.take(100).realized

        then:
        result.any { it.size() == 10 }
        result.any { it.size() == 20 }
        result.forEach { assert it.size() == 10 || it.size() == 20 }
    }
}

package spock.genesis.generators.values

import spock.lang.Specification

class ValueGeneratorSpec extends Specification {

	def 'always returns the same value'() {
		setup:
			def generator = new ValueGenerator(value)
		expect:
			generator.take(20).every { it.is(value) }
		where:
			value << [[], [some: 'map'], 1, null, new Date()]
		
	}
}

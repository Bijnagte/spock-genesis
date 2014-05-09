package spock.genesis.generators.composites

import spock.genesis.generators.values.IntegerGenerator
import spock.genesis.generators.values.NullGenerator
import spock.genesis.generators.values.StringGenerator
import spock.lang.Specification

class ListGeneratorSpec extends Specification {

	
	def 'list generator next'() {
		setup:
			def generator = new ListGenerator(gen, limit)
		when:
			def result = generator.next()
		then:
			result.size() <= limit
		where:
			gen						| limit
			new IntegerGenerator()	| 40
			new StringGenerator(20)	| 40
			new NullGenerator()		| 1
			new IntegerGenerator()	| 0
	}
}

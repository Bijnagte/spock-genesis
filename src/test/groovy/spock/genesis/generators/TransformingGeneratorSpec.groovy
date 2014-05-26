package spock.genesis.generators

import spock.lang.Specification

class TransformingGeneratorSpec extends Specification {

	Iterator supplier = Mock()
	
	def 'transforming generator calls closure with next value'() {
		setup:
			def transform = { val -> val + 1 }
			def generator = new TransformingGenerator(supplier, transform)
		when:
			def result =  generator.next()
		then:
			1 * supplier.next() >> 20
			result == 21
	}
}

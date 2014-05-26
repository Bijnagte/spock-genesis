package spock.genesis.generators

import spock.lang.Specification

class FilteredGeneratorSpec extends Specification {
	
	Iterator supplier = Mock()
	
	def 'calls supplier until a result that matches the predicate is found'() {
		setup:
			def predicate = { it == 7 }
			def generator = new FilteredGenerator(supplier, predicate)
			supplier.hasNext() >> true
		when:
			def result = generator.next()
		then:
			result == 7
			3 * supplier.next() >>> [2, 4, 7, 8]
	}

	def 'all values are returned even with extra calls to has next'() {
		setup:
			def predicate = {arg -> true }
			def generator = new FilteredGenerator(supplier, predicate)
			supplier.hasNext() >> true
			supplier.next() >>> [2, 4, 7]
		when:
			generator.hasNext()
			def result = generator.next()
		then:
			result == 2
		when:
			generator.hasNext()
			generator.hasNext()
			result = generator.next()
		then:
			result == 4
	}
	
	def 'has next is false if no match can be found before supplier is empty'() {
		setup:
			def predicate = { it == 1 }
			def supplier = [2, 3, 4].iterator()
			def generator = new FilteredGenerator(supplier, predicate)
		expect:
			generator.hasNext() == false
			supplier.hasNext() == false
	}

}

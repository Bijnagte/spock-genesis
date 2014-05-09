package spock.genesis.generators

import spock.lang.Specification

class MultiSourceGeneratorSpec extends Specification {

	
	def 'multiple iterators run until all are empty'() {
		setup:
			def generator = new MultiSourceGenerator(iterables*.iterator())
			def expectedCount = iterables*.size().sum()
		when:
			def result = generator.realized
			
		then: 'all of the elements were returned'
			result.size() == expectedCount
			iterables.every { list ->
				list.every { it in result }
			}
		and: 'the order does not match'
			result != iterables.flatten() 
		where:
			iterables << [
				[['a', 'b', 'c'],[1, 2, 3, 4, 5, 6]],
				[[null, 'b', 'c', [key:'value']],[1, 2, 3, new Date()]] 
				]
	}
}

package spock.genesis.generators.values

import spock.lang.Specification

class IntegerGeneratorSpec extends Specification {
	
	def 'test limited generation'() {
		setup:
			def generator = new IntegerGenerator().take(limit)
		when:
			def results = generator.realized
		then:
			results.size() == limit
			results.every { 
				it instanceof Integer
			}
		
		where:
			limit << [0, 20, 500, 20000]
			
	}
	
	def 'test limited range generation'() {
		setup:
			def range = max - min
			def generator = new IntegerGenerator(min, max).take(range * 2)
		when:
			def results = generator.realized
		then:
			results.size() == range * 2
			
			results.every {
				it <= max &&
				it >= min
			}
		and: 'there are negative results in moderate samples'
			results.size() > 10 ? results.any { it < 0 } : true
		
		where:
			min		| max
			0		| 1
			-1		| 1
			-76080 	| 8000
	}
	
	def 'test filter'() {
		setup:
			def generator = new IntegerGenerator().filter { it % 2 == 0 }.map { [it] }.take(2).repeat.take(limit)
		when:
			def results = generator.realized
		then:
			results.size() == limit
		where:
			limit << [0, 20]
	}
	
	def 'test with nulls'() {
		setup:
			int qty = perNull * 1000
			def targetNullRate = 1.0 / (perNull + 1)
			def generator = new IntegerGenerator().withNulls(perNull).take(qty)
		when:
			def results = generator.realized
		then:
			results.size() == qty
		and:
			int nulls = results.count(null)
			(nulls / qty) < (1.1 * targetNullRate)
			(nulls / qty) > (0.9 * targetNullRate)
		where:
			perNull << [5, 10]
		
	}
}

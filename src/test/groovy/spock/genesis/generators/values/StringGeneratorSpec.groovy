package spock.genesis.generators.values

import spock.lang.Specification

class StringGeneratorSpec extends Specification {

	def 'test make string generation'() {
		setup:
			def generator = new StringGenerator(length, potentialCharacters)
		when:
			String result = generator.next()
		then:
			Set potential = potentialCharacters.toSet()
			result.size() <= length
			result.every {
				it in potential
			}
		
		where:
			length	| potentialCharacters
			0		| 'a'
			1		| 'a'
			100		| 'abc5$@'
			100		| ('A'..'z').collect()
	}
	
	def 'default random string generation'() {
		setup:
			def generator = new StringGenerator()
			Set potential = CharacterGenerator.DEFAULT_CHARACTERS.toSet()*.toString()
		when:
			String result = generator.next()
		then:
			result.size() <= StringGenerator.DEFAULT_LENGTH_LIMIT
			result.every {				
				it in potential
			}
		where:
			iteration << (0..1000)
	}
}

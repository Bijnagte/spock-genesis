package spock.genesis.generators.composites

import spock.genesis.generators.test.CloseableIterator
import spock.genesis.generators.values.IntegerGenerator
import spock.genesis.generators.values.StringGenerator
import spock.lang.Specification

class RandomMapGeneratorSpec extends Specification {

	def 'generates keys and values of the expected types'() {
		setup:
			def generator = new RandomMapGenerator(new StringGenerator(10), new IntegerGenerator())
		when:
			Map result = generator.next()
		then:
			result.size() > 0
 			result.each { key, value ->
				key instanceof String
				value instanceof Integer
			}
	}
	
	def 'calling close does nothing if the generators have no close method'() {
		setup:
			Iterator keyGenerator = Mock()
			Iterator valueGenerator = Mock()
			
			def generator = new RandomMapGenerator(keyGenerator, valueGenerator)
		when:
			generator.close()
		then:
			0 * keyGenerator._
			0 * valueGenerator._
	}
	
	def 'calling close calls close on key and value generators'() {
		setup:
			CloseableIterator keyGenerator = Mock()
			CloseableIterator valueGenerator = Mock()
			def generator = new RandomMapGenerator(keyGenerator, valueGenerator)
		when:
			generator.close()
		then:
			1 * keyGenerator.close()
			1 * valueGenerator.close()
	}
}

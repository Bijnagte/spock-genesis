package spock.genesis.generators

import spock.genesis.generators.test.CloseableIterator
import spock.lang.Specification

class GeneratorDecoratorSpec extends Specification {
	
	def 'calling close does nothing if wrapped generator does not have a close'() {
		setup:
			Iterator wrapped = Mock()
			def generator = new GeneratorDecorator(wrapped)
		when:
			generator.close()
		then:
			0 * wrapped._
	}
	
	def 'calling close closes wrapped generator'() {
		setup:
			CloseableIterator wrapped = Mock()
			def generator = new GeneratorDecorator(wrapped)
		when:
			generator.close()
		then:
			1 * wrapped.close()
	}
	
	def 'has next delegates to wrapped generator'() {
		setup:
			Iterator wrapped = Mock()
			def generator = new GeneratorDecorator(wrapped)
		when:
			generator.hasNext()
		then:
			1 * wrapped.hasNext()
			0 * wrapped._
	}

	def 'next delegates to wrapped generator'() {
		setup:
			Iterator wrapped = Mock()
			def generator = new GeneratorDecorator(wrapped)
		when:
			generator.next()
		then:
			1 * wrapped.next()
			0 * wrapped._
	}
}

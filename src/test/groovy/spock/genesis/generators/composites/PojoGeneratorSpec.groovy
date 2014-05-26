package spock.genesis.generators.composites

import spock.lang.Specification

class PojoGeneratorSpec extends Specification {

	def 'has next true if param generator has next'() {
		setup:
			def iterator = iterable.iterator()
			def generator = new PojoGenerator(null, iterator)
		expect:
			iterator.hasNext() == generator.hasNext()
		where:
			iterable << [[1],[]]
	}
	
	def 'has next false if param generator does not hav'() {
		setup:
			def iterator = [1].iterator()
			def generator = new PojoGenerator(null, iterator)
		expect:
			generator.hasNext()
	}
	
	
	
	def 'make tuple constructor object'() {
		setup:
			def tuple = [string, integer]
			def generator = new PojoGenerator(TupleConstructorObj, [tuple].iterator())
		
		when:
			def result = generator.next()
		then:
			result instanceof TupleConstructorObj
			result.string == string
			result.integer == integer
		where:
			string	| integer
			'A'		| 0
			null	| 0
			'B'		| null
	}

	def 'make map constructor object'() {
		setup:
			def map = [string: string, integer: integer]
			def generator = new PojoGenerator(MapConstructorObj, [map].iterator())		
		when:
			def result = generator.next()
		then:
			result instanceof MapConstructorObj
			result.string == string
			result.integer == integer
		where:
			string	| integer
			'A'		| 0
			null	| 0
			'B'		| null

	}

	def 'make default constructor object'() {
		setup:
			def map = [string: string, integer: integer]
			def generator = new PojoGenerator(DefaultConstructorObj, [map].iterator())
		when:
			def result = generator.next()
		then:
			result instanceof DefaultConstructorObj
			result.string == string
			result.integer == integer
		where:
			string	| integer
			'A'		| 0
			null	| 0
			'B'		| null
	}
	
	def 'make single arg constructor object'() {
		setup:
			def generator = new PojoGenerator(SingleArgConstructorObj, [arg].iterator())
		when:
			def result = generator.next()
		then:
			result instanceof SingleArgConstructorObj
			if (arg instanceof String) {
				result.string == arg
			} else if (arg instanceof Integer) {
				result.integer == arg
			} else {
				assert false
			}
		where:
			arg	<< ['A', 1, 'Test', 200]
	}
	
	def 'make var args constructor object fails'() {
		setup:
			Integer[] args = [10, 1, 77, 200]
			def generator = new PojoGenerator(VarArgsConstructorObj, [args].iterator())
		when:
			def result = generator.next()
		then:
			thrown(IllegalArgumentException)
	}
	
	def 'make var args constructor object succeeds if array wrapped in a list'() {
		setup:
			Integer[] args = [10, 1, 77, 200]
			def argList = [args]
			def generator = new PojoGenerator(VarArgsConstructorObj, [argList].iterator())
		when:
			def result = generator.next()
		then:
			result instanceof VarArgsConstructorObj
			result.args == args
	}
	
	static class TupleConstructorObj {
		String string
		Integer integer

		TupleConstructorObj(String string, Integer integer) {
			this.string = string
			this.integer = integer
		}
	}
	
	static class MapConstructorObj {
		String string
		Integer integer

		MapConstructorObj(Map args) {
			string = args.string
			integer = args.integer	
		}
	}
	
	static class VarArgsConstructorObj {
		Integer[] args
		VarArgsConstructorObj(Integer... args) {
			this.args = args
		}
	}
	
	static class DefaultConstructorObj {
		String string
		Integer integer
	}
	
	static class SingleArgConstructorObj {
		String string
		Integer integer
		
		SingleArgConstructorObj(String string) {
			this.string = string	
		}
		
		SingleArgConstructorObj(Integer integer) {
			this.integer = integer
		}
	}
}
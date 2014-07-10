package spock.genesis

import spock.genesis.extension.ExtensionMethods
import spock.genesis.generators.Generator
import spock.genesis.generators.MultiSourceGenerator
import spock.lang.Specification

class SamplesSpec extends Specification {
	/*
	 * It is not necessary to "use" extension methods in your own code.
	 * They will be registered with the runtime automatically 
	 */
	
	
	def 'using static factory methods'() {
		expect:
			Gen.string.next() instanceof String
			Gen.bytes.next() instanceof byte[]
			Gen.double.next() instanceof Double
			Gen.int.next() instanceof Integer
			Gen.long.next() instanceof Long
			Gen.char.next() instanceof Character
	}
	
	def 'create multi source generator with & operator'() {
		setup:
			def gen = Gen.string & Gen.int
		expect:
			gen instanceof MultiSourceGenerator
			gen.any { it instanceof Integer }
			gen.any { it instanceof String }
			
	}
	
	def 'multiply by int limits the quantity generated'() {
		setup:
			def gen = Gen.string * 3
		when:
			def results = gen.collect()
		then:
			results.size() == 3
	}
	
	def 'multiply int by generator limits the quantity generated'() {
			
		setup:
			def gen
			use (ExtensionMethods) { gen = 3 * Gen.string }
		when:
			def results = gen.collect()
		then:
			results.size() == 3
	}
	
	def 'convert collection to generator'() {
			
		setup:
			def source = [1, 2, 3]
			def gen 
			use (ExtensionMethods) { gen = source.toGenerator() }
		expect:
			Generator.isAssignableFrom(gen.class)
		when:
			def results = gen.collect()
		then:
			results == source
	}
	
	static class Data {
		String s
		Integer i
		Date d	
	}
	
	def 'generate type with map'() {
		setup:
			def gen = Gen.type(Data, s: Gen.string, i: Gen.int, d: Gen.date)
		when:
			Data result = gen.next()
		then:
			result.d
			result.i
			result.s
	}
	
	def 'generate type then call method on instance'() {
		setup:
			def gen = Gen.type(Data, i: Gen.int, d: Gen.date).map { it.s = it.toString(); it }
		when:
			Data result = gen.next()
		then:
			result.d
			result.i
			result.s == result.toString()
	}
	
	static class TupleData {
		String s
		Integer i
		Date d

		TupleData(String s, Integer i, Date d) {
			this.s = s
			this.i = i
			this.d = d
		}		
	}

	def 'generate type with tuple'() {
		setup:
			def gen = Gen.type(TupleData, Gen.string, Gen.value(42), Gen.date )
		when:
			TupleData result = gen.next()
		then:
			result.d
			result.i == 42
			result.s
	}
	
	def 'generate with factory'() {
		setup:
			def gen = Gen.using { new Date(42) }
		expect:
			gen.next() == new Date(42)
	}
	
	def 'call methods on generated value using with'() {
		setup:
			def gen = Gen.date.with { setTime(1400) }
			
		expect:
			gen.next().getTime() == 1400
	}
	
	def 'generate from multiple iterators in sequence'() {
		setup:
			def gen = Gen.these(1, 2, 3).then([4, 5])
		expect:
			gen.collect() == [1, 2, 3, 4, 5]
	}
	
	def 'generate a random value from specified values'() {
		setup:
			def range = 1..100
			def gen = Gen.any(range)
		when: 'generate a list for each value until it is generated'
			def results = range.collect { num ->
				gen.takeWhile { it != num }.collect()
			}
		then: 'at least one should have gotten multiple results before finding the value'
			results.any { it.size() > 1 }
		and: 'all results should be from the supplied values'
			results.flatten().every { it in range }
	}
}

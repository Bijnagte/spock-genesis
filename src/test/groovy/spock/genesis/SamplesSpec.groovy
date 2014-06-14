package spock.genesis

import spock.genesis.generators.MultiSourceGenerator
import spock.lang.Specification

class SamplesSpec extends Specification {

	def 'using static factory methods'() {
		expect:
			Gen.string.next() instanceof String
			Gen.bytes.next() instanceof byte[]
			Gen.double.next() instanceof Double
			Gen.int.next() instanceof Integer
			Gen.long.next() instanceof Long
			Gen.char.next() instanceof Character
	}
	
	def 'multi source with & operator'() {
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
}

package spock.genesis

import spock.genesis.generators.CyclicGenerator
import spock.genesis.generators.FactoryGenerator
import spock.genesis.generators.GeneratorDecorator
import spock.genesis.generators.composites.DefinedMapGenerator
import spock.genesis.generators.composites.PojoGenerator
import spock.genesis.generators.composites.RandomMapGenerator
import spock.genesis.generators.composites.TupleGenerator
import spock.genesis.generators.values.ByteArrayGenerator
import spock.genesis.generators.values.CharacterGenerator
import spock.genesis.generators.values.DateGenerator
import spock.genesis.generators.values.DoubleGenerator
import spock.genesis.generators.values.IntegerGenerator
import spock.genesis.generators.values.LongGenerator
import spock.genesis.generators.values.RandomElementGenerator
import spock.genesis.generators.values.StringGenerator
import spock.genesis.generators.values.ValueGenerator

class Gen {

	static StringGenerator getString() {
		new StringGenerator()
	}

	static ByteArrayGenerator getBytes() {
		new ByteArrayGenerator()
	}

	static IntegerGenerator getInt() {
		new IntegerGenerator()
	}

	static LongGenerator getLong() {
		new LongGenerator()
	}

	static CharacterGenerator getChar() {
		new CharacterGenerator()
	}

	static DoubleGenerator getDouble() {
		new DoubleGenerator()
	}

	static ValueGenerator value(def value) {
		new ValueGenerator(value)
	}

	static RandomElementGenerator<?> any(Collection<?> source) {
		new RandomElementGenerator<?>(source)
	}
	
	static RandomElementGenerator any(Object... source) {
		new RandomElementGenerator(source.toList())
	}

	static CyclicGenerator cycle(Iterator source) {
		new CyclicGenerator(source)
	}

	static CyclicGenerator cycle(Iterable source) {
		new CyclicGenerator(source.iterator())
	}

	static PojoGenerator type(Map<String, Object> keysToValueGenerators, Class target) {
		new PojoGenerator(target, map(keysToValueGenerators))
	}

	static PojoGenerator type(Class target, Iterator... argGenerators) {
		new PojoGenerator(target, tuple(argGenerators))
	}

	static DefinedMapGenerator map(Map keysToValueGenerators) {
		new DefinedMapGenerator(keysToValueGenerators)
	}

	static RandomMapGenerator map(Iterator keyGenerator, Iterator valueGenerator) {
		new RandomMapGenerator(keyGenerator, valueGenerator)
	}

	static TupleGenerator tuple(Iterator... generators) {
		new TupleGenerator(generators)
	}

	static DateGenerator getDate() {
		new DateGenerator()
	}

	static FactoryGenerator using(Closure factory) {
		new FactoryGenerator(factory)
	}
	
	static GeneratorDecorator these(Iterator iterator) {
		new GeneratorDecorator(iterator)
	}
	
	static GeneratorDecorator these(Iterable iterable) {
		these(iterable.iterator())
	}
	
	static GeneratorDecorator these(Object... values) {
		these(values.iterator())
	}
	
}

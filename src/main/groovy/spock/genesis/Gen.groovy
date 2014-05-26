package spock.genesis

import spock.genesis.generators.values.ByteArrayGenerator
import spock.genesis.generators.values.CharacterGenerator
import spock.genesis.generators.values.IntegerGenerator
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
	
	static CharacterGenerator getChar() {
		new CharacterGenerator()
	}
	
	static ValueGenerator value(def value) {
		new ValueGenerator(value)
	}
}

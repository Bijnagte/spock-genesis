package spock.genesis.extension

import spock.genesis.generators.Generator
import spock.genesis.generators.GeneratorDecorator
import spock.genesis.generators.LimitedGenerator

class ExtensionMethods {

	static LimitedGenerator multiply(Integer qty, Generator generator) {
		generator * qty
	}

	static LimitedGenerator multiply(BigInteger qty, Generator generator) {
		generator * qty
	}

	static GeneratorDecorator toGenerator(Iterable self) {
		new GeneratorDecorator(self.iterator())
	}

	static GeneratorDecorator toGenerator(Collection self) {
		new GeneratorDecorator(self.iterator())
	}

	static GeneratorDecorator toGenerator(Iterator self) {
		new GeneratorDecorator(self)
	}
}

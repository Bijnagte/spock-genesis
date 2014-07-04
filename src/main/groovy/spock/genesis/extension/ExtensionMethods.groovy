package spock.genesis.extension

import spock.genesis.generators.Generator
import spock.genesis.generators.LimitedGenerator

class ExtensionMethods {

	LimitedGenerator multiply(int qty, Generator generator) {
		generator.multiply(qty)
	}
	
	LimitedGenerator multiply(BigInteger qty, Generator generator) {
		generator.multiply(qty)
	}
}

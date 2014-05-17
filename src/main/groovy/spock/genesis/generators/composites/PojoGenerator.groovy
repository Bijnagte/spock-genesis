package spock.genesis.generators.composites

import java.lang.reflect.Constructor

import spock.genesis.generators.Generator

class PojoGenerator<E> extends Generator<E> {
	Class target
	
	Iterator generator
	
	PojoGenerator(Class target, Iterator generator) {
		this.target = target
		this.generator = generator
	}
	
	@Override
	boolean hasNext() {
		generator.hasNext()
	}

	@Override
	E next() {
		def params = generator.next()
		
		def constructor = findSingleArgConstructor(params)
		
		if (constructor) {
			constructor.newInstance(params)
		} else if (List.isAssignableFrom(params.getClass())) {
			params.asType(target)
		} else if (Map.isAssignableFrom(params.getClass())) {
			params.asType(target)
		}
	}
		
	Constructor findSingleArgConstructor(def param) {
		target.constructors.find {
			it.parameterTypes.length == 1 &&
			it.parameterTypes[0] == param.getClass()
		}
	}
}
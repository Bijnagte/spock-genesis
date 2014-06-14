package spock.genesis.generators.composites

import java.lang.reflect.Constructor

import spock.genesis.generators.Generator

class PojoGenerator<E> extends Generator<E> {
	Class<E> target
	
	Iterator generator
	
	PojoGenerator(Class<E> target, Iterator generator) {
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
		def clazz = params.getClass()
		def constructor = findSingleArgConstructor(clazz)
		
		if (constructor) {
			constructor.newInstance(params)
		} else if (List.isAssignableFrom(clazz)) {
			params.asType(target)
		} else if (Map.isAssignableFrom(clazz)) {
			params.asType(target)
		}
	}
		
	Constructor findSingleArgConstructor(Class clazz) {
		target.constructors.find {
			it.parameterTypes.length == 1 &&
			it.parameterTypes[0] == clazz
		}
	}
}
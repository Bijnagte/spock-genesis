package spock.genesis.generators.composites

import java.lang.reflect.Constructor

import org.codehaus.groovy.runtime.NullObject

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
			constructor = findTupleConstructor(params)
			assert constructor
			constructor.newInstance(*params)
		} else if (Map.isAssignableFrom(params.getClass())) {
			constructor = findNoArgConstructor()
			assert constructor
			def result = constructor.newInstance()
			params.each { key, value ->
				result[key] = value
			}
			result
		}
	}
	
	Constructor findTupleConstructor(def params) {
		List<Class> paramTypes = params*.getClass()
		target.constructors.find {
			signatureMatches(it.parameterTypes, paramTypes)
		}
	}
	
	Constructor findSingleArgConstructor(def param) {
		target.constructors.find {
			signatureMatches(it.parameterTypes, [param.getClass()])
		}
	}
	
	Constructor findNoArgConstructor() {
		target.constructors.find {
			it.parameterTypes.length == 0
		}
	}
	
	boolean signatureMatches(Class[] targetParams, List<Class> params) {
		if (targetParams.length != params.size()) {
			false
		} else {
			def result = true
			targetParams.eachWithIndex { Class target, i ->
				if (!targetMatchesParam(target, params[i])) {
					result = false
				}
			}
			result
		}
	}
	
	boolean targetMatchesParam(Class target, Class param) {
		def isNullClass = param == NullObject || param == null
		isNullClass || target.isAssignableFrom(param)
	}
}
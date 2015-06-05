package spock.genesis.generators.composites

import spock.genesis.generators.Generator

import java.lang.reflect.Constructor

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
        Class clazz = params.getClass()
        Constructor constructor = findSingleArgConstructor(clazz)

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

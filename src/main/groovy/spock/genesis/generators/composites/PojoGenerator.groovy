package spock.genesis.generators.composites

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
        Class clazz = params.getClass()
        if (hasConstructorFor(clazz)) {
            target.metaClass.invokeConstructor(params)
        } else if (List.isAssignableFrom(clazz) || Map.isAssignableFrom(clazz)) {
            params.asType(target)
        }
    }

    boolean hasConstructorFor(Class clazz) {
        target.constructors.any {
            it.parameterTypes.length == 1 &&
                    it.parameterTypes[0].isAssignableFrom(clazz)
        }
    }
}

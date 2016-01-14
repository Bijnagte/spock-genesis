package spock.genesis.generators.composites

import spock.genesis.generators.Generator
import spock.genesis.generators.GeneratorUtils

class PojoGenerator<E> extends Generator<E> {
    final Class<E> target
    final Iterator generator

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
        } else if (List.isAssignableFrom(clazz)) {
            target.newInstance(*params)
        } else if (Map.isAssignableFrom(clazz)) {
            target.newInstance(params)
        }
    }

    boolean hasConstructorFor(Class clazz) {
        target.constructors.any {
            it.parameterTypes.length == 1 &&
                    it.parameterTypes[0].isAssignableFrom(clazz)
        }
    }

    boolean isFinite() {
        GeneratorUtils.isFinite(generator)
    }
}

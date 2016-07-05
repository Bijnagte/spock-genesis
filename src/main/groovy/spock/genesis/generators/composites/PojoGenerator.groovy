package spock.genesis.generators.composites

import spock.genesis.generators.Generator
import spock.genesis.generators.GeneratorUtils
import spock.genesis.generators.UnmodifiableIterator

class PojoGenerator<E> extends Generator<E> {
    final Class<E> target
    final Iterable generator

    PojoGenerator(Class<E> target, Iterable generator) {
        this.target = target
        this.generator = generator
    }

    UnmodifiableIterator<E> iterator() {
        new UnmodifiableIterator<E>() {
            private final Iterator iterator = generator.iterator()

            @Override
            boolean hasNext() {
                iterator.hasNext()
            }

            @Override
            E next() {
                def params = iterator.next()
                Class clazz = params.getClass()
                if (hasConstructorFor(clazz)) {
                    target.metaClass.invokeConstructor(params)
                } else if (List.isAssignableFrom(clazz)) {
                    target.newInstance(*params)
                } else if (Map.isAssignableFrom(clazz)) {
                    target.newInstance(params)
                }
            }

            private boolean hasConstructorFor(Class clazz) {
                target.constructors.any {
                    it.parameterTypes.length == 1 &&
                            it.parameterTypes[0].isAssignableFrom(clazz)
                }
            }
        }
    }

    boolean isFinite() {
        GeneratorUtils.isFinite(generator)
    }
}

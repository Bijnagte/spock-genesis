package spock.genesis.generators.composites

import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import spock.genesis.extension.ExtensionMethods
import spock.genesis.generators.Generator
import spock.genesis.generators.Permutable
import spock.genesis.generators.UnmodifiableIterator

import java.lang.reflect.Constructor

@CompileStatic
@SuppressWarnings(['Instanceof'])
class PojoGenerator<E, T> extends Generator<E> implements Closeable, Permutable {
    final Class<E> target
    final Generator<T> generator

    PojoGenerator(Class<E> target, Iterable<T> generator) {
        this.target = target
        this.generator = ExtensionMethods.toGenerator(generator)
    }

    UnmodifiableIterator<E> iterator() {
        new UnmodifiableIterator<E>() {
            private final Iterator iterator = generator.iterator()

            @Override
            boolean hasNext() {
                iterator.hasNext()
            }

            @Override
            @CompileDynamic
            E next() {
                T params = iterator.next()
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
                target.constructors.any { Constructor constructor ->
                    constructor.parameterTypes.length == 1 &&
                            constructor.parameterTypes[0].isAssignableFrom(clazz)
                }
            }
        }
    }

    boolean isFinite() {
        generator.finite
    }

    void close() {
        generator.close()
    }

    @Override
    PojoGenerator<E,T> seed(Long seed) {
        generator.seed(seed)
        super.seed(seed)
        this
    }

    PojoGenerator<E,T> permute() {
        if (generator instanceof Permutable) {
            new PojoGenerator<E, T>(target, ((Permutable<T>) generator).permute())
        } else {
            throw new UnsupportedOperationException("generator of type ${generator.getClass()} is not permutable")
        }
    }

    PojoGenerator<E,T> permute(int maxDepth) {
        if (generator instanceof Permutable) {
            new PojoGenerator<E, T>(target, ((Permutable<T>) generator).permute(maxDepth))
        } else {
            throw new UnsupportedOperationException("generator of type ${generator.getClass()} is not permutable")
        }
    }
}

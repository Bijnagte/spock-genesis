package spock.genesis.extension

import groovy.transform.CompileStatic
import spock.genesis.generators.Generator
import spock.genesis.generators.GeneratorDecorator
import spock.genesis.generators.LimitedGenerator
import spock.genesis.generators.ObjectIteratorGenerator

@CompileStatic
class ExtensionMethods {

    static <T> Generator<T> multiply(Integer qty, Generator<T> generator) {
        generator * qty
    }

    static <T> Generator<T> multiply(BigInteger qty, Generator<T> generator) {
        generator * qty.toInteger()
    }

    static <T> Generator<T> toGenerator(Iterable<T> self, boolean finite = false) {
        new GeneratorDecorator<T>(self, finite)
    }

    static Generator<String> toGenerator(String self) {
        new ObjectIteratorGenerator<String>(self)
    }

    static <T> Generator<T> toGenerator(Collection<T> self) {
        new LimitedGenerator<T>(self)
    }

    static Generator toGenerator(Class clazz) {
        if (clazz.isEnum()) {
            toGenerator(clazz.iterator().collect())
        } else {
            toGenerator([clazz])
        }
    }

    static <T> Generator<T> toGenerator(T... self) {
        new LimitedGenerator<T>(self)
    }

    static Generator toGenerator(Iterator self) {
        new GeneratorDecorator(new Iterable() {
            @Override
            Iterator iterator() {
                self
            }
        })
    }

    static <T> Generator<T> toGenerator(Generator<T> self) {
        self
    }
}

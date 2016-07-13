package spock.genesis.extension

import groovy.transform.CompileStatic
import spock.genesis.generators.Generator
import spock.genesis.generators.IterableGenerator
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
        new IterableGenerator<T>(self, finite)
    }

    static Generator<String> toGenerator(String self) {
        new ObjectIteratorGenerator<String>(self)
    }

    static Generator<Object> toGenerator(Object self) {
        new ObjectIteratorGenerator(self)
    }

    static <K,V> Generator<Map.Entry<K,V>> toGenerator(Map<K,V> self) {
        new ObjectIteratorGenerator(self)
    }

    static <T> Generator<T> toGenerator(Collection<T> self) {
        new IterableGenerator<T>(self)
    }

    static Generator toGenerator(Class clazz) {
        if (clazz.isEnum()) {
            toGenerator(clazz.iterator().collect())
        } else {
            toGenerator([clazz])
        }
    }

    static <T> Generator<T> toGenerator(T... self) {
        new IterableGenerator<T>(self)
    }

    static <T> Generator<T> toGenerator(Iterator<T> self) {
        new IterableGenerator<T>(new Iterable () {
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

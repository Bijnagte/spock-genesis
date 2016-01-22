package spock.genesis.extension

import spock.genesis.generators.Generator
import spock.genesis.generators.GeneratorDecorator
import spock.genesis.generators.LimitedGenerator

class ExtensionMethods {

    static Generator multiply(Integer qty, Generator generator) {
        generator * qty
    }

    static Generator multiply(BigInteger qty, Generator generator) {
        generator * qty
    }

    static Generator toGenerator(Iterable self, boolean finite  = false) {
        new GeneratorDecorator(self.iterator(), finite)
    }

    static Generator toGenerator(Collection self) {
        new LimitedGenerator(self)
    }

    static Generator toGenerator(Class clazz) {
        if (clazz.isEnum()) {
            toGenerator(clazz.iterator())
        } else {
            toGenerator([clazz])
        }
    }

    static Generator toGenerator(Object... self) {
        new LimitedGenerator(self)
    }

    static Generator toGenerator(Iterator self) {
        new GeneratorDecorator(self)
    }

    static Generator toGenerator(Generator self) {
        self
    }
}

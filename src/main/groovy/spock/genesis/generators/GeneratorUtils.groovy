package spock.genesis.generators

import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic

@CompileStatic
class GeneratorUtils {

    static boolean anyFinite(Collection<? extends Iterable> iterables) {
        iterables.any { isFinite(it) }
    }

    static boolean anyFinite(Iterable... iterables) {
        iterables.any { Iterable iterable -> isFinite(iterable) }
    }

    static boolean allFinite(Collection<? extends Iterable> iterables) {
        iterables.every { isFinite(it) }
    }

    static boolean allFinite(Iterable... iterators) {
        iterators.every { Iterable iterable -> isFinite(iterable) }
    }

    @CompileDynamic
    static boolean isFinite(Iterable iterable) {
        !iterable.iterator().hasNext() || iterable.respondsTo('isFinite') && iterable.isFinite()
    }
}

package spock.genesis.generators

class GeneratorUtils {

    static boolean anyFinite(Collection<Iterable> iterators) {
        iterators.any { isFinite(it) }
    }

    static boolean anyFinite(Iterable... iterators) {
        iterators.any { isFinite(it) }
    }

    static boolean allFinite(Collection<Iterator> iterators) {
        iterators.every { isFinite(it) }
    }

    static boolean allFinite(Iterable... iterators) {
        iterators.every { isFinite(it) }
    }

    static boolean isFinite(Iterable iterable) {
        !iterable.iterator().hasNext() || iterable.respondsTo('isFinite') && iterable.isFinite()
    }
}

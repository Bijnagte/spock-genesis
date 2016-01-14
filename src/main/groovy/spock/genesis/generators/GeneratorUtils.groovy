package spock.genesis.generators

class GeneratorUtils {

    static boolean anyFinite(Collection<Iterator> iterators) {
        iterators.any { isFinite(it) }
    }

    static boolean anyFinite(Iterator... iterators) {
        iterators.any { isFinite(it) }
    }

    static boolean allFinite(Collection<Iterator> iterators) {
        iterators.every() { isFinite(it) }
    }

    static boolean allFinite(Iterator... iterators) {
        iterators.every() { isFinite(it) }
    }

    static boolean isFinite(Iterator iterator) {
        !iterator.hasNext() || iterator.respondsTo('isFinite') && iterator.isFinite()
    }
}

package spock.genesis.generators

import groovy.transform.CompileStatic

/**
 * A lazy infinite generator that repeats an iterator.
 * This generator keeps track of 1 iterator worth of data so infinite sources could lead to excessive memory usage.
 * @param < E >   the generated type
 */
@CompileStatic
class CyclicGenerator<E> extends GeneratorDecorator<E> {

    CyclicGenerator(E... array) {
        this(array.size() == 1 ? new ObjectIteratorGenerator<E>(array[0]) : array.toList())
    }

    CyclicGenerator(Iterable<E> iterable) {
        super(iterable, GeneratorUtils.isFinite(iterable) || !Generator.isInstance(iterable))
    }

    @Override
    UnmodifiableIterator<E> iterator() {
        new UnmodifiableIterator<E>() {
            private Iterator<E> source = generator.iterator()
            private final List repeatSource = []
            private boolean hasRepeated = false
            private boolean started = false

            @Override
            boolean hasNext() {
                if (started) {
                    true
                } else {
                    source.hasNext()
                }
            }

            @Override
            E next() {
                started = true
                if (!source.hasNext()) {
                    hasRepeated = true
                    source = repeatSource.iterator()
                }
                E val = source.next()
                if (!hasRepeated) {
                    repeatSource.add(val)
                }
                val
            }
        }
    }
    boolean isFinite() {
        !iterator().hasNext()
    }
}

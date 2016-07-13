package spock.genesis.generators

import groovy.transform.CompileStatic

@CompileStatic
class TransformingGenerator<E, T> extends GeneratorDecorator<E> {
    private final Closure<T> transform

    TransformingGenerator(Generator<E> iterable, Closure<T> transform) {
        super(iterable)
        this.transform = transform
    }

    UnmodifiableIterator<T> iterator() {
        final Iterator<T> ITERATOR = super.iterator()
        new UnmodifiableIterator<T>() {
            @Override
            boolean hasNext() {
                ITERATOR.hasNext()
            }

            T next() {
                transform.call(ITERATOR.next())
            }
        }
    }
}

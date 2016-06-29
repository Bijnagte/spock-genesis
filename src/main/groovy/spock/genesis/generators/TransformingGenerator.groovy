package spock.genesis.generators

class TransformingGenerator<E, T> extends GeneratorDecorator<T> {
    private final Closure<T> transform

    TransformingGenerator(Iterable<E> iterable, Closure<T> transform) {
        super(iterable)
        this.transform = transform
    }

    UnmodifiableIterator<E> iterator() {
        final Iterator<E> ITERATOR = super.iterator()
        new UnmodifiableIterator<E>() {
            @Override
            boolean hasNext() {
                ITERATOR.hasNext()
            }

            T next() {
                transform(ITERATOR.next())
            }
        }
    }
}

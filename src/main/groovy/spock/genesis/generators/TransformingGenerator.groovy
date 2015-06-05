package spock.genesis.generators

class TransformingGenerator<E, T> extends GeneratorDecorator<E> {
    private final Closure<T> transform

    TransformingGenerator(Iterator iterator, Closure<E> transform) {
        super(iterator)
        this.transform = transform
    }

    T next() {
        transform(generator.next())
    }
}

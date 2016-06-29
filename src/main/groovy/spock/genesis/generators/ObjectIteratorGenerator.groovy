package spock.genesis.generators

class ObjectIteratorGenerator<E> extends Generator<E> {
    private final Object object

    ObjectIteratorGenerator(object) {
        this.object = object
    }

    @Override
    UnmodifiableIterator iterator() {
        new UnmodifiableIterator<E>() {
            final private Iterator<E> iterator = object.iterator()
            @Override
            boolean hasNext() {
                iterator.hasNext()
            }

            @Override
            Object next() {
                iterator.next()
            }
        }
    }

    @Override
    boolean isFinite() { true }
}

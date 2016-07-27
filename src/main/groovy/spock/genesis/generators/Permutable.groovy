package spock.genesis.generators

interface Permutable<T> extends Iterable {
    Generator<T> permute()
    Generator<T> permute(int maxDepth)
}

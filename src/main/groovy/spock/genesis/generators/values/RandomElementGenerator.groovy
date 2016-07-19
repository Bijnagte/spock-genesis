package spock.genesis.generators.values

import groovy.transform.CompileStatic
import spock.genesis.generators.InfiniteGenerator
import spock.genesis.generators.InfiniteIterator

/** A lazy infinite {@code Generator} that returns a random element from a source Collection
 * @param < E >     the generated type
 */
@CompileStatic
class RandomElementGenerator<E> extends InfiniteGenerator<E> {

    final List<E> elementSource
    final WholeNumberGenerator indexSource

    RandomElementGenerator(Collection<E> elementSource) {
        this.elementSource = elementSource.toList().asImmutable()
        this.indexSource = new WholeNumberGenerator(this.elementSource.size() - 1)
    }

    @Override
    InfiniteIterator<E> iterator() {
        new InfiniteIterator<E>() {
            private final Iterator<Integer> indexIterator = indexSource.iterator()

            @Override
            E next() {
                elementSource[indexIterator.next()]
            }
        }
    }

    @Override
    RandomElementGenerator<E> seed(Long seed) {
        indexSource.seed(seed)
        super.seed(seed)
        this
    }
}

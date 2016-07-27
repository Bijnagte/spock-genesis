package spock.genesis.generators.composites

import groovy.transform.CompileStatic
import spock.genesis.generators.Generator
import spock.genesis.generators.Permutable
import spock.genesis.generators.UnmodifiableIterator

@CompileStatic
class PermutationGenerator<E> extends TupleGenerator<E> implements Permutable {

    static final int MAX_PERMUTATIONS = 10000
    private final int maxDepth

    PermutationGenerator(List<Generator<E>> generators, int maxDepth) {
        super(generators)
        this.maxDepth = maxDepth
    }

    PermutationGenerator(List<Generator<E>> generators) {
        this(generators, pickMaxDepth(generators))
    }

    @Override
    UnmodifiableIterator<List<E>> iterator() {
        final List<UnmodifiableIterator<E>> ITERATORS = generators.collect { generator ->
            generator.take(maxDepth).iterator()
        }

        new UnmodifiableIterator<List<E>>() {
            private final List<List<E>> emitted = []
            private final List<List<E>> currentEmmited = []
            private int column = 0
            private E value
            private Iterator<List<E>> emittedIterator

            @Override
            boolean hasNext() {
                if (!emitted) {
                    //first
                    ITERATORS.every { it.hasNext() }
                } else if (emittedIterator?.hasNext() || ITERATORS[column].hasNext()) {
                    true
                } else {
                    //find a column that still has values to generate
                    while (!ITERATORS[column].hasNext() && column + 1 < ITERATORS.size()) {
                        column++
                    }
                    if (ITERATORS[column].hasNext()) {
                        emittedIterator = null
                        emitted.addAll(currentEmmited) // the previous columns emitted tuples for substitution
                        currentEmmited.clear()
                        true
                    } else {
                        false
                    }
                }
            }

            @Override
            List<E> next() {
                if (emitted) {
                    if (!emittedIterator?.hasNext()) { // done with the value
                        value = ITERATORS[column].next() // get the next value
                        emittedIterator = emitted.iterator()
                    }

                    List result = emittedIterator.next().collect() // copy the next previously emitted tuple
                    result[column] = value // substitute the value
                    currentEmmited << result // keep track of values for this column
                    result.collect()
                } else { //first
                    List result = ITERATORS.collect { it.next() }
                    emitted << result
                    result.collect()
                }
            }
        }
    }

    private static int pickMaxDepth(List<Generator<E>> generators) {
        Math.floor(nthRoot(MAX_PERMUTATIONS, generators.size()))
    }

    private static double nthRoot(int base, int exponent) {
        double tolerance = 0.01
        double approximation = 1
        double root
        while (true) {
            root = ((exponent - 1) * approximation + base / (approximation) ** (exponent - 1)) / exponent
            if ((root - approximation).abs() < tolerance) {
                break
            }
            approximation = root
        }
        root
    }
}

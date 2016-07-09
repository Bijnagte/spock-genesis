package spock.genesis.generators.values

import groovy.transform.CompileStatic
import spock.genesis.generators.InfiniteGenerator
import spock.genesis.generators.InfiniteIterator

@CompileStatic
class DoubleGenerator extends InfiniteGenerator<Double> {

    final Random random = new Random()

    InfiniteIterator<Double> iterator() {
        new InfiniteIterator<Double>() {
            @Override
            Double next() {
                random.nextDouble()
            }
        }
    }
}

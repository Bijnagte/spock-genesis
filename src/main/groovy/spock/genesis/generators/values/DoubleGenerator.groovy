package spock.genesis.generators.values

import spock.genesis.generators.InfiniteGenerator

class DoubleGenerator extends InfiniteGenerator<Double> {

    final Random random = new Random()

    @Override
    Double next() {
        random.nextDouble()
    }

}

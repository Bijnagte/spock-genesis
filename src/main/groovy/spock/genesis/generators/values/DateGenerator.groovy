package spock.genesis.generators.values

import spock.genesis.generators.InfiniteGenerator
import spock.genesis.generators.InfiniteIterator

class DateGenerator extends InfiniteGenerator<Date> {

    final LongGenerator millisProvider

    DateGenerator() {
        this.millisProvider = new LongGenerator()
    }

    DateGenerator(Date minDate, Date maxDate) {
        this.millisProvider = new LongGenerator(minDate.time, maxDate.time)
    }

    InfiniteIterator<Date> iterator() {
        final Iterator<Long> TIME = millisProvider.iterator()
        new InfiniteIterator<Date>() {
            @Override
            Date next() {
                new Date(TIME.next())
            }
        }
    }
}

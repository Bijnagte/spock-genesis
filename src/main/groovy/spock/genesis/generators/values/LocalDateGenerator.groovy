package spock.genesis.generators.values

import spock.genesis.generators.InfiniteGenerator
import spock.genesis.generators.InfiniteIterator

import java.time.LocalDate

class LocalDateGenerator extends InfiniteGenerator<LocalDate> {
    final LongGenerator millisProvider

    LocalDateGenerator() {
        this.millisProvider = new LongGenerator()
    }

    LocalDateGenerator(LocalDate minDate, LocalDate maxDate) {
        this.millisProvider = new LongGenerator(minDate.toEpochDay(), maxDate.toEpochDay())
    }

    InfiniteIterator<LocalDate> iterator() {
        final Iterator<Long> TIME = millisProvider.iterator()
        new InfiniteIterator<LocalDate>() {
            @Override
            LocalDate next() {
                LocalDate.ofEpochDay(TIME.next())
            }
        }
    }

    @Override
    LocalDateGenerator seed(Long seed) {
        super.seed(seed)
        millisProvider.seed(seed)
        this
    }
}

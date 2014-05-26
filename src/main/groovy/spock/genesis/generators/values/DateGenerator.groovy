package spock.genesis.generators.values

import spock.genesis.generators.InfiniteGenerator

class DateGenerator extends InfiniteGenerator<Date> {

	final LongGenerator millisProvider
	
	DateGenerator() {
		this.millisProvider = new LongGenerator()
	}
	
	DateGenerator(Date minDate, Date maxDate) {
		this.millisProvider = new LongGenerator(minDate.millis(), maxDate.millis())
	}
	
	@Override
	Date next() {
		new Date(millisProvider.next())
	}
}

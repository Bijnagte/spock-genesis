package spock.genesis.generators.values

import spock.genesis.generators.InfiniteGenerator

class DateGenerator extends InfiniteGenerator<Date> {

	final LongGenerator millisProvider
	
	@Override
	Date next() {
		new Date(millisProvider.next())
	}
}

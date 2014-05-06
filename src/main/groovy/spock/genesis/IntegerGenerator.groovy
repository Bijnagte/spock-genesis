package spock.genesis

class IntegerGenerator extends RandomGenerator<Integer> {

	final Integer rangeLimit

	IntegerGenerator() {
		super()
		this.rangeLimit = null
	}
	
	IntegerGenerator(Integer rangeLimit) {
		super()
		this.rangeLimit = rangeLimit
	}

	@Override
	Integer next() {
		if (null != rangeLimit) {
			def num = random.nextInt(rangeLimit)
			random.nextBoolean() ? num : 0 - num
		} else {
			random.nextInt()
		}
	}
}

package spock.genesis.generators.composites

import spock.genesis.generators.Generator
import spock.genesis.generators.values.WholeNumberGenerator

class RandomMapGenerator<K,V> extends Generator<Map<K,V>> implements Closeable {

	static final int DEFAULT_ENTRY_LIMIT = 100
	final WholeNumberGenerator sizeSource
	final Iterator<K> keyGenerator
	final Iterator valueGenerator

	RandomMapGenerator(Iterator<K> keyGenerator, Iterator valueGenerator) {
		this.sizeSource = new WholeNumberGenerator(DEFAULT_ENTRY_LIMIT)
		this.keyGenerator = keyGenerator
		this.valueGenerator = valueGenerator
	}

	RandomMapGenerator(int maxSize, Iterator<K> keyGenerator, Iterator valueGenerator) {
		this.sizeSource = new WholeNumberGenerator(maxSize)
		this.keyGenerator = keyGenerator
		this.valueGenerator = valueGenerator
	}

	RandomMapGenerator(int maxSize, int minSize, Iterator<K> keyGenerator, Iterator valueGenerator) {
		this.sizeSource = new WholeNumberGenerator(minSize, maxSize)
		this.keyGenerator = keyGenerator
		this.valueGenerator = valueGenerator
	}

	RandomMapGenerator(IntRange sizeRange, Iterator<K> keyGenerator, Iterator valueGenerator) {
		this.sizeSource = new WholeNumberGenerator(sizeRange)
		this.keyGenerator = keyGenerator
		this.valueGenerator = valueGenerator
	}

	boolean hasNext() {
		keyGenerator.hasNext() && valueGenerator.hasNext()
	}

	@Override
	Map<K,V> next() {
		int targetSize = sizeSource.next()
		int i = 0
		def result = [:]
		while (i < targetSize && hasNext()) {
			result[keyGenerator.next()] = valueGenerator.next()
		}
		result
	}

	void close() {
		[keyGenerator, valueGenerator].each {
			if (it.respondsTo('close')) {
				it.close()
			}
		}
	}
}

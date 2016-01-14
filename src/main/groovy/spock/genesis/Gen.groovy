package spock.genesis

import spock.genesis.generators.CyclicGenerator
import spock.genesis.generators.FactoryGenerator
import spock.genesis.generators.Generator
import spock.genesis.generators.GeneratorDecorator
import spock.genesis.generators.LimitedGenerator
import spock.genesis.generators.composites.DefinedMapGenerator
import spock.genesis.generators.composites.ListGenerator
import spock.genesis.generators.composites.PojoGenerator
import spock.genesis.generators.composites.RandomMapGenerator
import spock.genesis.generators.composites.TupleGenerator
import spock.genesis.generators.values.ByteArrayGenerator
import spock.genesis.generators.values.CharacterGenerator
import spock.genesis.generators.values.DateGenerator
import spock.genesis.generators.values.DoubleGenerator
import spock.genesis.generators.values.IntegerGenerator
import spock.genesis.generators.values.LongGenerator
import spock.genesis.generators.values.RandomElementGenerator
import spock.genesis.generators.values.StringGenerator
import spock.genesis.generators.values.ValueGenerator

import java.util.regex.Pattern

/**
 * static factory methods for Generators
 */
@SuppressWarnings(['MethodCount'])
class Gen {

    /**
     * @return an infinite lazy String Generator
     */
    static StringGenerator getString() {
        new StringGenerator()
    }
    /**
     *
     * @param maxLength
     * @return an infinite lazy String Generator
     */
    static StringGenerator string(int maxLength) {
        new StringGenerator(maxLength)
    }

    static StringGenerator string(String potentialCharacters) {
        new StringGenerator(potentialCharacters)
    }

    static StringGenerator string(int minLength, int maxLength) {
        new StringGenerator(minLength, maxLength)
    }

    static StringGenerator string(Pattern regex) {
        new StringGenerator(regex)
    }

    static ByteArrayGenerator getBytes() {
        new ByteArrayGenerator()
    }

    static IntegerGenerator getInteger() {
        new IntegerGenerator()
    }

    static IntegerGenerator integer(int min, int max) {
        new IntegerGenerator(min, max)
    }

    static IntegerGenerator integer(IntRange range) {
        new IntegerGenerator(range.from, range.to)
    }

    static LongGenerator getLong() {
        new LongGenerator()
    }

    @Deprecated
    static CharacterGenerator getChar() {
        new CharacterGenerator()
    }

    static CharacterGenerator getCharacter() {
        new CharacterGenerator()
    }

    static CharacterGenerator character(String potentialCharacters) {
        new CharacterGenerator(potentialCharacters)
    }

    static DoubleGenerator getDouble() {
        new DoubleGenerator()
    }

    static ValueGenerator value(value) {
        new ValueGenerator(value)
    }

    /**
     * @param source {@link Collection} of {@link Object} to pick from at random
     * @return a {@link RandomElementGenerator}
     */
    static RandomElementGenerator any(Collection source) {
        new RandomElementGenerator(source)
    }

    /**
     * @param source Array of {@link Object} to pick from at random
     * @return a {@link RandomElementGenerator}
     */
    static RandomElementGenerator any(Object... source) {
        new RandomElementGenerator(source.toList())
    }

    static CyclicGenerator cycle(Iterator source) {
        new CyclicGenerator(source)
    }

    static CyclicGenerator cycle(Iterable source) {
        new CyclicGenerator(source.iterator())
    }

    static PojoGenerator type(Map<String, Object> keysToValueGenerators, Class target) {
        new PojoGenerator(target, map(keysToValueGenerators))
    }

    static PojoGenerator type(Class target, Iterator... argGenerators) {
        new PojoGenerator(target, tuple(argGenerators))
    }

    static DefinedMapGenerator map(Map keysToValueGenerators) {
        new DefinedMapGenerator(keysToValueGenerators)
    }

    static RandomMapGenerator map(Iterator keyGenerator, Iterator valueGenerator) {
        new RandomMapGenerator(keyGenerator, valueGenerator)
    }

    static ListGenerator list(Generator valueGenerator) {
        new ListGenerator(valueGenerator)
    }

    static ListGenerator list(Generator valueGenerator, int maxLength) {
        new ListGenerator(valueGenerator, maxLength)
    }

    static ListGenerator list(Generator valueGenerator, int minLength, int maxLength) {
        new ListGenerator(valueGenerator, minLength, maxLength)
    }

    static TupleGenerator tuple(Iterator... generators) {
        new TupleGenerator(generators)
    }

    static DateGenerator getDate() {
        new DateGenerator()
    }

    static DateGenerator date(Date minDate, Date maxDate) {
        new DateGenerator(minDate, maxDate)
    }

    static FactoryGenerator using(Closure factory) {
        new FactoryGenerator(factory)
    }

    static GeneratorDecorator these(Iterator iterator, boolean finite = false) {
        new GeneratorDecorator(iterator, finite)
    }

    static GeneratorDecorator these(Iterable iterable) {
        these(iterable.iterator(), true)
    }

    static GeneratorDecorator these(Object... values) {
        new LimitedGenerator(values)
    }

    static GeneratorDecorator these(Collection values) {
        new LimitedGenerator(values)
    }

    static GeneratorDecorator once(Object value) {
        these([value])
    }
}

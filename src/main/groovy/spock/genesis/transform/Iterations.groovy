package spock.genesis.transform

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

/**
 * Annotation to mark Spock feature methods that should have the date providers in the where block limited.
 * See {@link GenASTTransformation} for implementation.
 */
@Retention(RetentionPolicy.SOURCE)
@Target([ElementType.METHOD, ElementType.TYPE])
@interface Iterations {
    /**
     * The number of iterations to limit to or default to 100.
     */
    int value() default 100 //the default value is not used by the AST transformation but is here for reference
}

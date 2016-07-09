package spock.genesis.transform

import groovy.transform.CompileStatic
import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.AnnotatedNode
import org.codehaus.groovy.ast.AnnotationNode
import org.codehaus.groovy.ast.ClassHelper
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.MethodNode
import org.codehaus.groovy.ast.ModuleNode
import org.codehaus.groovy.ast.expr.ArgumentListExpression
import org.codehaus.groovy.ast.expr.ConstantExpression
import org.codehaus.groovy.ast.expr.ConstructorCallExpression
import org.codehaus.groovy.ast.expr.Expression
import org.codehaus.groovy.ast.stmt.BlockStatement
import org.codehaus.groovy.ast.stmt.ReturnStatement
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.transform.ASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation
import org.spockframework.compiler.AstNodeCache
import spock.genesis.generators.LimitedGenerator

/**
 * Global AST transformation to modify data providers after Spock compilation
 */
@CompileStatic
@GroovyASTTransformation(phase = CompilePhase.CANONICALIZATION)
@SuppressWarnings(['Instanceof', 'CrapMetric'])
class GenASTTransformation implements ASTTransformation {

    final static AstNodeCache NODE_CACHE = new AstNodeCache()
    final static ClassNode ITERATIONS_ANNOTATION = ClassHelper.makeWithoutCaching(Iterations)
    final static ClassNode LIMITED_CLASS_GENERATOR = ClassHelper.makeWithoutCaching(LimitedGenerator)

    @Override
    void visit(ASTNode[] nodes, SourceUnit sourceUnit) {
        ModuleNode module = (ModuleNode) nodes[0]
        List<ClassNode> classes = module.classes

        for (ClassNode clazz in classes) {
            if (isSpec(clazz)) {
                def classIterationsAnnotation = findIterationsAnnotation(clazz)
                Integer classIterations
                if (classIterationsAnnotation) {
                    classIterations = getNumberOfIterations(classIterationsAnnotation)
                }
                Map<String, Integer> featureIterations = getFeatureIterations(clazz, classIterations)

                if (featureIterations) {
                    modifyDataProviders(clazz, featureIterations)
                }
            }
        }
    }

    private Map<String, Integer> getFeatureIterations(ClassNode clazz, Integer classIterations) {
        Map<String, Integer> featureIterations = [:]
        for (MethodNode method in clazz.methods) {
            if (isFeature(method)) {
                Integer methodIterations = classIterations
                def iterationsAnnotation = findIterationsAnnotation(method)
                if (iterationsAnnotation) {
                    methodIterations = getNumberOfIterations(iterationsAnnotation)
                }
                if (methodIterations != null) {
                    featureIterations.put(method.name, methodIterations)
                }
            }
        }
        featureIterations
    }

    private void modifyDataProviders(ClassNode clazz, Map<String, Integer> featureIterations) {
        Set<String> modifyFeatureNames = featureIterations.keySet()

        for (MethodNode method in clazz.methods) {
            if (isDataProvider(method)) {
                String featureName = getFeatureName(method.name)
                if (modifyFeatureNames.contains(featureName)) {
                    int limit = featureIterations.get(featureName)
                    if (method.code instanceof BlockStatement) {
                        BlockStatement code = (BlockStatement) method.code
                        if (code.statements[0] instanceof ReturnStatement) {
                            // all data providers should be only a single return statement but making sure no exceptions
                            // get thrown getting to this point
                            ReturnStatement returnStatement = (ReturnStatement) code.statements[0]
                            returnStatement.expression = wrapWithLimitedGenerator(returnStatement.expression, limit)
                        }
                    }
                }
            }
        }
    }

    @SuppressWarnings(['UnnecessarySubstring'])
    private String getFeatureName(String dataProviderName) {
        // spock names methods the same as the feature with prov and a number appended
        // feature:  $spock_feature_0_9
        // 1st provider: $spock_feature_0_9prov0
        // 2nd provider: $spock_feature_0_9prov1
        int featureEnd = dataProviderName.indexOf('prov')
        if (featureEnd != -1) {
            dataProviderName.substring(0, featureEnd)
        }
    }

    private AnnotationNode findAnnotation(ClassNode annotation, AnnotatedNode annotatedNode) {
        annotatedNode.annotations.find { it.classNode.isDerivedFrom(annotation) }
    }

    private boolean isDataProvider(MethodNode method) {
        //spock annotates the methods created from the where block with DataProviderMetadata
        findAnnotation(NODE_CACHE.DataProviderMetadata, method)
    }

    private boolean isFeature(MethodNode method) {
        //spock annotates the feature methods with FeatureMetadata
        findAnnotation(NODE_CACHE.FeatureMetadata, method)
    }

    private AnnotationNode findIterationsAnnotation(AnnotatedNode annotatedNode) {
        findAnnotation(ITERATIONS_ANNOTATION, annotatedNode)
    }

    private boolean isSpec(ClassNode clazz) {
        clazz.isDerivedFrom(NODE_CACHE.Specification)
    }

    private Integer getNumberOfIterations(AnnotationNode iterationsAnnotation) {
        if (iterationsAnnotation) {
            def value = iterationsAnnotation.getMember('value')
            if (value instanceof ConstantExpression) {
                ConstantExpression valueExpression = (ConstantExpression) value
                return (Integer) valueExpression.value
            }
        }
    }

    private static Expression wrapWithLimitedGenerator(Expression expression, int limit) {
        def args = new ArgumentListExpression(expression, new ConstantExpression(limit))
        new ConstructorCallExpression(LIMITED_CLASS_GENERATOR, args)
    }
}

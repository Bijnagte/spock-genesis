ruleset {
	ruleset('rulesets/basic.xml')
	ruleset('rulesets/braces.xml')
	ruleset('rulesets/concurrency.xml')
	ruleset('rulesets/convention.xml') {
		NoDef(enabled: false)
	}
	ruleset('rulesets/design.xml')
	ruleset('rulesets/dry.xml')
	//ruleset('rulesets/enhanced.xml')
	ruleset('rulesets/exceptions.xml')
	ruleset('rulesets/formatting.xml') {
		ClassJavadoc {
			enabled = false
		}
	}
	ruleset('rulesets/generic.xml')
	ruleset('rulesets/groovyism.xml')
	ruleset('rulesets/imports.xml')
	ruleset('rulesets/junit.xml')
	ruleset('rulesets/logging.xml')
	ruleset('rulesets/naming.xml')
	ruleset('rulesets/security.xml') {
		InsecureRandom(enabled: false)
	}
	ruleset('rulesets/serialization.xml')
	ruleset('rulesets/size.xml') {
		CrapMetric {
			coberturaXmlFile = 'file:build/reports/cobertura/coverage.xml'
			maxMethodCrapScore = 20
			maxClassAverageMethodCrapScore = 20
		}
	}
	ruleset('rulesets/unnecessary.xml') {
		UnnecessaryCollectCall(enabled: false)
	}
	ruleset('rulesets/unused.xml')
}
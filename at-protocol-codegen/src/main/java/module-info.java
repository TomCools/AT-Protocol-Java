import be.tomcools.atprotocol.codegen.ATProtocolCodeGenerator;
import be.tomcools.atprotocol.codegen.generator.ATProtocolCodeGeneratorImpl;

module be.tomcools.atprotocol.codegen {
	exports be.tomcools.atprotocol.codegen;

	requires com.github.wnameless.json.flattener;
	requires com.fasterxml.jackson.core;
	requires com.squareup.javapoet;
	requires org.apache.commons.lang3;
	requires java.compiler;
	requires java.net.http;
	requires com.fasterxml.jackson.annotation;
	requires com.fasterxml.jackson.databind;

	opens be.tomcools.atprotocol.codegen.lexicon to com.fasterxml.jackson.databind;
	opens be.tomcools.atprotocol.codegen.lexicon.primitives to com.fasterxml.jackson.databind;
	opens be.tomcools.atprotocol.codegen.lexicon.types to com.fasterxml.jackson.databind;
	opens be.tomcools.atprotocol.codegen.parser to com.fasterxml.jackson.databind;

	provides ATProtocolCodeGenerator with ATProtocolCodeGeneratorImpl;
}

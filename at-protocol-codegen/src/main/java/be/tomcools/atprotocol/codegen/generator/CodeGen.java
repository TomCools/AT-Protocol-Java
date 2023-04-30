package be.tomcools.atprotocol.codegen.generator;

import be.tomcools.atprotocol.codegen.generator.objects.ObjectClassGenerator;
import be.tomcools.atprotocol.codegen.generator.procedure.ProcedureClassGenerator;
import be.tomcools.atprotocol.codegen.generator.procedure.ProcedureInDocument;

import static java.util.stream.Collectors.groupingBy;

public class CodeGen {

	GenerationContext context;
	ObjectClassGenerator objectClassGenerator;
	ProcedureClassGenerator procedureClassGenerator;

	public CodeGen(GenerationContext context) {
		this.context = context;
		this.objectClassGenerator = new ObjectClassGenerator();
		this.procedureClassGenerator = new ProcedureClassGenerator();
	}

	public void generateClasses() {
		context.files().forEach(c -> {
			c.getObjects()
					.forEach(object -> objectClassGenerator.generateClassForObject(object, context.outputDirectory()));
		});
				context.files().stream().flatMap(context -> context.getProcedures().stream())
						.collect(groupingBy(ProcedureInDocument::determineFQClassName))
						.forEach((fqClassName, procedure) -> procedureClassGenerator.generateClient(fqClassName, procedure, context.outputDirectory()));



	}

}

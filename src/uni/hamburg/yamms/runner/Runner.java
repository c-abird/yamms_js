package uni.hamburg.yamms.runner;

import javax.script.*;

import org.apache.commons.cli.*;

import uni.hamburg.yamms.io.IOConfig;

public class Runner {
	public static void main(String[] args) throws Exception {
		// create Options object
		Options options = new Options();

		// add t option
		options.addOption("c", true, "JavaScript Config File");
		options.addOption("b", true, "Basedir for Storage Services");
		options.addOption("i", true, "Interval for Compound Project");

		CommandLineParser parser = new PosixParser();
		CommandLine cmd = parser.parse(options, args);

		IOConfig.getInstance().setBasePath(cmd.getOptionValue("b", "."));

		Runner runner = new Runner();

		if (cmd.hasOption("c")) {
			runner.runScript(cmd);
		} else {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("m3sc", options);
		}
	}

	public void runScript(CommandLine cmd) throws Exception {
		// create a script engine manager
		ScriptEngineManager factory = new ScriptEngineManager();
		// create JavaScript engine
		ScriptEngine engine = factory.getEngineByName("JavaScript");

		// import m3sc packages
		String[] packages = { "fieldTerms", "solver", "io", "math", "model",
				"physics", "runner", "solver.stepHandlers", "profiling" };
		for (String p : packages) {
			engine.eval("importPackage(Packages.uni.hamburg.yamms." + p + ");");
		}

		// evaluate JavaScript code from given file
		engine.eval(new java.io.FileReader(cmd.getOptionValue("c")));

		// get project object
		Object project = engine.get("project");
		ProjectWrapper projectWrapper = null;

		// have to do this instanceof fork/wrapping
		// while scripting doesn't support abstract class implementation
		if (project instanceof SimpleProject) {
			projectWrapper = new SimpleProjectWrapper((SimpleProject) project);
		} else if (project instanceof CompoundProject) {
			projectWrapper = new CompoundProjectWrapper(
					(CompoundProject) project);
		}

		// go, go, goooo
		projectWrapper.run(cmd);
	}
}

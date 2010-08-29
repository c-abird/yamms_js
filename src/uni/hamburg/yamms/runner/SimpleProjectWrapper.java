package uni.hamburg.yamms.runner;

import org.apache.commons.cli.CommandLine;

public class SimpleProjectWrapper implements ProjectWrapper {

	private SimpleProject _project;

	public SimpleProjectWrapper(SimpleProject project) {
		_project = project;
	}
	
	@Override
	public void run(CommandLine cmd) {
		_project.startSimulation(cmd.getArgs());
	}

}

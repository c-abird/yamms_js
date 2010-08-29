package uni.hamburg.yamms.runner;

import org.apache.commons.cli.CommandLine;

public class CompoundProjectWrapper implements ProjectWrapper {
	
	private CompoundProject _project;

	public CompoundProjectWrapper(CompoundProject project) {
		_project = project;
	}
	
	@Override
	public void run(CommandLine cmd) {
		// parse interval parameter
		int start, stop;
    	int simCount = _project.getParamSets().length;
    	
		if (cmd.hasOption("i")) {
			String option = cmd.getOptionValue("i");
			// single simulation
			if (option.matches("^[0-9]+$")) {
				start = stop = Integer.parseInt(option);
			}
			// range
			else if (option.matches("^[0-9]+-[0-9]+$")) {
				String[] interval = cmd.getOptionValue("i").split("-");
				start = Integer.parseInt(interval[0]);
				stop = Integer.parseInt(interval[1]);
			}
			// mpi like rank/size syntax
			else if (option.matches("^[0-9]+/[0-9]+$")) {
				String[] interval = cmd.getOptionValue("i").split("/");
				int rank = Integer.parseInt(interval[0]);
				int size = Integer.parseInt(interval[1]);	
				int simCountPerNode = (simCount / size) + 1;
				//if (simCountPerNode == 0) simCountPerNode = 1;
				
				start = rank * simCountPerNode;
				stop = start + simCountPerNode - 1;
			}
			else {
				// argument not valid
				return;
			}
		} else {
			start = 0;
			stop = simCount - 1;
		}
		
		// correct stop if necessary
		if (stop >= simCount) stop = simCount - 1;
		
		// nothing to do
		if (start >= simCount) return;
    	
    	// start simulations
    	for (int i=start; i<=stop; i++) {
//    		final Object[] runArgs = _project.retrieveParamSet(i);
    		final Object[] runArgs = _project.getParamSets()[i];
    		Runnable simulation = new Runnable() {
				@Override
				public void run() {
					_project.startSimulation(runArgs);
				}
    		};
    		new Thread(simulation).start();
    	}
	}

}

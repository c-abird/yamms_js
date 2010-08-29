# YammsJs
YammsJs is a JavaScript interface to the YammsCore

## Build and Run
* Make shure you have an up-to-date yamms_core.jar in your lib folder
* Run `ant` to build a runnable jar file.
* Create a run.js project file (See next section)
* Run `java jar dist/bin/yamms_js.jar -c run.js`

## Solving the muMag standard problem 4:
The standard problem 4 as defined by the muMag group could be simulated by the following:

### run.js
    var project = new SimpleProject() {
      startSimulation: function(params) {
        // Load the initial magnetization
        var m = OmfFileService.readFile("sp4.omf");

        // Set the spatially constant saturation magnetization
        var ms = RealScalarField.getUniformField(m.topology, 8e5);

        // Set up the constituents of the effective field
        var field = new CompositeFieldTerm();
        field.addFieldTerm(new ExchangeField(13e-12, ms));
        field.addFieldTerm(new DemagField(m.topology));
        field.addFieldTerm(new StaticZeemanField([
          -24.6e-3 / Constants.MU0, 
          4.3e-3 / Constants.MU0,
          0
        ]));

        // Set up the model (the LLG)
        var model = new BasicModel(2.211e5, 0.02, ms, field);
        
        // Set up the solver (time integrator)
        var solver = new DormandPrinceSolver(0, 1e-10, 1, 1e-4);

        // Register storage handlers
        solver.addHandler(new OmfStorageHandler("./omf", ""), 50);
        solver.addHandler(new ScalarStorageHandler("table"), 10);

        // Start the simulation
        solver.integrate(model, m, 1e-9);
      }
    };

Copyright (C) 2009-2010 Claas Abert

Distributed under the LGPL. See LICENSE file.

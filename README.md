Example utilization
```java
GeneticAlgorithm<Boolean> ga = new GeneticAlgorithm<>();

EndConditionFactory endConditionFactory = new EndConditionFactory();

ga.setCrossoverFunction(new KPointCrossover<>(5));
ga.setChromosomePrototype(new BooleanWithFitnessChromosome(100));
ga.setEndCondition(endConditionFactory.createEpochNumReached(300));
ga.setSelectionStrategy(new TournamentSelection<>(10));
ga.setPopulationSize(500);
ga.setKeepBestN(5);

ga.addCallback(new FitnessPlotter<>());

PopulationPlotter<Boolean> populationPlotter = new PopulationPlotter<>(Main::chromosomeToArray);
populationPlotter.setBrightnessStrategy(new NoOutliersMinMaxBrightnessStrategy(1));
populationPlotter.setPopulationStrategy(new CachedPopulationStrategy<>(5000));
ga.addCallback(populationPlotter);

ga.run();
```
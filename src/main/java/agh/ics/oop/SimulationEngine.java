package agh.ics.oop;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SimulationEngine {

    private final List<Simulation> simulations = new ArrayList<>();
    private final List<Thread> threads = new ArrayList<>();
    private final ExecutorService executor = Executors.newFixedThreadPool(4);

    public SimulationEngine(List<Simulation> simulations){
        this.simulations.addAll(simulations);
    }

    public void runSync(){
        for (Simulation sim : simulations){
            sim.run();
        }
    }

    public void runAsync(){
        for (Simulation sim: simulations) {
            Thread thread = new Thread(sim);
            threads.add(thread);
            thread.start();
        }
    }

    public void awaitSimulationsEnd() throws InterruptedException {
        for (Thread t: threads){
            t.join();
        }
        threads.clear();
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);
    }

    public void runAsyncInThreadPool(){
        for (Simulation sim : simulations){
            executor.submit(sim);
        }
    }
}

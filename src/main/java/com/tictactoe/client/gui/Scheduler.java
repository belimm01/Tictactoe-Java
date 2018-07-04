package com.tictactoe.client.gui;

import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.util.Duration;

//spuste nejakou metodu za nejaky zadany cas
public class Scheduler {
    public static ScheduledService<Void> schedule(Runnable runnable, int mills) {
        ScheduledService<Void> scheduledService = new ScheduledService<Void>() {

            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        while (true) {
                            if (isCancelled()) {
                                updateMessage("Cancelled");
                                break;
                            }

                            runnable.run();

                            try {
                                Thread.sleep(mills);
                            } catch (InterruptedException interrupted) {
                                if (isCancelled()) {
                                    updateMessage("Cancelled");
                                    break;
                                }
                            }
                        }
                        return null;
                    }
                };
            }
        };
        scheduledService.setPeriod(Duration.seconds(1));
        scheduledService.start();
        return scheduledService;
    }
}

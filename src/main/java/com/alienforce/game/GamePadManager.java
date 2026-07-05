package com.alienforce.game;

import java.util.function.Consumer;
import java.util.stream.Stream;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Event;

public class GamePadManager {
    private final Consumer<Event> consumer;
    private Controller controller;

    public GamePadManager(final Consumer<Event> consumer) {
        this.consumer = consumer;
        initialize();
    }

    private void initialize() {
        controller = Stream.of(ControllerEnvironment.getDefaultEnvironment().getControllers())
            .filter(controllerLocal -> controllerLocal.getType() == Controller.Type.GAMEPAD)
            .findFirst()
            .orElse(null);
    }

    public void update() {
        if (controller == null) {
            return;
        }

        controller.poll();
        final var queue = controller.getEventQueue();
        final var event = new Event();
        while (queue.getNextEvent(event)) {
            consumer.accept(event);
        }
    }
}

public interface Updatable {
    // every object that implements Updatable must implement their form of update()
    void update(double timeUnit); // is called every 'time unit'
}
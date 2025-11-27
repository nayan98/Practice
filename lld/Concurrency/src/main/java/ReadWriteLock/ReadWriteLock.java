package ReadWriteLock;

public class ReadWriteLock {
    // Prefer Writer Read Write lock
    Boolean writer;
    Integer reader;
    Integer waitingWriter;
    ReadWriteLock() {
        writer = false;
        reader = 0;
        waitingWriter = 0;
    }
    public synchronized void readLock() {
        try {
            while( writer || waitingWriter>0 ) {
                wait();
            }
            reader++;
        }
        catch (InterruptedException ignore) {
            Thread.currentThread().interrupt();
        }
    }
    public synchronized void readUnlock() {
        reader--;
        if (reader == 0) {
            notifyAll();
        }
    }
    public synchronized void writeLock() {
        try {
            waitingWriter++;
            while (writer || reader>0) {
                wait();
            }
            waitingWriter--;
            writer = true;
        }
        catch (InterruptedException ignore) {

        }
    }
    public synchronized void writeUnlock() {
        writer = false;
        notifyAll();
    }
}

package IAmNotJustJess.destroyTheMonument.utility;

import java.io.IOException;
import java.io.PipedOutputStream;
import java.io.PipedInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

public class PipedDeepCopy {
    private static final Object ERROR = new Object();

    public static Object copy(Object originalObject) {
        Object object = null;
        try {
            PipedInputStream pipedInputStream = new PipedInputStream();
            PipedOutputStream pipedOutputStream = new PipedOutputStream(pipedInputStream);

            Deserializer deserializer = new Deserializer(pipedInputStream);

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(pipedOutputStream);
            objectOutputStream.writeObject(originalObject);

            object = deserializer.getDeserializedObject();

            if (object == ERROR)
                object = null;
        }
        catch(IOException ioe) {
            ioe.printStackTrace();
        }

        return object;
    }

    private static class Deserializer extends Thread {

        private Object object = null;
        private Object lockedObject = null;

        private PipedInputStream pipedInputStream = null;

        public Deserializer(PipedInputStream pipedInputStream) throws IOException {
            lockedObject = new Object();
            this.pipedInputStream = pipedInputStream;
            start();
        }

        public void run() {
            Object object1 = null;
            try {
                ObjectInputStream oin = new ObjectInputStream(pipedInputStream);
                object1 = oin.readObject();
            }
            catch(IOException | ClassNotFoundException exception) {
                exception.printStackTrace();
            }

            synchronized(lockedObject) {
                if (object1 == null)
                    object = ERROR;
                else
                    object = object1;
                lockedObject.notifyAll();
            }
        }

        public Object getDeserializedObject() {
            try {
                synchronized(lockedObject) {
                    while (object == null) {
                        lockedObject.wait();
                    }
                }
            }
            catch(InterruptedException ie) {
            }
            return object;
        }
    }

}
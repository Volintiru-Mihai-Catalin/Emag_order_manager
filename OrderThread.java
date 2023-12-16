import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class OrderThread extends Thread {

    private final Integer id;
    private final BufferedReader orderReader;
    private final String fileName;
    private final BufferedWriter outputPathProd;
    private final BufferedWriter outputPathOrd;
    private final Integer maxTh;

    private final ExecutorService tpe;

    private final ReentrantLock mutex = new ReentrantLock();

    public OrderThread(Integer id, BufferedReader orderReader, BufferedWriter outputPathProd, BufferedWriter outputPathOrd, String fileName, Integer maxTh, ExecutorService tpe) {
        this.id = id;
        this.orderReader = orderReader;
        this.outputPathProd = outputPathProd;
        this.outputPathOrd = outputPathOrd;
        this.fileName = fileName;
        this.maxTh = maxTh;
        this.tpe = tpe;
    }

    @Override
    public void run() {
        try {
            String str;

            while ((str = orderReader.readLine()) != null) {
                String[] split_str = str.split(",", 2);

                if (Integer.parseInt(split_str[1]) != 0) {
                    Future<String> result = tpe.submit(new ProductThreads(fileName, outputPathProd, str));
                    if (result.get().compareTo("OK") == 0) {
                        mutex.lock();
                        synchronized (outputPathOrd) {
                            outputPathOrd.write(str + ",shipped");
                            outputPathOrd.newLine();
                            outputPathOrd.flush();
                        }
                        mutex.unlock();
                    }
                }
            }

        } catch (IOException | ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

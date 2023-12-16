import java.io.BufferedWriter;
import java.io.File;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.ReentrantLock;

public class ProductThreads implements Callable<String> {

    private final String fileName;
    private final BufferedWriter outputFile;
    private final String str;
    private final ReentrantLock mutex = new ReentrantLock();

    public ProductThreads(String fileName, BufferedWriter outputPath, String str) {
        this.fileName = fileName;
        this.outputFile = outputPath;
        this.str = str;
    }

    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     * @throws Exception if unable to compute a result
     */
    @Override
    public String call() throws Exception {

        String[] split_str = str.split(",", 2);
        int prodNum = Integer.parseInt(split_str[1]);
        Scanner scn = new Scanner(new File(fileName));

        while (scn.hasNextLine()) {
            if (prodNum == 0) {
                return "OK";
            }
            String product = scn.nextLine();
            if (product != null) {
                String[] product_split = product.split(",", 2);
                if (split_str[0].compareTo(product_split[0]) == 0) {
                    mutex.lock();
                    synchronized (outputFile) {
                        outputFile.write(product + ",shipped");
                        outputFile.newLine();
                        outputFile.flush();
                    }
                    mutex.unlock();
                    prodNum--;
                }
            }
        }

        return "OK";
    }
}

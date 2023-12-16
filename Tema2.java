import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class Tema2 {

    public static void main (String[] args) throws IOException {
        FileReader orderFile = new FileReader(args[0] + "/orders.txt");
        BufferedReader orderReader = new BufferedReader(orderFile);

        OrderThread[] orders = new OrderThread[Integer.parseInt(args[1])];
        String productFile = args[0] + "/order_products.txt";
        BufferedWriter outputFileProd = new BufferedWriter(new FileWriter("order_products_out.txt"));
        BufferedWriter outputFileOrder = new BufferedWriter(new FileWriter("orders_out.txt"));
        int maxTh = Integer.parseInt(args[1]);

        ExecutorService tpe = Executors.newFixedThreadPool(maxTh);

        for (int index = 0; index < Integer.parseInt(args[1]); index++) {
            orders[index] = new OrderThread(index, orderReader, outputFileProd, outputFileOrder, productFile, maxTh, tpe);
            orders[index].start();
        }

        for (int index = 0; index < maxTh; index++) {
            try {
                orders[index].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        tpe.shutdown();
        orderReader.close();
        outputFileOrder.close();
        outputFileProd.close();
    }

}

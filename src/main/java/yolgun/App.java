package yolgun;

import java.util.concurrent.TimeUnit;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;

/**
 * Hello world!
 *
 */
public class App 
{
    static final MetricRegistry metrics = new MetricRegistry();
    static {
        ConsoleReporter reporter = ConsoleReporter.forRegistry(metrics)
                                                  .convertRatesTo(TimeUnit.SECONDS)
                                                  .convertDurationsTo(TimeUnit.MILLISECONDS)
                                                  .build();
        reporter.start(1, TimeUnit.SECONDS);
    }

    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );

        UltHttpConnection ultHttpConnection = new UltHttpConnection();
        MyConnection myConnection = new MyConnection();

        while (true) {
            metrics.meter("requests").mark();

//            try {
//                String result = ultHttpConnection.processHttpMessage("https://www.google.com", null);
//                System.out.println("result: " + result + "<-----------------------");
//                System.out.println("<-----------------------");
//                Thread.sleep(100);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

            try {
                String result = myConnection.processHttpMessage("https://www.google.com", null);
//                System.out.println("result: " + result + "<-----------------------");
//                System.out.println("<-----------------------");
//                Thread.sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

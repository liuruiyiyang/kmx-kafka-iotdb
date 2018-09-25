package tsinghua;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tsinghua.conf.Config;
import tsinghua.conf.ConfigDescriptor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * author: liurui
 *
 */
public class KafkaIoTDB
{
    private static Config config = ConfigDescriptor.getInstance().getConfig();
    private static Logger logger = LoggerFactory.getLogger(KafkaIoTDB.class);

    public static void main( String[] args )
    {
        ExecutorService executorService = Executors.newFixedThreadPool(config.CLIENT_NUMBER);
        logger.info("Using {} threads ...");
        for(int i = 0;i < config.CLIENT_NUMBER;i++){
            executorService.submit(new KConsumer(config.TOPIC));
        }
        executorService.shutdown();
    }
}

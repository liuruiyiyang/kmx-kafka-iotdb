package tsinghua;

import com.alibaba.fastjson.JSON;
import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tsinghua.conf.Config;
import tsinghua.conf.ConfigDescriptor;
import tsinghua.db.IoTDB;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class KConsumer extends Thread {
    private String topic;
    private Config config ;
    private final String TENANT = "tenant";
    private final String USER = "user";
    private final String TSTABLE = "tstable";
    private final String TIMESTAMP = "timestamp";
    private final String TAGS = "tags";
    private final String SENSORS = "sensors";
    private Map<String, String> tagMap ;
    String tenant ;
    String user ;
    String tsTable ;
    private static Logger logger = LoggerFactory.getLogger(KConsumer.class);

    public KConsumer(String topic){
        super();
        config = ConfigDescriptor.getInstance().getConfig();
        this.topic = topic;
    }


    //    { 
//        "tenant":"myTenant", 
//        "user":"user1", 
//        "tstable":"group_0",
//            "timestamp":"2018-01-01T00:00:00.000+08:00",
//            "tags":{
//        "device":"d_0",
//                "part":"p_0"
//    }, 
//        "sensors":{ 
//        "s_0":5, 
//        "s_1":123, 
//        "s_2":10.34, 
//        "s_3":23 
//    } 
//
// insert into root.group_0.d_0.p_0 (timestamp,s_0,s_1,s_2,s_3) values(2018-01-01T00:00:00.000+08:00,5,123,10.34,23)
    @Override
    public void run(){
        ConsumerConnector consumerConnector = createConsumer();
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(topic, 1); // 1表示consumer thread线程数量
        Map<String, List<KafkaStream<byte[], byte[]>>> messageStreams = consumerConnector.createMessageStreams(topicCountMap);
        KafkaStream<byte[], byte[]> stream = messageStreams.get(topic).get(0);
        ConsumerIterator<byte[], byte[]> iterator = stream.iterator();
        IoTDB ioTDB = null;
        try {
            ioTDB = new IoTDB();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            while (iterator.hasNext()) {
                String message = new String(iterator.next().message());
                System.out.println(Thread.currentThread().getName() + message);
                Map<String, Object> sensorMap = null;
                long timestamp = -1;
                try {
                    tenant = JSON.parseObject(message).getString(TENANT);
                    user = JSON.parseObject(message).getString(USER);
                    tsTable = JSON.parseObject(message).getString(TSTABLE);
                    timestamp = JSON.parseObject(message).getLong(TIMESTAMP);
                    tagMap = (Map<String, String>) JSON.parseObject(message).get(TAGS);
                    sensorMap = (Map<String, Object>) JSON.parseObject(message).get(SENSORS);
                } catch (Exception e){
                    logger.error("JSON格式错误无法解析: {}", message);
                    e.printStackTrace();
                }
                logger.info("接收到: " + message);
                String path = parseTags(tagMap);
                if(sensorMap!=null&&timestamp!=-1) {
                    String sql = createInsertSQL(path, sensorMap, timestamp);
                    if (ioTDB != null)
                        ioTDB.executeSQL(sql);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            try {
                assert ioTDB != null;
                ioTDB.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    private String createInsertSQL(String path, Map sensorMap, long timestamp){
        StringBuilder sb = new StringBuilder();
        sb.append("insert into ").append(path).append(" ");
        sb.append("(timestamp");
        sensorMap.forEach((k,v) -> sb.append(",").append(k));
        sb.append(") values(").append(timestamp);
        sensorMap.forEach((k,v)->{
            sb.append(",").append(v);
        });
        sb.append(")");
        return sb.toString();
    }

    private String parseTags(Map tagMap){
        StringBuilder sb = new StringBuilder();
        sb.append("root.").append(tsTable);
        //FIXME tag的顺序可能存在是否匹配iotdb路径的问题
        tagMap.forEach((k,v) -> sb.append(".").append(v));
        return sb.toString();
    }

    private ConsumerConnector createConsumer(){
        Properties properties = new Properties();
        properties.put("zookeeper.connect", config.DB_URL);
        properties.put("group.id", "group1");
        return Consumer.createJavaConsumerConnector(new ConsumerConfig(properties));
    }

    public static void main(String[] args){
        new KConsumer("test").start();
    }

}


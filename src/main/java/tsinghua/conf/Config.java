package tsinghua.conf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Config {
	private static final Logger LOGGER = LoggerFactory.getLogger(Config.class);
	private String deviceCode;

	public Config(){

	}

	public String host="192.168.130.20";
	public String port="6667";
	public String TOPIC="test2";
	/** 设备数量 */
	public int DEVICE_NUMBER = 2;
	/** 测试客户端线程数量 */
	public int CLIENT_NUMBER = 5;
	/** 每个设备的传感器数量 */
	public int SENSOR_NUMBER = 5;
	/** 数据采集步长 */
	public long POINT_STEP = 5000;
	/** 数据发送缓存条数 */
	public int CACHE_NUM = 10;
	/** 存储组数量 */
	public int GROUP_NUMBER = 1;
	/** 数据编码方式 */
	public String ENCODING = "PLAIN";
	/**是否为多设备批插入模式*/
	public boolean MUL_DEV_BATCH = false;
	/**是否为批插入乱序模式*/
	public boolean IS_OVERFLOW = false;
	/**乱序模式*/
	public int OVERFLOW_MODE = 0;
	/**批插入乱序比例*/
	public double OVERFLOW_RATIO = 1.0;

	public double LAMBDA = 3;

	public int MAX_K = 10;

	public boolean IS_RANDOM_TIMESTAMP_INTERVAL = false ;

	public int START_TIMESTAMP_INDEX = 20;

	public boolean USE_OPS = false;

	public double CLIENT_MAX_WRT_RATE = 10000000.0;

	/**系统性能检测时间间隔-2秒*/
 	public int INTERVAL = 0;
 	/**系统性能检测网卡设备名*/
 	public String NET_DEVICE = "e";
 	/**存储系统性能信息的文件路径*/
 	public String SERVER_MODE_INFO_FILE = "";

	/**一个样例数据的存储组名称*/
 	public String STORAGE_GROUP_NAME ;
	/**一个样例数据的时序名称*/
 	public String TIMESERIES_NAME ;
	/**一个时序的数据类型*/
 	public String TIMESERIES_TYPE ;
	/**时序数据取值范围*/
	public String TIMESERIES_VALUE_SCOPE ;
	/**样例数据生成路径及文件名*/
	public String GEN_DATA_FILE_PATH = "/home/liurui/sampleData";
	/**上一次结果的日志路径*/
	public String LAST_RESULT_PATH = "/var/lib/jenkins/workspace/IoTDBWeeklyTest/iotdb-benchmark/logs";

	/**存放SQL语句文件的完整路径*/
	public String SQL_FILE = "/var/lib/jenkins/workspace/IoTDBWeeklyTest/iotdb-benchmark/SQLFile";

	/** 文件的名字 */
	public String FILE_PATH ;
	/** 是否从文件读取数据*/
	public boolean READ_FROM_FILE = false;
	/** 一次插入到数据库的条数 */
	public int BATCH_OP_NUM = 100;

	public boolean TAG_PATH = true;

	public String LOG_STOP_FLAG_PATH;

	public int STORE_MODE = 1;

	public long LOOP = 10000;

	/** 数据采集丢失率 */
	public double POINT_LOSE_RATIO = 0.01;
	// ============各函数比例start============//FIXME 传参数时加上这几个参数
	/** 线性 默认 9个 0.054 */
	public double LINE_RATIO = 0.054;
	/** 傅里叶函数 6个 0.036 */
	// public static double SIN_RATIO=0.386;//0.036
	public double SIN_RATIO = 0.036;// 0.036
	/** 方波 9个 0.054 */
	public double SQUARE_RATIO = 0.054;
	/** 随机数 默认 86个 0.512 */
	public double RANDOM_RATIO = 0.512;
	/** 常数 默认 58个 0.352 */
	// public static double CONSTANT_RATIO= 0.002;//0.352
	public double CONSTANT_RATIO = 0.352;// 0.352


	/** 历史数据结束时间 */
	public long HISTORY_END_TIME;

	// 负载生成器参数 start
	/** LoadBatchId 批次id */
	public Long PERFORM_BATCH_ID;

	// 负载测试完是否删除数据
	public boolean IS_DELETE_DATA = false;
	public double WRITE_RATIO = 0.2;
	public double SIMPLE_QUERY_RATIO = 0.2;
	public double MAX_QUERY_RATIO = 0.2;
	public double MIN_QUERY_RATIO = 0.2;
	public double AVG_QUERY_RATIO = 0.2;
	public double COUNT_QUERY_RATIO = 0.2;
	public double SUM_QUERY_RATIO = 0.2;
	public double RANDOM_INSERT_RATIO = 0.2;
	public double UPDATE_RATIO = 0.2;

	//iotDB查询测试相关参数
	public int QUERY_SENSOR_NUM = 1;
	public int QUERY_DEVICE_NUM = 1;
	public int QUERY_CHOICE = 1;
	public String QUERY_AGGREGATE_FUN = "";
	public long QUERY_INTERVAL = DEVICE_NUMBER * POINT_STEP;
	public double QUERY_LOWER_LIMIT = 0;
	public boolean IS_EMPTY_PRECISE_POINT_QUERY = false;
	public long TIME_UNIT = QUERY_INTERVAL / 2;
	public long QUERY_SEED = 1516580959202L;
	public int QUERY_LIMIT_N = 1;
	public int QUERY_LIMIT_OFFSET = 0;
	public int QUERY_SLIMIT_N = 1;
	public int QUERY_SLIMIT_OFFSET = 0;
	public boolean CREATE_SCHEMA = true;

	//mysql相关参数
	// mysql服务器URL以及用户名密码
	public String MYSQL_URL = "jdbc:mysql://166.111.141.168:3306/benchmark?"
			+ "user=root&password=Ise_Nel_2017&useUnicode=true&characterEncoding=UTF8&useSSL=false";
	//是否将结果写入mysql
	public boolean IS_USE_MYSQL = false;
	public boolean IS_SAVE_DATAMODEL = false;

	public String REMARK = "";
	public String VERSION = "";

	// InfluxDB参数
	// 服务器URL
	public String DB_URL = "192.168.130.16:2181";
	// 使用的数据库名
	public String INFLUX_DB_NAME = "test";
	
	// 使用的数据库
	public String DB_SWITCH = "IoTDB";

	//benchmark 运行模式
	public String BENCHMARK_WORK_MODE="";
	//the file path of import data
	public String IMPORT_DATA_FILE_PATH = "";
	//import csv数据文件时的BATCH
	public int BATCH_EXECUTE_COUNT = 5000;
	//mataData文件路径
	public String METADATA_FILE_PATH = "";


}

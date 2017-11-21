package util;

import java.io.FileNotFoundException;


public class CreateCodeMain {
    static String CONTROLLER_SUFFIX=".htm";//生成功能URL后缀，web.xml中配置
    static String SRC="src";//源文件目录(自定义，默认src)
    static String DAO_PACKAGE = "com.pay.usercard.dao";//DAO目录
    static String SERVICE_PACKAGE = "com.pay.usercard.service";//业务类目录
    static String CONTROLLER_PACKAGE = "com.pay.usercard.controller";//控制类目录
    static String JSP_PACKAGE = "pay/jsp/pay/usercard";//jsp目录
    static String DB_DRIVER_CLASS = "oracle.jdbc.driver.OracleDriver";//数据库驱动类
    static String DB_CONNECTION_URL = "jdbc:oracle:thin:@localhost:1521:orcl";//数据库连接URL
    static String DB_USER_NAME = "pay_db";//数据库用户localdb
    static String DB_PASSWORD = "pay_db123";//数据库密码abc123    
//    static String DB_CONNECTION_URL = "jdbc:oracle:thin:@118.244.200.97:1521:tds";//数据库连接URL
//    static String DB_USER_NAME = "newpaycard";//数据库用户localdb
//    static String DB_PASSWORD = "Asdf1234";//数据库密码abc123
    static String LIST_PAGE_TITLE = "同卡进出绑定信息";//列表页tab 标题
    static String TABLE = "";//生成数据库对象的表
    //查询条件：银行状态，银行编号，银行名称，托管银行标志
    static String [] SEARCH_COLUMN = {"CUST_ID","ACCOUNT_NO","CREDENTIAL_NO"};//生成查询列
    /**
     * @param args
     * @throws Exception 
     * @throws FileNotFoundException 
     */
    public static void main(String[] args) throws Exception {
    	//创建多页面
    	new CreateCodeFromDB(CONTROLLER_SUFFIX,SRC,DAO_PACKAGE,SERVICE_PACKAGE
			,CONTROLLER_PACKAGE,JSP_PACKAGE,DB_DRIVER_CLASS,DB_CONNECTION_URL,DB_USER_NAME
			,DB_PASSWORD,LIST_PAGE_TITLE,TABLE,SEARCH_COLUMN).start();
    	//创建单页面
//    	new CreateCodeFromDBSample(CONTROLLER_SUFFIX,SRC,DAO_PACKAGE,SERVICE_PACKAGE
//			,CONTROLLER_PACKAGE,JSP_PACKAGE,DB_DRIVER_CLASS,DB_CONNECTION_URL,DB_USER_NAME
//			,DB_PASSWORD,LIST_PAGE_TITLE,TABLE,SEARCH_COLUMN).start();
    }
}
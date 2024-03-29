package kim.sesame.framework.util.ssh;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

import java.io.*;

//@Slf4j
public class RemoteCommandUtil {

    private static String DEFAULTCHART = "UTF-8";

    /**
     * 登录主机
     *
     * @return 登录成功返回true，否则返回false
     */
    public static Connection login(String ip, String userName, String userPwd) {

        boolean flg = false;
        Connection conn = null;
        try {
            conn = new Connection(ip);
            conn.connect();//连接
            flg = conn.authenticateWithPassword(userName, userPwd);//认证
            if (flg) {
//                log.info("=========登录成功=========" + conn);
                return conn;
            }
        } catch (IOException e) {
//            log.error("=========登录失败=========" + e.getMessage());
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * 远程执行shll脚本或者命令
     *
     * @param cmd 即将执行的命令
     * @return 命令执行完后返回的结果值
     */
    public static String execute(Connection conn, String cmd) {
        String result = "";
        try {
            if (conn != null) {
                Session session = conn.openSession();//打开一个会话
                session.execCommand(cmd);//执行命令
                result = processStdout(session.getStdout(), DEFAULTCHART);
                //如果为得到标准输出为空，说明脚本执行出错了
                if (isEmpty(result)) {
//                    log.info("得到标准输出为空,链接conn:" + conn + ",执行的命令：" + cmd);
                    result = processStdout(session.getStderr(), DEFAULTCHART);
                } else {
//                    log.info("执行命令成功,链接conn:" + conn + ",执行的命令：" + cmd);
                }
                conn.close();
                session.close();
            }
        } catch (IOException e) {
//            log.info("执行命令失败,链接conn:" + conn + ",执行的命令：" + cmd + "  " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 远程执行shll脚本或者命令
     * @return
     */
    public static String execute(String ip, String userName, String userPwd, String cmd) {
        Connection conn = login(ip, userName, userPwd);
        return execute(conn, cmd);
    }

    /**
     * 解析脚本执行返回的结果集
     *
     * @param in      输入流对象
     * @param charset 编码
     * @return 以纯文本的格式返回
     */
    private static String processStdout(InputStream in, String charset) {
        InputStream stdout = new StreamGobbler(in);
        StringBuffer buffer = new StringBuffer();
        ;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(stdout, charset));
            String line = null;
            while ((line = br.readLine()) != null) {
                buffer.append(line + "\n");
            }
        } catch (UnsupportedEncodingException e) {
//            log.error("解析脚本出错：" + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
//            log.error("解析脚本出错：" + e.getMessage());
            e.printStackTrace();
        }
        return buffer.toString();
    }


    /**
     * string 非空校验
     */
    private static boolean isNotEmpty(Object obj) {
        if (obj == null || obj.equals("")) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * string 对象为空的判断
     */
    private static boolean isEmpty(Object obj) {
        return !isNotEmpty(obj);
    }
}
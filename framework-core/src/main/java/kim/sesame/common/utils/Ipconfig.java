package kim.sesame.common.utils;

import lombok.Getter;
import lombok.Setter;

import java.net.InetAddress;
import java.net.NetworkInterface;

/**
 * 获取本机ip地址和相关信息
 * Ipconfig ip = Ipconfig.getInfo();
 */
@Setter
@Getter
public class Ipconfig {

    /**
     * 本机名称
     */
    private String localname;
    /**
     * 本机的ip
     */
    private String localip;
    /**
     * mac 地址
     */
    private String mac;

    @Override
    public String toString() {
        return "Ipconfig{" +
                "localname='" + localname + '\'' +
                ", localip='" + localip + '\'' +
                ", mac='" + mac + '\'' +
                '}';
    }

    public static Ipconfig getInfo() {

        Ipconfig bean = new Ipconfig();

        InetAddress ia = null;
        try {
            ia = ia.getLocalHost();

            String localname = ia.getHostName();
            String localip = ia.getHostAddress();

            bean.setLocalip(localip);
            bean.setLocalname(localname);
            //System.out.println("本机名称是："+ localname);
            //System.out.println("本机的ip是 ："+localip);

//            InetAddress ia1 = InetAddress.getLocalHost();//获取本地IP对象
//            bean.setMac(getMACAddress(ia1));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return bean;
    }

    /**
     * 获取MAC地址的方法
     *
     * @param ia
     * @return
     * @throws Exception
     */
    private static String getMACAddress(InetAddress ia) throws Exception {
        //获得网络接口对象（即网卡），并得到mac地址，mac地址存在于一个byte数组中。
        byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();

        //下面代码是把mac地址拼装成String
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < mac.length; i++) {
            if (i != 0) {
                sb.append("-");
            }
            //mac[i] & 0xFF 是为了把byte转化为正整数
            String s = Integer.toHexString(mac[i] & 0xFF);
            //System.out.println("--------------");
            //System.err.println(s);

            sb.append(s.length() == 1 ? 0 + s : s);
        }

        //把字符串所有小写字母改为大写成为正规的mac地址并返回
        return sb.toString().toUpperCase();
    }

public static void main(String[] args) {
    System.out.println(getInfo());
}
    /*
    Enumeration allNetInterfaces = NetworkInterface.getNetworkInterfaces();
InetAddress ip = null;
while (allNetInterfaces.hasMoreElements())
{
NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
System.out.println(netInterface.getName());
Enumeration addresses = netInterface.getInetAddresses();
while (addresses.hasMoreElements())
{
ip = (InetAddress) addresses.nextElement();
if (ip != null && ip instanceof Inet4Address)
{
System.out.println("本机的IP = " + ip.getHostAddress());
}
}
}

     */
}
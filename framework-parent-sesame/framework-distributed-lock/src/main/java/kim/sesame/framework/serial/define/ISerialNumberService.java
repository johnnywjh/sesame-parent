package kim.sesame.framework.serial.define;


/**
 * 序列号Service
 *
 **/
public interface ISerialNumberService {
    /**
     * 生成序列号
     * @param serialNumberRule 序列号规则
     * @param params 参数
     * @return
     */
    String generateSerialNumber(SerialNumberRule serialNumberRule, String... params);
}

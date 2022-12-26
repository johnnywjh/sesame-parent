package kim.sesame.framework.serial.service;

import kim.sesame.common.utils.Argument;
import kim.sesame.common.utils.DateUtil;
import kim.sesame.framework.serial.dao.SerialNumberRuleDao;
import kim.sesame.framework.serial.define.ISerialNumberService;
import kim.sesame.framework.serial.define.SerialNumberRule;
import kim.sesame.framework.serial.entity.SerialNumberRuleEntity;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;

/**
 * 序列号Service
 **/
@Service
@Slf4j
public class SerialNumberService implements ISerialNumberService, InitializingBean {

    /**
     * Redis 锁前缀
     */
    private static final String LOCK_KEY_PREFIX = "serial_number_lock";

    @SuppressWarnings("all")
    @Autowired
    private SerialNumberRuleDao serialNumberRuleDao;
    @Autowired
    private RedissonClient redissonClient;

    @Override
    public void afterPropertiesSet() throws Exception {
        serialNumberRuleDao.check_notExistsCreate();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
//    @Lock(keys = "#serialNumberRule.code",keyConstant = LOCK_KEY_PREFIX)
    public String generateSerialNumber(SerialNumberRule serialNumberRule, String... params) {
        Argument.notNull(serialNumberRule, "序号规则不能为空!");

        RLock lock = redissonClient.getLock(LOCK_KEY_PREFIX + serialNumberRule.getCode());
        try {
            lock.lock();

            SerialNumberRuleEntity serialNumberRuleEntity = getSerialNumberRule(serialNumberRule);
            SerialNumberEntry result = spliceSerialNumber(serialNumberRule,
                    serialNumberRuleEntity, true, params);
            serialNumberRuleEntity.setCurTime(Calendar.getInstance().getTime());
            serialNumberRuleEntity.setCurNum(result.getNextSequenceValue());
            serialNumberRuleDao.updateSerialNumberRule(serialNumberRuleEntity);
            return result.getSerialNumber();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 获取序号
     *
     * @param serialNumberRule
     * @return
     */
    private SerialNumberRuleEntity getSerialNumberRule(SerialNumberRule serialNumberRule) {
        // 根据业务类型查询序号并添加悲观锁
        SerialNumberRuleEntity serialNumberRuleEntity = serialNumberRuleDao
                .querySerialNumberRuleForUpdate(serialNumberRule.getCode());
        if (serialNumberRuleEntity == null) {
            // 业务类型序号不存在则添加序号信息
            serialNumberRuleEntity = new SerialNumberRuleEntity();
            serialNumberRuleEntity.setId(serialNumberRule.getCode());
            serialNumberRuleEntity.setCode(serialNumberRule.getCode());
            serialNumberRuleEntity.setName(serialNumberRule.getName());
            serialNumberRuleEntity.setCurNum(0L);
            serialNumberRuleEntity.setCurTime(new Date());
            serialNumberRuleDao.addSerialNumberRule(serialNumberRuleEntity);
            // 新增序号后添加悲观锁
            serialNumberRuleEntity = serialNumberRuleDao
                    .querySerialNumberRuleForUpdate(serialNumberRule.getCode());
        }
        return serialNumberRuleEntity;
    }

    /**
     * 拼接序号
     *
     * @param serialNumberRule       序号规则
     * @param serialNumberRuleEntity 序号实体
     * @param params                 参数
     */
    private SerialNumberEntry spliceSerialNumber(
            SerialNumberRule serialNumberRule,
            SerialNumberRuleEntity serialNumberRuleEntity, boolean formal,
            String[] params) {
        StringBuffer newCode = new StringBuffer();
        long newSeq = 0;
        boolean resetNo = false;
        if (serialNumberRule.isNeedParams()) {
            // todo 需要拼接参数待实现

        }
        // 是否需要时间
        if (serialNumberRule.isNeedTime()) {
            // 如果对于上一个已生成的编号已跨天，需重置对应的sequence
//            String currentDate = DateUtil.dateToString(
//                    serialNumberRuleEntity.getCurTime(),
//                    DateUtil.DATE_FORMATTER);
//
//            String nowDate = DateUtil.dateToString(
//                    Calendar.getInstance().getTime(), DateUtil.DATE_FORMATTER);
            Long differDayNum = DateUtil.getDaysDiff(serialNumberRuleEntity.getCurTime(), new Date());
            if (differDayNum > 0) {
                resetNo = true;
            }
        }
        // 字母前缀
        if (serialNumberRule.isNeedLetterPrefix()) {
            newCode.append(serialNumberRule.getLetterPrefix());
        }
        // 时间前缀
        if (serialNumberRule.isNeedTime()) {
            newCode.append(DateUtil.dateToString(Calendar.getInstance().getTime(), serialNumberRule.getTimeFormat()));
        }
        // 分隔符
        if (serialNumberRule.isNeedDelimiter()) {
            newCode.append(serialNumberRule.getDelimiter());
        }
        // 数字项
        if (serialNumberRule.isNeedNumber()) {
            // 针对日期+数字情况，如果跨天，则重置为1
            if (resetNo) {
                newSeq = 1;
                // 针对无需重置的情况，使当前值累加1
            } else {
                newSeq = serialNumberRuleEntity.getCurNum() + 1;
            }
            // 数字项是否固定位数
            if (serialNumberRule.isFixedNumLen()) {
                newCode.append(String.format("%0" + serialNumberRule.getNumLen() + "d", newSeq));
            } else {
                newCode.append(String.valueOf(newSeq));
            }
        }
        // 后缀
        if (serialNumberRule.isNeedLetterSuffix()) {
            newCode.append(serialNumberRule.getLetterSuffix());
        }
        return new SerialNumberEntry(newCode.toString(), Long.valueOf(newSeq));
    }

    private class SerialNumberEntry {
        /**
         * 生成的序号
         */
        private String serialNumber;
        /**
         * 下一个序列值
         */
        private Long nextSequenceValue;

        public SerialNumberEntry(String serialNumber, long nextSequenceValue) {
            this.serialNumber = serialNumber;
            this.nextSequenceValue = nextSequenceValue;
        }

        public String getSerialNumber() {
            return serialNumber;
        }

        public void setSerialNumber(String serialNumber) {
            this.serialNumber = serialNumber;
        }

        public long getNextSequenceValue() {
            return nextSequenceValue;
        }

        public void setNextSequenceValue(long nextSequenceValue) {
            this.nextSequenceValue = nextSequenceValue;
        }
    }
}

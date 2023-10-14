package kim.sesame.common.utils;

import cn.hutool.core.math.Money;

import java.math.BigDecimal;

public class MoneyUtil {

    public static Money parseToMoney(BigDecimal amount) {
        if (amount == null) {
            return new Money();
        }
        return new Money(amount);
    }

    public static Money parseToMoney(String amount) {
        if (amount == null) {
            return new Money();
        } else {
            return new Money(amount);
        }
    }

    /**
     * 传入分
     */
    public static Money parseToMoneyByCent(Long cent) {
        Money money = new Money();
        money.setCent(cent);
        return money;
    }
    /**
     * 传入分
     */
    public static String parseLongToMoneyStr(Long cent) {
        return parseToMoneyByCent(cent).toString();
    }
    /**
     * 传入分
     */
    public static BigDecimal parseLongToAmount(Long cent) {
        return parseToMoneyByCent(cent).getAmount();
    }

    public static Long parseToLong(BigDecimal amount) {
        if (amount == null) {
            return 0L;
        } else {
            return new Money(amount).getCent();
        }
    }
}

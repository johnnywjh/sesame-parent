package kim.sesame.utils;

import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;

public class IdUtils {

    static DefaultIdentifierGenerator defaultIdentifierGenerator = new DefaultIdentifierGenerator();

    public static Long getId() {
        return defaultIdentifierGenerator.nextId(null);
    }

}

package kim.sesame.common.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class JwtUser  implements java.io.Serializable {
    private String loginUserId;
    private long ttlTimeSeconds;

}

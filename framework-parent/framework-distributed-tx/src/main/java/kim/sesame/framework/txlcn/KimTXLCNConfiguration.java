package kim.sesame.framework.txlcn;

import com.codingapi.txlcn.tc.config.EnableDistributedTransaction;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@ComponentScan
@EnableDistributedTransaction
public class KimTXLCNConfiguration {

}

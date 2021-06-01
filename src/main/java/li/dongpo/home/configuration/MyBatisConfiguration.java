package li.dongpo.home.configuration;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author dongpo.li
 * @date 2021/5/27
 */
@Configuration
@MapperScan("li.dongpo.home.repository")
public class MyBatisConfiguration {

}

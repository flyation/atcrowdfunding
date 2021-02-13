import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.mapper.AdminMapper;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

// Spring整合JUnit
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-persist-mybatis.xml"})
public class Test {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private AdminMapper adminMapper;

    @org.junit.Test
    public void test1() throws SQLException {
        Connection connection = dataSource.getConnection();
        System.out.println(connection);
    }

    @org.junit.Test
    public void test2() throws SQLException {
//        Logger logger = LoggerFactory.getLogger(adminMapper.getClass());

        Admin admin = new Admin(null, "tom6", "123123", "汤姆", "tom@qq.com", null);
        int count = adminMapper.insert(admin);
//        logger.debug("影响行数：" + count);
    }

    @org.junit.Test
    public void testLog() {
        Logger logger = LoggerFactory.getLogger(Test.class);
        logger.trace("trace...");
        logger.debug("debug...");
        logger.info("info...");
        logger.warn("warn...");
        logger.error("error...");
    }
}

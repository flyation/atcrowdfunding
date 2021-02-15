import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.mapper.AdminMapper;
import com.atguigu.crowd.service.api.AdminService;
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
@ContextConfiguration(locations = {"classpath:spring-persist-mybatis.xml", "classpath:spring-persist-tx.xml"})
public class Test {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private AdminService adminService;

    @org.junit.Test
    public void test1() throws SQLException {
        Connection connection = dataSource.getConnection();
        System.out.println(connection);
    }

    @org.junit.Test
    public void test2() throws SQLException {
        Admin admin = new Admin(null, "tom6", "123123", "汤姆", "tom@qq.com", null);
        int count = adminMapper.insert(admin);
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

    @org.junit.Test
    public void testTx() {
        Admin admin = new Admin(null, "jerry8", "123123", "杰瑞", "jerry@qq.com", null);
        adminService.saveAdmin(admin);
    }
    @org.junit.Test
    public void testInsert() {
        for (int i = 0; i < 238; i++) {
            Admin admin = new Admin(null, "jerry" + i, "123123", "杰瑞" + i, "jerry" + i + "@qq.com", null);
            adminMapper.insert(admin);
        }
    }

}

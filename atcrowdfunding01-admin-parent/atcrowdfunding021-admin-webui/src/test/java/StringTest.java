import com.atguigu.crowd.util.CrowdUtil;

public class StringTest {

    @org.junit.Test
    public void test() {
        String source = "123123";
        System.out.println(CrowdUtil.md5(source));
    }
}

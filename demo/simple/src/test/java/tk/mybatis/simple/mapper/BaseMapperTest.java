package tk.mybatis.simple.mapper;

import java.io.IOException;
import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.BeforeClass;

public class BaseMapperTest {

    public static SqlSessionFactory sqlSessionFactory;
    
    @BeforeClass
    public static void init() {
        try {
         Reader reader =  Resources.getResourceAsReader("mybatis-config.xml");//加载配置文件和mapper
         sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
         reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

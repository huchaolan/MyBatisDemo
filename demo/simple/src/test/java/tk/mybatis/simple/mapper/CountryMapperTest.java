package tk.mybatis.simple.mapper;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.BeforeClass;
import org.junit.Test;

import tk.mybatis.simple.model.Country;

public class CountryMapperTest {
    private static SqlSessionFactory sqlSessionFactory;
    
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
    
    @Test
    public void testSelectAll() {
        SqlSession sqlSession = sqlSessionFactory.openSession();//打开一个会话
        try{
            List<Country> clist = sqlSession.selectList("selectAll");//查询Country表，在Mapper中查询selectAll的sql，并将查询结果组装resultType指定的集合并返回
            printCountryList(clist);
        }finally{
            sqlSession.close();//关闭连接
        }
    }
    
    private void printCountryList(List<Country> countryList) {
        for(Country country:countryList) {
            System.out.printf("%-4d%4s%4s\n",country.getId(),country.getCountryname(),country.getCountrycode());
        }
    }
}

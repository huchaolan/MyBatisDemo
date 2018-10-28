package tk.mybatis.simple.mapper;

import static org.junit.Assert.fail;

import java.util.List;

import junit.framework.Assert;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import tk.mybatis.simple.model.SysRole;
import tk.mybatis.simple.model.SysUser;

public class UserMapperTest extends BaseMapperTest{

    @Test
    public void testSelectById(){
        SqlSession session = sqlSessionFactory.openSession();
        try{
            UserMapper userMapper = session.getMapper(UserMapper.class);
            SysUser user = userMapper.selectById(1L);
            Assert.assertNotNull(user);
            Assert.assertEquals("admin",user.getUserName());
        }catch(Exception e){
            e.printStackTrace();
            fail("testSelectById excpetion");
        }finally{
            session.close();
        }
    }
    
    @Test
    public void testSelectAllUser() {
        SqlSession session = sqlSessionFactory.openSession();
        try{
           UserMapper userMapper = session.getMapper(UserMapper.class);
           List<SysUser> userList = userMapper.selectAllUser();
           Assert.assertNotNull(userList);
           Assert.assertTrue(!userList.isEmpty());
           for(SysUser user:userList) {
               System.out.println(user);
           }
        }catch(Exception e){
            e.printStackTrace();
            fail("testSelectById excpetion");
        }finally{
            session.close();
        }
    }
    
    @Test
    public void testSelectRoleById() {
        SqlSession session = sqlSessionFactory.openSession();
        try{
           UserMapper userMapper = session.getMapper(UserMapper.class);
           List<SysRole> roleList = userMapper.selectRoleById(1L);
           Assert.assertNotNull(roleList);
           Assert.assertTrue(!roleList.isEmpty());
        }catch(Exception e){
            e.printStackTrace();
            fail("testSelectById excpetion");
        }finally{
            session.close();
        }
    }
}

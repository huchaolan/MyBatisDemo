package tk.mybatis.simple.mapper;

import java.util.List;

import tk.mybatis.simple.model.SysRole;
import tk.mybatis.simple.model.SysUser;

public interface UserMapper {

    public SysUser selectById(Long id);
    
    public List<SysUser> selectAllUser();
    
    public List<SysRole> selectRoleById(Long userid);
}

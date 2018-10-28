package tk.mybatis.simple.mapper;

import tk.mybatis.simple.model.SysUser;

public interface UserMapper {

    public SysUser selectById(Long id);
}

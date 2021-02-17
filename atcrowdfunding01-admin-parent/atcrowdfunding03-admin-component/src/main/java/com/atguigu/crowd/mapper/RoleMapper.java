package com.atguigu.crowd.mapper;

import com.atguigu.crowd.entity.Role;
import com.atguigu.crowd.entity.RoleExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleMapper {
    int countByExample(RoleExample example);

    int deleteByExample(RoleExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Role record);

    int insertSelective(Role record);

    /**
     * 根据关键词查询角色
     * @param keyword 关键词
     * @return 角色列表
     */
    List<Role> selectRoleByKeyword(String keyword);

    List<Role> selectByExample(RoleExample example);

    Role selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Role record, @Param("example") RoleExample example);

    int updateByExample(@Param("record") Role record, @Param("example") RoleExample example);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);

    List<Role> selectAssignedRole(Integer adminId);

    List<Role> selectUnassignedRole(Integer adminId);
}

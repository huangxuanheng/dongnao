package com.dongnaoedu.mall.manager.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dongnaoedu.mall.manager.pojo.TbRole;
import com.dongnaoedu.mall.manager.pojo.TbRoleExample;

public interface TbRoleMapper {

	long countByExample(TbRoleExample example);

	int deleteByExample(TbRoleExample example);

	int deleteByPrimaryKey(Integer id);

	int insert(TbRole record);

	int insertSelective(TbRole record);

	List<TbRole> selectByExample(TbRoleExample example);

	TbRole selectByPrimaryKey(Integer id);

	int updateByExampleSelective(@Param("record") TbRole record, @Param("example") TbRoleExample example);

	int updateByExample(@Param("record") TbRole record, @Param("example") TbRoleExample example);

	int updateByPrimaryKeySelective(TbRole record);

	int updateByPrimaryKey(TbRole record);

	List<String> getUsedRoles(@Param("id") int id);

}
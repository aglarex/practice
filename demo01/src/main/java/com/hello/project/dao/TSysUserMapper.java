package com.hello.project.dao;

import java.util.List;

import com.hello.project.core.Mapper;
import com.hello.project.model.TSysUser;

public interface TSysUserMapper extends Mapper<TSysUser> {
	public List<TSysUser> querySysUserLogin(TSysUser tSysUser);
}
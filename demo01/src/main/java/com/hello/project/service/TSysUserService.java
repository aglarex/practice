package com.hello.project.service;

import com.hello.project.model.TSysUser;

import java.util.List;

import com.hello.project.core.Service;

/**
 * Created by CodeGenerator on 2018/09/05.
 */
public interface TSysUserService extends Service<TSysUser> {
	public List<TSysUser> querySysUserLogin(TSysUser tSysUser);
}

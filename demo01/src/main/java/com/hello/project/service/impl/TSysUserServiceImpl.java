package com.hello.project.service.impl;

import com.hello.project.dao.TSysUserMapper;
import com.hello.project.model.TSysUser;
import com.hello.project.service.TSysUserService;
import com.hello.project.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.annotation.Resource;

/**
 * Created by CodeGenerator on 2018/09/05.
 */
@Service
@Transactional
public class TSysUserServiceImpl extends AbstractService<TSysUser> implements TSysUserService {
	@Resource
	private TSysUserMapper tSysUserMapper;

	@Override
	public List<TSysUser> querySysUserLogin(TSysUser tSysUser) {
		List<TSysUser> list = tSysUserMapper.querySysUserLogin(tSysUser);
		return list;
	}

}

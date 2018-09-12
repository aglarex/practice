package com.hello.project.web;

import com.hello.project.core.Result;
import com.hello.project.core.ResultGenerator;
import com.hello.project.model.TSysUser;
import com.hello.project.service.TSysUserService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * Created by CodeGenerator on 2018/09/05.
 */
@RestController
@RequestMapping("/t/sys/user")
public class TSysUserController {
	@Resource
	private TSysUserService tSysUserService;

	@ApiOperation(value = "测试User表增加接口", notes = "User表增加")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "tSysUser", value = "用户实体类", required = true, dataType = "TSysUser") })
	@PostMapping("/add")
	public Result add(@RequestBody TSysUser tSysUser) {
		tSysUserService.save(tSysUser);
		return ResultGenerator.genSuccessResult();
	}

	@PostMapping("/delete")
	public Result delete(@RequestParam Integer id) {
		tSysUserService.deleteById(id);
		return ResultGenerator.genSuccessResult();
	}

	@PostMapping("/update")
	public Result update(TSysUser tSysUser) {
		tSysUserService.update(tSysUser);
		return ResultGenerator.genSuccessResult();
	}

	@PostMapping("/detail")
	public Result detail(@RequestParam Integer id) {
		TSysUser tSysUser = tSysUserService.findById(id);
		return ResultGenerator.genSuccessResult(tSysUser);
	}

	@PostMapping("/list")
	public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
		PageHelper.startPage(page, size);
		List<TSysUser> list = tSysUserService.findAll();
		PageInfo pageInfo = new PageInfo(list);
		return ResultGenerator.genSuccessResult(pageInfo);
	}

	@GetMapping("/login")
	public Result login(@RequestParam(required=false) String tUserName, @RequestParam(required=false) String tUserPwd, HttpServletResponse response) {
		//response.setHeader("Access-Control-Allow-Origin", "*");
		List<TSysUser> result = null;
		if (!"".equals(tUserName) && !"".equals(tUserPwd)) {
			TSysUser tSysUser = new TSysUser();
			tSysUser.settUserName(tUserName);
			tSysUser.settUserPwd(tUserPwd);
			result = tSysUserService.querySysUserLogin(tSysUser);
		}

		if (result != null && result.size() > 0) {
			return ResultGenerator.genSuccessResult(result);
		}
		return ResultGenerator.genFailResult("登录失败！！！");
	}
}

package com.dongnaoedu.mall.manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dongnaoedu.mall.common.pojo.DataTablesResult;
import com.dongnaoedu.mall.common.pojo.Result;
import com.dongnaoedu.mall.common.utils.ResultUtil;
import com.dongnaoedu.mall.manager.pojo.TbThanks;
import com.dongnaoedu.mall.manager.service.ThanksService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author allen
 */
@RestController
@Api(description = "捐赠管理")
public class ThanksController {

	@Autowired
	private ThanksService thanksService;

	@RequestMapping(value = "/thanks/list", method = RequestMethod.GET)
	@ApiOperation(value = "获取捐赠列表")
	public DataTablesResult getThanksList() {

		DataTablesResult result = thanksService.getThanksList();
		return result;
	}

	@RequestMapping(value = "/thanks/count", method = RequestMethod.GET)
	@ApiOperation(value = "获取捐赠列表总数")
	public Result<Object> getThanksCount() {

		Long result = thanksService.countThanks();
		return new ResultUtil<Object>().setData(result);
	}

	@RequestMapping(value = "/thanks/add", method = RequestMethod.POST)
	@ApiOperation(value = "添加捐赠")
	public Result<Object> addThanks(@ModelAttribute TbThanks tbThanks) {

		thanksService.addThanks(tbThanks);
		return new ResultUtil<Object>().setData(null);
	}

	@RequestMapping(value = "/thanks/update", method = RequestMethod.POST)
	@ApiOperation(value = "编辑捐赠")
	public Result<Object> updateThanks(@ModelAttribute TbThanks tbThanks) {

		thanksService.updateThanks(tbThanks);
		return new ResultUtil<Object>().setData(null);
	}

	@RequestMapping(value = "/thanks/del/{ids}", method = RequestMethod.DELETE)
	@ApiOperation(value = "删除捐赠")
	public Result<Object> delThanks(@PathVariable int[] ids) {

		for (int id : ids) {
			thanksService.deleteThanks(id);
		}
		return new ResultUtil<Object>().setData(null);
	}

	@RequestMapping(value = "/thanks/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "通过id获取捐赠")
	public Result<TbThanks> getThanks(@PathVariable int id) {

		TbThanks tbThanks = thanksService.getThankById(id);
		return new ResultUtil<TbThanks>().setData(tbThanks);
	}
}

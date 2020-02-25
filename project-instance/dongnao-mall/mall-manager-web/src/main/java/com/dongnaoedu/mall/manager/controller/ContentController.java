package com.dongnaoedu.mall.manager.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dongnaoedu.mall.common.pojo.DataTablesResult;
import com.dongnaoedu.mall.common.pojo.Result;
import com.dongnaoedu.mall.common.utils.ResultUtil;
import com.dongnaoedu.mall.content.service.ContentService;
import com.dongnaoedu.mall.manager.pojo.TbPanelContent;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

/**
 * @author allen
 */
@RestController
@Api(description = "板块内容管理")
public class ContentController {

	private final static Logger log = LoggerFactory.getLogger(ContentController.class);

	@Autowired
	private ContentService contentService;

	@RequestMapping(value = "/content/list/{panelId}", method = RequestMethod.GET)
	@ApiOperation(value = "通过panelId获得板块内容列表")
	public DataTablesResult getContentByCid(@PathVariable int panelId) {

		DataTablesResult result = contentService.getPanelContentListByPanelId(panelId);
		return result;
	}

	@RequestMapping(value = "/content/add", method = RequestMethod.POST)
	@ApiOperation(value = "添加板块内容")
	@ApiImplicitParam(name = "tbPanelContent", value = "板块对象", required = true, dataType = "TbPanelContent")
	public Result<Object> addContent(@ModelAttribute TbPanelContent tbPanelContent) {

		contentService.addPanelContent(tbPanelContent);
		return new ResultUtil<Object>().setData(null);
	}

	@RequestMapping(value = "/content/update", method = RequestMethod.POST)
	@ApiOperation(value = "编辑板块内容")
	@ApiImplicitParam(name = "tbPanelContent", value = "板块对象", required = true, dataType = "TbPanelContent")
	public Result<Object> updateContent(@ModelAttribute TbPanelContent tbPanelContent) {

		contentService.updateContent(tbPanelContent);
		return new ResultUtil<Object>().setData(null);
		
	}

	@RequestMapping(value = "/content/del/{ids}", method = RequestMethod.DELETE)
	@ApiOperation(value = "删除板块内容")
	@ApiImplicitParam(name = "ids", value = "用户id列表", required = true, dataType = "int")
	public Result<Object> addContent(@PathVariable int[] ids) {

		for (int id : ids) {
			contentService.deletePanelContent(id);
		}
		return new ResultUtil<Object>().setData(null);
	}
}

package com.dongnaoedu.mall.manager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dongnaoedu.mall.common.pojo.Result;
import com.dongnaoedu.mall.common.pojo.ZTreeNode;
import com.dongnaoedu.mall.common.utils.ResultUtil;
import com.dongnaoedu.mall.manager.pojo.TbItemCat;
import com.dongnaoedu.mall.manager.service.ItemCatService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author allen
 */
@RestController
@Api(description = "商品分类信息")
public class ItemCatController {

	@Autowired
	private ItemCatService itemCatService;

	@RequestMapping(value = "/item/cat/list", method = RequestMethod.GET)
	@ApiOperation(value = "通过父ID获取商品分类列表")
	public List<ZTreeNode> getItemCatList(@RequestParam(name = "id", defaultValue = "0") int parentId) {

		List<ZTreeNode> list = itemCatService.getItemCatList(parentId);
		return list;
	}

	@RequestMapping(value = "/item/cat/add", method = RequestMethod.POST)
	@ApiOperation(value = "添加商品分类")
	public Result<Object> addItemCategory(@ModelAttribute TbItemCat tbItemCat) {

		itemCatService.addItemCat(tbItemCat);
		return new ResultUtil<Object>().setData(null);
	}

	@RequestMapping(value = "/item/cat/update", method = RequestMethod.POST)
	@ApiOperation(value = "编辑商品分类")
	public Result<Object> updateItemCategory(@ModelAttribute TbItemCat tbItemCat) {

		itemCatService.updateItemCat(tbItemCat);
		return new ResultUtil<Object>().setData(null);
	}

	@RequestMapping(value = "/item/cat/del/{id}", method = RequestMethod.DELETE)
	@ApiOperation(value = "删除商品分类")
	public Result<Object> deleteItemCategory(@PathVariable Long id) {

		itemCatService.deleteItemCat(id);
		return new ResultUtil<Object>().setData(null);
	}
}

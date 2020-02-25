package com.dongnaoedu.mall.manager.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dongnaoedu.mall.common.exception.MallException;
import com.dongnaoedu.mall.common.pojo.ZTreeNode;
import com.dongnaoedu.mall.manager.dto.DtoUtil;
import com.dongnaoedu.mall.manager.mapper.TbItemCatMapper;
import com.dongnaoedu.mall.manager.pojo.TbItemCat;
import com.dongnaoedu.mall.manager.pojo.TbItemCatExample;
import com.dongnaoedu.mall.manager.service.ItemCatService;

/**
 * @author allen
 */
@Service("itemCatService")
public class ItemCatServiceImpl implements ItemCatService {

	@Autowired
	private TbItemCatMapper tbItemCatMapper;

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public TbItemCat getItemCatById(Long id) {

		TbItemCat tbItemCat = tbItemCatMapper.selectByPrimaryKey(id);
		if (tbItemCat == null) {
			throw new MallException("通过id获取商品分类失败");
		}
		return tbItemCat;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<ZTreeNode> getItemCatList(int parentId) {

		TbItemCatExample example = new TbItemCatExample();
		TbItemCatExample.Criteria criteria = example.createCriteria();
		// 排序
		example.setOrderByClause("sort_order");
		// 条件查询
		criteria.andParentIdEqualTo(new Long(parentId));
		List<TbItemCat> list = tbItemCatMapper.selectByExample(example);

		// 转换成ZtreeNode
		List<ZTreeNode> resultList = new ArrayList<>();

		for (TbItemCat tbItemCat : list) {

			ZTreeNode node = DtoUtil.TbItemCat2ZTreeNode(tbItemCat);

			resultList.add(node);
		}

		return resultList;
	}

	@Override
	@Transactional
	public int addItemCat(TbItemCat tbItemCat) {

		if (tbItemCat.getParentId() == 0) {
			// 根节点
			tbItemCat.setSortOrder(0);
			tbItemCat.setStatus(1);
		} else {
			TbItemCat parent = tbItemCatMapper.selectByPrimaryKey(tbItemCat.getParentId());
			tbItemCat.setSortOrder(0);
			tbItemCat.setStatus(1);
			tbItemCat.setCreated(new Date());
			tbItemCat.setUpdated(new Date());
		}

		if (tbItemCatMapper.insert(tbItemCat) != 1) {
			throw new MallException("添加商品分类失败");
		}
		return 1;
	}

	@Override
	@Transactional
	public int updateItemCat(TbItemCat tbItemCat) {

		TbItemCat old = getItemCatById(tbItemCat.getId());
		tbItemCat.setCreated(old.getCreated());
		tbItemCat.setUpdated(new Date());

		if (tbItemCatMapper.updateByPrimaryKey(tbItemCat) != 1) {
			throw new MallException("添加商品分类失败");
		}
		return 1;
	}

	@Override
	@Transactional
	public void deleteItemCat(Long id) {

		deleteZTree(id);
	}

	@Override
	@Transactional
	public void deleteZTree(Long id) {

		// 查询该节点所有子节点
		List<ZTreeNode> node = getItemCatList(Math.toIntExact(id));
		if (node.size() > 0) {
			// 如果有子节点，遍历子节点
			for (int i = 0; i < node.size(); i++) {
				deleteItemCat((long) node.get(i).getId());
			}
		}
		// 没有子节点直接删除
		if (tbItemCatMapper.deleteByPrimaryKey(id) != 1) {
			throw new MallException("删除商品分类失败");
		}
	}

}

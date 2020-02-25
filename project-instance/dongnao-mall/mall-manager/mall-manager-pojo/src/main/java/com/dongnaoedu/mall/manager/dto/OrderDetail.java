package com.dongnaoedu.mall.manager.dto;

import java.io.Serializable;
import java.util.List;

import com.dongnaoedu.mall.manager.pojo.TbOrder;
import com.dongnaoedu.mall.manager.pojo.TbOrderItem;
import com.dongnaoedu.mall.manager.pojo.TbOrderShipping;

/**
 * @author allen
 */
public class OrderDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	private TbOrder tbOrder;

	private List<TbOrderItem> tbOrderItem;

	private TbOrderShipping tbOrderShipping;

	public List<TbOrderItem> getTbOrderItem() {
		return tbOrderItem;
	}

	public void setTbOrderItem(List<TbOrderItem> tbOrderItem) {
		this.tbOrderItem = tbOrderItem;
	}

	public TbOrder getTbOrder() {
		return tbOrder;
	}

	public void setTbOrder(TbOrder tbOrder) {
		this.tbOrder = tbOrder;
	}

	public TbOrderShipping getTbOrderShipping() {
		return tbOrderShipping;
	}

	public void setTbOrderShipping(TbOrderShipping tbOrderShipping) {
		this.tbOrderShipping = tbOrderShipping;
	}
}

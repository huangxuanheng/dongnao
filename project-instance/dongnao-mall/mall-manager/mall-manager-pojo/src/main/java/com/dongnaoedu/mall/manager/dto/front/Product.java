package com.dongnaoedu.mall.manager.dto.front;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author allen
 */
public class Product implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long productId;

	private BigDecimal salePrice;

	private String productName;

	private String subTitle;

	private String productImageBig;

	// 类别
	private Long cid;

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public BigDecimal getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public String getProductImageBig() {
		return productImageBig;
	}

	public void setProductImageBig(String productImageBig) {
		this.productImageBig = productImageBig;
	}

	public Long getCid() {
		return cid;
	}

	public void setCid(Long cid) {
		this.cid = cid;
	}
}

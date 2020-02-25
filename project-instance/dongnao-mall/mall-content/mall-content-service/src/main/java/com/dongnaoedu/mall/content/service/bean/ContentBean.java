package com.dongnaoedu.mall.content.service.bean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;

/**
 * 方式1：java api
 * 方式2：使用@Component + @Value + @Autowired
 * 方式3：@EnableApolloConfig + @Configuration
 * 
 * @author allen
 *
 */
//@Component
public class ContentBean {
	private static final Logger log = LoggerFactory.getLogger(ContentBean.class);

    @Value("${PRODUCT_HOME}")
    private String PRODUCT_HOME;

    @Value("${PRODUCT_ITEM}")
    private String PRODUCT_ITEM;

    @Value("${RECOMEED_PANEL_ID}")
    private Integer RECOMEED_PANEL_ID;

    @Value("${THANK_PANEL_ID}")
    private Integer THANK_PANEL_ID;

    @Value("${RECOMEED_PANEL}")
    private String RECOMEED_PANEL;

    @Value("${THANK_PANEL}")
    private String THANK_PANEL;

    @Value("${ITEM_EXPIRE}")
    private int ITEM_EXPIRE;

    @Value("${HEADER_PANEL_ID}")
    private int HEADER_PANEL_ID;

    @Value("${HEADER_PANEL}")
    private String HEADER_PANEL;

	public String getPRODUCT_HOME() {
		return PRODUCT_HOME;
	}

	public void setPRODUCT_HOME(String PRODUCT_HOME) {
		log.info("updating PRODUCT_HOME, old value: {}, new value: {}", this.PRODUCT_HOME, PRODUCT_HOME);
		this.PRODUCT_HOME = PRODUCT_HOME;
	}

	public String getPRODUCT_ITEM() {
		return PRODUCT_ITEM;
	}

	public void setPRODUCT_ITEM(String PRODUCT_ITEM) {
		this.PRODUCT_ITEM = PRODUCT_ITEM;
	}

	public Integer getRECOMEED_PANEL_ID() {
		return RECOMEED_PANEL_ID;
	}

	public void setRECOMEED_PANEL_ID(Integer RECOMEED_PANEL_ID) {
		this.RECOMEED_PANEL_ID = RECOMEED_PANEL_ID;
	}

	public Integer getTHANK_PANEL_ID() {
		return THANK_PANEL_ID;
	}

	public void setTHANK_PANEL_ID(Integer THANK_PANEL_ID) {
		this.THANK_PANEL_ID = THANK_PANEL_ID;
	}

	public String getRECOMEED_PANEL() {
		return RECOMEED_PANEL;
	}

	public void setRECOMEED_PANEL(String RECOMEED_PANEL) {
		this.RECOMEED_PANEL = RECOMEED_PANEL;
	}

	public String getTHANK_PANEL() {
		return THANK_PANEL;
	}

	public void setTHANK_PANEL(String THANK_PANEL) {
		this.THANK_PANEL = THANK_PANEL;
	}

	public int getITEM_EXPIRE() {
		return ITEM_EXPIRE;
	}

	public void setITEM_EXPIRE(int ITEM_EXPIRE) {
		this.ITEM_EXPIRE = ITEM_EXPIRE;
	}

	public int getHEADER_PANEL_ID() {
		return HEADER_PANEL_ID;
	}

	public void setHEADER_PANEL_ID(int HEADER_PANEL_ID) {
		this.HEADER_PANEL_ID = HEADER_PANEL_ID;
	}

	public String getHEADER_PANEL() {
		return HEADER_PANEL;
	}

	public void setHEADER_PANEL(String HEADER_PANEL) {
		this.HEADER_PANEL = HEADER_PANEL;
	}
	
    @ApolloConfigChangeListener("application")
    private void someChangeHandler(ConfigChangeEvent changeEvent) {
        log.info("[someChangeHandler]Changes for namespace {}", changeEvent.getNamespace());
        if (changeEvent.isChanged("test")) {
            System.out.println("配置中心的test值被改变了......");
        }
//        if (changeEvent.isChanged("batch")) {
//            setBatch(Integer.valueOf(changeEvent.getChange("batch").getNewValue()));
//        }
    }
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ContentBean [PRODUCT_HOME=" + PRODUCT_HOME + ", PRODUCT_ITEM=" + PRODUCT_ITEM + ", RECOMEED_PANEL_ID="
				+ RECOMEED_PANEL_ID + ", THANK_PANEL_ID=" + THANK_PANEL_ID + ", RECOMEED_PANEL=" + RECOMEED_PANEL
				+ ", THANK_PANEL=" + THANK_PANEL + ", ITEM_EXPIRE=" + ITEM_EXPIRE + ", HEADER_PANEL_ID="
				+ HEADER_PANEL_ID + ", HEADER_PANEL=" + HEADER_PANEL + "]";
	}
	
}

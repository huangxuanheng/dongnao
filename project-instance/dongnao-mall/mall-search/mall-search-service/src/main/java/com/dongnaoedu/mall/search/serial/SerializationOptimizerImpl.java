package com.dongnaoedu.mall.search.serial;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.alibaba.dubbo.common.serialize.support.SerializationOptimizer;
import com.alibaba.fastjson.JSONObject;
import com.dongnaoedu.mall.manager.pojo.TbAddress;
import com.dongnaoedu.mall.manager.pojo.TbBase;
import com.dongnaoedu.mall.manager.pojo.TbDict;
import com.dongnaoedu.mall.manager.pojo.TbExpress;
import com.dongnaoedu.mall.manager.pojo.TbItem;
import com.dongnaoedu.mall.manager.pojo.TbItemCat;
import com.dongnaoedu.mall.manager.pojo.TbItemDesc;
import com.dongnaoedu.mall.manager.pojo.TbLog;
import com.dongnaoedu.mall.manager.pojo.TbMember;
import com.dongnaoedu.mall.manager.pojo.TbOrder;
import com.dongnaoedu.mall.manager.pojo.TbOrderItem;
import com.dongnaoedu.mall.manager.pojo.TbOrderShipping;
import com.dongnaoedu.mall.manager.pojo.TbPanel;
import com.dongnaoedu.mall.manager.pojo.TbPanelContent;
import com.dongnaoedu.mall.manager.pojo.TbPermission;
import com.dongnaoedu.mall.manager.pojo.TbRole;
import com.dongnaoedu.mall.manager.pojo.TbRolePerm;
import com.dongnaoedu.mall.manager.pojo.TbShiroFilter;
import com.dongnaoedu.mall.manager.pojo.TbThanks;
import com.dongnaoedu.mall.manager.pojo.TbUser;

public class SerializationOptimizerImpl implements SerializationOptimizer {

    public Collection<Class> getSerializableClasses() {
        List<Class> classes = new LinkedList<Class>();
        
        //这里可以把所有需要进行序列化的类进行添加
        classes.add(JSONObject.class);
        // 业务类
        classes.add(TbAddress.class);
        classes.add(TbBase.class);
        classes.add(TbDict.class);
        classes.add(TbExpress.class);
        classes.add(TbItemCat.class);
        classes.add(TbItemDesc.class);
        classes.add(TbItem.class);
        classes.add(TbLog.class);
        classes.add(TbMember.class);
        classes.add(TbMember.class);
        classes.add(TbOrderItem.class);
        classes.add(TbOrder.class);
        classes.add(TbOrderShipping.class);
        classes.add(TbPanelContent.class);
        classes.add(TbPanel.class);
        classes.add(TbPermission.class);
        classes.add(TbRole.class);
        classes.add(TbRolePerm.class);
        classes.add(TbShiroFilter.class);
        classes.add(TbThanks.class);
        classes.add(TbUser.class);
        
        return classes;
    }
}

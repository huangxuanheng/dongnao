-- 需要引入Redis、模板、json库、日志
local common = require("common")
local read_redis = common.read_redis  
local read_http = common.read_http  
local cjson = require("cjson")  
local cjson_decode = cjson.decode
local ngx_log = ngx.log  
local ngx_ERR = ngx.ERR  
local ngx_exit = ngx.exit
local goodsId = ngx.var.goodsId
    
-- 先通过Lua脚本到Redis中获取
local goodsKey = "goodsId::" .. goodsId 
local goodsInfoStr = read_redis("127.0.0.1", 6379, {goodsKey})

-- Redis没有再到程序中查询
if not goodsInfoStr then  
   ngx_log(ngx_ERR, "redis not found goods info, back to http, goodsId : ", goodsId)  

   goodsInfoStr = read_http({goodsId = goodsId})
end

-- 读取不到商品信息，报错并退出
if not goodsInfoStr then  
   ngx_log(ngx_ERR, "http not found goods info, goodsId : ", goodsId)  
   return ngx_exit(404)  
end  

-- 解码字符串内容为json
local info = cjson_decode(goodsInfoStr);

-- 模板渲染
local template = require "resty.template"
template.caching(true)  
template.render("goods_detail.html", info)
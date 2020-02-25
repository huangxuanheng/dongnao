-- 定义一个外界可见的表
local _M = {}

-- 受保护的自定义数据内容
local data = {
   dog = 5,
   cat = 3,
   pig = 1,
}

-- function给_M定义一个方法，get_age，用来返回data中的数据
function _M.get_age(name)
   return data[name]
end	-- end表示方法结束

-- 向外界暴露表
return _M
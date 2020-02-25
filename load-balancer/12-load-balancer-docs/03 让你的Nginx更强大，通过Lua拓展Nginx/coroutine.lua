co = coroutine.create(function ()
    for i=1,10 do
        print("co", i)
        coroutine.yield()
    end
end) 


coroutine.resume(co)   -- 输出 co 1
coroutine.resume(co)   -- 输出 co 2
coroutine.resume(co)   -- 输出 co 3 
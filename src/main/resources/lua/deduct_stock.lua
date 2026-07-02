-- KEYS[i]: Redis 中 SKU 库存的 key, 例如 "bakery:sku:stock:12"
-- ARGV[i]: 对应的扣减数量
local keys_count = #KEYS
local quantities = {}

-- 1. 解析 ARGV 数量为数字
for i = 1, keys_count do
    quantities[i] = tonumber(ARGV[i])
end

-- 2. 预先校验所有 SKU 库存是否充足
for i = 1, keys_count do
    local stock_key = KEYS[i]
    local deduct_qty = quantities[i]
    local current_stock = redis.call('get', stock_key)
    
    if not current_stock then
        return -2 -- 错误码：Redis 中对应的库存 Key 不存在或未初始化
    end
    
    if tonumber(current_stock) < deduct_qty then
        return -1 -- 错误码：库存不足
    end
end

-- 3. 全体 SKU 充足，执行原子化扣减
for i = 1, keys_count do
    redis.call('decrby', KEYS[i], quantities[i])
end

return 1 -- 扣减成功

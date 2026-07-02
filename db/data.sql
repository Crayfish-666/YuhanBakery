-- ==================== 1. 用户表数据 (密码均为 123456) ====================
INSERT INTO `user` (`id`, `username`, `password`, `phone`, `role`, `balance`, `total_recharge`) VALUES
(1, 'customer', '$2a$10$1G9vXwF3YBCRevOMY19xme8Z6D5vhDt0k04rLEZxfj/yura0/mnOa', '13800138000', 0, 5000.00, 5000.00),
(2, 'admin', '$2a$10$1G9vXwF3YBCRevOMY19xme8Z6D5vhDt0k04rLEZxfj/yura0/mnOa', '13800138001', 1, 0.00, 0.00),
(3, 'rider', '$2a$10$1G9vXwF3YBCRevOMY19xme8Z6D5vhDt0k04rLEZxfj/yura0/mnOa', '13800138002', 2, 120.00, 0.00),
(4, 'customer2', '$2a$10$1G9vXwF3YBCRevOMY19xme8Z6D5vhDt0k04rLEZxfj/yura0/mnOa', '13800138003', 0, 800.00, 1000.00),
(5, 'customer3', '$2a$10$1G9vXwF3YBCRevOMY19xme8Z6D5vhDt0k04rLEZxfj/yura0/mnOa', '13800138004', 0, 1200.00, 2000.00);

-- ==================== 2. 商品分类数据 (8个) ====================
INSERT INTO `category` (`id`, `name`, `sort`) VALUES
(1, '精致生日蛋糕', 10),
(2, '法式西点慕斯', 20),
(3, '手工烘焙面包', 30),
(4, '日式吐司三明治', 40),
(5, '现磨咖啡系列', 50),
(6, '现制清凉冰饮', 60),
(7, '精美点心曲奇', 70),
(8, '超值烘焙套餐', 80);

-- ==================== 3. 商品主表数据 (50个单品) ====================
INSERT INTO `product` (`id`, `category_id`, `name`, `description`, `image`, `status`) VALUES
-- 1. 精致生日蛋糕 (10款)
(1, 1, '黑森林巧克力蛋糕', '传统德国风味，樱桃酒香配合浓郁黑巧克力碎。', 'https://cyh666.oss-cn-hangzhou.aliyuncs.com/YuhanBakery/f8482b93-6afe-4a9e-97fc-59961632041e.png', 1),
(2, 1, '鲜草莓奶油千层', '二十余层手工千层皮夹满红艳香甜草莓与乳脂奶油。', 'https://cyh666.oss-cn-hangzhou.aliyuncs.com/YuhanBakery/3bc757b9-8462-4b25-b051-92ffd33a1e6e.png', 1),
(3, 1, '榛果海盐焦糖蛋糕', '甜咸交织，香脆榛果碎增添多重口感。', 'https://cyh666.oss-cn-hangzhou.aliyuncs.com/YuhanBakery/f8482b93-6afe-4a9e-97fc-59961632041e.png', 1),
(4, 1, '蓝莓生芝士生日蛋糕', '香浓的进口芝士加上新鲜采摘的有机蓝莓。', 'https://cyh666.oss-cn-hangzhou.aliyuncs.com/YuhanBakery/3bc757b9-8462-4b25-b051-92ffd33a1e6e.png', 1),
(5, 1, '经典红丝绒蛋糕', '如天鹅绒般丝滑，乳酪霜与可可香气的完美重奏。', 'https://cyh666.oss-cn-hangzhou.aliyuncs.com/YuhanBakery/f8482b93-6afe-4a9e-97fc-59961632041e.png', 1),
(6, 1, '芒果椰慕斯蛋糕', '芒果的清甜遇到椰奶的醇厚，夏日风情十足。', 'https://cyh666.oss-cn-hangzhou.aliyuncs.com/YuhanBakery/3bc757b9-8462-4b25-b051-92ffd33a1e6e.png', 1),
(7, 1, '宇治抹茶红豆生日蛋糕', '日式抹茶的微苦配上软糯蜜红豆，清雅怡人。', 'https://cyh666.oss-cn-hangzhou.aliyuncs.com/YuhanBakery/3bc757b9-8462-4b25-b051-92ffd33a1e6e.png', 1),
(8, 1, '提拉米苏狂欢蛋糕', '意式Espresso浸润的手指饼干与马斯卡彭慕斯。', 'https://cyh666.oss-cn-hangzhou.aliyuncs.com/YuhanBakery/f8482b93-6afe-4a9e-97fc-59961632041e.png', 1),
(9, 1, '焦糖巴斯克重芝士', '表面焦黑香脆，内里半熟流心，芝士控首选。', 'https://cyh666.oss-cn-hangzhou.aliyuncs.com/YuhanBakery/f8482b93-6afe-4a9e-97fc-59961632041e.png', 1),
(10, 1, '彩虹水果派对大蛋糕', '缤纷各色新鲜水果铺满，果香四溢色彩斑斓。', 'https://cyh666.oss-cn-hangzhou.aliyuncs.com/YuhanBakery/3bc757b9-8462-4b25-b051-92ffd33a1e6e.png', 1),

-- 2. 法式西点慕斯 (8款)
(11, 2, '抹茶流心慕斯', '细腻绿茶慕斯，一刀切下，微苦流心缓缓绽放。', 'https://cyh666.oss-cn-hangzhou.aliyuncs.com/YuhanBakery/7da1cb9e-739f-4285-93e5-ec620d53c2c2.png', 1),
(12, 2, '经典法式歌剧院', '咖啡糖水层层浸透，巧克力与奶油夹层的极致美味。', 'https://cyh666.oss-cn-hangzhou.aliyuncs.com/YuhanBakery/1e019d74-e142-45e4-ac6e-3f5d8787f742.png', 1),
(13, 2, '玫瑰覆盆子慕斯', '浪漫芬芳的玫瑰花香融合覆盆子的微酸清爽。', 'https://cyh666.oss-cn-hangzhou.aliyuncs.com/YuhanBakery/7da1cb9e-739f-4285-93e5-ec620d53c2c2.png', 1),
(14, 2, '柠檬罗勒小西点', '极富现代感的法式搭配，酸甜罗勒香带来清新口感。', 'https://cyh666.oss-cn-hangzhou.aliyuncs.com/YuhanBakery/1e019d74-e142-45e4-ac6e-3f5d8787f742.png', 1),
(15, 2, '香草百香果小布丁', '醇香马达加斯加香草籽混合百香果冻，滑嫩甜美。', 'https://cyh666.oss-cn-hangzhou.aliyuncs.com/YuhanBakery/1e019d74-e142-45e4-ac6e-3f5d8787f742.png', 1),
(16, 2, '皇家黑森林切片', '巧克力的香浓与酒渍樱桃的香醇完美糅合。', 'https://cyh666.oss-cn-hangzhou.aliyuncs.com/YuhanBakery/7da1cb9e-739f-4285-93e5-ec620d53c2c2.png', 1),
(17, 2, '栗子蒙布朗雪山', '浓郁法式栗子泥精绘雪山造型，口感细腻沙甜。', 'https://cyh666.oss-cn-hangzhou.aliyuncs.com/YuhanBakery/1e019d74-e142-45e4-ac6e-3f5d8787f742.png', 1),
(18, 2, '海盐焦糖闪电泡芙', '法式酥脆泡芙外皮包裹咸焦糖奶油内馅。', 'https://cyh666.oss-cn-hangzhou.aliyuncs.com/YuhanBakery/7da1cb9e-739f-4285-93e5-ec620d53c2c2.png', 1),

-- 3. 手工烘焙面包 (8款)
(19, 3, '金牌黄油牛角可颂', '层层折叠酥皮，每一口都是浓郁法国黄油乳香。', 'https://cyh666.oss-cn-hangzhou.aliyuncs.com/YuhanBakery/60560a20-abcc-49a9-adc7-64f35cebb536.png', 1),
(20, 3, '法式蒜香培根法棍', '外皮酥脆，麦香四溢，包裹秘制大蒜奶油酱。', 'https://cyh666.oss-cn-hangzhou.aliyuncs.com/YuhanBakery/60560a20-abcc-49a9-adc7-64f35cebb536.png', 1),
(21, 3, '蔓越莓红酒软欧包', '红酒揉合面团，加入大量蔓越莓干与香脆核桃。', 'https://cyh666.oss-cn-hangzhou.aliyuncs.com/YuhanBakery/60560a20-abcc-49a9-adc7-64f35cebb536.png', 1),
(22, 3, '日式盐面包 roll', '外皮焦脆，底部微咸焦黄，口感软糯拉丝。', 'https://cyh666.oss-cn-hangzhou.aliyuncs.com/YuhanBakery/60560a20-abcc-49a9-adc7-64f35cebb536.png', 1),
(23, 3, '经典肉桂卷', '红糖肉桂与面团慢发，烤出微焦，淋上芝士糖霜。', 'https://cyh666.oss-cn-hangzhou.aliyuncs.com/YuhanBakery/60560a20-abcc-49a9-adc7-64f35cebb536.png', 1),
(24, 3, '爆浆巧克力酥皮欧包', '黑巧流心内馅搭配外层可可酥皮，巧意浓浓。', 'https://cyh666.oss-cn-hangzhou.aliyuncs.com/YuhanBakery/60560a20-abcc-49a9-adc7-64f35cebb536.png', 1),
(25, 3, '香酥奶酥菠萝包', '传统港式菠萝包，金黄方格脆皮入口即化。', 'https://cyh666.oss-cn-hangzhou.aliyuncs.com/YuhanBakery/60560a20-abcc-49a9-adc7-64f35cebb536.png', 1),
(26, 3, '德式碱水扭结结', '碱水面包独特麦焦香，质地紧实，搭配岩盐粒。', 'https://cyh666.oss-cn-hangzhou.aliyuncs.com/YuhanBakery/60560a20-abcc-49a9-adc7-64f35cebb536.png', 1),

-- 4. 日式吐司三明治 (6款)
(27, 4, '北海道生牛乳吐司', '纯生牛乳和鲜奶油慢发酵，口感软糯香甜，拉丝优秀。', 'https://cyh666.oss-cn-hangzhou.aliyuncs.com/YuhanBakery/8e6badf8-0e65-461b-ace3-06f8d034d2bf.png', 1),
(28, 4, '黑芝麻核桃养生吐司', '丰富黑芝麻糊揉入，低糖少油，散发坚果原香。', 'https://cyh666.oss-cn-hangzhou.aliyuncs.com/YuhanBakery/8e6badf8-0e65-461b-ace3-06f8d034d2bf.png', 1),
(29, 4, '全麦多谷物减脂吐司', '精选燕麦、葵花籽等多谷物，饱腹感强，健康低卡。', 'https://cyh666.oss-cn-hangzhou.aliyuncs.com/YuhanBakery/8e6badf8-0e65-461b-ace3-06f8d034d2bf.png', 1),
(30, 4, '照烧鸡肉三明治', '经典日式照照鸡腿肉夹新鲜生菜番茄，能量饱满。', 'https://cyh666.oss-cn-hangzhou.aliyuncs.com/YuhanBakery/8e6badf8-0e65-461b-ace3-06f8d034d2bf.png', 1),
(31, 4, '金枪鱼沙拉蛋三明治', '金枪鱼酱、滑嫩滑蛋碎与日式沙拉酱黄金配比。', 'https://cyh666.oss-cn-hangzhou.aliyuncs.com/YuhanBakery/8e6badf8-0e65-461b-ace3-06f8d034d2bf.png', 1),
(32, 4, '黑椒牛肉片吐司包', '精选牛里脊肉薄片佐黑胡椒，夹入软糯吐司片。', 'https://cyh666.oss-cn-hangzhou.aliyuncs.com/YuhanBakery/8e6badf8-0e65-461b-ace3-06f8d034d2bf.png', 1),

-- 5. 现磨咖啡系列 (6款)
(33, 5, '生椰拿铁', '精选生椰乳与阿拉比卡浓缩咖啡完美融合，香甜冰爽。', 'https://cyh666.oss-cn-hangzhou.aliyuncs.com/YuhanBakery/93794bfb-a50e-42cf-8362-5c7ed68edb0a.png', 1),
(34, 5, '经典意式美式咖啡', '微酸花香与坚果风味，唤醒清晨的提神利器。', 'https://cyh666.oss-cn-hangzhou.aliyuncs.com/YuhanBakery/93794bfb-a50e-42cf-8362-5c7ed68edb0a.png', 1),
(35, 5, '焦糖玛奇朵', '香浓浓缩咖啡融入丝滑牛奶，覆盖焦糖网格。', 'https://cyh666.oss-cn-hangzhou.aliyuncs.com/YuhanBakery/93794bfb-a50e-42cf-8362-5c7ed68edb0a.png', 1),
(36, 5, '燕麦拿铁', 'Oatly 燕麦奶搭配精品咖啡豆，素食燕麦奶香。', 'https://cyh666.oss-cn-hangzhou.aliyuncs.com/YuhanBakery/93794bfb-a50e-42cf-8362-5c7ed68edb0a.png', 1),
(37, 5, '榛果风味拿铁', '醇厚拿铁融入香甜榛果糖浆，坚果回甘持久。', 'https://cyh666.oss-cn-hangzhou.aliyuncs.com/YuhanBakery/93794bfb-a50e-42cf-8362-5c7ed68edb0a.png', 1),
(38, 5, '脏脏冰咖啡 (Dirty)', '冰牛奶底托起温热浓缩咖啡，冷热交融分层奇妙。', 'https://cyh666.oss-cn-hangzhou.aliyuncs.com/YuhanBakery/93794bfb-a50e-42cf-8362-5c7ed68edb0a.png', 1),

-- 6. 现制清凉冰饮 (6款)
(39, 6, '爆柠鸭屎香柠檬茶', '精选单丛鸭屎香茶底，现场手打暴打多汁香柠檬。', 'https://cyh666.oss-cn-hangzhou.aliyuncs.com/YuhanBakery/338147f6-ae4d-4e78-8a85-1aa9c7f53b1a.png', 1),
(40, 6, '多肉芝芝葡萄', '饱满无籽葡萄果肉、茉莉绿茶底与雪白芝士奶盖。', 'https://cyh666.oss-cn-hangzhou.aliyuncs.com/YuhanBakery/338147f6-ae4d-4e78-8a85-1aa9c7f53b1a.png', 1),
(41, 6, '杨枝甘露轻盈版', '红西柚果肉、软糯西米露配上清爽椰浆芒果泥。', 'https://cyh666.oss-cn-hangzhou.aliyuncs.com/YuhanBakery/338147f6-ae4d-4e78-8a85-1aa9c7f53b1a.png', 1),
(42, 6, '满杯鲜橙乌龙茶', '清新四季春乌龙茶汤，满载橙子切片与纯橙原汁。', 'https://cyh666.oss-cn-hangzhou.aliyuncs.com/YuhanBakery/338147f6-ae4d-4e78-8a85-1aa9c7f53b1a.png', 1),
(43, 6, '冰冰茉莉奶绿', '清新茉莉花茶融汇轻盈牛乳，入口冰凉花香回甘。', 'https://cyh666.oss-cn-hangzhou.aliyuncs.com/YuhanBakery/338147f6-ae4d-4e78-8a85-1aa9c7f53b1a.png', 1),
(44, 6, '冰镇酸梅汤', '古法乌梅山楂熬制，消暑去油腻，透心冰凉。', 'https://cyh666.oss-cn-hangzhou.aliyuncs.com/YuhanBakery/338147f6-ae4d-4e78-8a85-1aa9c7f53b1a.png', 1),

-- 7. 精美点心曲奇 (6款)
(45, 7, '法式缤纷马卡龙', '精美圆壳，裙边完美，夹心香甜醇厚，送礼首选。', 'https://cyh666.oss-cn-hangzhou.aliyuncs.com/YuhanBakery/ffb1373e-83a9-4cef-8307-a933889ce4ba.png', 1),
(46, 7, '手工黄油曲奇饼干', '浓郁黄油，菊花挤花，口感酥松，入口即化。', 'https://cyh666.oss-cn-hangzhou.aliyuncs.com/YuhanBakery/ffb1373e-83a9-4cef-8307-a933889ce4ba.png', 1),
(47, 7, '抹茶蔓越莓牛轧糖', '奶香浓郁，韧性十足，抹茶茶香夹蔓越莓酸甜。', 'https://cyh666.oss-cn-hangzhou.aliyuncs.com/YuhanBakery/ffb1373e-83a9-4cef-8307-a933889ce4ba.png', 1),
(48, 7, '法式咸蛋黄酥饼', '咸甜起沙，中西交融，酥脆外壳包裹流心蛋黄。', 'https://cyh666.oss-cn-hangzhou.aliyuncs.com/YuhanBakery/ffb1373e-83a9-4cef-8307-a933889ce4ba.png', 1),
(49, 7, '可可松露曲奇', '微苦可可豆融入酥松松露质地曲奇，可可控狂喜。', 'https://cyh666.oss-cn-hangzhou.aliyuncs.com/YuhanBakery/ffb1373e-83a9-4cef-8307-a933889ce4ba.png', 1),
(50, 7, '香脆奶油蝴蝶酥', '经典多层蝴蝶造型酥皮，洒满砂糖颗粒，香甜酥脆。', 'https://cyh666.oss-cn-hangzhou.aliyuncs.com/YuhanBakery/ffb1373e-83a9-4cef-8307-a933889ce4ba.png', 1);

-- ==================== 4. SKU 规格数据 (82个) ====================
INSERT INTO `product_sku` (`id`, `product_id`, `spec_name`, `price`, `stock`, `status`) VALUES
-- 蛋糕规格 (1-10)
(1, 1, '6寸 (2-3人)', 158.00, 50, 1),
(2, 1, '8寸 (4-6人)', 208.00, 30, 1),
(3, 1, '10寸 (8-10人)', 298.00, 10, 1),
(4, 2, '6寸 (2-3人)', 168.00, 40, 1),
(5, 2, '8寸 (4-6人)', 218.00, 25, 1),
(6, 3, '6寸 (2-3人)', 178.00, 30, 1),
(7, 3, '8寸 (4-6人)', 228.00, 15, 1),
(8, 4, '6寸 (2-3人)', 148.00, 35, 1),
(9, 4, '8寸 (4-6人)', 198.00, 20, 1),
(10, 5, '6寸 (2-3人)', 158.00, 40, 1),
(11, 5, '8寸 (4-6人)', 208.00, 20, 1),
(12, 6, '6寸 (2-3人)', 168.00, 30, 1),
(13, 6, '8寸 (4-6人)', 218.00, 15, 1),
(14, 7, '6寸 (2-3人)', 178.00, 35, 1),
(15, 7, '8寸 (4-6人)', 228.00, 10, 1),
(16, 8, '6寸 (2-3人)', 148.00, 45, 1),
(17, 8, '8寸 (4-6人)', 198.00, 25, 1),
(18, 9, '6寸 (2-3人)', 158.00, 30, 1),
(19, 9, '8寸 (4-6人)', 208.00, 15, 1),
(20, 10, '6寸 (2-3人)', 168.00, 25, 1),
(21, 10, '8寸 (4-6人)', 218.00, 12, 1),

-- 慕斯规格 (11-18)
(22, 11, '单人份 (切片)', 35.00, 100, 1),
(23, 11, '双人份 (礼盒)', 62.00, 50, 1),
(24, 12, '单人份 (切片)', 38.00, 80, 1),
(25, 12, '双人份 (礼盒)', 68.00, 40, 1),
(26, 13, '单人份 (切片)', 36.00, 90, 1),
(27, 13, '双人份 (礼盒)', 64.00, 45, 1),
(28, 14, '单人份 (切片)', 35.00, 75, 1),
(29, 15, '单人份 (杯装)', 38.00, 85, 1),
(30, 16, '单人份 (切片)', 36.00, 95, 1),
(31, 17, '单人份 (经典)', 35.00, 60, 1),
(32, 18, '单件装', 18.00, 120, 1),
(79, 14, '双人份 (礼盒)', 60.00, 30, 1),
(80, 15, '双人份 (礼盒)', 65.00, 25, 1),
(81, 16, '双人份 (礼盒)', 63.00, 25, 1),
(82, 17, '双人份 (礼盒)', 60.00, 20, 1),

-- 面包规格 (19-26)
(33, 19, '单件装 (原味)', 18.00, 150, 1),
(34, 19, '3件特惠装', 48.00, 60, 1),
(35, 20, '单件装 (蒜香)', 16.00, 100, 1),
(36, 20, '3件分享装', 42.00, 40, 1),
(37, 21, '单件装 (红酒)', 15.00, 110, 1),
(38, 21, '3件实惠装', 40.00, 50, 1),
(39, 22, '单件装 (盐味)', 12.00, 200, 1),
(40, 23, '单件装', 20.00, 90, 1),
(41, 24, '单件装 (爆浆)', 18.00, 100, 1),
(42, 25, '单件装', 12.00, 180, 1),
(43, 26, '单件装 (岩盐)', 15.00, 130, 1),

-- 吐司规格 (27-32)
(44, 27, '整只装 (约450g)', 28.00, 80, 1),
(45, 27, '半只装 (切片)', 15.00, 100, 1),
(46, 28, '整只装 (约450g)', 26.00, 60, 1),
(47, 28, '半只装 (切片)', 14.00, 80, 1),
(48, 29, '整只装 (低卡)', 25.00, 70, 1),
(49, 30, '标准装', 28.00, 90, 1),
(50, 31, '标准装', 26.00, 85, 1),
(51, 32, '标准装', 25.00, 95, 1),

-- 咖啡规格 (33-38)
(52, 33, '中杯 (冷)', 19.00, 300, 1),
(53, 33, '大杯 (冷)', 23.00, 250, 1),
(54, 34, '中杯 (热)', 15.00, 400, 1),
(55, 34, '大杯 (冰)', 19.00, 350, 1),
(56, 35, '中杯 (热)', 20.00, 200, 1),
(57, 35, '大杯 (冰)', 24.00, 180, 1),
(58, 36, '中杯 (温)', 18.00, 150, 1),
(59, 37, '中杯 (温)', 22.00, 120, 1),
(60, 38, '冷杯', 20.00, 100, 1),

-- 冰饮规格 (39-44)
(61, 39, '标准杯 (少冰)', 18.00, 350, 1),
(62, 39, '特大杯 (正常冰)', 22.00, 280, 1),
(63, 40, '标准杯 (无糖)', 22.00, 240, 1),
(64, 40, '大杯 (微糖)', 26.00, 200, 1),
(65, 41, '标准杯 (冰)', 19.00, 180, 1),
(66, 42, '标准杯 (冰)', 18.00, 220, 1),
(67, 43, '标准杯 (冰)', 16.00, 250, 1),
(68, 44, '标准杯 (冰镇)', 15.00, 300, 1),

-- 曲奇规格 (45-50)
(70, 45, '精装礼盒 (12枚)', 98.00, 100, 1),
(71, 45, '家庭简装 (6枚)', 45.00, 150, 1),
(72, 46, '精装礼盒', 88.00, 120, 1),
(73, 46, '家庭分享装', 38.00, 200, 1),
(74, 47, '精装礼盒', 78.00, 140, 1),
(75, 47, '家庭分享装', 35.00, 180, 1),
(76, 48, '精装礼盒 (8枚)', 98.00, 90, 1),
(77, 49, '精装礼盒 (16枚)', 88.00, 110, 1),
(78, 50, '精装礼盒 (20枚)', 78.00, 130, 1);

-- ==================== 5. 套餐数据 (20个套餐) ====================
INSERT INTO `combo` (`id`, `name`, `description`, `price`, `image`, `status`) VALUES
(1, '奢华法式双人下午茶', '包含经典切片蛋糕与可颂，搭配两杯精选饮品。', 88.00, 'https://cyh666.oss-cn-hangzhou.aliyuncs.com/YuhanBakery/514e38af-1db0-435c-9b71-7d691ec9fbdb.png', 1),
(2, '甜蜜派对生日精选组合', '适合多人分享的缤纷小蛋糕与甜点拼盘。', 168.00, 'https://cyh666.oss-cn-hangzhou.aliyuncs.com/YuhanBakery/514e38af-1db0-435c-9b71-7d691ec9fbdb.png', 1),
(3, '全麦健康早餐双人餐', '北海道生牛乳吐司与燕麦拿铁的元气组合。', 45.00, 'https://cyh666.oss-cn-hangzhou.aliyuncs.com/YuhanBakery/514e38af-1db0-435c-9b71-7d691ec9fbdb.png', 1),
(4, '经典意式提神咖啡双杯', '两杯大杯意式咖啡（可冷/热），上班族必备。', 36.00, 'https://cyh666.oss-cn-hangzhou.aliyuncs.com/YuhanBakery/514e38af-1db0-435c-9b71-7d691ec9fbdb.png', 1),
(5, '鲜莓诱惑双人西点慕斯', '玫瑰覆盆子慕斯两件装，微酸可口，双倍甜蜜。', 78.00, 'https://cyh666.oss-cn-hangzhou.aliyuncs.com/YuhanBakery/514e38af-1db0-435c-9b71-7d691ec9fbdb.png', 1),
(6, '欧风面包分享家族装', '牛角可颂与红酒软欧包的实惠组合，适合家庭分享。', 58.00, 'https://cyh666.oss-cn-hangzhou.aliyuncs.com/YuhanBakery/514e38af-1db0-435c-9b71-7d691ec9fbdb.png', 1),
(7, '抹茶恋人慕斯咖啡单人餐', '一件抹茶流心慕斯加一杯热拿铁，悠闲午后绝配。', 48.00, 'https://cyh666.oss-cn-hangzhou.aliyuncs.com/YuhanBakery/514e38af-1db0-435c-9b71-7d691ec9fbdb.png', 1),
(8, '生椰拿铁配羊角单人元气餐', '一杯生椰拿铁，一只酥脆黄油可颂，开启元气满分。', 32.00, 'https://cyh666.oss-cn-hangzhou.aliyuncs.com/YuhanBakery/514e38af-1db0-435c-9b71-7d691ec9fbdb.png', 1),
(9, '榛果巧克力狂欢分享套组', '黑森林巧克力蛋糕（6寸）与巧克力流心欧包两件。', 198.00, 'https://cyh666.oss-cn-hangzhou.aliyuncs.com/YuhanBakery/514e38af-1db0-435c-9b71-7d691ec9fbdb.png', 1),
(10, '曲奇点心红茶轻奢礼包', '手工黄油曲奇礼盒与两杯茉莉奶绿，礼轻情意重。', 118.00, 'https://cyh666.oss-cn-hangzhou.aliyuncs.com/YuhanBakery/514e38af-1db0-435c-9b71-7d691ec9fbdb.png', 1),
(11, '马卡龙缤纷闺蜜下午茶', '一盒缤纷马卡龙礼盒加两杯爆柠鸭屎香茶。', 98.00, 'https://cyh666.oss-cn-hangzhou.aliyuncs.com/YuhanBakery/514e38af-1db0-435c-9b71-7d691ec9fbdb.png', 1),
(12, '芝士控盛宴家庭分享组', '巴斯克芝士蛋糕（6寸）加柠檬罗勒切片两份。', 128.00, 'https://cyh666.oss-cn-hangzhou.aliyuncs.com/YuhanBakery/514e38af-1db0-435c-9b71-7d691ec9fbdb.png', 1),
(13, '冰爽夏日果茶双杯套餐', '多肉芝芝葡萄与杨枝甘露各一杯，消暑利器。', 38.00, 'https://cyh666.oss-cn-hangzhou.aliyuncs.com/YuhanBakery/514e38af-1db0-435c-9b71-7d691ec9fbdb.png', 1),
(14, '日式吐司健康三明治双人组', '一份金枪鱼沙拉三明治加一份照烧鸡肉三明治。', 52.00, 'https://cyh666.oss-cn-hangzhou.aliyuncs.com/YuhanBakery/514e38af-1db0-435c-9b71-7d691ec9fbdb.png', 1),
(15, '黑森林大蛋糕派对庆典套餐', '8寸黑森林巧克力蛋糕配4杯冰美式，派对聚会必选。', 228.00, 'https://cyh666.oss-cn-hangzhou.aliyuncs.com/YuhanBakery/514e38af-1db0-435c-9b71-7d691ec9fbdb.png', 1),
(16, '抹茶流心慕斯双拼礼遇装', '两盒抹茶流心慕斯双人礼盒装，尊贵随享。', 68.00, 'https://cyh666.oss-cn-hangzhou.aliyuncs.com/YuhanBakery/514e38af-1db0-435c-9b71-7d691ec9fbdb.png', 1),
(17, '鲜草莓奶油千层配美式咖啡单人餐', '切片鲜草莓奶油千层与一杯大杯美式咖啡。', 52.00, 'https://cyh666.oss-cn-hangzhou.aliyuncs.com/YuhanBakery/514e38af-1db0-435c-9b71-7d691ec9fbdb.png', 1),
(18, '冰爽柑橘果茶配可颂小憩餐', '一杯鲜橙乌龙茶，配一只盐面包卷。', 28.00, 'https://cyh666.oss-cn-hangzhou.aliyuncs.com/YuhanBakery/514e38af-1db0-435c-9b71-7d691ec9fbdb.png', 1),
(19, '拿铁咖啡配巧克力吐司早餐', '一杯大杯拿铁咖啡，加一份半只北海道吐司。', 35.00, 'https://cyh666.oss-cn-hangzhou.aliyuncs.com/YuhanBakery/514e38af-1db0-435c-9b71-7d691ec9fbdb.png', 1),
(20, '元气满满午后能量加油站', '一杯燕麦拿铁，配一块巧克力爆浆软欧包。', 49.00, 'https://cyh666.oss-cn-hangzhou.aliyuncs.com/YuhanBakery/514e38af-1db0-435c-9b71-7d691ec9fbdb.png', 1);

-- ==================== 6. 套餐分组数据 ====================
INSERT INTO `combo_group` (`id`, `combo_id`, `group_name`, `choose_count`) VALUES
-- 下午茶 (1)
(1, 1, '法式甜点双拼 (任选二)', 2),
(2, 1, '精选现磨饮品 (任选二)', 2),
-- 派对 (2)
(3, 2, '缤纷主蛋糕 (固定)', 1),
(4, 2, '搭配西点 (任选三)', 3),
-- 早餐 (3)
(5, 3, '健康吐司 (任选一)', 1),
(6, 3, '早餐咖啡 (任选二)', 2);

-- ==================== 7. 套餐分组可选 SKU 关联表数据 ====================
INSERT INTO `combo_group_item` (`id`, `group_id`, `sku_id`, `extra_price`) VALUES
(1, 1, 22, 0.00), -- 抹茶切片
(2, 1, 24, 0.00), -- 歌剧院切片
(3, 1, 33, 0.00), -- 黄油可颂
(4, 2, 52, 0.00), -- 生椰拿铁
(5, 2, 55, 0.00), -- 大杯美式
(6, 2, 61, 0.00), -- 柠檬茶
(7, 3, 2, 0.00),  -- 8寸黑森林
(8, 4, 22, 0.00), -- 抹茶切片
(9, 4, 24, 0.00), -- 歌剧院切片
(10, 4, 32, 0.00), -- 闪电泡芙
(11, 5, 45, 0.00), -- 北海道吐司半只
(12, 6, 58, 0.00); -- 燕麦拿铁

-- ==================== 8. 用户地址簿数据 ====================
INSERT INTO `user_address` (`id`, `user_id`, `contact_name`, `contact_phone`, `address_detail`, `is_default`) VALUES
(1, 1, '张三', '13800138000', '杭州市西湖区西湖大道100号', 1),
(2, 1, '张三丰', '13800138000', '杭州市拱墅区运河街道200号', 0),
(3, 4, '李四', '13811112222', '杭州市滨江区江陵路88号', 1),
(4, 5, '王五', '13922223333', '杭州市钱塘区学源街15号', 1);

-- ==================== 9. 订单主表数据 (15个) ====================
INSERT INTO `orders` (`id`, `order_no`, `user_id`, `total_amount`, `status`, `delivery_type`, `rider_id`, `address_snapshot`, `contact_phone`, `appointment_time`, `pay_time`, `pay_method`, `version`) VALUES
-- 1. 已完成订单 (4个)
(1, 'ORD202606250001', 1, 158.00, 3, 1, 3, '{"contactName": "张三", "contactPhone": "13800138000", "addressDetail": "杭州市西湖区西湖大道100号"}', '13800138000', '2026-06-25 12:00:00', '2026-06-25 08:30:00', 1, 2),
(2, 'ORD202606250002', 1, 35.00, 3, 2, NULL, '{"contactName": "张三", "contactPhone": "13800138000", "addressDetail": "自提自取"}', '13800138000', NULL, '2026-06-25 08:35:00', 1, 1),
(3, 'ORD202606250003', 4, 78.00, 3, 1, 3, '{"contactName": "李四", "contactPhone": "13811112222", "addressDetail": "杭州市滨江区江陵路88号"}', '13811112222', NULL, '2026-06-25 09:10:00', 2, 2),
(4, 'ORD202606250004', 5, 208.00, 3, 1, 3, '{"contactName": "王五", "contactPhone": "13922223333", "addressDetail": "杭州市钱塘区学源街15号"}', '13922223333', NULL, '2026-06-25 09:15:00', 1, 2),

-- 2. 配送中订单 (3个)
(5, 'ORD202606250005', 4, 38.00, 2, 1, 3, '{"contactName": "李四", "contactPhone": "13811112222", "addressDetail": "杭州市滨江区江陵路88号"}', '13811112222', NULL, '2026-06-25 10:10:00', 2, 1),
(6, 'ORD202606250006', 1, 188.00, 2, 1, 3, '{"contactName": "张三", "contactPhone": "13800138000", "addressDetail": "杭州市西湖区西湖大道100号"}', '13800138000', NULL, '2026-06-25 10:15:00', 1, 1),
(7, 'ORD202606250007', 5, 52.00, 2, 1, 3, '{"contactName": "王五", "contactPhone": "13922223333", "addressDetail": "杭州市钱塘区学源街15号"}', '13922223333', NULL, '2026-06-25 10:20:00', 2, 1),

-- 3. 待接单订单 (3个)
(8, 'ORD202606250008', 1, 45.00, 1, 1, NULL, '{"contactName": "张三", "contactPhone": "13800138000", "addressDetail": "杭州市西湖区西湖大道100号"}', '13800138000', NULL, '2026-06-25 14:00:00', 1, 1),
(9, 'ORD202606250009', 4, 28.00, 1, 1, NULL, '{"contactName": "李四", "contactPhone": "13811112222", "addressDetail": "杭州市滨江区江陵路88号"}', '13811112222', NULL, '2026-06-25 14:05:00', 2, 1),
(10, 'ORD202606250010', 5, 88.00, 1, 1, NULL, '{"contactName": "王五", "contactPhone": "13922223333", "addressDetail": "杭州市钱塘区学源街15号"}', '13922223333', NULL, '2026-06-25 14:10:00', 1, 1),

-- 4. 未支付订单 (3个)
(11, 'ORD202606250011', 1, 35.00, 0, 1, NULL, '{"contactName": "张三", "contactPhone": "13800138000", "addressDetail": "杭州市西湖区西湖大道100号"}', '13800138000', NULL, NULL, NULL, 1),
(12, 'ORD202606250012', 4, 25.00, 0, 1, NULL, '{"contactName": "李四", "contactPhone": "13811112222", "addressDetail": "杭州市滨江区江陵路88号"}', '13811112222', NULL, NULL, NULL, 1),
(13, 'ORD202606250013', 5, 178.00, 0, 1, NULL, '{"contactName": "王五", "contactPhone": "13922223333", "addressDetail": "杭州市钱塘区学源街15号"}', '13922223333', NULL, NULL, NULL, 1),

-- 5. 已取消订单 (2个)
(14, 'ORD202606250014', 1, 98.00, 4, 1, NULL, '{"contactName": "张三", "contactPhone": "13800138000", "addressDetail": "杭州市西湖区西湖大道100号"}', '13800138000', NULL, NULL, NULL, 1),
(15, 'ORD202606250015', 4, 18.00, 4, 1, NULL, '{"contactName": "李四", "contactPhone": "13811112222", "addressDetail": "杭州市滨江区江陵路88号"}', '13811112222', NULL, NULL, NULL, 1);

-- ==================== 10. 订单明细快照表数据 ====================
INSERT INTO `order_item` (`id`, `order_id`, `product_id`, `sku_id`, `combo_id`, `price`, `quantity`) VALUES
(1, 1, 1, 1, NULL, 158.00, 1),
(2, 2, 11, 22, NULL, 35.00, 1),
(3, 3, 27, 44, NULL, 28.00, 1),
(4, 3, 30, 49, NULL, 28.00, 1),
(5, 4, 5, 11, NULL, 208.00, 1),
(6, 5, 12, 24, NULL, 38.00, 1),
(7, 6, 3, 7, NULL, 228.00, 1),
(8, 7, 27, 44, NULL, 28.00, 1),
(9, 7, 35, 56, NULL, 20.00, 1),
(10, 8, 27, 45, NULL, 15.00, 3),
(11, 9, 27, 44, NULL, 28.00, 1),
(12, 10, 46, 72, NULL, 88.00, 1),
(13, 11, 11, 22, NULL, 35.00, 1),
(14, 12, 29, 48, NULL, 25.00, 1),
(15, 13, 3, 6, NULL, 178.00, 1),
(16, 14, 45, 70, NULL, 98.00, 1),
(17, 15, 19, 33, NULL, 18.00, 1);

-- ==================== 11. 资金审计流水表数据 ====================
INSERT INTO `user_balance_log` (`id`, `user_id`, `amount`, `type`, `order_no`) VALUES
(1, 1, 5000.00, 1, NULL),
(2, 1, -158.00, 2, 'ORD202606250001'),
(3, 1, -35.00, 2, 'ORD202606250002'),
(4, 4, 1000.00, 1, NULL),
(5, 4, -78.00, 2, 'ORD202606250003'),
(6, 5, 2000.00, 1, NULL),
(7, 5, -208.00, 2, 'ORD202606250004'),
(8, 4, -38.00, 2, 'ORD202606250005'),
(9, 1, -188.00, 2, 'ORD202606250006'),
(10, 5, -52.00, 2, 'ORD202606250007'),
(11, 1, -45.00, 2, 'ORD202606250008'),
(12, 4, -28.00, 2, 'ORD202606250009'),
(13, 5, -88.00, 2, 'ORD202606250010');
-- ==================== 12. 商品评价表数据 ====================
INSERT INTO `product_review` (`id`, `order_no`, `product_id`, `user_id`, `rating`, `content`) VALUES
(1, 'ORD202606250001', 1, 1, 5, '味道非常正宗的黑森林，不愧是招牌，巧克力味很浓郁！'),
(2, 'ORD202606250001', 1, 4, 4, '还不错，就是稍微有点甜，整体可以。'),
(3, 'ORD202606250002', 11, 1, 5, '慕斯口感很细腻，抹茶味道很高级，非常推荐。'),
(4, 'ORD202606250003', 27, 4, 5, '这个吐司做早餐太棒了，特别软，拉丝效果好。'),
(5, 'ORD202606250004', 5, 5, 5, '红丝绒颜色很正，奶油也不腻，完美。');

-- ==================== 13. 优惠券表数据 ====================
INSERT INTO `coupon` (`id`, `name`, `type`, `min_amount`, `discount_amount`, `discount_rate`, `stock`, `start_time`, `end_time`, `status`) VALUES
(1, '新用户满50减10', 1, 50.00, 10.00, NULL, 1000, '2024-01-01 00:00:00', '2030-12-31 23:59:59', 1),
(2, '新店开业满50减25', 1, 50.00, 25.00, NULL, 1000, '2024-01-01 00:00:00', '2030-12-31 23:59:59', 1);

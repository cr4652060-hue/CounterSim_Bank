INSERT INTO t_sys_config (config_key, config_value, config_desc) VALUES
                                                                     ('default_cash', '140000.00', '柜员默认现金'),
                                                                     ('auth_threshold', '300000.00', '库箱授权阈值'),
                                                                     ('overflow_threshold', '400000.00', '超库阈值'),
                                                                     ('large_amount_threshold', '100000.00', '联网核查触发阈值'),
                                                                     ('default_cd_count', '100', '默认普通版存单库存'),
                                                                     ('current_rate', '0.0030', '活期利率'),
                                                                     ('card_prefix_fumin', '6223,6215', '福民卡前缀'),
                                                                     ('card_prefix_yikatong', '62232033', '个人一卡通前缀'),
                                                                     ('passbook_prefix_1', '9161110000101', '存折前缀1'),
                                                                     ('passbook_prefix_2', '9161110000102', '存折前缀2'),
                                                                     ('cd_prefix_1', '30159601100', '存单前缀'),
                                                                     ('new_cd_voucher_prefix', '079', '新存单凭证前缀'),
                                                                     ('new_passbook_voucher_prefix', '2219', '新存折凭证前缀');

INSERT INTO t_teller (teller_code, teller_name, avatar_path, current_cash, auth_threshold, overflow_threshold, status, created_at, updated_at) VALUES
                                                                                                                                                   ('01', '01号柜员', '/images/teller-01.png', 140000.00, 300000.00, 400000.00, 'ACTIVE', NOW(), NOW()),
                                                                                                                                                   ('02', '02号柜员', '/images/teller-02.png', 140000.00, 300000.00, 400000.00, 'ACTIVE', NOW(), NOW()),
                                                                                                                                                   ('03', '03号柜员', '/images/teller-03.png', 140000.00, 300000.00, 400000.00, 'ACTIVE', NOW(), NOW()),
                                                                                                                                                   ('04', '04号柜员', '/images/teller-04.png', 140000.00, 300000.00, 400000.00, 'ACTIVE', NOW(), NOW()),
                                                                                                                                                   ('05', '05号柜员', '/images/teller-05.png', 140000.00, 300000.00, 400000.00, 'ACTIVE', NOW(), NOW()),
                                                                                                                                                   ('06', '06号柜员', '/images/teller-06.png', 140000.00, 300000.00, 400000.00, 'ACTIVE', NOW(), NOW()),
                                                                                                                                                   ('07', '07号柜员', '/images/teller-07.png', 140000.00, 300000.00, 400000.00, 'ACTIVE', NOW(), NOW()),
                                                                                                                                                   ('08', '08号柜员', '/images/teller-08.png', 140000.00, 300000.00, 400000.00, 'ACTIVE', NOW(), NOW()),
                                                                                                                                                   ('09', '09号柜员', '/images/teller-09.png', 140000.00, 300000.00, 400000.00, 'ACTIVE', NOW(), NOW()),
                                                                                                                                                   ('10', '10号柜员', '/images/teller-10.png', 140000.00, 300000.00, 400000.00, 'ACTIVE', NOW(), NOW()),
                                                                                                                                                   ('11', '11号柜员', '/images/teller-11.png', 140000.00, 300000.00, 400000.00, 'ACTIVE', NOW(), NOW()),
                                                                                                                                                   ('12', '12号柜员', '/images/teller-12.png', 140000.00, 300000.00, 400000.00, 'ACTIVE', NOW(), NOW()),
                                                                                                                                                   ('13', '13号柜员', '/images/teller-13.png', 140000.00, 300000.00, 400000.00, 'ACTIVE', NOW(), NOW()),
                                                                                                                                                   ('14', '14号柜员', '/images/teller-14.png', 140000.00, 300000.00, 400000.00, 'ACTIVE', NOW(), NOW()),
                                                                                                                                                   ('15', '15号柜员', '/images/teller-15.png', 140000.00, 300000.00, 400000.00, 'ACTIVE', NOW(), NOW()),
                                                                                                                                                   ('16', '16号柜员', '/images/teller-16.png', 140000.00, 300000.00, 400000.00, 'ACTIVE', NOW(), NOW()),
                                                                                                                                                   ('17', '17号柜员', '/images/teller-17.png', 140000.00, 300000.00, 400000.00, 'ACTIVE', NOW(), NOW()),
                                                                                                                                                   ('18', '18号柜员', '/images/teller-18.png', 140000.00, 300000.00, 400000.00, 'ACTIVE', NOW(), NOW()),
                                                                                                                                                   ('19', '19号柜员', '/images/teller-19.png', 140000.00, 300000.00, 400000.00, 'ACTIVE', NOW(), NOW()),
                                                                                                                                                   ('20', '20号柜员', '/images/teller-20.png', 140000.00, 300000.00, 400000.00, 'ACTIVE', NOW(), NOW());

INSERT INTO t_teller_inventory (teller_code, voucher_type, start_no, end_no, total_count, remain_count, used_count, updated_at)
SELECT teller_code, 'ordinary_cd', CONCAT('079', LPAD(1, 7, '0')), CONCAT('079', LPAD(100, 7, '0')), 100, 100, 0, NOW()
FROM t_teller;

INSERT INTO t_customer (customer_key, customer_name, id_type, id_no, mobile, avatar_path, created_at, updated_at) VALUES
                                                                                                                      ('KH000001', '沈伟', '1001-第二代居民身份证', '33010219671121100X', '18115033520', '/images/customer-avatar.png', NOW(), NOW()),
                                                                                                                      ('KH000002', '夏雨林', '1001-第二代居民身份证', '370105195512212650', '13976619241', '/images/customer-avatar.png', NOW(), NOW());

INSERT INTO t_customer_media (customer_key, account_category, medium_type, medium_sub_type, medium_no, customer_account_no, currency, balance_amount, open_date, maturity_date, term_value, rate_value, deposit_type, auto_renew_flag, media_status, voucher_no, remark, created_at, updated_at) VALUES
                                                                                                                                                                                                                                                                                                     ('KH000001', '个人非结算账户', '存折', '普通存折', '91611100001019345705', '916110000010737315443', '156-CNY人民币', 4849.53, '2019-05-25', NULL, NULL, 0.0000, NULL, NULL, '正常', '221925861', '示例活期存折', NOW(), NOW()),
                                                                                                                                                                                                                                                                                                     ('KH000001', '个人非结算账户', '存单', '普通存单', '301596011001029721', '916110000010579378500', '156-CNY人民币', 5000.00, '2026-02-26', '2026-08-26', '半年', 1.5500, '整存整取', '是', '已开立', '0798098430', '示例整存整取存单', NOW(), NOW()),
                                                                                                                                                                                                                                                                                                     ('KH000002', '个人结算账户', '借记卡', '第三代社保卡', '6223203388761776', '9161100000106468526', '156-CNY人民币', 3649.25, '2022-11-16', NULL, NULL, 0.0000, NULL, NULL, '正常', '', '示例社保卡', NOW(), NOW());
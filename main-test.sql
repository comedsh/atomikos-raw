-- == 测试部分提交成功，部分失败( prepared 后，没有提交 )
-- acidrest 对应的事务先提交
SELECT * FROM atomikos_mio.acidrest;
SELECT * FROM atomikos_crm.canale;
-- 检查数据库当前有哪些事务是没有成功的
XA RECOVER;

XA COMMIT 'abc';

show variables like 'log_bin%';

show binary logs;

delete from atomikos_mio.acidrest;




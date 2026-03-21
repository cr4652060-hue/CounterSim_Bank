# CounterSim Bank

基于 **Java 17 + Spring Boot + Thymeleaf + MyBatis-Plus + MySQL** 的柜面培训模拟系统启动工程。

## 当前已完成

- 登录页：柜员 01-20 直接登录。
- 柜员主页面：三栏布局、顶部栏、右下角黄色客户到达模拟区。
- 当前临柜客户：从数据库随机抽取、切换、清空。
- 0055 柜员库箱查询：支持现金/凭证两种查询。
- 客户 Excel 导入：按任务单字段顺序导入客户主档和介质明细。
- MySQL 初始化脚本：包含系统配置、柜员、普通版存单库存初始数据。

## 启动方式

1. 在 MySQL 中执行：
    - `src/main/resources/db/mysql/schema.sql`
    - `src/main/resources/db/mysql/data.sql`
2. 修改 `src/main/resources/application.yml` 中数据库连接。
3. 启动：
    - `mvn spring-boot:run`
4. 打开：`http://localhost:18099/login`

## 下一步建议

- 补齐 1061/1062/1651/1657 四个交易页面及提交流程。
- 增加 3900 联网核查、人脸比对、授权弹窗、后置影像链路。
- 增加交易流水、授权流水、日累计统计、利率/前缀配置读取与规则引擎。
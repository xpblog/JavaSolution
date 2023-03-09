### 白名单解决方案
利用AOP拦截，获取ip，如果ip不在白名单列表，则报错。

知识点：
- 配置类相关注解，@Configuration @ConfigurationProperties
- AOP思想
- 切入点配置(如果切入点不存在，项目不能启动起来)
- 从请求上获取ip
package me.qpc.plugin;

import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;

@Component
@Intercepts
  ({
    @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class,
        RowBounds.class, ResultHandler.class}),
    @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
  })
public class SqlLogPlugin implements Interceptor {
  @Override
  public Object intercept(Invocation invocation) throws Throwable {

    // 打印sql信息
    Object arg = invocation.getArgs()[0];
    MappedStatement mappedStatement = (MappedStatement) arg;
    SqlSource sqlSource = mappedStatement.getSqlSource();
    BoundSql boundSql = sqlSource.getBoundSql(invocation.getArgs()[1]);
    String sql = boundSql.getSql().replaceAll("\n", "").replaceAll("\\s+", " ");
    int paramCount = sql.length() - sql.replaceAll("\\?","").length();
    if (paramCount != 0) {
      if (paramCount == 1) {
        sql = sql.replaceFirst("\\?", String.valueOf(invocation.getArgs()[1]));
      } else {
        MapperMethod.ParamMap map = (MapperMethod.ParamMap) invocation.getArgs()[1];
        for (int i = 1; i <= paramCount; i++) {
          String key = "param" + i;
          Object o = map.get(key);
          sql = sql.replaceFirst("\\?", String.valueOf(o));
        }
      }
    }
    System.err.println("=============执行sql info==============");
    System.out.println("     " + sql);
    System.out.println();

    // 打印sql执行时长
    long start = System.currentTimeMillis();
    Object proceed = invocation.proceed();
    long timeLong = System.currentTimeMillis() - start;
    System.err.printf("=============执行sql结束{%s}ms==============", timeLong);
    return proceed;
  }
}

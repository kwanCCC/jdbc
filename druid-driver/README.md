# druid driver

Druid driver是为了配合使用[Druid sql项目](http://git.oneapm.me/tps/druid-sql/tree/feature/v2)而封装出的jdbc driver,可以与myBatis集成做ORM映射,从而方便查询Druid或者MetricStore中的数据。

## 查询druid

- jdbc url
  `jdbc:ONEAPM://${druid_query_broker_endpoint}/druid/v2`
- driver class
  `com.blueocn.tps.jdbc.driver.Driver`

## 查询metricStore

- jdbc url
  `jdbc:METRIC_STORE://${metric_store_query_endpoint}/all?f=druid`
- driver class
  `com.blueocn.tps.jdbc.driver.Driver`

目前:
- database需要写死为all,但这对SQL没有影响,即目前driver总会使用SQL语句中指定的数据库名称和表名称;
- 必须添加`f=druid`参数;
- 查询metric Store与查询druid使用的是同一个driver class, **但在将来版本中可能会提供一个独立的driver class**。
# druid sql parser

This library can be used to parse our SQL dialect into Druid.io query model or Touch MetricStore query model. Like all other language compilers, the structure of this project includes:
 
- frontend, which translate the SQL dialect into a common internal query model
- backend, which translate the intermediate model into final query model

## TODO
- refactor the package structure to reflect the frontend vs. backend distinguish
- remove dependency or any other connection between the intermediate model from druid query model
- implement the backend for MetricStore query compilation
- document the SQL dialect
- roadmap, support latest version of Druid.io etc.

查看[最新文档](http://docs.oneapm.me/docs/druid-sql/en/latest/index.html)。


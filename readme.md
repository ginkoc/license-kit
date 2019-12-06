# License-kit

License-kit是一个license生成与使用的工具集，主要包括以下个子项目：license生成工具（`manager`），license校验工具（`checker`），license生成工具和校验工具需要的公共类（`common`）。以及一个使用`checker`导出API的样例项目（`example`）。

## 说明

------

### manager

manager是一个后台项目，只负责根据参数生成license，并且提供下载license的接口。[详见readme](manager/readme.md)

### checker

checker是一个导出api项目，生成的jar包用于在其他项目中校验manager中生成的license。[详见readme](checker/readme.md)

### common

common是一个公共类项目，作为backend项目和checker项目中用到的公共类的载体。

### example

example是一个样例项目，用于演示如何使用checker导出API的样例。


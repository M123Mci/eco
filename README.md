# eco（MagicRealms 维护版）

面向 **Java 25、Paper 26.2** 的 eco 插件框架，保留物品显示、配方、数据存储、调度和接管版联动接口。本版仅包含一个 v26_2 NMS 实现，不支持旧游戏版本。

采用官方 [Auxilor/eco](https://github.com/Auxilor/eco) 的 2026.36 源码，并以 7.4.0 对照基线逐项迁入接管改动。源码提交、行为差异和验收记录见 [迁移记录](docs/26.2-migration.md)。

## 本机构建

将真实依赖放入 D:/Minecraft/PluginLibs/Jars；校验值见 [本地 API 清单](docs/local-api-sha256.txt)。不得用空 API 替代缺失依赖。

    gradlew.bat clean shadowJar
    gradlew.bat :eco-core:core-nms:v26_2:checkNmsLinkage

插件输出为 build/libs/eco-2026.36-mr.1.jar。可使用 -PexternalPluginLibDir=... 覆盖依赖目录，-PlocalPluginRepoDir=... 覆盖本地 Maven 目录。

    gradlew.bat distribute publishPluginPublicationToLocalPluginsRepository :eco-api:publishApiPublicationToLocalPluginsRepository

API 发布坐标为 com.willfp:eco:2026.36-mr.1，完整插件为 com.willfp:eco-plugin:2026.36-mr.1，默认发布至 D:/Minecraft/PluginLibs/Maven。

## 下游依赖

下游构建从本地 Maven 读取上述 API；服务器安装完整插件，在下游 plugin.yml 声明 depend: [eco]。eco 本身不提供 /eco reload 命令；开发者通过 EcoPlugin.reload() 调用重载 API。

## 配置和数据

保留 YAML、MySQL、MariaDB 和 MongoDB 存储支持。修改 data-handler 前应备份持久化数据。数据库连接信息只写入服务器私有配置，不提交仓库。

原有 use-display-frame、display-frame-ttl、use-immediate-placeholder-translation-for-math 等配置继续生效。默认保留旧版强制非斜体的 Lore 格式语义。

保留上游 [MIT 许可证](LICENSE.md) 和作者归属。

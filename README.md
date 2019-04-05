# TorchRT
> Torch Runtime  
> 适用于Sponge的CraftBukkit兼容运行时

## 我们为什么要开发 TorchRT
目前社区中也已存在一些用于兼容**CraftBukkit**接口的**Minecraft模组**或**Sponge插件**。  
比如[**PoreRT**](https://github.com/Maxqia/PoreRT)（现今已停止开发）等，皆能对大部分CraftBukkit提供支持，但实现方案都**不尽完美**。  
由此，我们决定启动**TorchRT**的开发，打造一个更加完美的运行时支持系统。   

## 对于 TorchRT 的展望
我们计划开发**TorchRT**，并且此运行时支持系统的功能**包括但不仅限于**对**CraftBukkit的原生接口**提供支持。我们将会设计与开发一些更加适合开发者、使用者的特性与功能，不过对于这些内容的支持需要一定的时间。毕竟“万丈高楼平地起”，我们必须先保证我们能**尽可能完美支持CraftBukkit中的原生功能**，以及我们有能力、有精力完成这一庞大的工程并提供长期稳定的技术更新与支持。

### 已实现的功能
* **CocoaBean**   
   * 正在完善
   * 为 **TorchRT** 开发的JavaBean框架，可用于对运行时系统及其它组件的监控或调试
* **BanList**
   * 由于 **CraftBukkit** 中仍然在BanList中使用**玩家名称**而**不是UUID**，所以可能会存在一些历史遗留问题，并且并**不能保证**能够被解决。
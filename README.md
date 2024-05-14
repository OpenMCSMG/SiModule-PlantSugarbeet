以下是根据您提供的信息整理成的 Markdown 文档：

```markdown
# 测试指令
以下是一系列测试指令，用于调用 `GameHandle` 类中的不同方法。

## 指令列表

### 羊驼修复
修复羊驼数量。
```plaintext
/bd invoke 羊驼修复(1)
```
- **方法**: `sheepFix(amount: Int)`

### 岩浆史莱姆破坏
破坏岩浆史莱姆，并可指定观众名字。
```plaintext
/bd invoke 岩浆史莱姆(1, 'hh', 3)
```
- **方法**: `magmaSlimeBreak(amount: Int, userName: String, edgeSize: Int = 3)`

### TNT劈叉
触发 TNT 劈叉效果。
```plaintext
/bd invoke 三响炮(1)
```
- **方法**: `tntFart(amount: Int)`

### 村民帮助种植
村民帮助进行种植。
```plaintext
/bd invoke 朴实村民(1)
```
- **方法**: `villagerHelp(amount: Int)`

### 贪吃鸟（龙核生物）
贪吃鸟效果，可指定观众名字。
```plaintext
/bd invoke 贪吃鸟(1, 'hh')
```
- **方法**: `greedyBird(amount: Int, userName: String)`

### 幽灵麋鹿（劈叉）吃
幽灵麋鹿吃效果，可指定观众名字。
```plaintext
/bd invoke 小鹿乱撞(1, 'hh')
```
- **方法**: `deer(amount: Int, userName: String)`

### 牛吃！
牛吃效果，可指定观众名字。
```plaintext
/bd invoke 牛牛出击(1, 'hh')
```
- **方法**: `cowGoGoGo(amount: Int, userName: String)`

### 凋零+雷电
触发凋零和雷电效果。
```plaintext
/bd invoke 凋零风暴(1)
```
- **方法**: `witherStorm(amount: Int)`

### 末影龙
触发末影龙效果。
```plaintext
/bd invoke 末影龙大清洗(1)
```
- **方法**: `dragon(amount: Int)`

## GameHandle 类
`GameHandle` 是玩法主类，属于 `cn.cyanbukkit.plant` 包。以下是该类提供的一些方法及其简要说明。

### 方法列表

#### sheepFix(amount: Int)
- **功能**: 修复羊驼。

#### magmaSlimeBreak(amount: Int, userName: String, edgeSize: Int = 3)
- **功能**: 破坏岩浆史莱姆。
- **参数**:
  - `amount` (Int): 破坏的数量。
  - `userName` (String): 观众的名字。
  - `edgeSize` (Int, 默认为 3): 边缘大小。

#### tntFart(amount: Int)
- **功能**: 触发 TNT 劈叉效果。

#### villagerHelp(amount: Int)
- **功能**: 村民帮助种植。

#### greedyBird(amount: Int, userName: String)
- **功能**: 触发贪吃鸟效果。
- **参数**:
  - `amount` (Int): 触发的数量。
  - `userName` (String): 观众的名字。

#### deer(amount: Int, userName: String)
- **功能**: 触发幽灵麋鹿吃效果。
- **参数**:
  - `amount` (Int): 触发的数量。
  - `userName` (String): 观众的名字。

#### cowGoGoGo(amount: Int, userName: String)
- **功能**: 触发牛吃效果。
- **参数**:
  - `amount` (Int): 触发的数量。
  - `userName` (String): 观众的名字。

#### witherStorm(amount: Int)
- **功能**: 触发凋零+雷电效果。

#### dragon(amount: Int)
- **功能**: 触发末影龙效果。

### 注意事项
- 使用上述指令时，请确保 `GameHandle` 类已正确加载，并且调用的玩家具有相应的权限。
- 某些方法可能需要特定的游戏环境或模组支持，以确保效果正确触发。
```

请根据实际的类定义和方法功能，调整上述文档中的描述以确保其准确性。如果有任何参数或功能描述不准确，或者遗漏了某些重要的细节，请进行相应的修改。

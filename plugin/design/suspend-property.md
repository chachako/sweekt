# Suspend Property

让 **property** 具有与 `suspend fun` 相同的行为。有时候我们需要保证在协程上下文调用属性，并且想让 **getter/setter** 是可挂起的，所以可通过此编译器插件实现预期目标。



### Checker

- 当判断到没有在协程上下文中调用了注解了 **@Suspend** 的属性时报告错误
- 当非抽象属性注解了 **@Suspend** 时，**getter** (如果有) 必须要调用 `suspendGetter`，**setter** (如果有) 必须要调用 `suspendSetter`



### Suspend getter

```kotlin
@Suspend val name: String
  get() = suspendGetter {
    withContext(Dispatchers.IO) {
      fetchUser().name
    }
  }

suspend fun main() {
  print(name)
}
```

#### 幕后（伪反编译）：

```kotlin
val name: String

suspend fun getName(): String {
  return withContext(Dispatchers.IO) {
    fetchUser().name
  }
}

suspend fun main() {
  print(getName())
}
```

- 当编译时检测到 `suspendGetter` 则会将块中的代码移动到一个 `suspend fun`ction 中，并替换调用



### Suspend setter

```kotlin
@Suspend var time: Int = 0
  set(value) = suspendSetter {
    withContext(Dispatchers.Default) {
      field = computeTime(value)
    }
  }

suspend fun main() {
  time = System.currentTimeMills()
}
```

#### 幕后（伪反编译）：

```kotlin
var time: Int = 0

suspend fun setName(value: Int) {
  withContext(Dispatchers.Default) {
    time = value
  }
}

suspend fun main() {
  setName(System.currentTimeMills())
}
```

- 当编译时检测到 `suspendSetter` 则会将块中的代码移动到一个 `suspend fun`ction 中，并替换调用


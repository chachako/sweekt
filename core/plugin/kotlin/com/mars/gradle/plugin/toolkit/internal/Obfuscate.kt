@file:Suppress("unused", "SpellCheckingInspection", "PackageDirectoryMismatch")


// 一些 NDK 混淆配置，注意你的 NDK 必须拥有参数对应的 CLANG...


/* https://github.com/GoSSIP-SJTU/Armariris */
val armaririsFlags
  get() = arrayOf(
    /** 控制流扁平化 (把一些 if-else 语句，嵌套成 do-while) */
    "-mllvm -fla", // 激活控制流扁平化
    "-mllvm -split", // 激活基本块分割 ("split_num" 可设置次数，默认为 1)
    /** 指令替换 (指令序列替换标准二元运算符(+ , – , & , | 和 ^)) */
    "-mllvm -sub", // 激活指令替换
    "-mllvm -sub_loop=3", // 在函数上应用 n 次指令替换 (默认值：1)
    /** 虚假控制流程 (嵌套多层假的判断逻辑，一个简单的运算都会在外面包几层 if-else) */
    "-mllvm -bcf", // 激活虚假控制流程
//    "-mllvm -bcf_loop=3", // 虚假控制流嵌套强度 (强度过高会导致编译速度变慢，默认值：1)
//    "-mllvm -bcf_prob=40", // 基本块将以百分之 n 的概率进行模糊处理 (默认值：30 %)
    /** 字符串混淆 (加密为二进制) */
    "-mllvm -sobf", // 开启字符串加密
    "-mllvm -seed=0xdeadbeaf" // 指定随机数生成器种子流程
  )

/* https://github.com/amimo/goron */
val goronFlags
  get() = arrayOf(
    "-mllvm -irobf-cff", // 过程相关控制流平坦混淆
    "-mllvm -irobf-indbr", // 间接跳转,并加密跳转目标
    "-mllvm -irobf-icall", // 间接函数调用,并加密目标函数地址
    "-mllvm -irobf-indgv", // 间接全局变量引用,并加密变量地址
    "-mllvm -irobf-cse" // 字符串 (c string) 加密功能
  )

/*
* https://github.com/HikariObfuscator/Hikari/
* https://github.com/Checkson/blog/issues/16
*/
val hikariFlags
  get() = arrayOf(
    /** 控制流扁平化 (把一些 if-else 语句，嵌套成 do-while) */
    "-mllvm -enable-cffobf", // 激活控制流扁平化
    "-mllvm -enable-splitobf", // 激活基本块分割
    /** 虚假控制流程 (嵌套多层假的判断逻辑，一个简单的运算都会在外面包几层 if-else) */
    "-mllvm -enable-bcfobf", // 激活伪控制流
//    "-mllvm -bcf_loop=3", // 虚假控制流嵌套强度 (强度过高会导致编译速度变慢，默认值：1)
    "-mllvm -bcf_prob=40", // 基本块将以百分之 n 的概率进行模糊处理 (默认值：30 %)
    /** 指令替换 (指令序列替换标准二元运算符(+ , – , & , | 和 ^)) */
    "-mllvm -enable-subobf", // 激活指令替换
    "-mllvm -sub_loop=3", // 在函数上应用 n 次指令替换 (默认值：1)
//    /** 例子: class A(func a) 会被多个类包装：Wrapper1(func a) -> Wrapper2(func a) -> Wrapper3(func a) -> ... -> A(func a) */
//    "-mllvm -enable-funcwra", // 启用函数包装
//    "-mllvm -fw_prob=60", // 为混淆的每个 CallSite 选择概率 [％]
//    "-mllvm -fw_times=14", // 传递循环次数
    /** 反 class-dump */
    "-mllvm -enable-acdobf",
    /** 字符串加密 */
    "-mllvm -enable-strcry",
    /** 基于寄存器的相对跳转 */
    "-mllvm -enable-indibran"
  )
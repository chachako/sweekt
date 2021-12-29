@file:Suppress("SpellCheckingInspection")

import com.meowool.sweekt.isJvmArrayTypeDescriptor
import com.meowool.sweekt.isJvmTypeDescriptor
import com.meowool.sweekt.isJvmPrimitiveType
import com.meowool.sweekt.isJvmPrimitiveTypeDescriptor
import com.meowool.sweekt.toJvmPackageName
import com.meowool.sweekt.toJvmQualifiedTypeName
import com.meowool.sweekt.toJvmTypeDescriptor
import com.meowool.sweekt.toJvmTypeSimpleName
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe

/**
 * @author 凛 (RinOrz)
 */
class JvmDescriptorTestsJVM : StringSpec({
  "isJvmArrayTypeDescriptor" {
    Array<String>::class.java.name should {
      it shouldBe "[Ljava.lang.String;"
      it.isJvmArrayTypeDescriptor() shouldBe true
    }
    Array<Array<LongArray>>::class.java.name should {
      it shouldBe "[[[J"
      it.isJvmArrayTypeDescriptor() shouldBe true
    }
    String::class.java.name.isJvmArrayTypeDescriptor() shouldBe false
  }
  "isJvmDescriptor" {
    Map.Entry::class.java.name.toJvmTypeDescriptor('.') should {
      it shouldBe "Ljava.util.Map\$Entry;"
      it.isJvmTypeDescriptor() shouldBe true
    }
  }
})
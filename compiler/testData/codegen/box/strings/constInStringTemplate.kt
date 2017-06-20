// WITH_RUNTIME

import kotlin.test.assertEquals

const val constTrue = true
const val const42 = 42
const val constPiF = 3.14F
const val constPi = 3.14
const val constString = "string"

fun box(): String {
    assertEquals("true", "$constTrue")
    assertEquals("42", "$const42")
    assertEquals("3.14", "$constPiF")
    assertEquals("3.14", "$constPi")
    assertEquals("string", "$constString")

    assertEquals("null", "${null}")
    assertEquals("42", "${42}")

    return "OK"
}
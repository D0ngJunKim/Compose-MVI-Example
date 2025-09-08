package com.yeogibook.abcmm.presentation.extenstion

import com.google.gson.JsonObject

fun JsonObject.getAsStringOrNull(memberName: String): String? {
    return this.get(memberName)?.takeIf { it.isJsonPrimitive }?.asString
}

fun JsonObject.getAsIntOrNull(memberName: String): Int? {
    return this.get(memberName)?.takeIf { it.isJsonPrimitive }?.asInt
}
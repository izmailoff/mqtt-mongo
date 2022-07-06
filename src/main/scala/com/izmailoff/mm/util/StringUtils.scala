package com.izmailoff.mm.util

object StringUtils:

  def emptyToNull(str: String): String =
    if str == null || str.trim.isEmpty then null
    else str


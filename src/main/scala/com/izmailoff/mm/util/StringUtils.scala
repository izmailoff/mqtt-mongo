package com.izmailoff.mm.util

object StringUtils {

  def emptyToNull(str: String) =
    if (str == null || str.trim.isEmpty) null
    else str

}

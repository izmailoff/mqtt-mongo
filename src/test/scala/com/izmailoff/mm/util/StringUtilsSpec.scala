package com.izmailoff.mm.util

import org.specs2.matcher.DataTables
import org.specs2.mutable.Specification
import com.izmailoff.mm.util.StringUtils._

class StringUtilsSpec
  extends Specification
  with DataTables {

  "String utils java compat" should {
    "convert empty strings to null" in {

      "input"        || "expected value"  |
      (null: String) !! null              |
      " "            !! null              |
      "     "        !! null              |
      " \t \r\n \n " !! null              |
      "abc"          !! "abc"             |> {
        (input, expected) =>
          emptyToNull(input) must be equalTo expected
      }
    }
  }
}

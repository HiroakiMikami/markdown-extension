package jp.hiroaki_mikami.markdown.extension.buildin

import org.scalatest.FunSuite

/**
 * Created by mikami on 14/12/15.
 */
class Latex$Test extends FunSuite {
  test("should add \\\\( and \\\\) when text is one line") {
    assert(Latex(Seq(), "a") == "\\\\(a\\\\)")
  }
  test("should add \\\\[ and \\\\] when text is not one line") {
    assert(Latex(Seq(), "a\nb") == "\\\\[a\nb\\\\]")
  }
  test("should add \\ to \\, ), (, [, ], *, _ in order to avoid parse-error of markdown") {
    assert(Latex(Seq(), "\\frac{a}{b}") == "\\\\(\\\\frac{a}{b}\\\\)")
  }
}

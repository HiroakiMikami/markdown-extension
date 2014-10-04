package data

import org.scalatest.FunSuite

class LatexExtensionTest extends FunSuite {
  test("should add \\\\( and \\\\) when text is one line") {
    assert(LatexExtension("", "a") == "\\\\(a\\\\)")
  }
  test("should add \\\\[ and \\\\] when text is not one line") {
    assert(LatexExtension("", "a\nb") == "\\\\[a\nb\\\\]")
  }
  test("should add \\ to \\, ), (, [, ], *, _ in order to avoid parse-error of markdown") {
    assert(LatexExtension("", "\\frac{a}{b}") == "\\\\(\\\\frac{a}{b}\\\\)")
  }
  test("should add MathJax script to markdown") {
    assert(LatexExtension("") == """<script
                                   |  type="text/javascript"
                                   |  src="https://c328740.ssl.cf1.rackcdn.com/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML"
                                   |></script>
                                   |""".stripMargin
    )
  }
}

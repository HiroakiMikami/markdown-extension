function latexTest() {
  if(latexPlugin("", "\\frac{a}{b}") != "\\\\(\\\\frac{a}{b}\\\\)") {
    window.alert("test fail" + latexPlugin("", "\\frac{a}{b}"));
  } else if ( latexPlugin("", "\nz = \\sqrt{x}\n") != "\\\\[\nz = \\\\sqrt{x}\n\\\\]") {
    window.alert("test fail" + latexPlugin("", "\nz = \\sqrt{x}\n"));
  } else {
    window.alert("latex test pass");
  }
}


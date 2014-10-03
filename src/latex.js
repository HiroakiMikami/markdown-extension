// latex対応のplugin
function latexPlugin(attr, texText) {
  // 特殊文字(\, (, ), [, ], *, _の変更
  var retval = texText.split("\\").join("\\\\");
  retval = retval.split("(").join("\\(");
  retval = retval.split(")").join("\\)");
  retval = retval.split("[").join("\\[");
  retval = retval.split("]").join("\\]");
  retval = retval.split("*").join("\\*");
  retval = retval.split("_").join("\\_");

  // TODO これの変更はdom textを用いるほうが自然ではないかと思う.
  if (retval.split("\n").length == 1) {
    // 1行ならば, \(, \)で囲う
    return "\\\\(" + retval + "\\\\)";
  } else {
    // 1行ならば, \[, \]で囲う
    return "\\\\[" + retval + "\\\\]";
  }
}

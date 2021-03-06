markdown-extension
==================

markdownを自分が使いやすいように拡張した.
パイプ, markdown, echo, cat, dotコマンドが使える必要がある. 基本的にはUNIX専用.

## インストール
```
$ ./install.sh
```

## 使い方
```
$ emarkdown [<options>] <extended-markdown-file>
```
または, 標準入力に, markdownを書き込む.

## 基本形式
以下のような形式
```
$<commandName>[parameters]{ body }
```
parametersには, 文字列やJSONを用いることができる. また, bodyは複数行にわたって良い.

例えば,
```
$cmd1{ body }
$cmd2[param1]{ body }
$cmd3[param1=test]{ body }
$cmd4[param1="test"]{ body }
$cmd5[param1="[1, 2, 3]"]{ body }
$cmd6{
  multi-line
  body {
    internal brackets
  }
  \}
}
```
はすべて, 拡張ポイントであると認識される.

## 現在の拡張
### Latex式の埋め込み
MathJaxを用いた埋め込みをLatex同様に書くことができるようにするもの. 直接MathJaxを用いる場合と異なり, scriptの設定の必要がないなどの利点はある.
```
$tex{
\\frac{a}{b}
}
```
が,
```
\\[
\\frac{a}{b}
\\]
```
へと変換される.

### dotの埋め込み
dot(graphviz)がsvg形式でhtmlに出力される.
```
$dot{
digragh G {
  a -> b
}
}
```
などのように書く.

### codeの埋め込み
```
$code[言語名]{
  program
}
```
とすると, highlight.jsによってプログラムが可視化される.
例えば,
```
$code[c]{
  int function(int x, int y) {
      return x + y;
  }
}
```

### google検索結果へのリンク
```
$google{word}
$google[word]{title}
```
どちらも, googleでwordを検索した結果のURLへのリンクを生成する. 後者の場合は, titleという文字列も表示される.

### tableの埋め込み
まだ実装できていない.

### Headlineの表示
見出しを右に表示することができる.

## 拡張の追加方法
まだ実装できていない.

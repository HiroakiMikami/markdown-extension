markdown-extension
==================

markdownを自分が使いやすいように拡張した.
UNIX専用. パイプ, echo, dotがコマンドが使える必要がある.

## 現在の拡張
### Latex式の埋め込み
MathJaxを用いた埋め込みをLatex同様に書くことができるようにするもの. 直接MathJaxを用いる場合と異なり, scriptの設定の必要がない, \\や\_の使用時にmarkdown記法が優先されない, という利点がある.
```
<tex>
\frac{a}{b}
</tex>
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
<dot>
digragh G {
  a -> b
}
</dot>
```
などのように書く.

### codeの埋め込み
\<code\>\</code\>タグだけで, コードを埋め込める.

### Headlineの表示
todo

## メモ
* util.evalなどを使い, 動的に拡張を渡せるとより便利かもしれないと思った.
* optionで拡張のon, offができると望ましいかもしれない.
* Headlineをtableでなく, Javascriptによって実現したほうが良い.
* PostExtensionをString -> Stringでなく, (Head, Body) -> (Head, Body)にしたほうがprefix追加とかの面で便利

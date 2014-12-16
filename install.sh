#! /bin/sh

sbt assembly

echo "#! /bin/sh" > emarkdown
echo $(cd $(dirname $0) && pwd)/target/scala-2.11/markdown-extension.extension-assembly-0.0.jar $@ >> emarkdown

chmod u+x emarkdown

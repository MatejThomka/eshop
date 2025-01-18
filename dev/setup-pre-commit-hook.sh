#!/bin/bash

if [[ "$OSTYPE" == @(cygwin*|msys*|win32*) ]]; then
    PWD=$(pwd -W)
    echo "Windows OS detected"
else
    PWD=$(pwd)
    echo "Unix OS detected"
fi

cat <<EOF > .git/hooks/pre-commit
#!/bin/bash

docker run --rm --name spotless-apply \
    -w //app \
    -v $PWD/src:/app/src \
    -v $PWD/pom.xml:/app/pom.xml \
    -v maven_cache:/root/.m2 \
    maven:3.9-eclipse-temurin-21-jammy \
    mvn spotless:apply

EOF

echo "Setup of pre-commit hook successful"
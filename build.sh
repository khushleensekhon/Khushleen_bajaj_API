#!/bin/bash
set -e

echo "Compiling..."
mkdir -p out/classes out/META-INF

find src -name "*.java" | xargs javac -d out/classes

cat > out/META-INF/MANIFEST.MF << EOF
Manifest-Version: 1.0
Main-Class: com.chitkara.bfhl.BfhlApplication
EOF

jar cfm bfhl-api.jar out/META-INF/MANIFEST.MF -C out/classes .
echo "Built: bfhl-api.jar ($(du -sh bfhl-api.jar | cut -f1))"

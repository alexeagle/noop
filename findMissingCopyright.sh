#!/bin/sh

for i in `find src -name "*.scala"` ; do head -2 $i | tail -1 | grep -v Copyright && echo $i ; done

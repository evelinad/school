#!/bin/bash
[[ -z $COMPILER ]] && COMPILER="gcc"
SIZE=20013
OUT="out_optimized"
if [[ $COMPILER="gcc" ]]; then
   ./dsymv_optimized $SIZE $OUT
fi

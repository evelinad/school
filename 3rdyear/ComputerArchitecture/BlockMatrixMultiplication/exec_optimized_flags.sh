#!/bin/bash
[[ -z $COMPILER ]] && COMPILER="gcc"
SIZE=20013
OUT="out_optimized_flags"
if [[ $COMPILER="gcc" ]]; then
   ./dsymv_optimized_flags $SIZE $OUT
fi

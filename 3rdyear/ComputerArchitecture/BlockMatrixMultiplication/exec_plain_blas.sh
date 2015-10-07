#!/bin/bash
[[ -z $COMPILER ]] && COMPILER="gcc"
SIZE=20013
OUT="out_plain_blas"
if [[ $COMPILER="gcc" ]]; then
   ./dsymv_plain_blas $SIZE $OUT
fi

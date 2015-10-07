export OMP_SCHEDULE="guided,5"
export OMP_NUM_THREADS=8
time ./paralel $1 $2 $3

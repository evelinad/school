echo Testing Mandelbrot
	for i in 1 2 3 4
	do
	echo Test $i
		for j in 1 2 4 8 10 15 30
		do
			echo N=$j
			time mpirun -n $j ./main mandelbrot$i.in out.pgm 2>&1
			./imgdiff32 out.pgm mandelbrot$i.pgm
			echo ---------------
		done
	done
echo Testing Julia
	for i in 1 2 3 4 5 6
	do
	echo Test $i
		for j in 1 2 4 8 10 15 30
		do
			echo N=$j
			time mpirun -n $j ./main julia$i.in out.pgm
			./imgdiff32 out.pgm julia$i.pgm
			echo ---------------
		done
done

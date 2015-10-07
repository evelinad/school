#!/bin/bash
#
# Author: Evelina Dumitrescu
# 331CA
#
# Job submitting script for each queue
#


#files for plotting data
plain_file="plain.dat"
blas_file="blas.dat"
opt_file="optimized.dat"
opt_flags_file="optimized_flags.dat"

#do some cleanup
rm -f $plain_file $blas_file $opt_file $opt_flags_file 2> /dev/null
touch $plain_file $blas_file $opt_file $opt_flags_file
rm -rf "out/"

#start testing
echo -e "==================================\nTesting homework\n==================================\n"

#load atlas modules
echo -e "\n==================================\nLoading modules\n==================================\n"
module load libraries/atlas-3.10.1-gcc-4.4.6-nehalem
module load libraries/atlas-3.10.1-gcc-4.4.6-opteron
module load libraries/atlas-3.10.1-gcc-4.4.6-quad
module list

#launch nehalem job
echo -e "\n==================================\nCompiling for Nehalem architecture\n==================================\n"
make -f MakefileNehalem clean
make -f MakefileNehalem
echo -e "\n==================================\nRunning Nehalem jobs\n==================================\n"
echo -e "\nStarting jobs ..."
mprun.sh --job-name ExecPlainBlas-N --queue ibm-nehalem.q \
	--script exec_plain_blas.sh --show-qsub --show-script --batch-job
mprun.sh --job-name ExecOptimized-N --queue ibm-nehalem.q \
        --script exec_optimized.sh --show-qsub --show-script --batch-job
mprun.sh --job-name ExecOptimizedFlags-N --queue ibm-nehalem.q \
        --script exec_optimized_flags.sh --show-qsub --show-script --batch-job

#wait for jobs to start
while [ $((`ls -l out/ | wc -l` - 1)) -lt 6 ]
do
  sleep 5
done
echo -e "Done ...\nWaiting for all jobs to finish ...\n"


#wait until all execution times have been written
output_plain_blasN=`cat out_plain_blas 2>/dev/null`
output_optimizedN=`cat out_optimized 2>/dev/null`
output_optimized_flagsN=`cat out_optimized_flags 2>/dev/null`
while [ "$output_plain_blasN" == "" ] || [ "$output_optimizedN" == "" ] || [ "$output_optimized_flagsN" == "" ];
  do
	output_plain_blasN=`cat out_plain_blas 2>/dev/null`
	output_optimizedN=`cat out_optimized 2>/dev/null`
	output_optimized_flagsN=`cat out_optimized_flags 2>/dev/null`
	sleep 3
  done

#extract data for plotting
echo -e "\nExtracting results ...\nFinished jobs ...\n"
echo 5  `head -1 out_plain_blas` >> $plain_file
echo 6  `tail -1 out_plain_blas` >> $blas_file
echo 7 $output_optimizedN >> $opt_file
echo 8 $output_optimized_flagsN >> $opt_flags_file 

#launch job for opteron
echo -e "\n==================================\nCompiling for Opteron architecture\n==================================\n"
make -f MakefileOpteron clean
make -f MakefileOpteron
echo -e "\n==================================\nRunning Opteron jobs\n==================================\n"
echo -e "\nStarting jobs ..."


mprun.sh --job-name ExecPlainBlas-O --queue ibm-opteron.q \
        --script exec_plain_blas.sh --show-qsub --show-script --batch-job
mprun.sh --job-name ExecOptimized-O --queue ibm-opteron.q \
        --script exec_optimized.sh --show-qsub --show-script --batch-job
mprun.sh --job-name ExecOptimizedFlags-O --queue ibm-opteron.q \
        --script exec_optimized_flags.sh --show-qsub --show-script --batch-job

#wait for jobs to start
while [ $((`ls -l out/ | wc -l` - 1)) -lt 12 ]
do
  sleep 5
done
echo -e "Done ...\nWaiting for all jobs to finish ...\n"


#wait until all execution times have been written
output_plain_blasO=`cat out_plain_blas 2>/dev/null`
output_optimizedO=`cat out_optimized 2>/dev/null`
output_optimized_flagsO=`cat out_optimized_flags 2>/dev/null`
while [ "$output_plain_blasO" == "" ] || [ "$output_optimizedO" == "" ] || [ "$output_optimized_flagsO" == "" ];
  do
        output_plain_blasO=`cat out_plain_blas 2>/dev/null`
        output_optimizedO=`cat out_optimized 2>/dev/null`
        output_optimized_flagsO=`cat out_optimized_flags 2>/dev/null`
        sleep 3
  done


#extract data for plotting
echo -e "\nExtracting results ...\nFinished jobs ...\n"
echo 15  `head -1 out_plain_blas` >> $plain_file
echo 16  `tail -1 out_plain_blas` >> $blas_file
echo 17 $output_optimizedO >> $opt_file
echo 18 $output_optimized_flagsO >> $opt_flags_file


#launch quad jobs
echo -e "\n==================================\nCompiling for Quad architecture\n==================================\n"
make -f MakefileQuad clean
make -f MakefileQuad
echo -e "\n==================================\nRunning Quad jobs\n==================================\n"
mprun.sh --job-name ExecPlainBlas-Q --queue ibm-quad.q \
        --script exec_plain_blas.sh --show-qsub --show-script --batch-job
mprun.sh --job-name ExecOptimized-Q --queue ibm-quad.q \
        --script exec_optimized.sh --show-qsub --show-script --batch-job
mprun.sh --job-name ExecOptimizedFlags-Q --queue ibm-quad.q \
        --script exec_optimized_flags.sh --show-qsub --show-script --batch-job

#wait for jobs to start
while [ $((`ls -l out/ | wc -l` - 1)) -lt 18 ]
do
  sleep 5
done
echo -e "Done ...\nWaiting for all jobs to finish ...\n"


#wait until all execution times have been written
output_plain_blasQ=`cat out_plain_blas 2>/dev/null`
output_optimizedQ=`cat out_optimized 2>/dev/null`
output_optimized_flagsQ=`cat out_optimized_flags 2>/dev/null`
while [ "$output_plain_blasQ" == "" ] || [ "$output_optimizedQ" == "" ] || [ "$output_optimized_flagsQ" == "" ];
  do
        output_plain_blasQ=`cat out_plain_blas 2>/dev/null`
        output_optimizedQ=`cat out_optimized 2>/dev/null`
        output_optimized_flagsQ=`cat out_optimized_flags 2>/dev/null`
        sleep 3
  done


#extract data for plotting
echo -e "\nExtracting results ...\nFinished jobs ...\n"
echo 25  `head -1 out_plain_blas` >> $plain_file
echo 26  `tail -1 out_plain_blas` >> $blas_file
echo 27 $output_optimizedQ >> $opt_file
echo 28 $output_optimized_flagsQ >> $opt_flags_file


#plot
gnuplot plot_script.pg



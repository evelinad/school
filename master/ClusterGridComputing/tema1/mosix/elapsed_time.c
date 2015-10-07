#include <stdio.h>
#include <sys/time.h>
#include <sys/types.h>
#define LINES 200
#define COLS 9000000

int line, col;
struct timeval t1, t2;
double value, elapsed_time;
FILE *out;


void get_elapsed_time(int line)
{
 double elapsed_time;

 gettimeofday(&t2, NULL);
 elapsed_time = (t2.tv_sec - t1.tv_sec) * 1000.0;
 elapsed_time += (t2.tv_usec - t1.tv_usec) / 1000.0;

 fprintf(out, "line %d, value %lg, %lg time passed!\n", line, value, elapsed_time); 
}


void do_long_computation()
{
  for (line = 0; line <= LINES; line++) {
    for (col = 0; col <= COLS; col++) 
      value = (double)(col + 1) / (double)(line + 1);

    if ((line % 10) == 0) {
      get_elapsed_time(line);
    }
  }
}

int main(int argc, char **argv)
{
  out = fopen("time-waste.out", "w");
  gettimeofday(&t1, NULL);
  do_long_computation();
  fclose(out);
  return 0;
}

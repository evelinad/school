#include<stdio.h>
#include<string.h>
#include<stdlib.h>
#include<vector>
#include<iostream>
#include<math.h>
using namespace std;
#define oo 65000
#define MAX(a,b) ((a)>(b))?a:b
#define A 0
#define B 1

/*definire variabile*/
int Pmin, Pmax, n;

int ** vresource;
int **vprice;
int **vbudget;
int **vcost;
int **vcostRes;

int T;
int nrColA, nrColB;     
int maxPriceA, maxPriceB;
FILE *fout;

void read_data(const char *);
void dostuff(const char *);
int minimum_cost(int,int,bool); 
void init();
/* 
prelucrare matrici pt anul 0

*/
void init()
{
 int i,j;
    nrColA=nrColB=0;
    maxPriceA=maxPriceB=-oo;

    for(i=0;i<n;i++)
      for(j=0;j<n;j++)
      {
        /*
        determin valorile minime pentru vcost[i][j], vcostRes[i][j]
        cati colonisti sunt cu resursa A sau B
        pretul maxim pt resursa A sau B
        */
    	vcost[i][j]=oo;
        vcostRes[i][j] = oo;
        for(int i1=0;i1<n ;i1++)
        {
        for (int j1=0;j1<n ;j1++)

            {
                int val;
                if(vresource[i1][j1]==1-vresource[i][j])
                {
                val = vprice[i1][j1]  + (int)(abs(i-i1)+abs(j-j1));
                vcost[i][j] = (vcost[i][j]>val)?val:vcost[i][j];
                }
                if(vresource[i1][j1]==vresource[i][j])
                {
                val = vprice[i1][j1]  + (int)(abs(i-i1)+abs(j-j1));
                vcostRes[i][j] = (vcostRes[i][j]>val)?val:vcostRes[i][j];
                }
             }
        }

        if(vresource[i][j] == 0)
        {
            nrColA+=1;
          if(maxPriceA<vprice[i][j]) maxPriceA=vprice[i][j];

        }
        else
           {
                nrColB+=1;
                if(maxPriceB<vprice[i][j])maxPriceB=vprice[i][j];
           }
           
           

  


        }

         for(i=0;i<n;i++)
        for(j=0;j<n;j++)
        {
            /*
            actualizez valorile pt anul urmator
            */
            // punctul 2
            if(vcost[i][j]>vbudget[i][j]) 
            {

            vprice[i][j]=vprice[i][j]+(vcost[i][j]-vbudget[i][j]);
            vbudget[i][j]=vcost[i][j];

            if(vprice[i][j]>Pmax)
            {
           
                    vresource[i][j]=1-vresource[i][j];
                    vbudget[i][j]=Pmax;
                    vprice[i][j]=(Pmin+Pmax)/2;
                 
            }

            }
            //punctul 3
            else
            if(vcost[i][j]<vbudget[i][j])
            {

            vprice[i][j]+=(vcost[i][j]-vbudget[i][j])/2;
            vbudget[i][j] = vcost[i][j];
            if(vprice[i][j]<Pmin)
            {
                vprice[i][j]=(vprice[i][j]>Pmin)?vprice[i][j]:Pmin;
            }

            }
            //punctul 4
            else
            if(vcost[i][j]==vbudget[i][j])
            {
                vbudget[i][j]=vcost[i][j];
                vprice[i][j]=vcostRes[i][j] + 1;
                if(vprice[i][j]>Pmax)
                   {
           
                    vresource[i][j]=1-vresource[i][j];
                    vbudget[i][j]=Pmax;
                   vprice[i][j]=(Pmin+Pmax)/2;
                 
                    }

                
            }
            
            
            
        }   


    
    
}



/*
    metoda determina valorile maxime pentru pretul resurselor de tip A si B
    si numarul colonistilor pentru fiecare resursa pentru anul T
*/
void finalize()
{
    int i,j;
    nrColA=nrColB=0;
    maxPriceA=maxPriceB=-oo;

    for(i=0;i<n;i++)
      for(j=0;j<n;j++)
      {
         /*
        determin valorile minime pentru vcost[i][j], vcostRes[i][j]
        cati colonisti sunt cu resursa A sau B
        pretul maxim pt resursa A sau B
        */
    	vcost[i][j]=oo;
          vcostRes[i][j] = oo;
        for(int i1=0;i1<n ;i1++)
        {
        for (int j1=0;j1<n ;j1++)

            {
                int val;
                if(vresource[i1][j1]==1-vresource[i][j])
                {
                val = vprice[i1][j1]  + (int)(abs(i-i1)+abs(j-j1));
                vcost[i][j] = (vcost[i][j]>val)?val:vcost[i][j];
                }
                  if(vresource[i1][j1]==vresource[i][j])
                {
                val = vprice[i1][j1]  + (int)(abs(i-i1)+abs(j-j1));
                vcostRes[i][j] = (vcostRes[i][j]>val)?val:vcostRes[i][j];
                }
             }
        }

        if(vresource[i][j] == 0)
        {
            nrColA+=1;
          if(maxPriceA<vprice[i][j]) maxPriceA=vprice[i][j];

        }
        else
           {
                nrColB+=1;
                if(maxPriceB<vprice[i][j])maxPriceB=vprice[i][j];
           }





        }

    

	/*
	spre deosebire de metoda dostuff, nu mai recalculez valorile din matrici 
	pt anul viitor
	*/

    /*
    scriu in fisierul de output numarul de colonisti 
    + valorile maxime pt fiecare resursa
    */
    fprintf(fout,"%d %d %d %d\n",nrColA,maxPriceA,nrColB,maxPriceB);
}


/*
    metoda prelucreaza matricile vprice, vbudget, vcost, vcostRes pentru 
    anii de la 0 la T-1
*/
void dostuff(const char *filename)
{
init();
fout=fopen(filename, "w+");
int year=0;
int i,j;

string str="";
char aux[100];

for(year=1;year<T;year++)
{
    nrColA=nrColB=0;
    maxPriceA=maxPriceB=-oo;
    for(i=0;i<n;i++)
    {
        
      for(j=0;j<n;j++)
    {
     /*
        determin valorile minime pentru vcost[i][j], vcostRes[i][j]
        cati colonisti sunt cu resursa A sau B
        pretul maxim pt resursa A sau B
        */
        vcost[i][j]=oo;
        vcostRes[i][j] = oo;

  	for(int i1=0;i1<n ;i1++)
        {
        for (int j1=0;j1<n ;j1++)

            {
                int val;
                if(vresource[i1][j1]==1-vresource[i][j])
                {
                val = vprice[i1][j1]  + (int)(abs(i-i1)+abs(j-j1));
                vcost[i][j] = (vcost[i][j]>val)?val:vcost[i][j];
                }
                if(vresource[i1][j1]==vresource[i][j])
                {
                val = vprice[i1][j1]  + (int)(abs(i-i1)+abs(j-j1));
                vcostRes[i][j] = (vcostRes[i][j]>val)?val:vcostRes[i][j];
                }
             }
    	}

        if(vresource[i][j] == 0)
        {
		

            nrColA+=1;
          if(maxPriceA<vprice[i][j]) maxPriceA=vprice[i][j];
            
        }
        else
           {
                nrColB+=1;
                if(maxPriceB<vprice[i][j])maxPriceB=vprice[i][j];
           }
            
            
                  
        }

    }
     for(i=0;i<n;i++)
        for(j=0;j<n;j++)
        {
            /*
             actualizez valorile pt anul urmator
            */
            
            // punctul 2
            if(vcost[i][j]>vbudget[i][j]) 
            {

            vprice[i][j]=vprice[i][j]+(vcost[i][j]-vbudget[i][j]);
            vbudget[i][j]=vcost[i][j];

            if(vprice[i][j]>Pmax)
            {
           
                    vresource[i][j]=1-vresource[i][j];
                    vbudget[i][j]=Pmax;
                    vprice[i][j]=(Pmin+Pmax)/2;
                 
            }

            }
            //punctul 3
            else
            if(vcost[i][j]<vbudget[i][j])
            {

            vprice[i][j]+=(vcost[i][j]-vbudget[i][j])/2;
            vbudget[i][j] = vcost[i][j];
            if(vprice[i][j]<Pmin)
            {
                vprice[i][j]=(vprice[i][j]>Pmin)?vprice[i][j]:Pmin;
            }

            }
            //punctul 4
            else
            if(vcost[i][j]==vbudget[i][j])
            {
                vbudget[i][j]=vcost[i][j];
                vprice[i][j]=vcostRes[i][j] + 1;
                if(vprice[i][j]>Pmax)
                   {
           
                    vresource[i][j]=1-vresource[i][j];
                    vbudget[i][j]=Pmax;
                   vprice[i][j]=(Pmin+Pmax)/2;
                 
                    }

                
            }
            
            
            
        }   
    /*
    scriu in fisierul de output numarul de colonisti 
    + valorile maxime pt fiecare resursa
    */
    fprintf(fout,"%d %d %d %d\n",nrColA,maxPriceA,nrColB,maxPriceB);



}
/*
 actualizari pentru anul T
*/
finalize();
    /*
    retin solutia pentru versource[i][j],vprice[i][j], vbudget[i][j]
    intr-un string si il afisez pe urma in fisierul de output
    */
   for(i=0;i<n;i++)
    
   
{
    for(j=0;j<n;j++)
    {

        sprintf(aux, "(%d,%d,%d)",vresource[i][j],vprice[i][j],vbudget[i][j]);
        str+=aux;
        if(j<n-1) str+=" ";
    }
    
    if(i<n-1)str+="\n";
}

fprintf(fout,"%s",str.c_str());

fclose(fout);

}


/*
  metoda citeste datwle din fisier
*/

void read_data(const char *filename)
{

FILE * fin;

fin=fopen(filename,"r");

fscanf(fin,"%d %d %d\n\n", &Pmin, &Pmax, &n);
int i,j;
int l;
/* alocare memorie*/
vcost=(int **)calloc(n+1,sizeof(int *));
vcostRes=(int **)calloc(n+1,sizeof(int *));
vresource=(int **)calloc(n+1,sizeof(int *));
vbudget=(int **)calloc(n+1,sizeof(int *));
vprice=(int **)calloc(n+1,sizeof(int *));
/*citesc matricea de resourse*/
for(i=0;i<n;i++)	
	{
        vcost[i]=(int *)calloc(n+1,sizeof(int));
        vcostRes[i]=(int *)calloc(n+1,sizeof(int));
        vresource[i]=(int *)calloc(n+1,sizeof(int));
	    for(j=0;j<n;j++)
	    {  
            fscanf(fin,"%d", &l);
            vresource[i][j]=l;		
	    }

	}
fscanf(fin,"\n\n");
/*citesc matricea de preturi*/
for(i=0;i<n;i++)
{
        vprice[i]=(int *)calloc(n+1,sizeof(int));
       
	    for(j=0;j<n;j++)
	    {  
            fscanf(fin,"%d", &l);
            vprice[i][j]=l;
		
	    }


}


fscanf(fin,"\n\n");
/*citesc matricea de bugete*/
for(i=0;i<n;i++)
{

        vbudget[i]= (int *)calloc(n+1,sizeof(int));
	    for(j=0;j<n;j++)
	    {  
            fscanf(fin,"%d", &l);
            vbudget[i][j] =l;
		
	    }


}

fclose(fin);


}
/*
    Main
*/
int main(int argc , char **argv)
{

T=atoi(argv[1]);    
/*citire date*/
read_data(argv[2]);
/* prelucrari date*/
dostuff(argv[3]);

return 0;
}

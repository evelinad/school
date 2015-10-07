//DUMITRESCU EVELINA 331CA
//TEMA4 APD
#include<mpi.h>
#include<stdio.h>
#include<iostream>
#include<fstream>
#include<string>
#include <time.h>
#include<stdlib.h>
#include <string>
#include <sstream>
#include<vector>
#include <map>
#define PROBE 1
#define ECHO 2
#define EMPTY_ECHO 3
#define LEADER_ELECTION 4
#define RESULT 5
#define COUNT_MESS 6
#define MESS 7
#define PORTS 8

#define LENGTH 200

#define BLOCKED 0

#define FINISHED 1
#define NOT_FINISHED 0


using namespace std;
//matrice de topologie din fisier intrare
int **topology;
//matrice de topologie pt fiecare nod
int **m;
//matrice buffer
int **recvm;
//matrice pt 'porturi' blocked/unblocked
int **mports;
//matrice buffer
int **recvmports;
//rank + nr procese
int rank, size;
// nr de mesaje al fiecarui nod
int num_mess;
//parintele nodului
int parent = -1;
int gata=0;
//variabila pt a marca alegerea primului + al doilea lider ales
int first_election = NOT_FINISHED;
int second_election = NOT_FINISHED;

//proturile blocate/de forwarding ; principui de functionare al STP
int *ports;
int *fin;
//next hopurile pentru fiecare nod
int *next_hop;

MPI_Status stat;
int nr;

//structura pentru a memora mesajele din fisiere
struct message 
{
    char dest;
    char *m;
};

//vector pentru stocarea mesajelor din fisierul de intrare
vector <vector<message >  >mess;
//mesajele pe care fiecare nod trebuie sa le trasmita mai departe
vector<message   >node_mess;


/*
====================================================================
    Metode pentru logging support
====================================================================
*/
void afisare(int n, int **topology)
{
    for(int i=0;i<n;i++)
        {
            for(int j=0;j<n;j++)
                cout<<topology[i][j]<< " ";
            cout<<"\n";
         }    
         cout<<endl<<endl;
}

void afisare2(int rank,char message[100],int n, int **topology)
{
char bla[10];
sprintf(bla,"node%d", rank);

FILE *f=fopen(bla, "a+");
fprintf(f,"%s",message);
    for(int i=0;i<n;i++)
        {
            for(int j=0;j<n;j++)
                fprintf(f,"%d ",topology[i][j]);
            fprintf(f,"\n");
         }    
         fprintf(f, "\n");
         fclose(f);
}

void afisare3(int rank,char message[100],int n, int *topology)
{
char bla[10];
sprintf(bla,"node%d", rank);

FILE *f=fopen(bla, "a+");
fprintf(f,"%s",message);
    
 for(int j=0;j<n;j++)
                fprintf(f,"%d ",topology[j]);
            fprintf(f,"\n");
           
fprintf(f, "\n");
         fclose(f);
}


void afisare4(int rank,char message[100])
{
char bla[10];
sprintf(bla,"node%d", rank);
FILE *f=fopen(bla, "a+");
fprintf(f,"%s\n",message);           
fclose(f);

}
/*
====================================================================
    Metode pentru alocare continua a unei matrici in memorie
====================================================================
*/
int malloc2d(int ***array, int n, int m) {

    /* allocate the n*m contiguous items */
    int *p = (int *)calloc(n*m,sizeof(int));
    if (!p) return -1;

    /* allocate the row pointers into the memory */
    (*array) = (int **)calloc(n,sizeof(int*));
    if (!(*array)) {
       free(p);
       return -1;
    }

    /* set up the pointers into the contiguous memory */
    for (int i=0; i<n; i++) 
       (*array)[i] = &(p[i*m]);

    return 0;
}
/*
====================================================================
    Metoda determina numarul de noduri din topologie
====================================================================
*/
int countlines(char filename[20])
{
int nr=0;
string line;
ifstream topology_file(filename);
while(getline(topology_file, line)) nr++;
topology_file.close();
return nr;
}
/*
====================================================================
    leader election process
====================================================================
*/
void election()
{
int max;
int max1=-1;//valoarea primului lider
max = max1;
int leader;
//cele 2 runde
for(int round = 0;round<2;round++)
{
//trimit la toate nodurile ca alegerile inca nu s-au facut
first_election = NOT_FINISHED;
char ceva[100];
MPI_Bcast(&first_election,1,MPI_INT,0,MPI_COMM_WORLD);
if(rank == 0)
{
int * candidates = (int *)calloc(nr, sizeof(int));
int vote=rank;
while(1)
{
vote = rank;
//primesc voturile nodurilor fii
for(int i=0;i<nr;i++)   
    if(ports[i] == 1)
    {
    int * buffer = (int *)calloc(nr, sizeof(int));
    MPI_Recv(buffer, nr, MPI_INT,i, LEADER_ELECTION,MPI_COMM_WORLD,&stat);
    sprintf(ceva,"am primit de la %d lista cu voturile candidatilor\n",i);
    afisare3(rank,ceva,nr,buffer);
    //adun rezultatele
    for(int j=0;j<nr;j++)
        candidates[j]+=buffer[j];
    }
        
//un nod nu se poate vota pe sine
while(vote == rank)
        {
            srand ( time(NULL) );
            vote = rand()%nr;
        }
sprintf(ceva, "am ales pe %d\n", vote);        

afisare4(rank,ceva);
//adun votul nodului rank
candidates[vote] ++;
sprintf(ceva,"lista cu voturile candidatilor a mea este: \n");
afisare3(rank,ceva,nr,candidates);
max = -1;
int n=0;
//determin leaderul
for(int i=0;i<nr;i++)
{
    if(max<candidates[i])
    {
        max=candidates[i];
        n=1;
        leader = i;
        
    }
    else
    if(max == candidates[i])
    {
        n++;
    }
  }
//sunt 2 sau mai multi candidati cu nr de voturi maxime egale 
if(n!=1 || (max1 == max && round == 1))
{
//    cout<<max1<<"   "<<max<<endl;
    if(n!=1)
    afisare4(rank,"mai multi candidati cu numar de voturi maxime egale, se repeta runda\n");
    else 
    afisare4(rank,"leader 1 este la fel cu leader 2, se repeta runda\n");
    first_election = NOT_FINISHED;
    //trimit mesaje ca nu s-a terminat alegerea
    for(int i=0;i<nr;i++)
        if(m[rank][i] == 1)
        {
            MPI_Send(&first_election, 1, MPI_INT,i,RESULT,MPI_COMM_WORLD);  
            sprintf(ceva,"trimit mesaj NOT_FINISHED catre nodul %d\n",i);
            afisare4(rank,ceva);
  
        }
}
else 
{
if(round == 0)
{
max1 = max;
sprintf(ceva,"am ales leaderul %d  pentru etapa %d\n",leader, round+1);
afisare4(rank,ceva);
}

else
{
sprintf(ceva,"am ales leaderul %d  pentru etapa %d\n",leader, round+1);
afisare4(rank,ceva);

}
//trimit mesaje ca s-a terminat alegerea
first_election = FINISHED;
for(int i=0;i<nr;i++)
    if(m[rank][i] == 1)
    {
        MPI_Send(&first_election, 1, MPI_INT,i,RESULT,MPI_COMM_WORLD);  
        sprintf(ceva,"trimit mesaj FINISHED catre nodul %d\n",i);
        afisare4(rank,ceva);
  
        
    }
break;
}
}    
    
    
    
}
else
{
//rank!=0
int * candidates = (int *)calloc(nr, sizeof(int));
int vote=rank;
while(1)
{
vote = rank;
//astept voturile de la fii
for(int i=0;i<nr;i++) 
      
    if(ports[i] == 1 && parent!=i)
    {
    int * buffer = (int *)calloc(nr, sizeof(int));
//    cout<<"rank "<<rank<<" vecin cu "<<i<<endl;
    MPI_Recv(buffer, nr, MPI_INT,i, LEADER_ELECTION,MPI_COMM_WORLD,&stat);
   sprintf(ceva,"am primit de la %d lista cu voturile candidatilor\n",i);
    afisare3(rank,ceva, nr, buffer);
    for(int j=0;j<nr;j++)
        candidates[j]+=buffer[j];
    }
//cat timp nodul ales este diferit de nodul curent
while(vote == rank)
        {
            srand ( time(NULL) );
            vote = rand()%nr;
        }
sprintf(ceva,"am ales%d\n",vote);
afisare3(rank,ceva, nr, candidates);   
candidates[vote] ++;
sprintf(ceva,"lista cu voturile candidatilor a mea este: \n");
afisare3(rank,ceva,nr,candidates);   
//trimit lista nodului parinte
MPI_Send(candidates, nr, MPI_INT,parent,LEADER_ELECTION,MPI_COMM_WORLD);   
sprintf(ceva,"o trimit nodului parinte %d \n",parent);
afisare4(rank,ceva);
//astept mesajul de la parinte de finalizare/reluare a alegerilor
MPI_Recv(&first_election,1, MPI_INT,parent, RESULT,MPI_COMM_WORLD,&stat);
if(first_election == FINISHED)
    sprintf(ceva,"am primit de la nodul parinte %d mesaj de finalizare a alegerilor %d\n",parent, FINISHED);
else
    sprintf(ceva,"am primit de la nodul parinte %d mesaj de reluare a alegerilor %d\n",parent, NOT_FINISHED);
afisare4(rank,ceva);
//trimit mai departe decizia
for(int i=0;i<nr;i++)
    if(ports[i] == 1 && parent!=i)
    {
    MPI_Send(&first_election, 1, MPI_INT,i,RESULT,MPI_COMM_WORLD);  
    sprintf(ceva,"trimit decizia catre %d\n",i);
    afisare4(rank,ceva);

    }
//mesaj de finalizare => break    
if(first_election == FINISHED) break;

}
}
MPI_Barrier(MPI_COMM_WORLD);

}
}
/*
====================================================================
    Constructie topologie noduri 
    Fiecare nod i cunoaste initial doar  linia topology[i]
====================================================================
*/
void build_topology()
{
char ceva[100];
if(rank == 0)    
{
   

    //trimit mesaje sonda
    for(int i=0;i<nr;i++)
        if(m[rank][i] == 1)
         {
      
             MPI_Send(&rank,1,MPI_INT,i,PROBE,MPI_COMM_WORLD);
             sprintf(ceva, "trimit mesaj PROBE catre %d \n", i);
             afisare4(rank,ceva);
             
         }        
  //astept raspunsurile de la nodurile adiacente
    for(int i=0;i<nr;i++)
        if(m[0][i] == 1)
        {

        MPI_Recv(&(recvm[0][0]), nr * nr, MPI_INT,i, MPI_ANY_TAG,MPI_COMM_WORLD,&stat);
        //ECHO message
        if(stat.MPI_TAG == ECHO)
        {
            for(int j = 0;j<nr;j++)
                for(int k=0;k<nr;k++)
                    m[j][k] = (m[j][k] | recvm[j][k]);
            sprintf(ceva , "am primit mesaj ECHO de la %d\n",i);
            afisare4(rank,ceva);        
        } 
        //EMPTY_ECHO message
        else 
        {
            ports[i] = BLOCKED;  
            sprintf(ceva, "am primit EMPTY_ECHO de la %d => blochez portul \n", i);
            afisare4(rank, ceva);
            
         }   
        }     
   
   afisare2(rank,"topologia finala: \n",nr, m);  
   gata = 1;

         
}
else
{
int aux;
char message [100];
//am primit un mesaj sonda
MPI_Recv(&aux,1,MPI_INT,MPI_ANY_SOURCE,PROBE,MPI_COMM_WORLD, &stat);
sprintf(ceva, "am primit mesaj PROBE de la %d\n", aux);
afisare4(rank,ceva);

//parintele devine primul nod de la care s-a primit mesaj
if(parent==-1)parent= aux;
/*else
{
    cout<<" trimit ecou vid parintele meu e "<<parent<<" am primit si "<<aux<<endl;
   MPI_Send(&(m[0][0]),nr*nr,MPI_INT,stat.MPI_SOURCE,EMPTY_ECHO,MPI_COMM_WORLD);
   ports[stat.MPI_SOURCE] = BLOCKED;
    
}*/

for(int i=0;i<nr;i++)
    {
    //trimit mesaje sonda catre nodurile direct conectate diferite de parinte
    if(m[rank][i] == 1 && parent!=i)
    {
       MPI_Send(&rank,1,MPI_INT,i,PROBE,MPI_COMM_WORLD);
       sprintf(ceva, "am trimis mesaj PROBE catre %d\n", i);
       afisare4(rank,ceva);

        
    }
        
    }

for(int i=0;i<nr;i++)
    if(m[rank][i] == 1 && parent !=i)
    {
        MPI_Recv(&(recvm[0][0]),nr*nr, MPI_INT, i,MPI_ANY_TAG,MPI_COMM_WORLD,&stat);
        // am primit mesaj ecou
        if(stat.MPI_TAG == ECHO)
        {
        sprintf(ceva, "am primit mesaj ECHO de la %d\n", i);
        afisare4(rank,ceva);

        for(int j=0;j<nr;j++)
            for(int k=0;k<nr;k++)
                m[j][k] = (m[j][k] | recvm[j][k]);
        }
       //am primit ecou vid 
       else 
       { 
        
          ports[i] = BLOCKED;
          sprintf(ceva, "am primit mesaj EMPTY_ECHO de la %d => blochez portul\n", aux);
          afisare4(rank,ceva);

       }
    }
//trimit matricea catre parinte    
MPI_Send(&(m[0][0]), nr *nr, MPI_INT,parent,ECHO,MPI_COMM_WORLD);    
sprintf(ceva, "am trimis mesaj ECHO catre nod parinte %d cu matricea mea finala\n", aux);
afisare4(rank,ceva);

}
MPI_Barrier(MPI_COMM_WORLD);
//pentru a scoti timp trimit direct topologia formata de nodul 0 
//catre toate nodurile
MPI_Bcast(&(m[0][0]),nr*nr ,MPI_INT,0,MPI_COMM_WORLD);
MPI_Barrier(MPI_COMM_WORLD);
char messg[100];
sprintf(messg, "am primit matrice finala: \n");
afisare2(rank,messg,nr, m);
afisare3(rank, "\nvectorul de porturi\n",nr, ports);
//determin AMA din topologie necesar transmiterii de mesaje
//mecanism similar celui de stabilire a topologiei
if(rank == 0)
{   
    for(int i =0 ;i<nr;i++)
        if(m[rank][i]  == 1)
            {
                MPI_Recv(&(recvmports[0][0]),nr*nr,MPI_INT,i,PORTS,MPI_COMM_WORLD, &stat);
                for(int j=0;j<nr;j++)
                    for(int k=0;k<nr;k++)
                        mports[j][k] = (mports[j][k] | recvmports[j][k]);
            }
}
else
{
    for(int i=0;i<nr;i++)
    if(m[rank][i] == 1 && i!=parent && ports[i] == 1)
     {
      MPI_Recv(&(recvmports[0][0]),nr*nr,MPI_INT,i,PORTS,MPI_COMM_WORLD, &stat);
      
    }
    for(int j=0;j<nr;j++)
                    for(int k=0;k<nr;k++)
                        mports[j][k] = (mports[j][k] | recvmports[j][k]);

    MPI_Send(&(mports[0][0]), nr *nr, MPI_INT,parent,PORTS,MPI_COMM_WORLD);  
}        
afisare2(rank, "matricea AMA pentru transmiterea de mesaje:\n",nr, mports);
}


/*
====================================================================
    Metoda prelucreaza datele din fisiere
====================================================================
*/
void initialize_data(char *file1, char* file2)
{
string line;
ifstream topology_file(file1);
ifstream messages_file(file2);
//procesul cu rank 0 determina numarul de noduri din topologie
if(rank == 0)nr=countlines(file1);
//transmite restul nodurilor
MPI_Bcast(&nr, 1, MPI_INT, 0, MPI_COMM_WORLD);
char ceva[100];
sprintf(ceva, "numar total de noduri %d\n", nr);
afisare4(rank,ceva);
//aloc memorie pentru structuri de date
malloc2d(&topology,nr,nr);
malloc2d(&recvm,nr,nr);
malloc2d(&m,nr,nr);
malloc2d(&mports,nr,nr);
malloc2d(&recvmports,nr,nr);
ports=(int *)calloc(nr, sizeof(int));
fin=(int *)calloc(nr, sizeof(int));


if(rank == 0)
{
for(int i=0;i<nr;i++)
{   vector<message> vect;
   mess.push_back(vect);
}
//citesc fisierul de topologie
nr = 0;
while(getline(topology_file, line))
{
   
    char *pstr;
    char str[100];
    strcpy(str, line.c_str());
    pstr = strtok (str," ");
    pstr = strtok (NULL, " ");
    while (pstr != NULL)
    {

        topology[nr][atoi(pstr)]=1;        
        pstr = strtok (NULL, " ");
        
    } 
   nr ++;
    
}

topology_file.close();

}

//trimit matricile initiale catre toate nodurile
MPI_Bcast(&(topology[0][0]),nr*nr ,MPI_INT,0,MPI_COMM_WORLD);
MPI_Bcast(&(recvm[0][0]),nr* nr,MPI_INT,0,MPI_COMM_WORLD);
MPI_Bcast(&(m[0][0]),nr * nr,MPI_INT,0,MPI_COMM_WORLD);
MPI_Bcast(&(recvmports[0][0]),nr* nr,MPI_INT,0,MPI_COMM_WORLD);
MPI_Bcast(&(mports[0][0]),nr * nr,MPI_INT,0,MPI_COMM_WORLD);

char mes[100];
sprintf(mes,"%d\n", rank);
afisare2(15,mes,nr, topology);
//fiecare nod stie initial doar de vecinii direct conectati/ linia asociata lui din matrice
for (int i=0;i<nr;i++)
{
    m[rank][i]=topology[rank][i];
    ports[i]=m[rank][i];
    mports[rank][i] = ports[i];
}

afisare2(rank, "initial topologia este:\n", nr,m);
//prelucrez fisierul de mesaje
if(rank == 0)
{
int num_mess;
messages_file>>num_mess;
getline(messages_file, line);
int s;
char d;
string mesg;
for(int i=0;i<num_mess;i++)
{
    string strmesg="";
    getline(messages_file, line);
   
   istringstream  iss(line);
   iss>>mesg;
   s = atoi(mesg.c_str());
   iss>>mesg;
   
   d= (char)atoi(mesg.c_str()) + '0';
    if(strcmp(mesg.c_str(), "B") ==0) d = 'B';
   while(iss>>mesg)strmesg+=" "+mesg;
   
   message  m;
   m.dest=d;
   m.m=strdup(strmesg.c_str());
   //adaug mesajul in vectorul de mesaje pe pozitia corespunzatoare nodului sursa s
   mess[s].push_back(m) ;

}
messages_file.close();


int aux;
//distribui mesajele din vectorul mess fiecarui nod 
char *mesaj=(char *)calloc(LENGTH, sizeof(char));
for(int i=0;i<mess.size();i++)
    {
        //trimit mai intai cate mesaje va avea nodul de transmis
        aux=mess[i].size();
        MPI_Send(&aux,1,MPI_INT,i,COUNT_MESS,MPI_COMM_WORLD);
        //trimit pe rand mesajele 
        // mesaj[0] resprezinta destinatia mesaj[1 .. n] mesajul propriu-zis
        for(int j=0;j<mess[i].size();j++)
        {
                memset(mesaj,'\0',LENGTH);
                mesaj[0]=mess[i][j].dest;
                strcat(mesaj,mess[i][j].m);
                MPI_Send(mesaj,LENGTH,MPI_CHAR,i,MESS,MPI_COMM_WORLD);
              
        }
  }
}


// am primit numarul de mesaje
MPI_Recv(&num_mess,1,MPI_INT,0,COUNT_MESS,MPI_COMM_WORLD,&stat);
sprintf(ceva, "am de transmis %d mesaje :\n", num_mess);
afisare4(rank, ceva);
char *mesaj;
mesaj=(char *)calloc(LENGTH, sizeof(char));
//astept pe rand mesajele 
for(int i=0;i<num_mess;i++)
    {
      memset(mesaj,'\0',LENGTH);
      MPI_Recv(mesaj,LENGTH,MPI_CHAR,0,MESS,MPI_COMM_WORLD,&stat);
      afisare4(rank, mesaj);
      message  m;
      m.dest=mesaj[0];
      m.m=strdup(mesaj+1);
      //adaug mesajele in vectorul corespunzator fiecarui nod
      node_mess.push_back(m);

    }
afisare4(rank, "\n");
}

/*
====================================================================
    algoritm BFS pt stabilirea next hopurilor
====================================================================
*/
void BFS()
{
next_hop=(int *)calloc(nr, sizeof(int));
int *hop_counts_min=(int *)calloc(nr, sizeof(int));
for(int i=0;i<nr;i++)
    hop_counts_min[i] = 100;

for(int j=0;j<nr;j++)
{
if(m[rank][j] == 1)
{

int *queue=(int *)calloc(nr, sizeof(int));
int *s=(int *)calloc(nr, sizeof(int));
int *hop_counts=(int *)calloc(nr, sizeof(int));
for(int i=0;i<nr;i++) 
    hop_counts[i] = 1;
int ic=0;
int sf=0;
queue[ic]= j;
hop_counts[ic] = 0;
int i;
s[j] =1;

while(ic<=sf)
{
    i=0;
    while(i<nr)
    {
        if(m[queue[ic]][i] == 1 &&s[i] == 0)
        {
            sf++;
            queue[sf] = i;
            s[i] = 1;
            hop_counts[i] = hop_counts[queue[ic]] + 1;
        }
        i++;
    }
    ic++;
}
//aleg ruta cu numar minim de hopuri
for(i=0;i<nr;i++)
{
if(hop_counts_min[i]>hop_counts[i]) 
{
    hop_counts_min[i]= hop_counts[i];
    next_hop[i]=j;
}

}
hop_counts_min[rank] = 0;
next_hop[rank] = -1;
free(queue);
free(s);
free(hop_counts);
}
}


}
/*   
================================================================
    metoda trimite mesajele de la nodurile sursa la nodul radina 0
    si mai departe la nodurile destinatie urmand arborele AMA 
    din mports
================================================================
*/
void send_messages()
{
//rulez bfs pt fiecare nod pt a determina next hopurile
BFS();
afisare3(rank, "next hop:\n",nr, next_hop);
int n;
char *mesaj = (char *) calloc(LENGTH,sizeof(char));
//astept sa primesc de la toate nodurile fii numarul de mesaje
// si apoi mesajele 
for(int i=0;i<nr;i++)
    if(mports[rank][i] ==  1 && i != parent)
    {
        n=0;
        MPI_Recv(&n,1,MPI_INT,i,COUNT_MESS,MPI_COMM_WORLD,&stat);        
        num_mess+=n;
        for(int j=0;j<n;j++)
        {
             memset(mesaj, 0, LENGTH);   
             MPI_Recv(mesaj,LENGTH,MPI_CHAR,i,MESS,MPI_COMM_WORLD,&stat);
             message m;
             m.dest=mesaj[0];
             m.m=strdup(mesaj+1);
             node_mess.push_back(m);
        }
    }

//rank diferit de parinte
if(rank !=0)
{
    //trimit numarul de mesaje
    MPI_Send(&num_mess,1,MPI_INT,parent,COUNT_MESS,MPI_COMM_WORLD);
    //trimit pe rand mesajele
    for(int i=0;i<node_mess.size();i++)
    {
        memset(mesaj, 0, LENGTH);
        mesaj[0]=node_mess[i].dest;
        strcat(mesaj+1,node_mess[i].m);
        MPI_Send(mesaj,LENGTH, MPI_CHAR,parent,MESS,MPI_COMM_WORLD);
     }   
}
MPI_Barrier(MPI_COMM_WORLD);
char ceva[100];
//nodul root trimite pe arborele AMA mesajele la
//nodurile destinatie
if(rank == 0)
 {
for(int i=0;i<node_mess.size();i++)
    {
       memset(mesaj, 0, LENGTH);
         mesaj[0]=node_mess[i].dest;
        strcat(mesaj+1,node_mess[i].m);
        //mesaj de broadcast => trimit pe tot arborele
      if(mesaj[0]=='B')
      {

     for(int j=0;j<nr;j++)
        //daca portul nu este BLOCKED 
        if(mports[rank][j] == 1)
        {
      sprintf(ceva, "trimit mesajul broadcast %s catre nodul %d\n",mesaj+1,j);
      afisare4(rank, ceva);
      MPI_Send(mesaj,LENGTH, MPI_CHAR,j,MESS,MPI_COMM_WORLD);  
      }
      }
      //mesaj normal => il trimit catre next hop
      else
      {
      
      int dest =    next_hop[mesaj[0] - '0'];
      sprintf(ceva, "trimit mesajul  %s cu destinatia %d catre nodul %d\n",mesaj+1,mesaj[0]-'0',dest);
      afisare4(rank, ceva);  
      MPI_Send(mesaj,LENGTH, MPI_CHAR,dest,MESS,MPI_COMM_WORLD);  
      
  
}
    }
    //am terminat de trimis mesajele => trimit FINISHED
    for(int j=0;j<nr;j++)
          if(mports[rank][j] == 1)
            {
             MPI_Send(mesaj,LENGTH, MPI_CHAR,j,FINISHED,MPI_COMM_WORLD);  
             sprintf(ceva, "trimit mesaj de tip FINISHED catre nodul %d\n",j);
             afisare4(rank, ceva);  
            } 
             
    }
else
{
   //cat timp nu am primit finished
    while(1)
    {
      memset(mesaj, 0, LENGTH);      
      MPI_Recv(mesaj,LENGTH,MPI_CHAR,parent,MPI_ANY_TAG,MPI_COMM_WORLD,&stat);
      
      // am primit mesajd e tip finished
      if(stat.MPI_TAG == FINISHED)
      { 
        // il trimit mai departe pe arborele AMA
       for(int i=0;i<nr;i++)
        if(mports[rank][i] == 1 && i!=parent)
        {     
              MPI_Send(mesaj,LENGTH, MPI_CHAR,i,FINISHED,MPI_COMM_WORLD); 
              sprintf(ceva, "trimit mesaj de FINISHED catre nodul %d\n",i);
              afisare4(rank, ceva);   
      }
      sprintf(ceva, "am primit mesaj FINISHED de la %d\n",parent);
      afisare4(rank, ceva);  
      //ies din bucla
      break;
      }
      //mesaj de broadcast => il dau mai departe la toti vecinii  
      if(mesaj[0] == 'B')
      {
  
      for(int i=0;i<nr;i++)
        if(mports[rank][i] == 1 && i!=parent)
        {
         sprintf(ceva, "trimit mesaj de broadcast catre nodul %d\n",i);
         afisare4(rank, ceva); 
         MPI_Send(mesaj,LENGTH, MPI_CHAR,i,MESS,MPI_COMM_WORLD);  
       }     
    }
    //mesaj normal
    else
    {
        int dest = next_hop[mesaj[0] - '0'];
        //mesajul a ajuns la destinatie
        if(mesaj[0]-'0' == rank)
          {
          sprintf(ceva, "mesajul %s de la nod parinte %d  catre %d a ajuns la destinatie\n",mesaj+1,parent,rank);
          afisare4(rank, ceva); 
          }
          // il dau mai departe catre next hop     
          else
          {
           sprintf(ceva, "trimit mesajul %s cu destinatie catre nodul %d\n",mesaj+1,mesaj[0]-'0',dest);
           afisare4(rank, ceva); 
        MPI_Send(mesaj,LENGTH, MPI_CHAR,dest,MESS,MPI_COMM_WORLD);  
      } 
          
    } 
 }
 }
    
}
/*
====================================================================
====================================================================
*/

int main(int argc, char **argv)
{


MPI_Init(&argc, &argv);
MPI_Comm_rank(MPI_COMM_WORLD, &rank);
MPI_Comm_size(MPI_COMM_WORLD, &size);
afisare4(rank, "\n==========================\n INITIALIZE DATA \n==========================\n");
initialize_data(argv[1], argv[2]);
afisare4(rank, "\n==========================\n BUILD TOPOLOGY \n==========================\n");
build_topology();
afisare4(rank, "\n==========================\n SEND MESSAGES \n==========================\n");
send_messages();
afisare4(rank, "\n==========================\n LEADER ELECTION \n==========================\n");
election();
MPI_Finalize();
return 0;
}

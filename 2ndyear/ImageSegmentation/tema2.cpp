//DUMITRESCU EVELINA 321CA
#include<iostream>
#include<fstream>
#include<cmath>
#include<queue>
#include<ctime>
#include<vector>
#include<string>
#include<cmath>
#include<stdlib.h>
#include<algorithm>
#include<ctime>
#define PI 3.1415926153
#define DPI 6.2831853071
#define UMAX 10
#define oo 0x3f3f3f3f
#define NONE -3
#define MIN ( a < b ) ? a  : b
using namespace std;

/*
   structura ce defineste o imagine
*/
struct fileimg {
	char *header;
	int max;
	int w, h;
	 vector < vector < unsigned int > >value;

};

/*
  structura ce defineste datele pentru un nod din graf
*/
typedef struct data {

	int nod;
	double cap;
	int f;
} data;

/*
  structura ce defineste idul unui nod + lista de vecini
*/
struct node {
	int id;
	 vector < data > neigh;
};
node f;
node gg;
double miu_f, miu_b, sigma_f, sigma_b;
int card_f, card_b;
vector < node > graph_img;
double l, thresold;
int lastnode;
fileimg imagine;

/*
  metoda afiseaza nodurile si costurile muchiilor din graf
*/
void afisare_graph()
{
	FILE *f = fopen("tema.out", "w");

	int nr1, nr2;
	for (int i = 0; i < graph_img.size() - 2; i++) {
		fprintf(f, "nod: %d %d\n", i + 1,
			imagine.value[i / imagine.w][i % imagine.w]);
		for (int j = 0; j < graph_img.at(i).neigh.size(); j++) {
			nr1 = graph_img.at(i).neigh.at(j).nod + 1;
			fprintf(f, "v: %d, c: %f, f: 0.0\n", nr1,
				graph_img.at(i).neigh.at(j).cap);
		}

		fprintf(f, "\n");
	}
	fprintf(f, "sursa\n\n");
	for (int j = 0; j < graph_img.at(graph_img.size() - 2).neigh.size();
	     j++) {
		fprintf(f, "v: %d, c: %f, f: 0.0\n",
			graph_img.at(graph_img.size() - 2).neigh.at(j).nod + 1,
			graph_img.at(graph_img.size() - 2).neigh.at(j).cap);

	}
	fprintf(f, "\n");
	for (int j = 0; j < graph_img.at(graph_img.size() - 1).neigh.size();
	     j++) {
		fprintf(f, "v: %d, c: %f, f: 0.0\n",
			graph_img.at(graph_img.size() - 2).neigh.at(j).nod + 1,
			graph_img.at(graph_img.size() - 1).neigh.at(j).cap);

	}

	fclose(f);
}

/*
  metoda citeste continutul unei imagini si 
  creeaza o structura fileimg in care sunt memorate aceste date
*/
fileimg get_data(string filename)
{

	fileimg fi;
	FILE *f = fopen(filename.c_str(), "r");
	size_t n;
	int max;
	fi.header = (char *)calloc(100, sizeof(char));
	getline(&fi.header, &n, f);
	fscanf(f, "%d %d %d", &fi.w, &fi.h, &fi.max);
	unsigned int nr;
	for (int i = 0; i < fi.h; i++) {
		vector < unsigned int >vect;
		for (int j = 0; j < fi.w; j++) {

			fscanf(f, "%d", &nr);
			vect.push_back(nr);

		}
		fi.value.push_back(vect);
	}

	fclose(f);
	return fi;
}

/*
  metoda calculeaza sigma si miu
*/
void get_sigma_and_miu(fileimg mask_b, fileimg mask_f, fileimg imagine)
{

	for (int i = 0; i < imagine.h; i++)
		for (int j = 0; j < imagine.w; j++) {
			if (mask_b.value.at(i).at(j)) {
				miu_b += imagine.value.at(i).at(j);
				card_b += 1;

			}

			if (mask_f.value.at(i).at(j)) {
				miu_f += imagine.value.at(i).at(j);
				card_f += 1;

			}
		}

	miu_b = miu_b / card_b;
	miu_f = miu_f / card_f;
	for (int i = 0; i < imagine.h; i++)
		for (int j = 0; j < imagine.w; j++) {
			if (mask_b.value.at(i).at(j))
				sigma_b +=
				    (miu_b -
				     imagine.value.at(i).at(j)) * (miu_b -
								   imagine.
								   value.at(i).
								   at(j));
			if (mask_f.value.at(i).at(j))
				sigma_f +=
				    (miu_f -
				     imagine.value.at(i).at(j)) * (miu_f -
								   imagine.
								   value.at(i).
								   at(j));

		}

	sigma_b /= card_b;
	sigma_f /= card_f;

}

/*
  returneaza o noua structura data 
*/
data new_data(int nod, double cap)
{
	data d;
	d.nod = nod;
	d.cap = cap;
	return d;
}

/*
  metoda construitese graful imaginii
*/
void build_graph(fileimg imagine)
{

	double log_f = log(sqrt(DPI * sigma_f));
	double log_b = log(sqrt(DPI * sigma_b));
	gg.neigh.clear();
	int direction[4][2] = { {-1, 0}, {0, -1}, {0, 1}, {1, 0} };
	int ind_x, ind_y;
	int index;
	lastnode = imagine.w * imagine.h;
	data d1;
	data d2;
	double cap1;
	/*
	parcurg matricea de pixeli
	*/
	for (int i = 0; i < imagine.h; i++)
		for (int j = 0; j < imagine.w; j++) {
			index = i * imagine.w + j;
			node aux;
			graph_img.push_back(aux);
      /*
      calculez valoarea muchiei de la nod la drena
      */
			cap1 =
			    ((double)(imagine.value.at(i).at(j) - miu_b) *
			     (imagine.value.at(i).at(j) -
			      miu_b)) / (2L * sigma_b) + log_b;
			cap1 = (cap1 > 10) ? 10 : cap1;
			d2 = new_data(lastnode + 1, cap1);
			graph_img.at(index).neigh.push_back(d2);
			d2.nod = index;
			gg.neigh.push_back(d2);
      /*
      calculez valoarea muchiei de la nod la sursa
      */
			cap1 =
			    ((double)(imagine.value.at(i).at(j) - miu_f) *
			     (imagine.value.at(i).at(j) -
			      miu_f)) / (2L * sigma_f) + log_f;
			cap1 = (cap1 > 10) ? 10 : cap1;
			d1 = new_data(lastnode, cap1);
			graph_img.at(index).neigh.push_back(d1);
			d1.nod = index;
			f.neigh.push_back(d1);
     /*
      calculez valoarea celorlalte muchii
      */
			for (int k = 0; k <= 3; k++) {
				ind_x = direction[k][0] + i;
				ind_y = direction[k][1] + j;
				if (ind_x >= 0 && ind_x < imagine.h
				    && ind_y >= 0 && ind_y < imagine.w) {
					d1 = new_data(ind_x * imagine.w + ind_y,
						      0);
					if ((double)
					    ((imagine.value.at(i).at(j) -
					      imagine.value.at(ind_x).
					      at(ind_y)) *
					     ((imagine.value.at(i).at(j) -
					       imagine.value.at(ind_x).
					       at(ind_y))) <=
					     thresold * thresold)) {
						d1.cap = l;
					}
					graph_img.at(index).neigh.push_back(d1);
				}

			}

		}
  /*
  inserez sursa si drena in graf
  */
	graph_img.push_back(f);
	graph_img.push_back(gg);

}

/*
  parcurgere BFS pentru maxflow
  returnez toata lista de noduri copil + 
  lista  de noduri parinte
*/
pair < vector < int >, vector < int > >BFS2(int sink, int dest)
{

	queue < int >q;
	vector < int >parents(graph_img.size() + 1, NONE);
	parents[sink] = -3;
	vector < int >childs;
	q.push(sink);
	int node;
	int node2;
	data dnode;
	while (!q.empty()) {
		node = q.front();
		q.pop();
		if (node != dest) {
			for (int i = 0; i < graph_img.at(node).neigh.size();
			     i++) {
				dnode = graph_img.at(node).neigh[i];
				node2 = dnode.nod;

				if (dnode.cap > 0) {
					if (node2 == dest) {
						childs.push_back(node);
					}
					if (parents.at(node2) == NONE) {
						q.push(node2);
						parents.at(node2) = node;

					}
				}
			}
		}
	}
	return make_pair(childs, parents);

}


/*
  detrmin maxflow pentru un drum din graf si saturez muchiile
*/
double getflow(vector < int >path)
{

	double flow = oo;
	double maxflow;
	int neighsize;
	double aux;
	/*
	detremin muchia de flux minim
	*/
	for (int i = path.size() - 1; i > 0; i--) {
		neighsize = graph_img.at(path[i]).neigh.size();
		for (int k = neighsize - 1; k >= 0; k--) {
			if (path[i - 1] ==
			    graph_img.at(path[i]).neigh.at(k).nod) {

				aux = graph_img.at(path[i]).neigh.at(k).cap;
				if (flow > aux)
					flow = aux;
				break;
			}

		}
	}
  
  /*
  saturez muchiile
  */
	for (int i = path.size() - 1; i > 0; i--) {
		neighsize = graph_img.at(path[i]).neigh.size();
		for (int k = neighsize - 1; k >= 0; k--) {
			if (path[i - 1] ==
			    graph_img.at(path[i]).neigh.at(k).nod) {
				graph_img.at(path[i]).neigh.at(k).cap -= flow;
				break;
			}

		}

	}
	return flow;
}
/*
  metoda aplica algoritmul Dinic pentru flux maxim din graf
*/
vector < int >max_flow(int sink, int dest)
{

	double maxflow = 0;
	vector < int >childs;
	vector < int >cale;
	vector < int >parents;
	int node;
	pair < vector < int >, vector < int > >p = BFS2(sink, dest);
	childs.clear();
	parents.clear();
	childs = p.first;
	parents = p.second;
	/*
	cat timp exista noduri child la care se poate ajunge
	*/
	while (!childs.empty()) {

		for (int i = 0; i < childs.size(); i++) {
			cale.clear();
			node = childs.at(i);
			cale.push_back(dest);
			cale.push_back(node);
			/*
			pornind de la nodurile parinte reconstruiesc calea
			*/
			while (node != sink) {
				node = parents.at(node);
				cale.push_back(node);
			}
			/*
			si saturez muchiile din cale
			*/
			maxflow += getflow(cale);

		}
		pair < vector < int >, vector < int > >p = BFS2(sink, dest);
		childs.clear();
		parents.clear();
		childs = p.first;
		parents = p.second;

	}

	printf("maxflow = %lf\n", maxflow);
	return parents;
}

/*
    taietura minima
*/
void cut(char *header, int w, int h, vector < int >parents)
{
	FILE *f = fopen("segment.pgm", "w");
	fprintf(f, "%s", header);
	fprintf(f, "%d %d \n%d\n", w, h, imagine.max);
	int val;
	for (int i = 0; i < graph_img.size() - 2; i++) {
		val = (parents[i] == NONE) ? 255 : 0;
		fprintf(f, "%d\n", val);
	}
	fclose(f);
}

int main()
{
	fileimg mask_f, mask_b, parametri;
	graph_img.clear();
	/*
	extrag datele din fisier
	*/
	imagine = get_data("imagine.pgm");
	mask_f = get_data("mask_fg.pgm");
	mask_b = get_data("mask_bg.pgm");
	FILE *f = fopen("parametri.txt", "r");
	fscanf(f, "%lf%lf", &l, &thresold);
	fclose(f);
	/*
	calculez sigma si miu
	*/
	get_sigma_and_miu(mask_b, mask_f, imagine);
	/*
	construiesc graful
	*/
	build_graph(imagine);
	/*
	aplic maxflow
	*/
	vector < int >parents = max_flow(lastnode, lastnode + 1);
	/*
	taietura minima 
	*/
	cut(imagine.header, imagine.w, imagine.h, parents);
	return 0;
}

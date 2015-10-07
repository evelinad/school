//Evelina Dumitrescu Tema1 PA

#include<iostream>
#include<fstream>
#include<stdio.h>
#include<string.h>
#include<algorithm>
#include<ctype.h>
#include<map>
#include<list>
#include<string.h>
#include<vector>
#include<math.h>
#define MAXINT 30000

using namespace std;

/*

 structuri in care sunt memorate intrarile in  dictionar
 si posibilele variante pentru corectia gramaticala

*/
struct word_entry
{
	char word[70];
	int frequence;
	int word_length;

};

struct possibility
{
	word_entry entry;
	int numwords;
	int price;
};
int price[100][100];
int Nwords = 0;
std::vector < word_entry > WordDictionary;

/*

  citire dictionar

*/
void readDictionary()
{
	FILE *f;
	f = fopen("dict.txt", "r");
	int freq;
	char *cuv = (char *)calloc(70, sizeof(char));
	while ((fscanf(f, "%s%d", cuv, &freq)) != EOF) {
		word_entry wd;
		strcpy(wd.word, cuv);
		wd.frequence = freq;
		wd.word_length = strlen(cuv);
		WordDictionary.push_back(wd);
		Nwords++;
		cuv = (char *)calloc(50, sizeof(char));

	}

	fclose(f);
}

/*

  initializare matrice Levenstein
  
*/

void init(int m, int n)
{

	for (int i = 0; i <= m; i++) {
		price[i][0] = i;
	}
	for (int j = 0; j <= n; j++) {
		price[0][j] = j;
	}
}

/*
  determinarea prefixului comun intre 2 cuvinte consecutive din dictionar
  pentru a nu recalcula intreaga matrice Levenstein   

*/
inline int prefix_left(char *s1, char * s2)
{
	int ind = 0;
	int i = 0;
	bool ok = true;

	int _length1 = strlen(s1);
	while ((ok == true) && (i < _length1) ) {
		if(s1[i] != s2[i]) ok = false;
		else ind = i;
		i++;

	}
	return ind +1 ;		

}

/*
  
    determinare distanta Levesnstein intre 2 cuvinte

*/
inline int Levenstein_distance(string str1, string str2, int m, int n, int leng)
{
	int i, j;
	int min;
	
	for (i = 1; i <= m; i++)
		for (j = 1; j <= n; j++)
			if (str1[i - 1] == str2[j - 1])
				price[i][j] = price[i - 1][j - 1];
			else {
				min = price[i - 1][j - 1];
				if (min > price[i - 1][j])
					min = price[i - 1][j];
				if (min > price[i][j - 1])
					min = price[i][j - 1];
				price[i][j] = min + 1;
			}
	return price[m][n];
}


/*
  metoda compara 2 posibile variante conform criteriilor din enunt

*/

inline bool compare(possibility p1, possibility p2)
{
	if (p1.price < p2.price)
		return true;
	else if (p1.price == p2.price) {
		if ((p1.numwords) < (p2.numwords))
			return true;
		else if ((p1.numwords) == (p2.numwords))
			if (p1.entry.frequence > p2.entry.frequence)
				return true;
			else if (p1.entry.frequence == p2.entry.frequence) {
				if (strcmp(p1.entry.word, p2.entry.word) < 0)
					return true;
			}
	}
	return false;

}

/*
  
  metoda returneaza care dintre cele 2 posibilitati este cea optima    
    
*/
inline possibility best_choice(possibility p1, possibility p2)
{

	return (compare(p1, p2) == true) ? p1 : p2;
}


/*

  determina varianta cu distanta levenstein minima   
    
*/
inline possibility minimLevenstein(string word)
{
	possibility min, temp;
	int pricemin;
	int pricetemp;
	int _length = word.length();
	int i;
	
	// determin distanta Levensetin pentru primul cuvant din dictionar
	
	pricemin =
	    Levenstein_distance(word, string(WordDictionary.at(0).word),
				_length, WordDictionary.at(0).word_length, 1);
	min.price = pricemin;
	strcpy(min.entry.word, WordDictionary.at(0).word);
	min.entry.frequence = WordDictionary.at(0).frequence;
	min.entry.word_length = WordDictionary.at(0).word_length;
	min.numwords = 1;
	
	// daca distanta Levenstein e zero atunci cuvantul dat se regaseste in dictionar
	
	if (pricemin == 0)
		return min;
	for (i = 1; (i < Nwords); i++)
	
	// nu are rost sa calculez distanta levenstein dintre cuvantul dat 
	// si cuvantul de pe pozitia i din dictionar
	
		if ((strlen(WordDictionary.at(i).word) - word.size())* (strlen(WordDictionary.at(i).word) - word.size())
		    <= 4)
		 {
		 
		 // construiesc o varianta temporara
		 
			pricetemp =
			    Levenstein_distance(word,
						string(WordDictionary.
						       at(i).word), _length,
						WordDictionary.
						at(i).word_length,1);
			temp.entry.word_length =
			    WordDictionary.at(i).word_length;
			temp.entry.frequence = WordDictionary.at(i).frequence;
			strcpy(temp.entry.word, WordDictionary.at(i).word);
			temp.price = pricetemp;
			temp.numwords = 1;
			if (pricetemp == 0)
				return temp;
				
			// compar varianta temporara cu minimul gasit pana acum si actualizez minimul daca este cazul
			
			min = (compare(min, temp) == true) ? min : temp;

		}

	return min;
}

/*

  metoda creeaza expresia finala corectata gramatical

*/

string generate_expression(string expression, int l)
{

	possibility mpossib[100][100];
	int i, k, p, q;
	i = k = p = q = 0;
	int col;
	int line;
	int diag;
	string str;
	
	//initializez matricea de solutii pe diagonala principala

	for (i = 0; i < l; i++) {
		str = expression.substr(i, 1);
		possibility minim;
		minim = minimLevenstein(str);
		strcpy(mpossib[i][i].entry.word, minim.entry.word);
		mpossib[i][i].entry.frequence = minim.entry.frequence;
		mpossib[i][i].price = minim.price;
		mpossib[i][i].numwords = 1;

	}
	
  // parcurg matricea superior triunghiulara
  // pe pozitia [lin,col] se va afla subsirul[lin,col] initial corectat
  
	for (diag = 1; diag < l; diag++)
		for (line = 0; line < l - diag; line++) {
			col = line + diag;
			string str;
			
			// extrag subsirul dintre pozitiile [line, diag+1]

			str = expression.substr(line, diag + 1);
			possibility minim;
			
			//formez o varianta
			
			minim = minimLevenstein(str);
			strcpy(mpossib[line][col].entry.word, minim.entry.word);

			mpossib[line][col].entry.frequence =
			    minim.entry.frequence;
			mpossib[line][col].price = minim.price;
			mpossib[line][col].numwords = 1;

			for (k = line; k < col; k++) {
        
        //verific care dintre variantele corespunzatoare sirului dinte pozitiile [i,j]
        // sau sirului obtinut prin concatenarea subsirurilor de la [i,k] si [k+1,j]
        
				possibility temp, temp2;
				strcpy(temp.entry.word,
				       mpossib[line][k].entry.word);
				strcat(temp.entry.word, " ");
				strcat(temp.entry.word,
				       mpossib[k + 1][col].entry.word);
				temp.numwords =
				    mpossib[line][k].numwords + mpossib[k +
									1]
				    [col].numwords;
				temp.entry.frequence =
				    mpossib[line][k].entry.frequence +
				    mpossib[k + 1][col].entry.frequence;
				temp.price =
				    mpossib[line][k].price + mpossib[k +
								     1]
				    [col].price;
				strcpy(temp2.entry.word,
				       mpossib[line][col].entry.word);
				temp2.numwords = mpossib[line][col].numwords;
				temp2.price = mpossib[line][col].price;
				temp2.entry.frequence =
				    mpossib[line][col].entry.frequence;
				
				// verific care dintre cele 2 variante este cea optima
				
				if (compare(temp, temp2) == true) {
					strcpy(mpossib[line][col].entry.word,
					       temp.entry.word);
					mpossib[line][col].entry.frequence =
					    temp.entry.frequence;
					mpossib[line][col].price = temp.price;

					mpossib[line][col].numwords =
					    temp.numwords;

				}

			}

		}
  // sirul corectat se afla pe pozitia [0,l-1] in matrice
	return string(mpossib[0][l - 1].entry.word);
}

int main()
{
	readDictionary();
	char expression[1000];
	cin.getline(expression, 1000);
	string string_expression = string(expression);
	init(98, 98);
	
	//sterg spatiile
	
	string_expression.erase(remove
				(string_expression.begin(),
				 string_expression.end(), ' '),
				string_expression.end());
	std::cout << generate_expression(string_expression,
					 string_expression.length()).
	    c_str() << std::endl;
	return 0;
}

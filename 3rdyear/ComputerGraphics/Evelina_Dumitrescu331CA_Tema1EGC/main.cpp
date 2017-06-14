//Dumitrescu Evelina 331CA
//Tema 1 EGC




//-----------------------------------------------------------------------------------------------
//					LAB 1
//
//	Fisiere de interes: Transform2d.cpp main.cpp
//
//	Functii WorldDrawer2d:
//	-init e apelat o singura data, la inceput.
//	-idle e apelat per cadru de desenare
//	-onKey la apasarea unei taste.
//
//	Obiecte:
//	- un obiect este reprezentat prin punct si topologia punctelor (cum sunt legate pctele)
//	- obiectele sunt compuse din triunghiuri! de exemplu cu 4 puncte si 6 indici de topologie
//	pot crea 2 triunghiuri adiacente ce impreuna formeaza un dreptunghi.
//
//	Sisteme de coordonate:
//	- sunt 2 tipuri de sisteme de coordonate (fix - bleu&magenta) si dinamic(rosu&albastru)
//	- ca un obiect sa fie desenat trebuie sa fie atasat unui sistem de coordonate
//	- cand un obiect e atasat la un sistem de coordonate urmeaza transformarile sistemului.
//
//	Control obiecte:
//	- daca translatez/rotesc un obiect/punct direct (ex: o->translate(1,1)) o fac in coordonate globale
//	- daca translatez/rotesc un obiect printr-un sistem de coordonate o fac in coordonate locale
//	- pentru simplitate toate coordonatele mentinute in obiecte(de c++) sunt globale.
//
//	Happy coding.
//----------------------------------------------------------------------------------------------
#include <algorithm>
#include "WorldDrawer2d.h"
#include <time.h>


using namespace std;
bool WorldDrawer2d::animation=true;
bool changedir=false;
#define NUM_PLAYERS 16
#define PI 3.141592653
bool has_ball ;
int player_ball;
float player_angle;
bool wall ;

static int iteration=1;
//used global vars
CoordinateSystem2d  *cs_dr1,*cs_dr2, *cs_gate1,*cs_gate11,*cs_gate2, *cs_gate22,*cs_backgr,*cs_line_gate1, *cs_line_gate2, *cs_middle;
Object2d  *o_dr1,*o_dr2, *o_gate1, *o_gate2, *o_gate11, *o_gate22,*o_backgr,*o_line_gate1,*o_line_gate2, *o_middle;
Object2d *player1[NUM_PLAYERS/2];
Object2d *player2[NUM_PLAYERS/2];
Object2d *ball;
Object2d *score1;
Object2d *score2;
CoordinateSystem2d *cs_ball;
CoordinateSystem2d *cs_score;
CoordinateSystem2d *cs_score1;
CoordinateSystem2d *cs_score2;
CoordinateSystem2d *cs_player1[NUM_PLAYERS/2];
CoordinateSystem2d *cs_player2[NUM_PLAYERS/2];
int board[4][3];
int points1;
int points2;
float ball_angle;
float rotate_angle;

/*
	metoda pentru desenare cerc
	foloseste coordonate polare
	parametri intrare raza + culoarea rgb
	returneaza Object2d*
*/
Object2d* drawCircle(float r, float red, float green, float blue )
{
	std::vector<Point2d> points4;
	std::vector<int> topology4;
	float x,y;
	float xinit,yinit;
	float radius = r;	
    x = (float)radius * cos(0 * 3.141592653/180.0f);
    y = (float)radius * sin(0 * 3.141592653/180.0f);
    int point = 0;
	xinit=x;
	yinit=y;
	for(int j = 1; j <= 360; j+=1)
    {		
		points4.push_back(Point2d(x,y));
		topology4.push_back(point);
		point++;
        x = (float)radius * cos(j * 3.141592653/180.0f);
        y = (float)radius * sin(j * 3.141592653/180.0f);
		points4.push_back(Point2d(x,y));
		topology4.push_back(point);
		point ++;
		points4.push_back(Point2d(0,0));		
		topology4.push_back(point);
		point++;
    }
	points4.push_back(Point2d(x,y));
	topology4.push_back(point);
	point++;
	points4.push_back(Point2d(xinit,yinit));
	topology4.push_back(point);
	point ++;
	points4.push_back(Point2d(0,0));
	topology4.push_back(point);
	Object2d* o = new Object2d(points4,topology4);
	o->setcolor(red,green,blue);
	return o;
}

/*
	metoda pentru desenare elipsa
	foloseste coordonate polare
	parametri intrare cele 2 raze ale elipsei+ coloar rgb
	returneaza Object2d
*/
Object2d* drawElipse(float r1,float r2, float red, float green, float blue )
{
	std::vector<Point2d> points4;
	std::vector<int> topology4;

	float x,y;
	float xinit,yinit;
	float a = r1;	
	float b = r2;
    x = (float)a * cos(0 * 3.141592653/180.0f);
    y = (float)b * sin(0 * 3.141592653/180.0f);
    int point = 0;
	xinit=x;
	yinit=y;
	for(int j = 1; j <= 360; j+=1)
    {		
		points4.push_back(Point2d(x,y));
		topology4.push_back(point);
		point++;
        x = (float)a * cos(j * 3.141592653/180.0f);
        y = (float)b * sin(j * 3.141592653/180.0f);
		points4.push_back(Point2d(x,y));
		topology4.push_back(point);
		point ++;
		points4.push_back(Point2d(0,0));		
		topology4.push_back(point);
		point++;
    }
	points4.push_back(Point2d(x,y));
	topology4.push_back(point);
	point++;
	points4.push_back(Point2d(xinit,yinit));
	topology4.push_back(point);
	point ++;
	points4.push_back(Point2d(0,0));
	topology4.push_back(point);
	Object2d* o = new Object2d(points4,topology4);
	o->setcolor(red,green,blue);
	return o;
}
/*
	metoda pentru desenare dreptunghi
	parametri intrare punctele dreptunghiului + culoarea rgb
	returneaza un Object2d
*/
Object2d * drawRectangle(Point2d p1, Point2d p2, Point2d p3, Point2d p4, float r, float g, float b)
{
	Object2d * o;
	std::vector<Point2d> points;
	std::vector<int> topology;
	points.push_back(p1);
	points.push_back(p2);
	points.push_back(p3);
	points.push_back(p4);
	topology.push_back(0);
	topology.push_back(1);
	topology.push_back(2);
	topology.push_back(2);
	topology.push_back(3);
	topology.push_back(0);
	o=new Object2d(points, topology);
	o->setcolor(r,g,b);
	return o;

}
/*
	metoda returneaza un object CoordinateSysten2d 
	pentru afisarea  caracterelor S C O R E in dreapta sus
	
*/
CoordinateSystem2d* init_score()
{
	 points1=0;
	 points2=0;
	CoordinateSystem2d * cs_score=new CoordinateSystem2d();
	Object2d *s,*s1,*s2;
	Object2d *c,*c1;
	Object2d *o,*o1;
	Object2d *r1,*r2,*r22,*r3;
	Object2d *e,*e2,*e1;
	s=drawRectangle(Point2d(1.5,0),Point2d(0,0),Point2d(0,2.5),Point2d(1.5,2.5),1,1,1);
	s1=drawRectangle(Point2d(1.5,1.5),Point2d(0.5,1.5),Point2d(0.5,2),Point2d(1.5,2),0.4,0.75,1);
	s2=drawRectangle(Point2d(1,0.5),Point2d(0,0.5),Point2d(0,1),Point2d(1,1),0.4,0.75,1);
	c=drawRectangle(Point2d(3.5,0),Point2d(2,0),Point2d(2,2.5),Point2d(3.5,2.5),1,1,1);
	c1=drawRectangle(Point2d(3.5,0.5),Point2d(2.5,0.5),Point2d(2.5,2),Point2d(3.5,2),0.4,0.75,1);
	o1=drawRectangle(Point2d(5,0.5),Point2d(4.5,0.5),Point2d(4.5,2),Point2d(5,2),0.4,0.75,1);
	o=drawRectangle(Point2d(5.5,0),Point2d(4,0),Point2d(4,2.5),Point2d(5.5,2.5),1,1,1);
	r22=drawRectangle(Point2d(7,1.5),Point2d(6.5,1.5),Point2d(6.5,2),Point2d(7,2),0.4,0.75,1);
	r2=drawRectangle(Point2d(7.5,1),Point2d(6,1),Point2d(6,2.5),Point2d(7.5,2.5),1,1,1);
	r1=drawRectangle(Point2d(6.5,0),Point2d(6,0),Point2d(6,1),Point2d(6.5,1),1,1,1);
	r3=drawRectangle(Point2d(7.5,0),Point2d(7,0),Point2d(6.5,1),Point2d(7,1),1,1,1);
	
	e=drawRectangle(Point2d(9.5,0),Point2d(8,0),Point2d(8,2.5),Point2d(9.5,2.5),1,1,1);
	e2=drawRectangle(Point2d(9.5,0.5),Point2d(8.5,0.5),Point2d(8.5,1),Point2d(9.5,1),0.4,0.75,1);
	e1=drawRectangle(Point2d(9.5,1.5),Point2d(8.5,1.5),Point2d(8.5,2),Point2d(9.5,2),0.4,0.75,1);
	cs_score->objectAdd(e1);
	cs_score->objectAdd(e2);
	cs_score->objectAdd(e);
	cs_score->objectAdd(r3);
	cs_score->objectAdd(r1);
	cs_score->objectAdd(r22);
	cs_score->objectAdd(r2);
	cs_score->objectAdd(o1);
	cs_score->objectAdd(o);
	cs_score->objectAdd(c1);
	cs_score->objectAdd(c);
	cs_score->objectAdd(s2);
	cs_score->objectAdd(s1);
	cs_score->objectAdd(s);
	
	cs_score->translate(9,12.5);
	return cs_score;

}

/*
	metoda returneaza un obiect CoordinateSysten2d pentru afisarea simbolului corespunzator
	parametrului value in coordonatele x si y de culoare rgb
*/
CoordinateSystem2d* draw_score(int value,float x, float y, float r, float g, float b)
{
	CoordinateSystem2d* cs_score=new CoordinateSystem2d();
	
	Object2d *o1,*o2,*o3;
	switch(value)
	{
		case 0:
			{
				
				o1=drawElipse(1.6,2.3f,r,g,b);
				o2=drawElipse(1.1,1.9,0.4f,0.75f,1.0f);
				cs_score->objectAdd(o2);
				cs_score->objectAdd(o1);
				
				cs_score->translate(x,y);
		
				break;
			}
		case 1:
				o1=drawRectangle(Point2d(0.3,0),Point2d(-0.3,0),Point2d(-0.3,4.4),Point2d(0.3,4.4),r,g,b);
				cs_score->objectAdd(o1);
				cs_score->translate(x,y-2.2);
				break;
			
		case 2:
				o1=drawRectangle(Point2d(0,0),Point2d(-0.6,0),Point2d(-0.6,4.4),Point2d(0,4.4),r,g,b);
				o2=drawRectangle(Point2d(0.9,0),Point2d(0.3,0),Point2d(0.3,4.4),Point2d(0.9,4.4),r,g,b);
				cs_score->objectAdd(o1);
				cs_score->objectAdd(o2);
				cs_score->translate(x,y-2.2);
				break;
				
		case 3:
				
				o1=drawRectangle(Point2d(-0.3,0),Point2d(-0.9,0),Point2d(-0.9,4.4),Point2d(-0.3,4.4),r,g,b);
				o2=drawRectangle(Point2d(0.6,0),Point2d(0,0),Point2d(0,4.4),Point2d(0.6,4.4),r,g,b);
				o3=drawRectangle(Point2d(1.5,0),Point2d(0.9,0),Point2d(0.9,4.4),Point2d(1.5,4.4),r,g,b);	
				cs_score->objectAdd(o1);
				cs_score->objectAdd(o2);
				cs_score->objectAdd(o3);
				cs_score->translate(x,y-2.2);
				break;
}		
	return cs_score;
}

/*
	metoda genereaza jucatorii pe board
*/
void WorldDrawer2d::generate_players()
{
	ball_angle=PI/2.0;
	rotate_angle=PI/4.0;
	cs_ball=new CoordinateSystem2d();
	int i;
	std::vector<int> components;
	int left_components=36;

	// formez un vector pentru cele 36 de celule de joc
	for(i=0;i<left_components;i++)
		components.push_back(i);
	wall = false;
	int poz;
	//impart regiunea de joc in 36 de celule egale de dimnesiune (x_unit, y_unit)
	float x_unit,y_unit;
	x_unit=24.0/6.0;
	y_unit=30.0/6.0;
	float x, y;
	float x1,y1;
	//determin carui jucator ii plaesz mingea initial
	srand ( time(NULL) );
	player_ball = rand() % NUM_PLAYERS;
	has_ball = true;

	for (i=0;i<NUM_PLAYERS;i++)
	{
		//extrag pe rand elementul din vector situat pe pozitia rand()%left components
		poz=rand();
		
		poz=poz%left_components;

		int el=components.at(poz);
		std::vector<int>::iterator iter=components.begin();
		components.erase(iter+poz);

		// pozitia unui jucator in cadrul gridului
		x=(float(el%6));
		y=(float(el/6));
		
		//pozitia unui jucator din cadrul unei celule (x, y)
		x1=el%5;
		if(x1 == 0) x1 = x_unit/2;
		y1=el%5;
		if (y1 == 0)y1 = y_unit/2;
		x=x*x_unit + x1;
		y=y*y_unit + y1;
		if (x>23.0) x= 23.0;
		if(x<-23.0)x=-23.0;
		if (y>29.0) y= 29.0;
		if(y<-29.0)y=-29.0;

		//desenez jucatorii
		if(i>NUM_PLAYERS/2-1)
		{
			player2[i-NUM_PLAYERS/2]=drawCircle(0.75f,0,0.1f,1);	
			cs_player2[i-NUM_PLAYERS/2]=new CoordinateSystem2d();
			cs_player2[i-NUM_PLAYERS/2]->objectAdd(player2[i-NUM_PLAYERS/2]);
		
			player2[i-NUM_PLAYERS/2]->translate(x-17,y-15);
			cs_used.push_back(cs_player2[i-NUM_PLAYERS/2]);
		}
		else 
		{
			player1[i]=drawCircle(0.75f,1,0,0);	
			cs_player1[i]=new CoordinateSystem2d();
			

			cs_player1[i]->objectAdd(player1[i]);
		
			player1[i]->translate(x-17,y-15);
			cs_used.push_back(cs_player1[i]);
		}
		left_components--;
	}

	//desenez mingea
	ball=drawCircle(0.5f,1,1,1);
	cs_ball->objectAdd(ball);
		
	if (player_ball>NUM_PLAYERS/2-1)
			ball->translate(player2[player_ball-NUM_PLAYERS/2]->axiscenter.x,player2[player_ball-NUM_PLAYERS/2]->axiscenter.y+1.25f);
	else
			ball->translate(player1[player_ball]->axiscenter.x,player1[player_ball]->axiscenter.y+1.25f);
	cs_used.push_back(cs_ball);
	
	
}

/*
	metoda genreaza regiunea de joc
*/
void WorldDrawer2d::generate_board()
{
	
	flick = false;
	
	// desenez linia din mijlocul terenului
	o_middle=drawRectangle(Point2d(12,-0.1),Point2d(-12,-0.1),Point2d(-12,0.1),Point2d(12,0.1),1,1,1);
	cs_middle->objectAdd(o_middle);	
	cs_middle->translate(-5,0);
	
	// dreptunghi penru background(cel albastru-gri)
	o_backgr=drawRectangle(Point2d(20,-20),Point2d(-20,-20),Point2d(-20,20),Point2d(20,20),0.4f,0.75f,1.0f);
	cs_backgr->objectAdd(o_backgr);	
	cs_backgr->translate(0,0);
	
	// desenz portile
	o_gate1=drawRectangle(Point2d(4,0),Point2d(-4,0),Point2d(-4,2.5),Point2d(4,2.5),0.5f,0.05f,0.1f);
	cs_gate1->objectAdd(o_gate1);	
	cs_gate1->translate(-5,15);

	o_gate11=drawRectangle(Point2d(3.7,0),Point2d(-3.7,0),Point2d(-3.7,2.2),Point2d(3.7,2.2),0.4f,0.75f,1.0f);
	cs_gate11->objectAdd(o_gate11);	
	cs_gate11->translate(-5,15);

	
	o_line_gate1=drawRectangle(Point2d(3.7,0),Point2d(-3.7,0),Point2d(-3.7,0.2),Point2d(3.7,0.2),1.0f,1.0f,1.0f);
	cs_line_gate1->objectAdd(o_line_gate1);	
	cs_line_gate1->translate(-5,15);
	

	o_gate2=drawRectangle(Point2d(4,0),Point2d(-4,0),Point2d(-4,2.5),Point2d(4,2.5),0.5f,0.05f,0.1f);
	cs_gate2->objectAdd(o_gate2);	
	cs_gate2->translate(-5,-17.5);
	
	o_gate22=drawRectangle(Point2d(3.7,0),Point2d(-3.7,0),Point2d(-3.7,2.2),Point2d(3.7,2.2),0.4f,0.75f,1.0f);
	cs_gate22->objectAdd(o_gate22);	
	cs_gate22->translate(-5,-17.2);

	o_line_gate2=drawRectangle(Point2d(3.7,0),Point2d(-3.7,0),Point2d(-3.7,0.2),Point2d(3.7,0.2),1.0f,1.0f,1.0f);
	cs_line_gate2->objectAdd(o_line_gate2);	
	cs_line_gate2->translate(-5,-15.2);

	
	o_dr1=drawRectangle(Point2d(12.2,-15.2),Point2d(-12.2,-15.2),Point2d(-12.2,15.2),Point2d(12.2,15.2),0.5f,0.05f,0.1f);
	cs_dr1->objectAdd(o_dr1);	
	cs_dr1->translate(-5,0);

	
	o_dr2=drawRectangle(Point2d(12,-15),Point2d(-12,-15),Point2d(-12,15),Point2d(12,15),0.3f,0.7f,0.25f);
	cs_dr2->objectAdd(o_dr2);
	cs_dr2->translate(-5,0);
	
	cs_used.push_back(cs_middle);
	cs_used.push_back(cs_line_gate1);
	cs_used.push_back(cs_gate11);
	cs_used.push_back(cs_gate1);

	cs_used.push_back(cs_line_gate2);
	cs_used.push_back(cs_gate22);
	cs_used.push_back(cs_gate2);

	cs_used.push_back(cs_dr2);
	cs_used.push_back(cs_dr1);
	cs_used.push_back(cs_backgr);

}
/*
	metoda afiseaza scorul initial 0-0
*/
void WorldDrawer2d::generate_score()
{
		
	cs_score1=draw_score(0,14,3,1,0,0);
	cs_score2=draw_score(0,14,-3,0,0,1);
	cs_score = init_score();
	cs_used.push_back(cs_score);
	cs_used.push_back(cs_score1);
	cs_used.push_back(cs_score2);
	
}
/*
	metoda initializeaza jocul
*/
void WorldDrawer2d::init(){
	animation=false;	
	flick = false;
	cs_dr1=new CoordinateSystem2d();	
	cs_dr2=new CoordinateSystem2d();
	cs_backgr=new CoordinateSystem2d();
	cs_gate1=new CoordinateSystem2d();
	cs_gate11=new CoordinateSystem2d();
	cs_middle=new CoordinateSystem2d();
	cs_line_gate1=new CoordinateSystem2d();
	cs_gate2=new CoordinateSystem2d();
	cs_gate22=new CoordinateSystem2d();	
	cs_line_gate2=new CoordinateSystem2d();
	generate_score();	
	generate_players();
	generate_board();	
	
	
	
}

/*
	metoda reseteaza jocul
*/
void WorldDrawer2d::reset_game()
{
	flick = true;
	iteration = 1;


	//sterg jucatorii vechi
	for(int i=0;i<NUM_PLAYERS/2 ;i++)
	{
		cs_player1[i]->objectRemove(player1[i]);
		delete player1[i];
		cs_player2[i]->objectRemove(player2[i]);
		delete player2[i];

	}
	//sterg elementele de pe zona de joc
	cs_ball->objectRemove(ball);
	delete ball;
	cs_gate1->objectRemove(o_gate1);
	delete o_gate1;
	cs_gate11->objectRemove(o_gate11);
	delete o_gate11;
	cs_gate2->objectRemove(o_gate2);
	delete o_gate2;
	cs_gate22->objectRemove(o_gate22);
	delete o_gate22;
	cs_line_gate1->objectRemove(o_line_gate1);
	delete o_line_gate1;
	cs_line_gate2->objectRemove(o_line_gate2);
	delete o_line_gate2;
	cs_middle->objectRemove(o_middle);
	delete o_middle;
	cs_score1->objectRemove(score1);
	delete score1;
	cs_score2->objectRemove(score2);
	delete score2;
	cs_dr1->objectRemove(o_dr1);
	delete o_dr1;
	cs_dr2->objectRemove(o_dr2);
	delete o_dr2;
	cs_backgr->objectRemove(o_backgr);
	delete o_backgr;
	
	while(!cs_used.empty())
	{
		cs_used.pop_back();
	}
	ball_angle=PI/2.0;
	rotate_angle=PI/4.0;
	has_ball = true;
	 wall = false;
	
	
	// generez zona de joc din nou
	WorldDrawer2d::generate_score();	
	WorldDrawer2d::generate_players();
	WorldDrawer2d::generate_board();
	animation =false;
	points1=points2=0;

}

/*
	metoda este apelata la fiecare frame in parte si randeaza continutul grafic
*/
void WorldDrawer2d::onIdle(){
	//per frame
	
	
	double delta=0.65;
	float ball_x, ball_y;
	ball_x=ball->axiscenter.x;
	ball_y=ball->axiscenter.y;
	if (ball_angle<0) ball_angle = 2 * PI+ ball_angle;
	if(ball_angle>2 *PI) ball_angle-=2 * PI;

	//una din echipe a ajuns la 3 puncte =>rest game
	if(points1 == 3 || points2 == 3)
		{	animation = false;
			reset_game();
			flick = true;
			
		}
	
	iteration++;
	if(animation == true )
	{
		float playercoord_x, playercoord_y;
		float ballcoord_x, ballcoord_y;
		ballcoord_x=ball->axiscenter.x;
		ballcoord_y=ball->axiscenter.y;
		float dist=0;
	
		//verific daca mingea a atuns una din porti 
		//daca da updatez scorul
		if(ball->axiscenter.y>14.25 && ball->axiscenter.x<=-2.2&& ball->axiscenter.x>=-8.2)
		{ 
		points2++;
		remove( cs_used.begin(), cs_used.end(), cs_score2 );

		cs_score2=draw_score(points2,14,-3,0,0,1);
		cs_used.insert(cs_used.begin(),cs_score2);
		
	
		srand(time(NULL));
		player_ball=rand()%(NUM_PLAYERS/2);
		playercoord_x=player1[player_ball]->axiscenter.x;
		playercoord_y=player1[player_ball]->axiscenter.y;
		cs_ball->objectRemove(ball);
		delete ball;
		ball=drawCircle(0.5f,1,1,1);
		cs_ball->objectAdd(ball);
		
		ball->translate(playercoord_x,playercoord_y+0.5f);
		has_ball = true;
		animation = false;
		wall = false;
	
		ball_angle = PI/2.0;
		
		
		}
		
		if(ball->axiscenter.y<-14.25&& ball->axiscenter.x<=-2.2&& ball->axiscenter.x>=-8.2)
		{
			points1++;
			vector<CoordinateSystem2d *>::iterator iter;
			remove( cs_used.begin(), cs_used.end(), cs_score1 );
			cs_score1=draw_score(points1,14,3,1,0,0);
			cs_used.insert(cs_used.begin(),cs_score1);

			player_ball=rand()%(NUM_PLAYERS/2);
			playercoord_x=player2[player_ball]->axiscenter.x;
			playercoord_y=player2[player_ball]->axiscenter.y;

			
			cs_ball->objectRemove(ball);
			delete ball;
			ball=drawCircle(0.5f,1,1,1);
			cs_ball->objectAdd(ball);
		
			ball->translate(playercoord_x,playercoord_y+0.5f);
	
			ball_angle = PI/2.0;
			player_ball+=NUM_PLAYERS/2;
			has_ball = true;
			wall = false;
			animation = false;
			
		}
	
		if(!has_ball)
		for(int i=0;i<NUM_PLAYERS/2;i++)
		{
			//verfic daca mingea a atins unul dintre jucatori
			//daca da, intra in posesia lui
			if(i!=player_ball || ( i == player_ball && wall == true))
			{
			playercoord_x=player1[i]->axiscenter.x;
			playercoord_y=player1[i]->axiscenter.y;
			//distanta euclidiana intre centrul mingiei si centru jucatorului
			dist=(playercoord_x-ballcoord_x)*(playercoord_x-ballcoord_x) + (playercoord_y-ballcoord_y) * (playercoord_y-ballcoord_y);
			if(dist<=(1.2 * 1.2) )
				{
					has_ball=true;
					player_ball=i;
					cs_ball->objectRemove(ball);
					delete ball;	
					ball=drawCircle(0.5f,1,1,1);
					cs_ball->objectAdd(ball);
					wall = false;
					ball->translate(playercoord_x,playercoord_y+0.5f);
					ball_angle=PI/2.0;
					animation = false;
					i=NUM_PLAYERS;
					break;
				}
			}
			if(i!=player_ball-(NUM_PLAYERS/2) || (i==player_ball-(NUM_PLAYERS/2) && wall == true))
			{
			playercoord_x=player2[i]->axiscenter.x;
			playercoord_y=player2[i]->axiscenter.y;
			dist=(playercoord_x-ballcoord_x)*(playercoord_x-ballcoord_x) + (playercoord_y-ballcoord_y) * (playercoord_y-ballcoord_y);
			if(dist<=(1.2 * 1.2))
				{
					wall = false;
					has_ball=true;
					player_ball=i+NUM_PLAYERS/2;

					cs_ball->objectRemove(ball);
					delete ball;
					ball=drawCircle(0.5f,1,1,1);
					ball_angle=PI/2.0;
					cs_ball->objectAdd(ball);

					ball->translate(playercoord_x,playercoord_y+0.5f);
					animation = false;
					i=NUM_PLAYERS;
					break;
				}
			}
		}
	//mingea a lovit marginea de  sus
	if(ball_y+0.3>15.0) 
	{ wall =true;
		// unghiul este de pi/2, adica pe verticala
		if((int)(ball_angle * 1000.0) == (int)(1000.0*PI/2.0))
		{
				ball_y-=delta;
				ball->translate(0,-delta);
				ball_angle=3.0*PI/2.0;
				
		}
		else
		//de cadran 1
		if(ball_angle>0 && ball_angle <PI/2.0)
		{
				ball_y-=delta;

				ball_x=ball_x+delta;
				ball->translate(delta,-delta);

				ball_angle=PI/4.0 + 3.0*PI/2.0;
		}
		else
		//de cadran 2
		if(ball_angle>PI/2.0 && ball_angle < PI)
		{
				ball_y-=delta;
				ball_x=ball_x-delta;
				ball->translate(-delta,-delta);
				ball_angle=PI + PI/4.0;
		}

	}
	else
	//ricosare jos
	if(ball_y-0.3<-15.0)
	{ wall = true;
		//mingea a lovit pe verticala , unghi 3 *pi/2
		if((int)(ball_angle * 1000.0)== (int)(3.0/2.0 * PI * 1000.0))
		{
				ball_y+=delta;
				ball->translate(0,delta);
				ball_angle=PI/2.0;
			
		}
		else
		//de cadran 4
		if(ball_angle>3.0*PI/2.0 && ball_angle < PI*2.0)
		{
				ball_y+=delta;

				ball_x=ball_x+delta;
				ball->translate(delta,delta);

				ball_angle=PI/4.0;
		}
		else
			//de cadran 3
		if(ball_angle>PI && ball_angle < 3.0*PI/2.0)
		{
				ball_y+=delta;

				ball_x=ball_x-delta;
				ball->translate(-delta,delta);

				ball_angle=PI/4.0 + PI/2.0;
		}

	}
	else
	//ricosare dreapta
	if(ball_x+0.3>7.0)
	{
		wall = true;
		//mingea a lovit pe orizontala, , unghi == pi
		if((int)(ball_angle * 1000.0) == (int)(2.0*PI * 1000.0) || (int)(ball_angle * 1000.0) == 0)
		{
				ball_x=ball_x-delta;

				ball->translate(-delta,0);

				ball_angle=PI;
		}
		else
		//de cadran 1
		if(ball_angle>0 && ball_angle < PI/2.0)
		{
				ball_y+=delta;

				ball_x=ball_x-delta;
				

				ball->translate(-delta,delta);

				ball_angle=3.0*PI/4.0;
		}
		else
		//de cadran 4
		if(ball_angle>3.0*PI/2.0 && ball_angle < PI*2.0)
		{
				ball_y-=delta;

				ball_x=ball_x-delta;
				ball->translate(-delta,-delta);

				ball_angle=PI+PI/4.0;
		}



	}
	else
	//ricosare stanga
	if((ball_x -0.3)<-17.0)
		{ wall = true;
			if((int)(ball_angle * 1000.0) == (int)(PI*1000.0))
			{
				ball_x=ball_x-delta;
				ball->translate(delta,0);

				ball_angle=0.0;
			}
			else
				//de cadran 2
		if(ball_angle>PI/2.0 && ball_angle < PI)
		{
				ball_y+=delta;

				ball_x=ball_x+delta;
				ball->translate(delta,delta);

				ball_angle=PI/4.0;
		}
		else
			//de cadran 3
		if(ball_angle>PI && ball_angle <3.0*PI/2.0)
		{
				ball_y-=delta;

				ball_x=ball_x+delta;
				

				ball->translate(delta,-delta);

				ball_angle=PI/4.0 + 3.0 * PI/2.0;
		}

		}
	else
	{
		//mingea nu a lovit nimic
		if((int)(ball_angle *1000.0) == 0.0 ||  (ball_angle *1000.0) == (int)(2.0 * PI * 1000.0))
		{
			ball->translate(delta,0);
		}
		else
		if((int)(ball_angle *1000.0)== (int)(PI/2.0 *1000.0))
		{

			ball->translate(0,delta);
		}
		else
		if((int)(ball_angle * 1000.0) == (int)(1000.0*PI))
		{
			ball->translate(-delta,0);
		}
		else
		if((int)(ball_angle *1000.0) ==(int) (PI * 3.0/2.0 *1000.0))
		{
			ball->translate(0,-delta);
		}
		else
		
		//minge de cadran 1
		if(ball_angle>0 && ball_angle<PI/2.0)
		{
				ball_y+=delta;
				ball_x+=delta;
				ball->translate(delta,delta);

		}
		else
		//minge de cadran 2
		if(ball_angle>PI/2.0 && ball_angle < PI)
			
			{
				ball_y+=delta;
				ball_x-=delta;
				ball->translate(-delta,delta);

				
		
		}
		else
		//minge de cadran 3
		if(ball_angle>PI && ball_angle<3.0/2.0 * PI)
		{
				
				ball_y-=delta;
				ball_x-=delta;
				ball->translate(-delta,-delta);

		}
		else
		//minge de cadran 4
		if(ball_angle>3.0/2.0*PI && ball_angle<2.0 * PI)
		{
			
				ball_y-=delta;
				ball_x+=delta;
				

				ball->translate(delta,-delta);

		}
		
	}

}
}
/*
	metoda este apelata de fiecare data cand este apasta o tasta
*/
void WorldDrawer2d::onKey(unsigned char key){

	if(flick == false)
	switch(key){
		
		//j =>deplasare stanga
		case 106:
			if(has_ball && ((ball->axiscenter.x-0.75)>-17.0))
			{
				ball->translate(-0.75f,0.0);	
				if(player_ball>NUM_PLAYERS/2-1)					
					player2[player_ball-NUM_PLAYERS/2]->translate(-0.75f,0.0);
				else
					player1[player_ball]->translate(-0.75f,0.0);
			}	
				break;
		//k =.deplasare dreapta
		case 107:
			if(has_ball && ((ball->axiscenter.x+0.75)<7.0))
			{
			ball->translate(0.75f,0);
			if(player_ball>NUM_PLAYERS/2-1)					
					player2[player_ball-NUM_PLAYERS/2]->translate(0.75f,0);
			else
				player1[player_ball]->translate(0.75f,0);
			}
			break;
		//m=>rotire cu pi/4 dreapta
		case 109:
			if(has_ball)
			
				{
				
				if(player_ball>NUM_PLAYERS/2-1)					
					{
						ball->rotateRelativeToPoint(player2[player_ball-NUM_PLAYERS/2]->axiscenter,-rotate_angle);
						player2[player_ball-NUM_PLAYERS/2]->rotateSelf(-rotate_angle);
						player_angle-=rotate_angle;
					}
				else
				{	
					ball->rotateRelativeToPoint(player1[player_ball]->axiscenter,-rotate_angle);
					player1[player_ball]->rotateSelf(-rotate_angle);
					player_angle-=rotate_angle;
				}
					
					ball_angle-=rotate_angle;
					if(ball_angle>(double)2.0*PI)ball_angle=ball_angle-(double)2.0*PI;
					if(ball_angle<0)ball_angle=((double)2.0*PI +ball_angle);
				
			}
			break;
		//n => rotire stanga cu pi/4
		case 110:
			if(has_ball)
			{
				if(player_ball>NUM_PLAYERS/2-1)					
					{
						ball->rotateRelativeToPoint(player2[player_ball-NUM_PLAYERS/2]->axiscenter,rotate_angle);
						player2[player_ball-NUM_PLAYERS/2]->rotateSelf(rotate_angle);
						player_angle+=rotate_angle;
					}
				else
				{	
					ball->rotateRelativeToPoint(player1[player_ball]->axiscenter,rotate_angle);
					player1[player_ball]->rotateSelf(rotate_angle);
					player_angle+=rotate_angle;
				}
				ball_angle+=rotate_angle;
				if(ball_angle>(double)2.0*PI)ball_angle=ball_angle-(double)2.0*PI;
				if(ball_angle<0)ball_angle=((double)2.0*PI +ball_angle);
				
			}	
			break;
		// tasta spce =>shoot
		case 32:
			{
			
			{

			animation=!animation;
			if(has_ball )
			{
			
				if(player_ball>NUM_PLAYERS/2-1)					
					{
						
						player2[player_ball-NUM_PLAYERS/2]->rotateSelf(-player_angle);
						player_angle=0;
					}
				else
				{	
					
					player1[player_ball]->rotateSelf(-player_angle);
					player_angle=0;
				}
			
			has_ball=false;
			}
			}	
			}
			break;
			
		
		default:
			break;
	}

}


int main(int argc, char** argv){
	
	WorldDrawer2d wd2d(argc,argv,600,600,200,100,std::string("Soccer"));
	wd2d.init();
	wd2d.run();
	return 0;
}
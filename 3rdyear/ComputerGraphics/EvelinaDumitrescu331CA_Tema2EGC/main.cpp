//DUMITRESCU EVELINA 331CA
//Tema2 EGC

#include "WorldDrawer3d.h"
#include <stdlib.h>

using namespace std;

#define PI 3.1415926535897932384626433832795
#define NUM_CUBES 27
#define SIDE 9
#define EDGE 3
#define ROTATION_ANGLE PI/6.0

//used global vars
bool WorldDrawer3d::animation=true;

static int currentmove;
static int nrmoves;
static bool update;
int WorldDrawer3d:: score;

//structure that defines RGB colors
struct Colors
{
float r, g, b;
Colors (float r=0.0, float g = 0.0, float b = 0.0): r(r), g(g),b(b){}
};
Colors col_vect[7]={Colors(1.0f, 0.0f, 1.0f),Colors(0.0f, 1.0f, 0.0f),Colors(1.0f, 0.0f, 0.0f),Colors(0.0f, 0.0f, 1.0f),Colors(1.0f, 1.0f, 0.0f),Colors(8.0f, 0.5f, 0.07f)};

//true when the user presses enter (the move count begins)
bool WorldDrawer3d::gamestart;
//true when the rubik cube is solved
bool WorldDrawer3d::gameover;
vector<int **>FACE;
struct Object
		{
			public:
			Object3d *o;
			Object3d *face[6];
			Point3d axiscenter;
		};

int BIG_CUBE_MATRIX[3][3][3];
Object BIG_CUBE_M[3][3][3];

void afisare()
{
	for(int i=0;i<6;i++)
	{
		cout << "======================== \n fata "<<i<<endl;
		for(int j=0;j<3;j++)
		{
			for(int k=0;k<3;k++)
				cout<<FACE[i][j][k]<< " ";
	
			cout <<endl;
		}
	}
}

/*
	draws a unit cube
*/
Object3d* WorldDrawer3d::drawCube(float r, float g, float b,float size )
{

	std::vector<Point3d> points;
	std::vector<int> topology;
	points.push_back(Point3d(size,size,size)); //0
	points.push_back(Point3d(size,size,-size)); //1
	points.push_back(Point3d(-size,size,-size)); //2
	points.push_back(Point3d(-size,size,size));//3
	points.push_back(Point3d(size,-size,size));//4
	points.push_back(Point3d(size,-size,-size));//5
	points.push_back(Point3d(-size,-size,-size));//6
	points.push_back(Point3d(-size,-size,size));//7
	topology.push_back(0);topology.push_back(1);topology.push_back(2);	//top
	topology.push_back(2);topology.push_back(3);topology.push_back(0);
	topology.push_back(6);topology.push_back(5);topology.push_back(4);	//bottom
	topology.push_back(7);topology.push_back(4);topology.push_back(6);
	topology.push_back(2);topology.push_back(3);topology.push_back(6);	//left
	topology.push_back(7);topology.push_back(3);topology.push_back(6);
	topology.push_back(0);topology.push_back(1);topology.push_back(5);	//right
	topology.push_back(0);topology.push_back(5);topology.push_back(4);
	topology.push_back(0);topology.push_back(3);topology.push_back(4);	//front
	topology.push_back(7);topology.push_back(3);topology.push_back(4);
	topology.push_back(5);topology.push_back(1);topology.push_back(2);	//back
	topology.push_back(6);topology.push_back(2);topology.push_back(5);
	Object3d *o = new Object3d(points,topology);
	o->setcolor(r,g,b);
	return o;
}

/*
	checks if the rubik cube is solved
*/
bool gameover_test()
{
	
	for(int face =0;face<6;face++)
	{
		
		for(int i=0;i<EDGE;i++)
			for(int j=0;j<EDGE;j++)
				if(FACE[face][i][j] != FACE[face][0][0]) return false;
	}

	
	return true;
}

//renders a bitmap string on the screen
void renderSpacedBitmapString(float x, float y,	void *font,char *string) {

  char *c;
  int x1=x;
 
  for (c=string; *c != '\0'; c++) {
	glColor3f (1,1,0);
	glRasterPos2f(x1,y);
	glutBitmapCharacter(font, *c);
	x1 = x1 + glutBitmapWidth(font,*c) + 3;
  }
}

/*
	initializes the current scene for the bitmap string rendering
*/
void renderScene(int x, int y,char * str) {

// do everything we need to render the world as usual

	// switch to projection mode
	glMatrixMode(GL_PROJECTION);
 
	// save previous matrix which contains the 
	//settings for the perspective projection
	glPushMatrix();

	// reset matrix
	glLoadIdentity();
	
	// set a 2D orthographic projection
	gluOrtho2D(0, 600, 0, 600);

	// invert the y axis, down is positive
	glScalef(1, -1, 1);

	// mover the origin from the bottom left corner
	// to the upper left corner
	glTranslatef(0, -600, 0);

	// switch back to modelview mode
	glMatrixMode(GL_MODELVIEW);



	glPushMatrix();
	glLoadIdentity();
	renderSpacedBitmapString(x,y,GLUT_BITMAP_TIMES_ROMAN_24,str);
	
	glPopMatrix();
	
	glMatrixMode(GL_PROJECTION);
	glPopMatrix();
	glMatrixMode(GL_MODELVIEW);
	glutSwapBuffers();

	
}

/*
	draws the rubik cube
*/
void WorldDrawer3d::init(){
	gamestart = false;
	gameover=false;
	score =0;
	Object3d *o = drawCube(0,0,0,12.505);
	cs_basis.objectAdd(o);
	cs_basis.objectTranslate(o,-5,-5,-5);
	int x,y,z;
	/*
		initialize the vector of matrices for the rubik cube sides
		used for checking if the rubik cube is solved
	*/
	for (int i=0;i<6;i++)
	{
		int **m;
		m=(int **)calloc(SIDE,sizeof(int *));
		for(int j=0;j<9;j++)
		{
			m[j]=(int *) calloc(SIDE,sizeof(int));
			for(int k=0;k<SIDE;k++)
				m[j][k]=i;
		}
			FACE.push_back(m);

	}
	//rubik cube and rubik cube sides 
	int cubeid=0;
	for(z=0;z<3;z++)
		for(y=0;y<3;y++)
			for(x=0;x<3;x++)
			{
				Object newobject;
				Object3d *o=drawCube(0,0,0,3.2);
				cs_basis.objectAdd(o);
				
				
				cs_basis.objectTranslate(o,0+x*10-15,0+y*10-15,0+z*10-15);
				
				newobject.o=o;
				
				newobject.axiscenter=Point3d(0+x*10-15,0+y*10-15,0+z*10-15);
				
					
				    Object3d *o1=drawCube(col_vect[0].r,col_vect[0].g,col_vect[0].b,2.5);
					cs_basis.objectAdd(o1);
					cs_basis.objectTranslate(o1,0+x*10-15,0+y*10-15,0+z*10-14);
					newobject.face[0]=o1;
				
				   Object3d *o2=drawCube(col_vect[1].r,col_vect[1].g,col_vect[1].b,2.5);
					cs_basis.objectAdd(o2);
					cs_basis.objectTranslate(o2,0+x*10-15,0+y*10-14,0+z*10-15);
					newobject.face[1]=o2;
				
					Object3d *o3=drawCube(col_vect[2].r,col_vect[2].g,col_vect[2].b,2.5);
					cs_basis.objectAdd(o3);
					cs_basis.objectTranslate(o3,0+x*10-14,0+y*10-15,0+z*10-15);
					newobject.face[2]=o3;
				
					
					Object3d *o4=drawCube(col_vect[3].r,col_vect[3].g,col_vect[3].b,2.5);
					cs_basis.objectAdd(o4);
					cs_basis.objectTranslate(o4,0+x*10-16,0+y*10-15,0+z*10-15);
					newobject.face[3]=o4;
				
					Object3d *o5=drawCube(col_vect[4].r,col_vect[4].g,col_vect[4].b,2.5);
					cs_basis.objectAdd(o5);
					cs_basis.objectTranslate(o5,0+x*10-15,0+y*10-16,0+z*10-15);
					newobject.face[4]=o5;
				
					Object3d *o6=drawCube(col_vect[5].r,col_vect[5].g,col_vect[5].b,2.5);
					cs_basis.objectAdd(o6);
					cs_basis.objectTranslate(o6,0+x*10-15,0+y*10-15,0+z*10-16);
					newobject.face[5]=o6;
				
	
				BIG_CUBE_MATRIX[z][y][x] = cubeid;
				BIG_CUBE_M[z][y][x]=newobject;
				
				cubeid++;

			}

}


void WorldDrawer3d::onIdle(){	//per frame
	static int iteration=1;
	if(gameover)
	{
	}
		
	iteration++;
		
	
}

/*
	onkey events
*/

void WorldDrawer3d::onKey(unsigned char key){
	Object aux[3][3];
	Object temp;

	
	if(gameover == false)
	switch(key){
		/*
			if ENTER is pressed the game begins
		*/
	case KEY_ENTER:
		{
			if(gamestart == false)
			{
				renderScene(230,30,"GOOD LUCK! :)");
			
				Sleep(5000);
			
			}
			gamestart = true;
	
			break;
		}
	/*
		-PI/6 rotation of the cube around the X,Y,X axis
	*/
	case KEY_LEFT:
			{
				
			switch(currentmove)
			{

			case 'x':
				glRotatef(-ROTATION_ANGLE,1.0f,0.0f,0.0f);
				break;
			case 'y':
				glRotatef(-ROTATION_ANGLE,0.0f,1.0f,0.0f);
				break;
			case 'z':
				glRotatef(-ROTATION_ANGLE,0.0f,0.0f,1.0f);
				break;
			}
			break;
			}
		case KEY_RIGHT:
			{
			switch(currentmove)
			{
		/*
			rotation around X,Y,Z axis with PI/6
		*/
		case 'x':
			//cs_basis.rotateXSelf(PI/6.0);
			glRotatef(ROTATION_ANGLE,1.0f,0.0f,0.0f);
			break;
		case 'y':
			glRotatef(ROTATION_ANGLE,0.0f,1.0f,0.0f);
			break;
		case 'z':
			glRotatef(ROTATION_ANGLE,0.0f,0.0f,1.0f);
			break;
		case 'v':
			
			break;
		case '5':
			{
		
				// 2nd slice parallel with Ox
		
				Point3d point = BIG_CUBE_M[1][1][1].o->axiscenter;
				if(nrmoves == 0)currentmove = '5';
				if (currentmove == '5' ) nrmoves ++;
				update = false;
				/*
					cube rotations
				*/
				if(currentmove == '5')
				for(int y=0;y<=2;y++)
					for(int x=0;x<=2;x++)
						{
							BIG_CUBE_M[1][y][x].o->rotateZRelativeToPoint(Point3d(-5,-5,-5),ROTATION_ANGLE);
							for(int k=0;k<=5;k++)
							BIG_CUBE_M[1][y][x].face[k]->rotateZRelativeToPoint(point,ROTATION_ANGLE);
							if(nrmoves == 3 ){aux[y][x]=BIG_CUBE_M[1][EDGE-x-1][y];update=true;}
						}
			
					/*
					internal matrix & score update

					*/
					if(update)
				{
					nrmoves = 0;
					if(gamestart)score++;
					int aux2[3];
						int i,j,k;
						int col;
				
					for(i=0;i<3;i++)
						aux2[i]=FACE[5][1][i];
					for(i=0;i<3;i++)
					{
						FACE[5][1][i]=FACE[1][i][1];
					}
					
					for(i=0;i<3;i++)
						FACE[1][i][1]=FACE[4][1][2-i];
					for(i=0;i<3;i++)
						FACE[4][1][i]=FACE[3][i][1];
					for(i=0;i<3;i++)
						FACE[3][i][1]=aux2[2-i];
			
				
				
					for (int i = 0; i < 3; ++i) 
						for (int j = 0; j < 3; ++j) 
						{
							BIG_CUBE_M[1][i][j]=aux[i][j];
			
						}
				}
				break;
			}
		case '4':
			{
				//3rd slice parallel with Ox
			   Point3d point = BIG_CUBE_M[2][1][1].o->axiscenter;
				if(nrmoves == 0)currentmove = '4';
				if (currentmove == '4' ) nrmoves ++;
				update = false;
				//cube rotations
				if(currentmove == '4')
				for(int y=0;y<=2;y++)
					for(int x=0;x<=2;x++)
					{
						BIG_CUBE_M[2][y][x].o->rotateZRelativeToPoint(Point3d(-5,-5,-5),ROTATION_ANGLE);
						for(int k=0;k<=5;k++)
							BIG_CUBE_M[2][y][x].face[k]->rotateZRelativeToPoint(point,ROTATION_ANGLE);
						if(nrmoves == 3){aux[y][x]=BIG_CUBE_M[2][EDGE-x-1][y];update=true;}
					}
				//internal matrix &score update
				if(update)
				{
					nrmoves = 0;
					score++;
					int aux2[3];
					int i,j,k;
					int col;

					for(i=0;i<3;i++)
						aux2[i]=FACE[5][2][i];
					for(i=0;i<3;i++)
					{
						FACE[5][2][i]=FACE[1][i][0];
					}
					
					for(i=0;i<3;i++)
						FACE[1][i][0]=FACE[4][2][2-i];
					for(i=0;i<3;i++)
						FACE[4][2][i]=FACE[3][i][2];
					for(i=0;i<3;i++)
						FACE[3][i][2]=aux2[2-i];
				
				
					for (int i = 0; i < 3; ++i) 
						for (int j = 0; j < 3; ++j) 
						{
							BIG_CUBE_M[2][i][j]=aux[i][j];
			
						}
				}
				break;
				}
	
		case '6':
			{
				//1st slice parallel with Ox
				update = false;
			   Point3d point = BIG_CUBE_M[0][1][1].o->axiscenter;
				if(nrmoves == 0)currentmove = '6';
				if (currentmove == '6' ) nrmoves ++;
				// cube rotations
				if(currentmove == '6')
				for(int y=0;y<=2;y++)
					for(int x=0;x<=2;x++)
					{
						BIG_CUBE_M[0][y][x].o->rotateZRelativeToPoint(Point3d(-5,-5,-5),ROTATION_ANGLE);
						for(int k=0;k<=5;k++)
							BIG_CUBE_M[0][y][x].face[k]->rotateZRelativeToPoint(point,ROTATION_ANGLE);
						if(nrmoves == 3){aux[y][x]=BIG_CUBE_M[0][EDGE-x-1][y];update=true;}
					
					}
				//internal matrix & score update
				if(update)
				{
					nrmoves = 0;
					score++;
					int aux2[3];
					int i,j,k;
					int col;
					for(i=0;i<3;i++)
						aux2[i]=FACE[5][0][i];
					for(i=0;i<3;i++)
					{
						FACE[5][0][i]=FACE[1][i][2];
					}
					
					for(i=0;i<3;i++)
						FACE[1][i][2]=FACE[4][0][2-i];
					for(i=0;i<3;i++)
						FACE[4][0][i]=FACE[3][i][0];
					for(i=0;i<3;i++)
						FACE[3][i][0]=aux2[2-i];
					for (int i = 0; i < 3; ++i) 
						for (int j = 0; j < 3; ++j) 
						{
							BIG_CUBE_M[0][i][j]=aux[i][j];
 
						}
				}
			
				break;
			}
		case '9':
			{
				//3rd slice parallel with Oz
				Point3d point = BIG_CUBE_M[1][1][2].o->axiscenter;
				if(nrmoves == 0)currentmove = '9';
				if (currentmove == '9' ) nrmoves ++;
				update = false;
				if(currentmove == '9')
				//cube rotations
				for(int z=0;z<=2;z++)
					for(int y=0;y<=2;y++)
					{
						BIG_CUBE_M[z][y][2].o->rotateXRelativeToPoint(Point3d(-5,-5,-5),ROTATION_ANGLE);
					
						for(int k=0;k<=5;k++)
						
							BIG_CUBE_M[z][y][2].face[k]->rotateXRelativeToPoint(point,ROTATION_ANGLE);
						
						if(nrmoves == 3)
						{
							aux[z][y] = BIG_CUBE_M[EDGE-y-1][z][2];update = true;
							
						
						}
					}
					
				//internal matrix update & score 
				if(update)
				{
					nrmoves = 0;
					score++;
					int aux2[3];
						int i,j,k;
						int col;
							
				
					for(i=0;i<3;i++)
						aux2[i]=FACE[5][i][2];
				
					for(i=0;i<3;i++)
						FACE[5][i][2]=FACE[2][2-i][0];
					
					for (i=0;i<3;i++)
						FACE[2][i][0]=FACE[4][i][2];

					for (i=0;i<3;i++)
						FACE[4][i][2]=FACE[0][2-i][2];
					for (i=0;i<3;i++)	
					FACE[0][i][2]=aux2[i];
					for (int i = 0; i < 3; ++i) 
						for (int j = 0; j < 3; ++j) 
						{
							BIG_CUBE_M[i][j][2]=aux[i][j];
		   
						}
				}
						
			break;
		
			
			}
		case '8':
			{
				//2nd slice parallel witch Oz 
				Point3d point = BIG_CUBE_M[1][1][1].o->axiscenter;
				if(nrmoves == 0)currentmove = '8';
				if (currentmove == '8' ) nrmoves ++;
				update = false;
				if(currentmove == '8')
				for(int z=0;z<=2;z++)
					for(int y=0;y<=2;y++)
					{
						BIG_CUBE_M[z][y][1].o->rotateXRelativeToPoint(Point3d(-5,-5,-5),ROTATION_ANGLE);
						for(int k=0;k<=5;k++)
							BIG_CUBE_M[z][y][1].face[k]->rotateXRelativeToPoint(point,ROTATION_ANGLE);
						if(nrmoves == 3){aux[z][y] = BIG_CUBE_M[EDGE-y-1][z][1];update = true;}
					
					}
				//internal matrix & score update	
				if(update)
				{
					nrmoves = 0;
					score++;
					int aux2[3];
						int i,j,k;
						int col;
							
					for(i=0;i<3;i++)
						aux2[i]=FACE[5][i][1];
				
					for(i=0;i<3;i++)
						FACE[5][i][1]=FACE[2][2-i][1];
					
					for (i=0;i<3;i++)
						FACE[2][i][1]=FACE[4][i][1];

					for (i=0;i<3;i++)
						FACE[4][i][1]=FACE[0][2-i][1];
					for (i=0;i<3;i++)	
						FACE[0][i][1]=aux2[i];
	
					for (int i = 0; i < 3; ++i) 
						for (int j = 0; j < 3; ++j) 
					{
							BIG_CUBE_M[i][j][1]=aux[i][j];
			
					}
				}
				break;
			}
		case '7':
		{
				//1st slice parallel with Oz
				Point3d point = BIG_CUBE_M[1][1][0].o->axiscenter;
				if(nrmoves == 0)currentmove = '7';
				if (currentmove == '7' ) nrmoves ++;
				update = false;
				if(currentmove == '7')
				// cube rotations
				for(int z=0;z<=2;z++)
					for(int y=0;y<=2;y++)
					{
						BIG_CUBE_M[z][y][0].o->rotateXRelativeToPoint(Point3d(-5,-5,-5),ROTATION_ANGLE);
						for(int k=0;k<=5;k++)
							BIG_CUBE_M[z][y][0].face[k]->rotateXRelativeToPoint(point,ROTATION_ANGLE);
						if(nrmoves == 3){	aux[z][y] = BIG_CUBE_M[EDGE-y-1][z][0];update = true;}
					}
				//internal matrix & score update
				if(update)
				{
					nrmoves = 0;
					score++;
					int aux2[3];
						int i,j,k;
						int col;
				
					for(i=0;i<3;i++)
						aux2[i]=FACE[5][i][0];
				
					for(i=0;i<3;i++)
						FACE[5][i][0]=FACE[2][2-i][2];
					
					for (i=0;i<3;i++)
						FACE[2][i][2]=FACE[4][i][0];

					for (i=0;i<3;i++)
						FACE[4][i][0]=FACE[0][2-i][0];
					for (i=0;i<3;i++)	
						FACE[0][i][0]=aux2[i];
					for (int i = 0; i < 3; ++i) 
						for (int j = 0; j < 3; ++j) 
						{
							BIG_CUBE_M[i][j][0]=aux[i][j];
			
						}
					}
			break;
			}
		case '3':
			{
				//1st slice parallel with Oy
				Point3d point = BIG_CUBE_M[1][0][1].o->axiscenter;
				if(nrmoves == 0)currentmove = '3';
				if (currentmove == '3') nrmoves ++;
				update = false;
				if(currentmove == '3')
				//cube rotations
				for(int z=0;z<=2;z++)
					for(int x=0;x<=2;x++)
						{
							BIG_CUBE_M[z][0][x].o->rotateYRelativeToPoint(Point3d(-5,-5,-5),ROTATION_ANGLE);
							for(int k=0;k<=5;k++)
							BIG_CUBE_M[z][0][x].face[k]->rotateYRelativeToPoint(point,ROTATION_ANGLE);
							if(nrmoves == 3){aux[z][x]=BIG_CUBE_M[EDGE-x-1][0][z];update = true;}
				
							
						}

				//internal matrix update & score
				if(update)
				{
					nrmoves = 0;
					score++;
					int aux2[3];
						int i,j,k;
					for(i=0;i<3;i++)
						aux2[i]=FACE[0][2][i];
					//updatez 4 fete
					for(i=1;i<=3;i++)
						for(j=0;j<3;j++)
						FACE[i-1][2][j]=FACE[i][2][j];
					for(i=0;i<3;i++)
						FACE[3][2][i]=aux2[i];

					for (int i = 0; i < 3; ++i) 
						for (int j = 0; j < 3; ++j) 
						{

							BIG_CUBE_M[i][0][j]=aux[i][j];
	
						}
				}

				break;
			}
			
		case '2':
			{
				//2nd slice parallel with Oy
				Point3d point = BIG_CUBE_M[1][1][1].o->axiscenter;
				if(nrmoves == 0)currentmove = '2';
				if (currentmove == '2' ) nrmoves ++;
				update = false;
				//cube rotations
				if(currentmove == '2')
				for(int z=0;z<=2;z++)
					for(int x=0;x<=2;x++)
					{
						BIG_CUBE_M[z][1][x].o->rotateYRelativeToPoint(Point3d(-5,-5,-5),ROTATION_ANGLE);
						for(int k=0;k<=5;k++)
							BIG_CUBE_M[z][1][x].face[k]->rotateYRelativeToPoint(point,ROTATION_ANGLE);

						if(nrmoves == 3){aux[z][x]=BIG_CUBE_M[EDGE-x-1][1][z];update = true;}
					}

	
				//internal matrix & score update
						if(update)
				{
					nrmoves = 0;
					score++;
					int aux2[3];
					int i,j,k;
					for(i=0;i<3;i++)
						aux2[i]=FACE[0][1][i];
					for(i=1;i<=3;i++)
						for(j=0;j<3;j++)
						FACE[i-1][1][j]=FACE[i][1][j];
					for(i=0;i<3;i++)
						FACE[3][1][i]=aux2[i];
			
					for (int i = 0; i < 3; ++i) 
						for (int j = 0; j < 3; ++j) 
					{

						BIG_CUBE_M[i][1][j]=aux[i][j];
			
					}
				}
					break;
			
			}
			case '1':
			{
				//3rd slice parallel with Oy rotation
				Point3d point = BIG_CUBE_M[1][2][1].o->axiscenter;
				if(nrmoves == 0)currentmove = '1';
				if (currentmove == '1' ) nrmoves ++;
				update = false;
				//cube rotations
				if(currentmove == '1')
				for(int z=0;z<=2;z++)
					for(int x=0;x<=2;x++)
						{
							BIG_CUBE_M[z][2][x].o->rotateYRelativeToPoint(Point3d(-5,-5,-5),ROTATION_ANGLE);
							for(int k=0;k<=5;k++)
							BIG_CUBE_M[z][2][x].face[k]->rotateYRelativeToPoint(point,ROTATION_ANGLE);
							if(nrmoves == 3){aux[z][x]=BIG_CUBE_M[EDGE-x-1][2][z];update = true;}
					}

				//internal matrix & score update	
				if(update)
				{
					nrmoves = 0;
					score++;
					int aux2[3];
						int i,j,k;
					for(i=0;i<3;i++)
						aux2[i]=FACE[0][0][i];
					for(i=1;i<=3;i++)
						for(j=0;j<3;j++)
						FACE[i-1][0][j]=FACE[i][0][j];
					for(i=0;i<3;i++)
						FACE[3][0][i]=aux2[i];
					
					for (int i = 0; i < 3; ++i) 
						for (int j = 0; j < 3; ++j) 
						{

							BIG_CUBE_M[i][2][j]=aux[i][j];
					
						}
				}
					break;
			}

			}




			break;
			}
	
		case KEY_SPACE:
				animation=!animation;
			break;
		//moves update
		case 'x':
			currentmove='x';
			break;
		case 'y':
			currentmove='y';
			break;
		case 'z':
			currentmove='z';
			break;
		default:
			if (key>='1' && key<='9' && nrmoves == 0) currentmove=key;
			break;
	}

	
	if((gameover_test()==true) && gamestart )
	{ 

		gameover = true;

	}
	




	
}


int main(int argc, char** argv){
	WorldDrawer3d wd3d(argc,argv,600,600,200,100,std::string("Rubik Cube"));
	
	wd3d.init();
	
	
	
	wd3d.run();

	return 0;
}
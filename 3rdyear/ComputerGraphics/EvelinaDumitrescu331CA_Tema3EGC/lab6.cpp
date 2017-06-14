//DUMITRESCU EVELINA 331CA
//include librarii de opengl, glut si glu
#pragma comment(lib, "opengl32.lib")
#pragma comment(lib, "glu32.lib")
#pragma comment(lib, "glut32.lib")

//includes
#include <stdio.h>
#include <iostream>
#include <stdlib.h>
#include <sstream>
#include <string>

//glut and glew
#include "glut.h"

//ground
#include "ground.h"

//camera
#include "camera.h"
using namespace std;

//definire constante
#define NX 20
#define NY 16
#define UNIT 2.0
#define WON 1
#define LOST 2
#define PI 3.1415926535897932384626433832795
#define ROTATION_ANGLE PI/12.0
#define TOPDOWN 1
#define FPS 2
#define TPS 3
#define DIST 0.2
#define MAX_DIST 0.301

static float spin;
static  int iterations;

//cam
Camera cameraFPS, cameraTPS;
int MODE2=TOPDOWN;
//definire corpuri pentru ilumininari corpuri
const GLfloat light_ambient[]  = { 0.0f, 0.0f, 0.0f, 1.0f };
const GLfloat light_diffuse[]  = { 1.0f, 1.0f, 1.0f, 1.0f };
const GLfloat light_specular[] = { 1.0f, 1.0f, 1.0f, 1.0f };
const GLfloat light_position[] = { 2.0f, 5.0f, 5.0f, 0.0f };

const GLfloat mat_ambient[]    = { 0.7f, 0.7f, 0.7f, 1.0f };
const GLfloat mat_diffuse[]    = { 0.8f, 0.8f, 0.8f, 1.0f };
const GLfloat mat_specular[]   = { 1.0f, 1.0f, 1.0f, 1.0f };
const GLfloat high_shininess[] = { 100.0f }; 

float angle=0;
bool ok =true;
//matrice labirint
static int Map[100][100];
bool gameover=false;
int won_or_lost;
struct character
{
	float direction;

	float rotation;
	float color;
	float life;
	float power;
	float x;
	float y;
	float z;
};
//chareacters
character Hero2;
character Artifact;
character Enemy1,Enemy2,Enemy3;
//functie afisare

void afisare_matrice1()
{
	cout<<"\n============================================\n";
	for(int i=0;i<NX;i++){
		for(int j=0;j<NY;j++)
			cout<<Map[i][j]<<" ";
		cout<<"\n";	
	}


}
void afisare_rotations()
{
	cout<<"\n====== FPS ====="<<endl;
	for(int i=0;i<cameraFPS.Rotations.size();i++)
		cout<<"axis"<<cameraFPS.Rotations.at(i).axis<<"angle"<<cameraFPS.Rotations.at(i).angle<<endl;
	cout<<"\n====== TPS ====="<<endl;
	for(int i=0;i<cameraTPS.Rotations.size();i++)
		cout<<"axis"<<cameraTPS.Rotations.at(i).axis<<"angle"<<cameraTPS.Rotations.at(i).angle<<endl;
}
/*
	afisare mesaj pentru gameover
*/
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
void renderText(int x, int y,char * str) {

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
	resetare joc
*/
void resetgame()
{
	gameover=false;
	won_or_lost=0;
	Hero2.x=Hero2.z=UNIT+UNIT/2.0;
	Hero2.rotation=0.0;
	Hero2.x+=UNIT*4.0;
	Hero2.direction=0;
	Enemy1.x=UNIT*14.0+UNIT/2.0;
	Enemy1.z=UNIT*4.0+UNIT/2.0;
	Enemy1.direction=1;
	Enemy2.x=UNIT+UNIT/2.0;
	Enemy2.z=UNIT*15.0+UNIT/2.0;
	Enemy2.direction=1;
	Enemy3.x=UNIT*9.0+UNIT/2.0;
	Enemy3.z=UNIT*6.0+UNIT/2.0;
	Enemy3.direction=1;
	Artifact.x=UNIT*5.0+UNIT/2.0;
	Artifact.z=UNIT*11.0+UNIT/2.0;

	MODE2=FPS;




	//set background
	glClearColor(0.35,0.5,0.95,1.0);

	//init camera
	Vector3D position, forward,up,right;
	position = Vector3D(0,-10.0,0);
	forward = Vector3D(0,0,-1);
	right =Vector3D(1,0,0);
	up = Vector3D(0,1,0);

	cameraFPS.init(position,forward,up,right, FPS);
	cameraFPS.type=FPS;
	cameraFPS.translate_Right(5.0*UNIT+UNIT/2.0);
	cameraFPS.translate_Forward(-UNIT-UNIT/2.0);


	cameraFPS.rotateFPS_OY(PI/2);
	cameraFPS.directiony=0;
	position = Vector3D(0,-8.0,0);
	forward = Vector3D(0,0,-1);
	right =Vector3D(1,0,0);
	up = Vector3D(0,1,0);
	cameraTPS.init(position,forward,up,right, TPS);
	cameraTPS.translate_Right(UNIT+UNIT/2.0);
	cameraTPS.translate_Forward(-UNIT-UNIT/2.0);
	cameraTPS.type=TPS;
	cameraTPS.rotateFPS_OY(PI/2);
	cameraTPS.directiony=0;

}
/*
	metoda pentru randarea scenei
*/
void display(){
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	//gamover test
	if((int)Enemy1.z*100 == (int)Hero2.z*100)

	{

		if(Hero2.x>28.0 && Hero2.x<30.0)
		{
			gameover = true;
			won_or_lost=LOST;
			
		}
	}
	if((int)Enemy2.z*100 == (int)Hero2.z*100&& (Hero2.x>2.0 && Hero2.x<4.0))

	{

		gameover = true;
		won_or_lost=LOST;
		
	}
	if((int)Enemy3.z*100 == (int)Hero2.z*100&& (Hero2.x>18.0 && Hero2.x<20.0))
	{
		gameover = true;
		won_or_lost=LOST;
		
	}
	if((int)Artifact.z*10 == (int)Hero2.z*10 && (int)Artifact.x*10 ==(int) Hero2.x*10)
	{
		gameover = true;
		won_or_lost=WON;
		
	}
	//afisare mesaj pentru gameover + resetare scena
	if(gameover)
	{
		if(won_or_lost == WON)renderText(100,200,"GAME OVER ! YOU WIN! :D");
		else renderText(100,200,"GAME OVER ! YOU LOST! :-(");
		iterations ++;
		if(iterations == 400) 
		{
			iterations =0;
			resetgame();
		}
	}
	else
	{
		//setup view
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		if(MODE2==FPS){cameraFPS.render(0);}
		if(MODE2==TPS){cameraTPS.render(1.0);}
		if(MODE2==TOPDOWN)gluLookAt(UNIT*8.0,80,UNIT*10.0,UNIT*8.0,-8,UNIT*10.0,0,-1,1);
		glEnable (GL_BLEND);
		glBlendFunc (GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		spin+=0.1;
		glPushMatrix();
		glColor3f( 1,0,0);

		//desenare erou
		glTranslatef(Hero2.x,-10, Hero2.z);
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);

		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LESS);

		glEnable(GL_LIGHT0);
		glEnable(GL_NORMALIZE);
		glEnable(GL_COLOR_MATERIAL);
		glEnable(GL_LIGHTING);

		glLightfv(GL_LIGHT0, GL_AMBIENT,  light_ambient);
		glLightfv(GL_LIGHT0, GL_DIFFUSE,  light_diffuse);
		glLightfv(GL_LIGHT0, GL_SPECULAR, light_specular);
		glLightfv(GL_LIGHT0, GL_POSITION, light_position);

		glMaterialfv(GL_FRONT, GL_AMBIENT,   mat_ambient);
		glMaterialfv(GL_FRONT, GL_DIFFUSE,   mat_diffuse);
		glMaterialfv(GL_FRONT, GL_SPECULAR,  mat_specular);
		glMaterialfv(GL_FRONT, GL_SHININESS, high_shininess); 
		glRotatef(-((Hero2.rotation *360.0)/(2.0*PI)-90.0), 0,1,0);
		glutSolidCone(UNIT/6.0,UNIT/3.0,20,20);
		glutSolidSphere(UNIT/6.0,20,20);
		glDisable(GL_CULL_FACE);

		glDisable(GL_LIGHT0);
		glDisable(GL_NORMALIZE);
		glDisable(GL_COLOR_MATERIAL);
		glDisable(GL_LIGHTING);

		glPopMatrix();

		//desenare inamic 1
		glPushMatrix();
		glColor3f( 0,1,0);
		glTranslatef(Enemy1.x,-10, Enemy1.z);
		if(Enemy1.z<UNIT*17.0 && Enemy1.direction==1)Enemy1.z+=DIST/20.0;
		if(Enemy1.z>=UNIT*17.0){Enemy1.direction*=-1;Enemy1.z-=DIST/20.0;}
		if(Enemy1.z>=UNIT *4.0 && Enemy1.direction==-1){Enemy1.z-=DIST/20.0;}
		if(Enemy1.z<UNIT *4.0){Enemy1.direction*=-1;Enemy1.z+=DIST/20.0;}

		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);

		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LESS);

		glEnable(GL_LIGHT0);
		glEnable(GL_NORMALIZE);
		glEnable(GL_COLOR_MATERIAL);
		glEnable(GL_LIGHTING);

		glLightfv(GL_LIGHT0, GL_AMBIENT,  light_ambient);
		glLightfv(GL_LIGHT0, GL_DIFFUSE,  light_diffuse);
		glLightfv(GL_LIGHT0, GL_SPECULAR, light_specular);
		glLightfv(GL_LIGHT0, GL_POSITION, light_position);

		glMaterialfv(GL_FRONT, GL_AMBIENT,   mat_ambient);
		glMaterialfv(GL_FRONT, GL_DIFFUSE,   mat_diffuse);
		glMaterialfv(GL_FRONT, GL_SPECULAR,  mat_specular);
		glMaterialfv(GL_FRONT, GL_SHININESS, high_shininess); 

		glRotatef(spin, 0,1,0);
		glutSolidTorus(UNIT/9.0,UNIT/2.5,50,100);

		glDisable(GL_CULL_FACE);

		glDisable(GL_LIGHT0);
		glDisable(GL_NORMALIZE);
		glDisable(GL_COLOR_MATERIAL);
		glDisable(GL_LIGHTING);

		glPopMatrix();

		//desenare inamic 2
		glPushMatrix();
		glColor3f( 0,1,1);


		glTranslatef(Enemy2.x,-10, Enemy2.z);
		if(Enemy2.z<=UNIT*15.0 && Enemy2.direction==1)Enemy2.z-=DIST/20.0;
		if(Enemy2.z<UNIT*4.0){Enemy2.direction*=-1;Enemy2.z+=DIST/20.0;}
		if(Enemy2.z>=UNIT *4.0 && Enemy2.direction==-1){Enemy2.z+=DIST/20.0;}
		if(Enemy2.z>UNIT *15.0){Enemy2.direction*=-1;Enemy2.z-=DIST/20.0;}

		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);

		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LESS);

		glEnable(GL_LIGHT0);
		glEnable(GL_NORMALIZE);
		glEnable(GL_COLOR_MATERIAL);
		glEnable(GL_LIGHTING);

		glLightfv(GL_LIGHT0, GL_AMBIENT,  light_ambient);
		glLightfv(GL_LIGHT0, GL_DIFFUSE,  light_diffuse);
		glLightfv(GL_LIGHT0, GL_SPECULAR, light_specular);
		glLightfv(GL_LIGHT0, GL_POSITION, light_position);

		glMaterialfv(GL_FRONT, GL_AMBIENT,   mat_ambient);
		glMaterialfv(GL_FRONT, GL_DIFFUSE,   mat_diffuse);
		glMaterialfv(GL_FRONT, GL_SPECULAR,  mat_specular);
		glMaterialfv(GL_FRONT, GL_SHININESS, high_shininess); 

		glRotatef(spin, 0,1,0);
		glutSolidTorus(UNIT/9.0,UNIT/2.5,50,100);

		glDisable(GL_CULL_FACE);

		glDisable(GL_LIGHT0);
		glDisable(GL_NORMALIZE);
		glDisable(GL_COLOR_MATERIAL);
		glDisable(GL_LIGHTING);

		glPopMatrix();

		//desenare inamic 3
		glPushMatrix();
		glColor3f( 1,1,0);


		glTranslatef(Enemy3.x,-10, Enemy3.z);
		if(Enemy3.z<=UNIT*14.0 && Enemy3.direction==1)Enemy3.z+=DIST/20.0;
		if(Enemy3.z<UNIT*6.0){Enemy3.direction*=-1;Enemy3.z+=DIST/20.0;}
		if(Enemy3.z>=UNIT *6.0 && Enemy3.direction==-1){Enemy3.z-=DIST/20.0;}
		if(Enemy3.z>UNIT *14.0){Enemy3.direction*=-1;Enemy3.z-=DIST/20.0;}

		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);

		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LESS);

		glEnable(GL_LIGHT0);
		glEnable(GL_NORMALIZE);
		glEnable(GL_COLOR_MATERIAL);
		glEnable(GL_LIGHTING);

		glLightfv(GL_LIGHT0, GL_AMBIENT,  light_ambient);
		glLightfv(GL_LIGHT0, GL_DIFFUSE,  light_diffuse);
		glLightfv(GL_LIGHT0, GL_SPECULAR, light_specular);
		glLightfv(GL_LIGHT0, GL_POSITION, light_position);

		glMaterialfv(GL_FRONT, GL_AMBIENT,   mat_ambient);
		glMaterialfv(GL_FRONT, GL_DIFFUSE,   mat_diffuse);
		glMaterialfv(GL_FRONT, GL_SPECULAR,  mat_specular);
		glMaterialfv(GL_FRONT, GL_SHININESS, high_shininess); 


		glRotatef(spin, 0,1,0);
		glutSolidTorus(UNIT/9.0,UNIT/2.5,50,100);
	
		glDisable(GL_CULL_FACE);

		glDisable(GL_LIGHT0);
		glDisable(GL_NORMALIZE);
		glDisable(GL_COLOR_MATERIAL);
		glDisable(GL_LIGHTING);

		glPopMatrix();


		glPushMatrix();
		glColor3f( 1,0,0.5);

		//desenare punct final in labirint
		glTranslatef(Artifact.x,-10, Artifact.z);

		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);

		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LESS);

		glEnable(GL_LIGHT0);
		glEnable(GL_NORMALIZE);
		glEnable(GL_COLOR_MATERIAL);
		glEnable(GL_LIGHTING);

		glLightfv(GL_LIGHT0, GL_AMBIENT,  light_ambient);
		glLightfv(GL_LIGHT0, GL_DIFFUSE,  light_diffuse);
		glLightfv(GL_LIGHT0, GL_SPECULAR, light_specular);
		glLightfv(GL_LIGHT0, GL_POSITION, light_position);

		glMaterialfv(GL_FRONT, GL_AMBIENT,   mat_ambient);
		glMaterialfv(GL_FRONT, GL_DIFFUSE,   mat_diffuse);
		glMaterialfv(GL_FRONT, GL_SPECULAR,  mat_specular);
		glMaterialfv(GL_FRONT, GL_SHININESS, high_shininess); 

		glRotatef(spin, 0,1,0);
		glutSolidSphere(UNIT/2.0,80,80);

		glDisable(GL_CULL_FACE);

		glDisable(GL_LIGHT0);
		glDisable(GL_NORMALIZE);
		glDisable(GL_COLOR_MATERIAL);
		glDisable(GL_LIGHTING);

		glPopMatrix();
		//desenare cuburi pentru peretii labirintului
		for(int i=0;i<NX;i++){
			for(int j=0;j<NY;j++){


				if(Map[i][j] == 1)
				{

					glPushMatrix();
					glColor3f( 0,0,0.1);
					glTranslatef(j*UNIT+UNIT/2.0, -10, i*UNIT+UNIT/2.0);

					glutSolidCube(UNIT);

					glColor3f( 0,0,0);
				
					glutWireCube(UNIT);
					glPopMatrix();
				
				}

				glPushMatrix();
				glColor3f( 0.8,0.8,0.8);
				glTranslatef(j*UNIT+UNIT/2.0, -12, i*UNIT+UNIT/2.0);

			
				glutSolidCube(UNIT-0.01);
				glColor3f( 0.5,0.5,0.7);

				glutWireCube(UNIT);
				glPopMatrix();
		




			}
			
		}

		glutSwapBuffers();
	}
}
void reshape(int width, int height){
	//set viewport
	glViewport(0,0,width,height);

	//set proiectie
	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	gluPerspective(45,(float)width/(float)height,0.2,300);
}

void idle();

void keyboard(unsigned char ch, int x, int y){
	float tempz,tempx;
	int x1,z1;
	bool collision = false;


	rotation rot;

	if(Hero2.rotation>2.0*PI) Hero2.rotation-=2.0*PI;
	if(Hero2.rotation<0.0) Hero2.rotation=2.0*PI+Hero2.rotation;
	switch(ch){

	case 27:	//esc
		exit(0);
		break;
	case '1': // set top down view
		MODE2=TOPDOWN;
		break;
	case '2': // set fps view
		MODE2=FPS;
		break;
	case '3': //set tps view
		MODE2=TPS;
		break;
	case 'w': //move forward

		
		if(gameover == false)
		{
			

			tempx=Hero2.x;
			tempz=Hero2.z;
			tempx+=DIST*cos(Hero2.rotation);
			tempz+=DIST*sin(Hero2.rotation);

			x1=(int)(tempx/UNIT);
			z1=(int)(tempz/UNIT);
			collision = false;
			
			//collision detection test
			if(Map[z1][x1] == 1)collision=true;
			if(Map[z1+1][x1]==1)
			{

				if((UNIT*(float)((float)z1+1.0)-tempz)*(UNIT*(float)((float)z1+1.0)-tempz)<(MAX_DIST*MAX_DIST))
				{
					collision=true;
					
				}
			}
		
			if(Map[z1-1][x1]==1)
			{

				if((UNIT*(float)z1-tempz)*(UNIT*(float)z1-tempz)<(MAX_DIST*MAX_DIST)){collision=true;}
			}
			if(Map[z1][x1+1]==1)
			{

				if((UNIT*(float)((float)x1+1.0)-tempx)*(UNIT*(float)((float)x1+1.0)-tempx)<(MAX_DIST*MAX_DIST)){collision=true;}
			}
			if(Map[z1][x1-1]==1)
			{

				if((UNIT*(float)x1-tempx)*(UNIT*(float)x1-tempx)<(MAX_DIST*MAX_DIST)){collision=true;}
			}

			
			if(collision == false)
			{
				
				Hero2.x=tempx;Hero2.z=tempz;
				cameraFPS.translate_Forward(DIST);
				cameraTPS.translate_Forward(DIST);
			}

		}

		break;

	case 's': //move backward 
		
	
		if(gameover == false)
		{

		
			tempx=Hero2.x;
			tempz=Hero2.z;
			tempx-=DIST*cos(Hero2.rotation);
			tempz-=DIST*sin(Hero2.rotation);

			x1=(int)(tempx/UNIT);
			z1=(int)(tempz/UNIT);
			collision = false;

			//collision detection test
			if(Map[z1][x1] == 1)collision=true;
			if(Map[z1+1][x1]==1)
			{

				if((UNIT*(float)((float)z1+1.0)-tempz)*(UNIT*(float)((float)z1+1.0)-tempz)<(MAX_DIST*MAX_DIST))
				{
					collision=true;
					
				}
			}
		
			if(Map[z1-1][x1]==1)
			{

				if((UNIT*(float)z1-tempz)*(UNIT*(float)z1-tempz)<(MAX_DIST*MAX_DIST)){collision=true;}
			}
			if(Map[z1][x1+1]==1)
			{

				if((UNIT*(float)((float)x1+1.0)-tempx)*(UNIT*(float)((float)x1+1.0)-tempx)<(MAX_DIST*MAX_DIST)){collision=true;}
			}
			if(Map[z1][x1-1]==1)
			{

				if((UNIT*(float)x1-tempx)*(UNIT*(float)x1-tempx)<(MAX_DIST*MAX_DIST)){collision=true;}
			}

			
			if(collision == false) {Hero2.x=tempx;Hero2.z=tempz;;cameraFPS.translate_Forward(-DIST);
			cameraTPS.translate_Forward(-DIST);}	
	


		}
		break;
	case 'a': // rotate left
		
		if(gameover == false)
		{
			
			Hero2.rotation-=ROTATION_ANGLE;
			cameraFPS.rotateFPS_OY(-ROTATION_ANGLE);
			cameraTPS.rotateTPS_OY(-ROTATION_ANGLE,DIST_HERO_CAMERA);

	


		}
		break;
	case 'd': //rotate right
		if(gameover == false)
		{
			
			Hero2.rotation+=ROTATION_ANGLE;
			cameraFPS.rotateFPS_OY(ROTATION_ANGLE);
			cameraTPS.rotateTPS_OY(ROTATION_ANGLE,DIST_HERO_CAMERA);

		


		}
		break;
	case 'q': //move up
		cameraFPS.translate_Up(0.5);
		cameraTPS.translate_Up(0.5);
		break;				
	case 'z': //move down
		cameraFPS.translate_Up(-0.5);
		cameraTPS.translate_Up(-0.5);
		break;
	case 'e'://rotate to the left on Ox axis
		cameraFPS.rotateFPS_OX(-ROTATION_ANGLE);
		cameraTPS.rotateTPS_OX(-ROTATION_ANGLE,DIST_HERO_CAMERA);

		break;
	case 'r'://rotate to the right on the Ox axis
		cameraFPS.rotateFPS_OX(ROTATION_ANGLE);
		cameraTPS.rotateTPS_OX(ROTATION_ANGLE,DIST_HERO_CAMERA);



		break;
	case 'f'://rotate to the left on Oy axis
		cameraFPS.rotateFPS_OY(-ROTATION_ANGLE);
		cameraTPS.rotateTPS_OY(-ROTATION_ANGLE,DIST_HERO_CAMERA);



		break;
	case 'g'://rotate to the right on the Oy axis
		cameraFPS.rotateFPS_OY(ROTATION_ANGLE);
		cameraTPS.rotateTPS_OY(ROTATION_ANGLE,DIST_HERO_CAMERA);



		break;
	case 'x'://rotate to the left on Oz axis
		cameraFPS.rotateFPS_OZ(-ROTATION_ANGLE);
		cameraTPS.rotateTPS_OZ(-ROTATION_ANGLE,DIST_HERO_CAMERA);



		break;
	case 'c'://rotate to the right on the Oz axis
		cameraFPS.rotateFPS_OZ(ROTATION_ANGLE);
		cameraTPS.rotateTPS_OZ(ROTATION_ANGLE,DIST_HERO_CAMERA);



		break;

	default:
		break;
	}
	
}


//idle
void idle(){
	angle = angle+0.01;
	if(angle >360) angle = angle-360;
	glutPostRedisplay();
}



int main(int argc, char *argv[]){

	FILE *fin=fopen("map.in","r");
	gameover=false;

	//citire matrice
	for(int i=0;i<NX;i++)
	{
		for(int j=0;j<NY;j++)
		{
			fscanf(fin,"%d",&Map[i][j]);
			
		}
		
	}

	//initializare pozitire personaje
	Hero2.x=Hero2.z=UNIT+UNIT/2.0;
	Hero2.rotation=0.0;
	Hero2.x+=UNIT*4.0;
	Hero2.direction=0;
	Enemy1.x=UNIT*14.0+UNIT/2.0;
	Enemy1.z=UNIT*4.0+UNIT/2.0;
	Enemy1.direction=1;
	Enemy2.x=UNIT+UNIT/2.0;
	Enemy2.z=UNIT*15.0+UNIT/2.0;
	Enemy2.direction=1;
	Enemy3.x=UNIT*9.0+UNIT/2.0;
	Enemy3.z=UNIT*6.0+UNIT/2.0;
	Enemy3.direction=1;
	Artifact.x=UNIT*5.0+UNIT/2.0;
	Artifact.z=UNIT*11.0+UNIT/2.0;

	//init glut
	MODE2=FPS;
	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_RGBA | GLUT_DOUBLE | GLUT_DEPTH);

	//init window
	glutInitWindowSize(800,800);
	glutInitWindowPosition(10,10);
	glutCreateWindow("I'M LOST");

	//callbacks
	glutDisplayFunc(display);
	glutReshapeFunc(reshape);
	glutKeyboardFunc(keyboard);
	glutIdleFunc(idle);


	//z test on
	glEnable(GL_DEPTH_TEST);

	//set background
	glClearColor(0.35,0.5,0.95,1.0);
	
	//init camera
	Vector3D position, forward,up,right;
	position = Vector3D(0,-10.0,0);
	forward = Vector3D(0,0,-1);
	right =Vector3D(1,0,0);
	up = Vector3D(0,1,0);


	cameraFPS.init(position,forward,up,right, FPS);
	cameraFPS.type=FPS;
	cameraFPS.translate_Right(5.0*UNIT+UNIT/2.0);
	cameraFPS.translate_Forward(-UNIT-UNIT/2.0);


	cameraFPS.rotateFPS_OY(PI/2);
	cameraFPS.directiony=0;
	position = Vector3D(0,-9.0,0);
	forward = Vector3D(0,0,-1);
	right =Vector3D(1,0,0);
	up = Vector3D(0,1,0);
	cameraTPS.init(position,forward,up,right, TPS);
	cameraTPS.translate_Right(UNIT+UNIT/2.0);
	cameraTPS.translate_Forward(-UNIT-UNIT/2.0);
	cameraTPS.type=TPS;



	cameraTPS.rotateFPS_OY(PI/2);
	cameraTPS.directiony=0;

	glutMainLoop();

	return 0;
}
//Tema 4 EGC 
// DUMITRESCU EVELINA 331CA


#include <stdlib.h>
#include <glut.h>
#include <stdio.h>
#include <stdarg.h>
#include <string.h>
#include "Support3d.h"
#include "SpaceShip.h"
#include "Camera.h"
#include "Object3D.h"
#include "Light.h"
#include "Vector3D.h"
#include "Vector4D.h"
#include "Satellite.h"
// dimensiunea cubului
#define SIZE 150
// distanta deplasare nava la apasarea unei taste
#define MOVING_DIST 0.5
#define ESC	27
//lista display nava
#define SPACESHIP 3
//nume fisier off pt nava
#define SPACESHIP_FILE "m1367.off"
//variabile pt gameover
#define WIN 1
#define LOSE 2
// nr maxim sateliti pt castigarea jocului
#define MAX_SATELLITES 10
//mesaje pt gameover
#define WIN_STRING "GAME OVER. YOU WIN." 
#define LOSE_STRING "GAME OVER. YOU LOSE. " 
//cat timp este vizibil laserul navei
#define COUNT 30
//mod desenare scena
#define RENDER 1
#define SELECT 2
//numar de stele
#define STARS        100
#define WAIT         30
//nr sateliti
#define SATELLITE    30
GLuint stars;
int _count;
bool readycamera;
//variabila pt gameover
int win_or_lose=0;
float *Vector3D::arr;
float *Vector4D::arr;
//tip camera
int viewmode;
//nava
SpaceShip * oship;
//delay apelate fctie de desenare
GLint delay=20;
//fisier coordonate nava
FILE *fspaceship;
GLfloat aspect;
GLfloat spin=0.0;
Vector4D sel_color(0,1,0,1);
int drawLists = 1;

// variabila folosita pentru a determina daca listele de afisare trebuiesc recalculate
int recomputeLists = 1;
//obiect selectat
int obiect = 0;
int pickS = 0;
//vector sateliti
Satellite *Satellites;
//variabila pt contor stele
int regenerateStars = WAIT;		
//camere
Camera sidecamera(SIZE, SIDEVIEW);
Camera shipcamera(SIZE, SHIPVIEW);
Camera backcamera(SIZE, BACKVIEW);
Camera endgamecamera(SIZE, BACKVIEW);
Camera satellitecamera(SIZE, SATELLITEVIEW);
int mainWindow;


void random(int *x, int *y){
	
	*x = rand() % (2 * SIZE*2) -  SIZE;
	*y = rand() % (2 * SIZE*2) -  SIZE;
}

/*
		Functie pentru afisare mesaj de gameover 

*/
void gameover(){
	char s[30];

	if(win_or_lose == WIN)
		strcpy(s, WIN_STRING);
	if(win_or_lose == LOSE)
		strcpy(s, LOSE_STRING);

	glColor4f(0,0,1,1);
	glRasterPos2f(0, 50);
	for(int i = 0; i < strlen(s); i++)
		glutBitmapCharacter(GLUT_BITMAP_TIMES_ROMAN_24, s[i]);
	
}
/*
		Functie pentru generarea stelelor pt cele 6 fete ale cubului ce margineste 
		spatiul de joc.
		
*/
void createStars(){
	

	stars = glGenLists(1);
	glNewList(stars, GL_COMPILE);
	GLfloat col[] = {1, 1, 1, 1};

	glPushMatrix();
	glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT, col);
	glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, col);
	glScalef( 0.5 , 0.5 , 0.5);
	glBegin(GL_POINTS);
	int i, x, y, z;
	float stripes=1.0/4.0;
		for (int j=0;j<4;j++)
		{
	for(i = 0; i < STARS; i++){
				z = 0 * j * stripes;
				random(&x, &y);
				//for(int i=0;i<)
				glVertex3f(x, y, z);
				random(&x, &y);
				
			}

	for(i = 0; i < STARS; i++){
				z = SIZE* j * stripes;
				random(&x, &y);
				//for(int i=0;i<)
				glVertex3f(x, y, z);
			
				random(&x, &y);
			
			}

	for(i = 0; i < STARS; i++){
				x = 0* j * stripes;
				random(&x, &y);
				//for(int i=0;i<)
				glVertex3f(x, y, z);
			
				random(&y, &z);
				
			}

	for(i = 0; i < STARS; i++){
				x = SIZE* j * stripes;
				random(&x, &y);
				//for(int i=0;i<)
				glVertex3f(x, y, z);
			
				random(&y, &z);
			
			}

	for(i = 0; i < STARS; i++){
				y = 0* j * stripes;
				random(&x, &y);
				//for(int i=0;i<)
				glVertex3f(x, y, z);
			
				random(&x, &z);
			
			}

	for(i = 0; i < STARS; i++){
			    y = SIZE* j * stripes;
				random(&x, &y);
				//for(int i=0;i<)
				glVertex3f(x, y, z);
			
		
			}
		}
		glEnd();
	glPopMatrix();
	glEndList();

}

// FUNCTII
//-------------------------------------------------

// functie de initializare a setarilor ce tin de contextul OpenGL asociat ferestrei
void init(void)
{
	//construieste listele de diplay
	glNewList(SPACESHIP,GL_COMPILE);
	glPushName(SPACESHIP);
	  glShadeModel(GL_SMOOTH);
	oship->Draw();
	glPopName();
	glEndList();
	// pregatim o scena noua in opengl
	glClearColor(0.0, 0.0, 0.03, 0.0);	// stergem tot
	glEnable(GL_DEPTH_TEST);			// activam verificarea distantei fata de camera (a adancimii)
	glShadeModel(GL_SMOOTH);			// mod de desenare SMOOTH
	glEnable(GL_LIGHTING);				// activam iluminarea
	glEnable(GL_NORMALIZE);				// activam normalizarea normalelor
}
/*
	Functie pentru citire coordonate nava
*/
SpaceShip * drawspaceship( )
{
	std::vector<Point3d> points;
	std::vector<int> topology;	
	fspaceship=fopen(SPACESHIP_FILE, "r");
	char  garbage[100];
	int numfaces, numvertices, numedges, nrpoints;
	fscanf(fspaceship,"%s %d %d %d\n", garbage, &numvertices, &numfaces, &numedges);
	float x, y, z;
	std::cout<<garbage<<" "<<numfaces<<" "<<numvertices<<" "<<numedges<<endl;
	float xmin,xmax,ymax,ymin,zmax,zmin;
	xmin = ymin=zmin=300.0;
	xmax=ymax=zmax=0.0;
	for(int i=0;i<numvertices;i++)
	{
		fscanf(fspaceship, "%f %f %f\n",&x,&y,&z);		
		if(xmin>x) xmin=x;
		if(ymin>y) ymin=y;
		if(zmin>z) zmin=z;
		if(xmax<x) xmax=x;
		if(ymax<y) ymax=y;
		if(zmax<z) zmax=z;
		points.push_back(Point3d(x,y,z));
	}
	printf(" %f %f %f %f %f %f\n",xmin,xmax,ymin,ymax,zmin,zmax);
	int p1,p2,p3;
	
	for(int i=0;i<numfaces;i++)
	{
		fscanf(fspaceship, "%d %d %d %d\n",&nrpoints,&p1,&p2,&p3);
		
		
		
		topology.push_back(p1);
		topology.push_back(p2);
		topology.push_back(p3);
		
	}
	fclose(fspaceship);
	
	SpaceShip* o=new SpaceShip(SIZE/2-10, SIZE/2, SIZE-SIZE/5.0, SIZE);
	o->points=points;
	o->topology=topology;
	o->Type=Custom;
	o->Visible=true;
	return o;
}
// functie de initializare a scenei 3D
void initScene(void)
{
	// initialize vector arrays
	viewmode = SIDEVIEW;
	Vector3D::arr = new float[3];
	Vector4D::arr = new float[4];
	win_or_lose = 0;
	readycamera = false;
	//pozitionarea celor 2 sori
	GLfloat positionSun1 [] = {SIZE/4, SIZE, 10, 0.0};
	GLfloat positionSun2 [] = {SIZE*3/4, 20, SIZE+20, 0.0};
	GLfloat colorSun1 [] = {0.2, 0.2, 0.2, 1};
	GLfloat colorSun2 [] = {0.3, 0.3, 0.3, 1};

	glLightfv(GL_LIGHT0, GL_AMBIENT, colorSun1);
	glLightfv(GL_LIGHT0, GL_DIFFUSE, colorSun1);
	glLightfv(GL_LIGHT0, GL_POSITION, positionSun1);
	glEnable(GL_LIGHT0);

	glLightfv(GL_LIGHT2, GL_AMBIENT, colorSun2);
	glLightfv(GL_LIGHT2, GL_DIFFUSE, colorSun2);
	glLightfv(GL_LIGHT2, GL_POSITION, positionSun2);
	glEnable(GL_LIGHT2);
	
	//glShadeModel(GL_SMOOTH );
	//desenare nava
	oship = drawspaceship();

	oship->SetColor(new Vector3D(1,1,1));

	oship->SetLevelOfDetail(12);

	
	oship->Wireframe=false;
	oship->Type=Custom;
	Satellites = new Satellite[SATELLITE];
				
	for(int i = 0; i < SATELLITE; i++)
		
		{
			Satellites[i].explodes=false;
			Satellites[i].SetShip(oship);
				Satellites[i].bonus=false;
			if(i <5)
				Satellites[i].bonus=true;
			if (i>=5) Satellites[i].type= (i%2) +1;
			
			
			Satellites[i].id=i;
			Satellites[i].setParameters(SIZE);
	
	}

	_count = COUNT;
}



// AFISARE SCENA
//-------------------------------------------------

// functie de desenare a scenei 3D
void drawScene(GLint mode)
{

glMatrixMode(GL_MODELVIEW);
	glLoadIdentity();
		
	glEnable(GL_LIGHTING);
	//tip camera
	if(viewmode == SIDEVIEW)
		sidecamera.Render();
	else
	if(viewmode == BACKVIEW)
		backcamera.Render();
	else
	if(viewmode == SHIPVIEW)
	{
		Vector3D vect=oship->translation;
		shipcamera.SetPosition(new Vector3D(vect.x+10,vect.y+10,vect.z+5));
		shipcamera.Render();

	}
	else if(viewmode == SATELLITEVIEW  && readycamera == true && obiect!=0)
	{
		satellitecamera.SetPosition(new Vector3D(Satellites[obiect-1].posx,Satellites[obiect-1].posy+3,Satellites[obiect-1].posz+3));
		satellitecamera.shipposition=Vector3D(oship->x,oship->y,oship->z);
		
		satellitecamera.Render();
	
	}
	else
		sidecamera.Render();
	//desenare nava doar in mod RENDER
	if(win_or_lose == 0 && mode == RENDER)
		glCallList(SPACESHIP);
		
		//desenare stele
		if(regenerateStars == 0){	
		createStars();
		regenerateStars = WAIT;
	} else
		regenerateStars--;

	glCallList(stars);
	glInitNames();
		glPushName(0);
	
	
	int i;
	int deselect;
	//desenare sateliti
	if(win_or_lose == 0)
	{
	for(i = 0; i < SATELLITE; i++){
		glPushMatrix();
	
	
		glLoadName(i + 1);

		if(i + 1 != obiect){
			deselect = Satellites[i].drawSatellite();
		} else
		{ 
			if(viewmode != SATELLITEVIEW)
			{
			glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT, sel_color.Array());
			glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, sel_color.Array());
			glBegin(GL_LINES);
		
			glVertex3f(oship->x+6.5, oship->y+10.0, oship->z+4.1);
				glVertex3f(Satellites[i].posx, Satellites[i].posy, Satellites[i].posz );
				
				glVertex3f(oship->x-0.001+6.5, oship->y+10.0, oship->z+4.1);
				glVertex3f(Satellites[i].posx, Satellites[i].posy, Satellites[i].posz );
				glVertex3f(oship->x+0.001+6.5, oship->y+10.0, oship->z+4.1);
				glVertex3f(Satellites[i].posx, Satellites[i].posy, Satellites[i].posz );
				glEnd();
			}
			deselect = Satellites[i].drawSatellite();
			
			
		}
						

		glPopMatrix();
	}
	//desenare laser nava
	if(obiect != 0 && _count > 0 && viewmode !=SATELLITEVIEW)_count--;
	//explozie sateliti
	if(obiect!= 0 && _count == 0 && viewmode !=SATELLITEVIEW){
		
		Satellites[obiect - 1].explodes=true;
		Satellites[obiect - 1].explode();
		obiect = 0;
		if(Satellites[i].bonus == true)oship->powerup();
			else oship->defeated_satellites++;
		_count = COUNT;
	}
}
	else 
	{
		endgamecamera.Render();
		gameover();
	}

	
}

/*
	Functie de desenare (se apeleaza cat de repede poate placa video)
 se va folosi aceeasi functie de display pentru toate subferestrele ( se puteau folosi si functii distincte 
 pentru fiecare subfereastra )
 */
void display(void)
{
	

	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	// Reconstruieste listele de display 
	if(recomputeLists)
	{
		glNewList(SPACESHIP,GL_COMPILE);
		glPushName(SPACESHIP);
		oship->Draw();
		glPopName();
		glEndList();

		recomputeLists--;

	}
	//mecanismul de transparenta folosind ALPHA TESTING / BLENDING

	glEnable(GL_ALPHA_TEST);
	glEnable(GL_DEPTH_TEST);

    glDisable(GL_CULL_FACE);
    //draw non transparent models first
    glAlphaFunc(GL_EQUAL,1.0);
    glDepthMask(GL_TRUE);
	glDisable(GL_BLEND);

	// Render Pass - deseneaza scena
	drawScene(RENDER);

	 //draw transparent objects
    glEnable(GL_BLEND); 
    //Blend function
    
	glBlendFunc(GL_SRC_ALPHA,GL_DST_ALPHA);

    glAlphaFunc(GL_NOTEQUAL,1.0);
    //Disable Z-buffer ops to blend with objects behind
    glDepthMask(GL_FALSE);
 
    drawScene(RENDER);

	glDepthMask(GL_TRUE);
    glDisable(GL_BLEND);

	// double buffering
	glutSwapBuffers();

	
		
}
/*
	Functie de timer
*/
void timer(int value)
{
    glutTimerFunc(value, timer,value);
	//check end game
	if(oship->defeated_satellites >= MAX_SATELLITES ) win_or_lose = WIN;
	if(oship->shield<=0) win_or_lose = LOSE;

	
	if(win_or_lose  == 0)
	{
	//collision detection
	for(int i = 0; i < SATELLITE; i++){
		
		if(Satellites[i].collides(oship, 0) == true){
			
			std::cout<<"coliziune"<<endl;
			recomputeLists=1;
			
			break;
		}
	}
	}
	//seteaza view pt gameover 
	else
	{
	viewmode=ENDVIEW;
	
	
	}
    glutPostRedisplay();
}
// PICKING
//-------------------------------------------------

// functia care proceseaza hitrecordurile pentru a vedea daca s-a click pe un obiect din scena
void processhits (GLint hits, GLuint buffer[])
{
   int i;
   GLuint names, *ptr, minZ,*ptrNames, numberOfNames;

   // pointer la inceputul bufferului ce contine hit recordurile
   ptr = (GLuint *) buffer;
   // se doreste selectarea obiectului cel mai aproape de observator
   minZ = 0xffffffff;
   for (i = 0; i < hits; i++) 
   {
      // numarul de nume numele asociate din stiva de nume
      names = *ptr;
	  ptr++;
	  // Z-ul asociat hitului - se retine 
	  if (*ptr < minZ) {
		  numberOfNames = names;
		  minZ = *ptr;
		  // primul nume asociat obiectului
		  ptrNames = ptr+2;
	  }
	  
	  // salt la urmatorul hitrecord
	  ptr += names+2;
  }

  // identificatorul asociat obiectului
  ptr = ptrNames;
  
  obiect = *ptr;
  if(viewmode == SATELLITEVIEW &&  readycamera == false)
  {
	  readycamera = true;
  }
  cout<<"am selectat" << obiect << "\n";
     
}

/*
	Functie ce realizeaza picking la pozitia la care s-a dat click cu mouse-ul
*/
void pick(int x, int y)
{
	// buffer de selectie
	GLuint buffer[1024];

	// numar hituri
	GLint nhits;

	// coordonate viewport curent
	GLint	viewport[4];

	// se obtin coordonatele viewportului curent
	glGetIntegerv(GL_VIEWPORT, viewport);
	// se initializeaza si se seteaza bufferul de selectie
	memset(buffer,0x0,1024);
	glSelectBuffer(1024, buffer);
	
	// intrarea in modul de selectie
	glRenderMode(GL_SELECT);
	glInitNames();
	glPushName(0);

	// salvare matrice de proiectie curenta
	glMatrixMode(GL_PROJECTION);
	glPushMatrix();
	glLoadIdentity();

	// se va randa doar intr-o zona din jurul cursorului mouseului de [1,1]
	glGetIntegerv(GL_VIEWPORT,viewport);
	gluPickMatrix(x,viewport[3]-y,1.0f,1.0f,viewport);

	gluPerspective(90,aspect, 0.1,1.6 * SIZE);
	glMatrixMode(GL_MODELVIEW);
	//glMatrixMode(GL_SELECT);
	// se "deseneaza" scena : de fapt nu se va desena nimic in framebuffer ci se va folosi bufferul de selectie
	drawScene(SELECT);

	// restaurare matrice de proiectie initiala
	glMatrixMode(GL_PROJECTION);						
	glPopMatrix();				
	glFlush();
	glMatrixMode(GL_MODELVIEW);
	// restaurarea modului de randare uzual si obtinerea numarului de hituri
	nhits=glRenderMode(GL_RENDER);	
	
	int shift = glutGetModifiers();

	if(shift & GLUT_ACTIVE_SHIFT )
		shift = 1;
	else 
		shift = 0;

	// procesare hituri
	if(nhits != 0)
	{
		processhits(nhits,buffer);
	}
	else
	{
		pickS = 0;
		obiect=0;
	}
				
}

/*
	Handler pentru tastatura
*/
void keyboard(unsigned char key , int x, int y)
{
	
	switch (key)
	{
	// la escape se iese din program
	case ESC : exit(0);break;
	//side camera
	case 'x':
		viewmode = SIDEVIEW;
		readycamera=false;
		obiect=0;
		break;
	//ship camera
	case 'c' :
		viewmode=SHIPVIEW;
		readycamera=false;
		obiect=0;		
		break;
	//satellite camera
	case 'v' :  
		viewmode = SATELLITEVIEW;
		
		obiect=0;
		break;
	//back camera
    case 'z' :
		viewmode = BACKVIEW;
		readycamera=false;		
		obiect=0;
		break;
	//deplasare camere inainte
	case 'w' :  
		if(viewmode == SIDEVIEW )
		sidecamera.MoveForward(1.0);
		if( viewmode == BACKVIEW)
			backcamera.MoveForward(1.0);
		break;
	//deplasare camere inapoi
	case 's' :  
		if(viewmode == SIDEVIEW )
		sidecamera.MoveBackward(1.0);
		if( viewmode == BACKVIEW)
			backcamera.MoveBackward(1.0);
		break;
	//deplasare camere sus
    case 'e' :  
		if(viewmode == SIDEVIEW )
		sidecamera.MoveUpward(1.0);
		if( viewmode == BACKVIEW)
		backcamera.MoveUpward(1.0);
		break;
	//deplasare camere jos
    case 'q' :  
		if(viewmode == SIDEVIEW)
		sidecamera.MoveDownward(1.0);
		if( viewmode == BACKVIEW)
			backcamera.MoveDownward(1.0);
		
		break;
	//rotatii camere dreapta
	case 'm' :  
	
		sidecamera.RotateY(3.1415/6.0);
		shipcamera.RotateY(3.1415/6.0);
		break;
	//deplasare stanga acamere
	case 'a' :		
		if(viewmode == SIDEVIEW )
		sidecamera.MoveLeft(1.0);
		if(viewmode == BACKVIEW)
		backcamera.MoveLeft(1.0);
			break;
	//deplasare dreapta camere
	case 'd' :  
		if(viewmode == SIDEVIEW)
		sidecamera.MoveRight(1.0);
		if(viewmode == BACKVIEW)
			backcamera.MoveRight(1.0);
		break;
	//rotatie camera stanga
	case 'n':
		
		sidecamera.RotateY(-3.1415/6.0);
		break;
	//deplasare nava inainte
	case 'i':
		oship->z-=MOVING_DIST;
		recomputeLists=1;
		oship->SetPosition(new Vector3D(oship->x,oship->y,oship->z));
		break;
	//deplasare nava inapoi
	case 'k':
		oship->z+=MOVING_DIST;
		recomputeLists=1;
		oship->SetPosition(new Vector3D(oship->x,oship->y,oship->z));
		break;
	//deplasare nava stanga
	case 'j':
		oship->x-=MOVING_DIST;
		recomputeLists=1;
		oship->SetPosition(new Vector3D(oship->x,oship->y,oship->z));
		break;
	//deplasare nava dreapta
	case 'l':	
		oship->x+=MOVING_DIST;
		recomputeLists=1;
		oship->SetPosition(new Vector3D(oship->x,oship->y,oship->z));
		break;
	//deplasare nava sus
	case 'o':
		oship->y+=MOVING_DIST;
		recomputeLists=1;
		oship->SetPosition(new Vector3D(oship->x,oship->y,oship->z));
		break;
	//deplasare nava jos
	case 'u':
		oship->y-=MOVING_DIST;
		recomputeLists=1;
		oship->SetPosition(new Vector3D(oship->x,oship->y,oship->z));
		break;
	default: break;
	}
	
}



/*
  Callback pentru a procesa inputul de mouse
*/
void mouse(int button, int state, int x, int y){

	switch(button){
	
	case GLUT_LEFT_BUTTON:
		if(state == GLUT_DOWN)
		{
			pick(x,y);
		}
		break;
	}
}


/*
	functie de proiectie
*/
void reshape(int w, int h)
{
	
	glViewport(0,0, (GLsizei) w, (GLsizei) h);
	// calculare aspect ratio ( Width/ Height )
	aspect = (GLfloat) w / (GLfloat) h;

	// intram in modul proiectie
	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	// incarcam matrice de perspectiva 

	
	gluPerspective(90, aspect,0.1, 2.0 * SIZE);
	// Initializeaza contextul OpenGL asociat ferestrei
	init();
	// Fereastra aplicatiei a fost redimensionata : trebuie sa recream subferestrele



}

/*
	Main
*/
int main(int argc, char** argv)
{
	
	
	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_DOUBLE|GLUT_RGB);
	glEnable(GL_DEPTH_TEST);
	int w = 600, h= 600;
	glutInitWindowSize(w,h);
	glutInitWindowPosition(100,100);
	
	// Main window
	mainWindow = glutCreateWindow("SPACE InVaDeRs");

	glutDisplayFunc(display);
	glutKeyboardFunc(keyboard);
	glutReshapeFunc(reshape);

	glutMouseFunc(mouse);
	glutTimerFunc(delay, timer, delay);
	// Initializeaza scena 3D
	initScene();

	glutMainLoop();
	return 0;
}
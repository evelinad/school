//Dumitrescu Evelina 331CA
//Tema 1 EGC


#include "WorldDrawer2d.h"

//static member init
CoordinateSystem2d WorldDrawer2d::cs_basis;
std::vector<CoordinateSystem2d*> WorldDrawer2d::cs_used;
bool WorldDrawer2d::flick;


void WorldDrawer2d::idleCallbackFunction(){
	//call client function
	
	{
	onIdle();
	//redisplay
	glutPostRedisplay();
	}
}

void WorldDrawer2d::reshapeCallbackFunction(int w, int h){
	
	{
	glViewport(0,0, w, h);

	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	float aspect = (float)w/(float)h;
	gluPerspective(90.0f, aspect, 0.1f, 300.0f);
	glMatrixMode(GL_MODELVIEW);
	glLoadIdentity();

	gluLookAt(0.0, 0.0, 20.0,0.0, 0.0, 0.0,	0.0, 1.0, 0.0);  //looking at xoy
	}
}

void WorldDrawer2d::displayCallbackFunction(){
	//Render objects-
	//am modificat fctia din lab pentru flicker
	if(flick == true)
{
	
glClearColor(1.0f,1.0f,1.0f,1.0f);
glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT); 
glutSwapBuffers();
Sleep(1000);
WorldDrawer2d:flick = false;
return;
}
	
	{
	glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);

	if(cs_basis.draw_axis){
		//draw basis coord system
		
	}
	

	//draw each used coord system
	for(unsigned int i=0;i<cs_used.size();i++){
		//draw used coord system
		if(cs_used[i]->draw_axis){

		}
		
		
		//draw objects
		for(unsigned int j=0;j<cs_used[i]->objects.size();j++){
			//set object color
			glColor3f(cs_used[i]->objects[j]->colorx,cs_used[i]->objects[j]->colory,cs_used[i]->objects[j]->colorz);

			//get data
			std::vector<Point2d> points = cs_used[i]->objects[j]->points;
			std::vector<int> topology = cs_used[i]->objects[j]->topology;
			
			//draw
			if(topology.size()<2){
				std::cout<<"Folositi triunghiuri, dimensiune minima topologie =3"<<std::endl;
				continue;
			}

			//obiectul
			glBegin(GL_TRIANGLES);
				for(unsigned int k=0;k<topology.size();k++){
					int index=topology[k];
					glVertex3f(points[index].x, points[index].y,0);
				}
			glEnd();
			
			
			//axele obiectului
			if(cs_used[i]->objects[j]->drawaxis){
				
			}
			
		}
	}

	//swap buffers
	glutSwapBuffers();
	}
}
void WorldDrawer2d::keyboardCallbackFunction(unsigned char key, int posx, int posy){
	
	{
	if(key==KEY_ESC) glutExit();
	//call client function
	onKey(key);
}
}



WorldDrawer2d::WorldDrawer2d(int argc, char **argv, int windowWidth, int windowHeight, int windowStartX, int windowStartY, std::string windowName){
	//init
	
	{
	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_DOUBLE|GLUT_RGB|GLUT_DEPTH);
	glutInitWindowSize(windowWidth,windowHeight);
	glutInitWindowPosition(windowStartX,windowStartY);
	glutCreateWindow(windowName.c_str());
	
	//bind funcs
	glutDisplayFunc(displayCallbackFunction);
	glutReshapeFunc(reshapeCallbackFunction);
	glutIdleFunc(idleCallbackFunction);
	glutKeyboardFunc(keyboardCallbackFunction);

	glClearColor(0.4f,0.5f,1,1);

	//zbuff
	glEnable(GL_DEPTH_TEST);
	}
	}
void WorldDrawer2d::run(){
	glutMainLoop();
}
WorldDrawer2d::~WorldDrawer2d(){
}
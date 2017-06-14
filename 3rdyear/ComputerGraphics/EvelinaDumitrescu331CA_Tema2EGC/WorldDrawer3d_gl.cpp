#include "WorldDrawer3d.h"

CoordinateSystem3d WorldDrawer3d::cs_basis;
std::vector<CoordinateSystem3d*> WorldDrawer3d::cs_used;
enum COLORS {RED=0, GREEN=1,BLUE=2,ORANGE=3,YELLOW=4,WHITE=5};
enum SIDE {FRONT, LEFT, RIGHT, TOP, BOTTOM};

struct Colors2
{
float r, g, b;
Colors2 (float r=0.0, float g = 0.0, float b = 0.0): r(r), g(g),b(b){}
};

Colors2 col_vect[3]={Colors2(0.3f, 1.0f, 0.2f),Colors2(0.2f, 0.8f, 0.6f),Colors2(0.3f, 1.0f, 0.2f)};
bool schimbascena=true;

void WorldDrawer3d::idleCallbackFunction(){
	//call client function
	onIdle();
	//redisplay
	glutPostRedisplay();
}

void WorldDrawer3d::reshapeCallbackFunction(int w, int h){
	glViewport(0,0, w, h);

	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	float aspect = (float)w/(float)h;
	gluPerspective(90.0f, aspect, 0.1f, 3000.0f);
	glMatrixMode(GL_MODELVIEW);
	glLoadIdentity();
	
	gluLookAt(20.0, 20.0, 20.0,0.0, 0.0, 0.0, 0.0, 1.0, 0.0);  //looking at xoy
}
/*
	draws on the screen 1 digit
*/
void WorldDrawer3d::drawdigit(int digit1, int deplx, int deply)
{
		for(int x=0;x<3;x++)
		for(int y=0;y<5;y++)
	switch(digit1)
	{
	case 0:
		{
			if((x==1 && y==1) || (x==1 && y==2)|| (x==1 && y==3));
			else
				{
					Object3d *o = drawCube(1,0,0,1);
					o->translate(x*1.5+deplx,y*1.5+deply,3);
					cs_basis.objectAdd(o);
			}
			break;
		}
	case 1:
		{
			if(x==0 || x==2 );
			else
			{
					Object3d *o = drawCube(1,0,0,1);
					o->translate(x*1.5+deplx,y*1.5+deply,3);
					cs_basis.objectAdd(o);
			}
			break;
		}
	case 2:
		{
			if((x==1 && y==3) || (x==0 && y==3)||(x==1 &&y==1)||(x==2 &&y==1));
			else
			{
					Object3d *o = drawCube(1,0,0,1);
					o->translate(x*1.5+deplx,y*1.5+deply,3);
					cs_basis.objectAdd(o);
			}
			break;

		}
			case 3:
		{
			if((x==1 && y==3) || (x==0 && y==3)||(x==1 &&y==1)||(x==0 &&y==1)||(x==0&&y==2));
			else
			{
					Object3d *o = drawCube(1,0,0,1);
					o->translate(x*1.5+deplx,y*1.5+deply,3);
					cs_basis.objectAdd(o);
			}
			break;
		}
			case 4:
		{
			if((x<=1 && y<=1) || (x==1 && y>=3));
			else
			{
					Object3d *o = drawCube(1,0,0,1);
					o->translate(x*1.5+deplx,y*1.5+deply,3);
					cs_basis.objectAdd(o);
			}
			break;
		}
		case 5:
		{
			if((x<=1 && y==1) || (x>=1 && y==3));
			else
			{
					Object3d *o = drawCube(1,0,0,1);
					o->translate(x*1.5+deplx,y*1.5+deply,3);
					cs_basis.objectAdd(o);
			}
			break;
		}
		case 6:
		{
			if((x==1 && y==1) || (x>=1 && y==3));
			else
			{
					Object3d *o = drawCube(1,0,0,1);
					o->translate(x*1.5+deplx,y*1.5+deply,3);
					cs_basis.objectAdd(o);
			}
			break;
		}
		case 7:
		{
			if((x<=1 && y<=3) );
			else
			{
					Object3d *o = drawCube(1,0,0,1);
					o->translate(x*1.5+deplx,y*1.5+deply,3);
					cs_basis.objectAdd(o);
			}
			break;
		}
		case 8:
		{
			if((x==1 && y==3) || (x==1&&y==1));
			else
			{
					Object3d *o = drawCube(1,0,0,1);
					o->translate(x*1.5+deplx,y*1.5+deply,3);
					cs_basis.objectAdd(o);
			}
			break;
		}
		case 9:
		{
			if((x==1 && y==3) || (x==1&&y==1)||(x==0&&y==1));
			else
			{
					Object3d *o = drawCube(1,0,0,1);
					o->translate(x*1.5+deplx,y*1.5+deply,3);
					cs_basis.objectAdd(o);
			}
			break;
		}
	}

}
/*
	draws the game over screen + total moves
*/
void WorldDrawer3d::drawscore(int score)
{
	char str[10];
	strcpy(str, "moves");
	int deplx=-10,deply=5;
	for(int x=0;x<5;x++)
		for(int y=0;y<6;y++)
		{
			if((x==1&&(y!=3))||(x==3&&(y!=3))||(x>0 &&y>=4));
			else
			{
				Object3d *o = drawCube(1,0,0,1);
				o->translate(x*1.5+deplx-3.5,y*1.5+deply,3);
				cs_basis.objectAdd(o);
			}
		}
	for(int i=1; i<strlen(str);i++)
	for(int x=0;x<3;x++)
		for(int y=0;y<5;y++)
		{
			switch(str[i])
				{	
			
					case 'o':
					{
						
						if((x==1 && y==1) || (x==1 && y==2)|| (x==1 && y==3));
						else
						{
							Object3d *o = drawCube(1,0,0,1);
							o->translate(x*1.5+deplx+i*6,y*1.5+deply,3);
							cs_basis.objectAdd(o);
						}
						break;
					}
					case 'v':
					{
						
						if((x==1 && y>=1) ||(x==0 &&y==0)||(x==2&&y==0));
						else
						{
							Object3d *o = drawCube(1,0,0,1);
							o->translate(x*1.5+deplx+i*6,y*1.5+deply,3);
							cs_basis.objectAdd(o);
						}
						break;
					}
						case 'e':
					{
						
						if((x==1 && y==3) ||(x>0 &&y==1));
						else
						{
							Object3d *o = drawCube(1,0,0,1);
							o->translate(x*1.5+deplx+i*6,y*1.5+deply,3);
							cs_basis.objectAdd(o);
						}
						break;
					}
					case 's':
					{
						
						if((x<=1 && y==1) ||(x>=1 &&y==3));
						else
						{
							Object3d *o = drawCube(1,0,0,1);
							o->translate(x*1.5+deplx+i*6,y*1.5+deply,3);
							cs_basis.objectAdd(o);
						}
						break;
					}

			}
		}


	int digit1, digit2;

	digit1=score/10;
	drawdigit(digit1,0,-5);
	digit2=score%10;
	drawdigit(digit2,6,-5);

}

void WorldDrawer3d::displayCallbackFunction(){
	//Render objects
	glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);

	if(gameover)
	{int n= cs_basis.objects.size();
	if(schimbascena==true)
	{
		for(int i=0;i<n;i++)
			cs_basis.objects.pop_back();
		schimbascena=false;
			glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		gluLookAt(1.0, 1.0, 30.0,0.0, 0.0, 0.0, 0.0, 1.0, 0.0);
	}
		drawscore(score);
		for(unsigned int j=0;j<cs_basis.objects.size();j++){
		//set object color
	

		//get data
		std::vector<Point3d> points = cs_basis.objects[j]->points;
		std::vector<int> topology = cs_basis.objects[j]->topology;
		
		//draw
		if(topology.size()<2){
			std::cout<<"Folositi triunghiuri, dimensiune minima topologie =3"<<std::endl;
			continue;
		}
		float r,g,b;
		int col =0;
		float x, y,z;
		//obiectul
		glBegin(GL_TRIANGLES);
		//std::cout<<topology.size()<<std::endl;
			for(unsigned int k=0;k<topology.size();k++){
				int index=topology[k];
				if(k % 6 == 0) 
				{
					r=col_vect[col].r;
					b=col_vect[col].b;
					g=col_vect[col].g;
					
					col ++;
					if(col>=3)col=0;
				}
				
					glColor3f(r,g,b);
				
				glVertex3f(points[index].x, points[index].y,points[index].z);
				
			}
		glEnd();

		}
	}
	else
	{
	glLineWidth(1);
	//draw basis coord system
	if(cs_basis.draw_axis){
		glBegin(GL_LINES);
			glColor3f(0,1,1);
			glVertex3f(cs_basis.axisup.x,-cs_basis.axisup.y,cs_basis.axisup.z);
			glVertex3f(cs_basis.axisup.x,cs_basis.axisup.y,cs_basis.axisup.z);
			glColor3f(1,0,1);
			glVertex3f(-cs_basis.axisright.x,cs_basis.axisright.y,cs_basis.axisright.z);
			glVertex3f(cs_basis.axisright.x,cs_basis.axisright.y,cs_basis.axisright.z);
			glColor3f(1,1,0);
			glVertex3f(cs_basis.axisfwd.x,cs_basis.axisfwd.y,-cs_basis.axisfwd.z);
			glVertex3f(cs_basis.axisfwd.x,cs_basis.axisfwd.y,cs_basis.axisfwd.z);
		glEnd();
		glPointSize(5);
		glBegin(GL_POINTS);
			glColor3f(0,1,1);	glVertex3f(cs_basis.axisup.x,cs_basis.axisup.y,cs_basis.axisup.z);
			glColor3f(1,0,1);	glVertex3f(cs_basis.axisright.x,cs_basis.axisright.y,cs_basis.axisright.z);
			glColor3f(1,1,0);	glVertex3f(cs_basis.axisfwd.x,cs_basis.axisfwd.y,cs_basis.axisfwd.z);
		glEnd();

	}

	for(unsigned int j=0;j<cs_basis.objects.size();j++){
		//set object color
		glColor3f(cs_basis.objects[j]->colorx,cs_basis.objects[j]->colory,cs_basis.objects[j]->colorz);

		//get data
		std::vector<Point3d> points = cs_basis.objects[j]->points;
		std::vector<int> topology = cs_basis.objects[j]->topology;
		
		//draw
		if(topology.size()<2){
			std::cout<<"Folositi triunghiuri, dimensiune minima topologie =3"<<std::endl;
			continue;
		}
		float r,g,b;
		int col =0;
		float x, y,z;
		//obiectul
		glBegin(GL_TRIANGLES);

			for(unsigned int k=0;k<topology.size();k++){
				int index=topology[k];
				glVertex3f(points[index].x, points[index].y,points[index].z);
			}
		glEnd();

		//axele obiectului
		glLineWidth(2);
		if(cs_basis.objects[j]->draw_axis){
			glBegin(GL_LINES);
				glColor3f(0,1,0);
				glVertex3f(cs_basis.objects[j]->axiscenter.x,cs_basis.objects[j]->axiscenter.y,cs_basis.objects[j]->axiscenter.z);
				glVertex3f(cs_basis.objects[j]->axisup.x,cs_basis.objects[j]->axisup.y,cs_basis.objects[j]->axisup.z);
				glColor3f(1,0,0);
				glVertex3f(cs_basis.objects[j]->axiscenter.x,cs_basis.objects[j]->axiscenter.y,cs_basis.objects[j]->axiscenter.z);
				glVertex3f(cs_basis.objects[j]->axisright.x,cs_basis.objects[j]->axisright.y,cs_basis.objects[j]->axisright.z);
				glColor3f(0,0,1);
				glVertex3f(cs_basis.objects[j]->axiscenter.x,cs_basis.objects[j]->axiscenter.y,cs_basis.objects[j]->axiscenter.z);
				glVertex3f(cs_basis.objects[j]->axisfwd.x,cs_basis.objects[j]->axisfwd.y,cs_basis.objects[j]->axisfwd.z);
			glEnd();
		}
	}

	//draw each used coord system
	for(unsigned int i=0;i<cs_used.size();i++){
		//draw used coord system
		glLineWidth(1);
		if(cs_used[i]->draw_axis){
			glBegin(GL_LINES);
				glColor3f(0,1,0);
				glVertex3f(cs_used[i]->axiscenter.x,cs_used[i]->axiscenter.y,cs_used[i]->axiscenter.z);
				glVertex3f(cs_used[i]->axisup.x,cs_used[i]->axisup.y,cs_used[i]->axisup.z);
				glColor3f(1,0,0);
				glVertex3f(cs_used[i]->axiscenter.x,cs_used[i]->axiscenter.y,cs_used[i]->axiscenter.z);
				glVertex3f(cs_used[i]->axisright.x,cs_used[i]->axisright.y,cs_used[i]->axisright.z);
				glColor3f(0,0,1);
				glVertex3f(cs_used[i]->axiscenter.x,cs_used[i]->axiscenter.y,cs_used[i]->axiscenter.z);
				glVertex3f(cs_used[i]->axisfwd.x,cs_used[i]->axisfwd.y,cs_used[i]->axisfwd.z);
			glEnd();
		}

		//draw objects
		for(unsigned int j=0;j<cs_used[i]->objects.size();j++){
			//set object color
			glColor3f(cs_used[i]->objects[j]->colorx,cs_used[i]->objects[j]->colory,cs_used[i]->objects[j]->colorz);

			//get data
			std::vector<Point3d> points = cs_used[i]->objects[j]->points;
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
					glVertex3f(points[index].x, points[index].y,points[index].z);
				}
			glEnd();
			
			//axele obiectului
			glLineWidth(2);
			if(cs_used[i]->objects[j]->draw_axis){
				glBegin(GL_LINES);
					glColor3f(0,1,0);
					glVertex3f(cs_used[i]->objects[j]->axiscenter.x,cs_used[i]->objects[j]->axiscenter.y,cs_used[i]->objects[j]->axiscenter.z);
					glVertex3f(cs_used[i]->objects[j]->axisup.x,cs_used[i]->objects[j]->axisup.y,cs_used[i]->objects[j]->axisup.z);
					glColor3f(1,0,0);
					glVertex3f(cs_used[i]->objects[j]->axiscenter.x,cs_used[i]->objects[j]->axiscenter.y,cs_used[i]->objects[j]->axiscenter.z);
					glVertex3f(cs_used[i]->objects[j]->axisright.x,cs_used[i]->objects[j]->axisright.y,cs_used[i]->objects[j]->axisright.z);
					glColor3f(0,0,1);
					glVertex3f(cs_used[i]->objects[j]->axiscenter.x,cs_used[i]->objects[j]->axiscenter.y,cs_used[i]->objects[j]->axiscenter.z);
					glVertex3f(cs_used[i]->objects[j]->axisfwd.x,cs_used[i]->objects[j]->axisfwd.y,cs_used[i]->objects[j]->axisfwd.z);
				glEnd();
			}
		}
	}
	}
	//swap buffers
	glutSwapBuffers();
}
void WorldDrawer3d::keyboardCallbackFunction(unsigned char key, int posx, int posy){
	if(key==KEY_ESC) glutExit();
	//call client function
	onKey(key);
}
void WorldDrawer3d::keyboardSpecialCallbackFunction(int key, int posx, int posy){
	//call client function
	onKey(key);
}

WorldDrawer3d::WorldDrawer3d(int argc, char **argv, int windowWidth, int windowHeight, int windowStartX, int windowStartY, std::string windowName){
	//init

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
	glutSpecialFunc(keyboardSpecialCallbackFunction);
	
	glClearColor(0.2f,0.4f,1,1);

	//zbuff
	glEnable(GL_DEPTH_TEST);
}
void WorldDrawer3d::run(){

	glutMainLoop();
}
WorldDrawer3d::~WorldDrawer3d(){
}
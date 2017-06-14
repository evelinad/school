#include "SpaceShip.h"
#include "Support3d.h"
#include "Light.h"

const GLfloat light_ambient[]  = { 0.0f, 0.0f, 0.0f, 1.0f };
const GLfloat light_diffuse[]  = { 1.0f, 1.0f, 1.0f, 1.0f };
const GLfloat light_specular[] = { 1.0f, 1.0f, 1.0f, 1.0f };
const GLfloat light_position[] = { 0.0f, -1.0f, 2.0f, 0.0f };

const GLfloat mat_ambient[]    = { 0.7f, 0.7f, 0.7f, 1.0f };
const GLfloat mat_diffuse[]    = { 0.8f, 0.8f, 0.8f, 1.0f };
const GLfloat mat_specular[]   = { 1.0f, 1.0f, 1.0f, 1.0f };
const GLfloat high_shininess[] = { 100.0f };


SpaceShip::SpaceShip(GLfloat posx, GLfloat posy, GLfloat posz, GLfloat size)
{
	
	x = posx;
	y = posy;
	z = posz;
	defeated_satellites = 0;
	boxSize = size;
	shield=MAX_HEALTH;
	green=0.6;
	shieldsize=50.0;
	printf("pozitii %f %f %f\n", posx, posy, posz);
	SetPosition(new Vector3D(posx, posy, posz));
//	SetPosition(new Vector3D(posx,posy,posz));

	
		spotdirection[0] = -1;
	spotdirection[1] = 0;
	spotdirection[2] = 0;
	spotdirection[3] = 1;

	light1[0] = -0.2;
	light1[1] = 0.35;
	light1[2] = 0.35;
	light1[3] = 1;

	light2[0] = -0.4;
	light2[1] = 0.35;
	light2[2] = 0.35;
	light2[3] = 1;
    light_s = new Light();
	light_s->SetLightType(IlluminationType::Spot);
	//// setam pozitia
	light_s->SetPosition(new Vector3D(size/4, size/4, size-60));		
	light_s2 = new Light();
	light_s2->SetLightType(IlluminationType::Spot);
	// setam pozitia
	light_s2->SetPosition(new Vector3D(-size/4, size/4, size-60));	

	
}

Point3d normalize(Point3d v)
{
	//v × u = (v.y * u.z − v.z * u.y, v.z * u.x − v.x * u.z, v.x * u.y − v.y * u.x).
	
	float len = (float)(sqrt((v.x * v.x) + (v.y * v.y) + (v.z * v.z)));

    // avoid division by 0
    if (len == 0.0f)
        len = 1.0f;

    // reduce to unit size
    v.x /= len;
    v.y /= len;
    v.z /= len;
	return v;
}
Point3d normal (Point3d p1, Point3d p2, Point3d p3)
{
	Point3d a, b,normal;

    // calculate the vectors A and B
    // note that v[3] is defined with counterclockwise winding in mind
    // a
    a.x =p1.x - p2.x;
    a.y = p1.y - p2.y;
    a.z = p1.z - p2.z;
    // b
    b.x = p2.x - p3.x;
    b.y = p2.y - p3.y;
    b.z = p2.z - p3.z;

    // calculate the cross product and place the resulting vector
    // into the address specified by vertex_t *normal
	normal.x = (a.y * b.z) - (a.z * b.y);
	normal.y = (a.z * b.x) - (a.x * b.z);
    normal.z = (a.x * b.y) - (a.y * b.x);

    // normalize
    normal=normalize(normal);
	return normal;
}
void SpaceShip::damage()
{
	shield-=0.1;
	
}
void SpaceShip::powerup()
{
	if(shield<MAX_HEALTH)
	shield+=0.1;
	
}
void SpaceShip::customDraw()
{

  
  
  light_s->Render();
  light_s2->Render();


	glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);





  
  glEnable(GL_LIGHT1); 
  //glEnable(GL_NORMALIZE);
 	glLightfv(GL_LIGHT1, GL_AMBIENT,  light_ambient);
		glLightfv(GL_LIGHT1, GL_DIFFUSE,  light_diffuse);
		glLightfv(GL_LIGHT1, GL_SPECULAR, light_specular);
		glLightfv(GL_LIGHT1, GL_POSITION, light_position);
		glMaterialfv(GL_FRONT, GL_AMBIENT,   mat_ambient);
		glMaterialfv(GL_LIGHT_MODEL_TWO_SIDE, GL_DIFFUSE,   mat_diffuse);
		glMaterialfv(GL_FRONT_AND_BACK, GL_SPECULAR,  mat_specular);
		glMaterialfv(GL_FRONT_AND_BACK, GL_SHININESS, high_shininess); 
	// TODO : aici va puteti desena un obiect personalizat/incarcat din fisier/pre-generat/etc
	//	glPushMatrix();
			glBegin(GL_TRIANGLES);
			int index;
			glScalef(shieldsize, shieldsize, shieldsize);
				for(unsigned int k=0;k<topology.size();k++){
					index=topology[k];
					if(k % 3 == 2)
					{
						Point3d p1,p2,p3;
						p1=points[topology[k]];
						p2=points[topology[k-1]];
						p3=points[topology[k-2]];
						Point3d n=normal(p1,p2,p3);
						glNormal3f(n.x,n.y,n.z);
					}
					glVertex3f(points[index].x, points[index].y,points[index].z);
				//	printf("%f %f %f \n",points[index].x, points[index].y,points[index].z);
				}
			//	printf("%d \n",topology.size());
	glEnd();
	//glPopMatrix();
		
	/*	glDisable(GL_NORMALIZE);
		glDisable(GL_COLOR_MATERIAL);
		glDisable(GL_LIGHTING);*/
	
		
		
		glDisable(GL_LIGHT1);glDisable(GL_CULL_FACE);
		//	glutSolidCube(1.0);
		//  glPushMatrix();

 // glEnable( GL_BLEND );

 //glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		glTranslatef( 0.21,0.14,0.54);
		
		GLfloat shieldColor[] = {0, green, 0, shield};
		glColor3f(0.4, 0.6, 0);
	//glNormal3f(0,1,0);
		glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT, shieldColor);
		glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, shieldColor);
		glMaterialfv(GL_FRONT_AND_BACK, GL_SPECULAR, (Vector4D(1,0,0,1)).Array());
		glMaterialfv(GL_FRONT_AND_BACK, GL_SHININESS, shieldColor);
		//glScalef(0.25,0.12,0.57);
		glutSolidSphere(0.5, 50, 50);
		//glScalef(1,1,1);
 // glPopMatrix();
		
          


}

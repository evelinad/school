#pragma once
#include <glut.h>
#include <time.h>
#include <stdlib.h>
#include <math.h>
#include<stdio.h>
#include "Satellite.h"


Vector4D ambient4f1(0.9, 0.4, 0.1, 1);
Vector4D diffuse4f1(0.9, 0.4, 0.1, 1);	
Vector4D specular4f1(0.9, 0.9, 0.9, 1.0);

Vector4D emmision4f1(0, 0, 0, 1);
static GLint shininess1 = 64;
Satellite::Satellite() {
	spin = 0;
	
}

void Satellite::SetShip(SpaceShip * _spaceship)
{
	spaceship=_spaceship;
}


void Satellite::setParameters(GLuint size){

	explosion = NULL;
	ambient4f1= Vector4D(0.9, 0.4, 0.1, 1);
	diffuse4f1=Vector4D(0.9, 0.4, 0.1, 1);	
	specular4f1=Vector4D(0.9, 0.9, 0.9, 1.0);

	Vector4D emmision4f1(0, 0, 0, 1);
	static GLint shininess1 = 64;
	GLfloat ast_color [][5] = { {0.5, 0.2, 0.2, 2},
								{0.55, 0.55, 0.55, 2},
								{0.01, 0.3, 0.1, 2},
								{0.7, 0.4, 0.3, 2},
								{0.7, 0.3, 0.5, 2},
								{0, 0, 0.2, 2}
								};

	int pick = rand() % 6;
	//setare pozitie + culoare asteroid
	for(int i = 0; i < 4; i++)
		color[i] = ast_color[pick][i];

	box = size;
	posx =  rand() % box;
	posy = rand() % box;
	posz = rand() % box;

	Size = (rand() % 5+3)/2.0;
	time = 0;
	


	dx = (double) (rand() % RAND_MAX) / RAND_MAX * 2;
	int sign=rand()%2-1;
	dx*=sign;
	
 {
		dz = (double) (rand() % RAND_MAX) / RAND_MAX;
		
	
		dy = (double) (rand() % RAND_MAX) / RAND_MAX;
		sign=rand()%2-1;
	//	dy*=sign;
	
	}

	v0 = (double) (rand() % RAND_MAX) / RAND_MAX;
	//acc = (double) (rand() % RAND_MAX) / RAND_MAX;
	
	//v0 = 0;
	

}

/*
		Desenare satelit
*/
int Satellite::drawSatellite(){

	
	glNormal3f(0,1,0);
	glTranslatef(posx, posy, posz);
	
	//daca nu e de tip power up
	if(bonus == false)
	{
	//daca nu explodeaza deseneaza cu culoarea obisnuita 
	if(explodes == false  )
	{
		
	glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT,color);
	glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, color);
	
	
	}
	//daca explodeaza se folosesc culorile pt explozie
	if(explodes == true)
	{
	
	glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT, ambient4f1.Array());
	glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, diffuse4f1.Array());
	glMaterialfv(GL_FRONT_AND_BACK, GL_SPECULAR, specular4f1.Array());
	glMaterialf(GL_FRONT_AND_BACK, GL_SHININESS, shininess1);
	}
	//scalare 
	glScalef(Size-1, Size-1, Size-1);
	//rotire in jurul axelor ox si oz
	glRotatef(spin, 0, spin/2.0,0);	
	//setare tipa asteroid => icoshaedron/dodecahedron
	if(type == ICOS)glutSolidIcosahedron();
	else glutSolidDodecahedron();
	//desenare inele satelit
	glRotatef(90,1,0,0);
	glRotatef(spin, 0, spin/2.0,0);	
		glutSolidTorus(0.05,2.2,20,20);
		glutSolidTorus(0.05,2,20,20);
		glutSolidTorus(0.05,1.8,20,20);
	}
	//satelit bonus; acelasi mod de desenare ca la satelitii normali
	if(bonus == true)
	{
		
		
		if(explodes == false)
		{
		glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT,Vector4D(1,0,0,1).Array());
		glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, Vector4D(1,0,0,1).Array());
		glScalef(Size, Size, Size);
		glutSolidSphere(0.9,20,20);
		glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT,Vector4D(0,0,1,1).Array());
		glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, Vector4D(0,0,1,1).Array());
		
		glRotatef(90,1,0,0);
		glRotatef(spin, 0, spin/2.0,0);	
		glutSolidTorus(0.05,1.7,20,20);
		glutSolidTorus(0.05,1.4,20,20);
		glutSolidTorus(0.05,1.1,20,20);
		
		}
		if(explodes == true)
	{
	
	glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT, ambient4f1.Array());
	glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, diffuse4f1.Array());
	glMaterialfv(GL_FRONT_AND_BACK, GL_SPECULAR, specular4f1.Array());
	glMaterialf(GL_FRONT_AND_BACK, GL_SHININESS, shininess1);
	glScalef(Size, Size, Size);
		glutSolidSphere(0.9,20,20);
		
		glRotatef(90,1,0,0);
		glRotatef(spin, 0, spin/2.0,0);
		glutSolidTorus(0.05,1.7,20,20);
		glutSolidTorus(0.05,1.4,20,20);
		glutSolidTorus(0.05,1.1,20,20);
		
	}
	
	}
	time += 0.01;
	//daca are loc explozia atunci nu se mai deplaseaza
	if(explodes == false)
	{
	posx += (v0 * time) * dx/7.0;
	posy += (v0 * time) * dy/7.0;
	posz += (v0 * time ) * dz/7.0;
	}
	//daca satelitul debaseste spatiul de joc se reseteaza parametri
	if(posx < 0 || posx > box ||
		posy < 0 || posy > box ||
		 posz > box){
			destroy();
			
			return 1;
	}
	if(explosion != NULL && explodes)
	{
		// daca satelitul explodeaza randeaza lumina pt explozie + efectul de fade out
		
		explosion->Renderexplight();
		if(counter < 100)
		{
				
			
		explosion->SetAttenuation(explosion->getAttenuation()-0.1);
		}
		else
			if(counter < 200)
		{
			
			explosion->SetAttenuation(explosion->getAttenuation()+0.05);
		}
		else
		 if(counter<300)
				{
					ambient4f1.a-=FADE;
					diffuse4f1.a-=FADE;
					specular4f1.a-=FADE;
					
				}
		if(counter<300)
		{
		counter++;

		}
		else 
			{
				explosion->Disable();
				explodes = false;
				delete explosion;

		explosion = NULL;
		destroy();
		counter=0;
		}
	}
	if(spin>360)spin =0;
	else spin+=0.5f;
	return 0;
}
/*
intoarce marimea Satelliteului
*/
GLfloat Satellite::getSize(){
	
	return Size;
}
/*
	Functie pentru test coliziuni cu nava
*/
bool Satellite::collides(SpaceShip *spaceship, int type){
	
	GLfloat dist = (posx - spaceship->x)*(posx - spaceship->x) + (posy - spaceship->y)*(posy - spaceship->y) + (posz - spaceship->z) * (posz - spaceship->z);
	GLfloat maxdist;
	maxdist=DIST1+Size/2.0;
	if(dist<maxdist*maxdist)
	{
		//cand are loc coliziunea introduc satelitul intr-un vector 
		//si il sterg abia cand se reseteaza pozitia acestuia/cand se depaseste spatiul de joc
		bool exists = false;
		
		for(int i=0;i<spaceship->satellites.size();i++)
		{
	
			if((spaceship->satellites[i]) == id) exists=true;
		}
	//	printf("\n");
		if(exists == false)
		{
		std::cout<<"coliziune "<<id<<endl;
		spaceship->satellites.push_back(id);
		//damage nava doar atunci cand nu este satelit bonus
		if(bonus == false)
		spaceship->damage();
		else spaceship->powerup();
		
		return true;
		}
		return false;
	}
	else 
		return false;

		return true;
}

/*
	Efect explozie satelit
*/
void Satellite::explode()
{
	Vector4D ambient4f(7, 5, 3, 1);
	Vector4D diffuse4f(7, 5, 3, 1);	
	Vector4D specular4f(0.9, 0.9, 0.9, 1.0);
	Vector4D position4f(posx,posy,posz,1);
	GLfloat attenuation = 5.1;
	explosion = new Light(ambient4f,diffuse4f,specular4f, position4f, attenuation);
	explosionradius = 1;
	counter = 0;
}
/*
	reseteaza parametri satelit
*/
void Satellite::destroy(){
	
	
	std::vector<int>::iterator position = std::find(spaceship->satellites.begin(), spaceship->satellites.end(), id);
if (position != spaceship->satellites.end()) 
    spaceship->satellites.erase(position);
	setParameters(box);
}
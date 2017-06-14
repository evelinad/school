
#ifndef SPACESHIP_H
#define SPACESHIP_H

#include "Object3D.h"
#include "Light.h"
#include <vector>
#include<stdlib.h>
#define MAX_HEALTH 0.5
using namespace std;

/*
	Clasa Plane
	deseneaza un plan cu numar variabil de quaduri in XOZ
*/

// derivata din Object3D
class SpaceShip : public Object3D
{

// VARIABILE
//-------------------------------------------------
private:
	// dimensiunea unei laturi
	float size;

// FUNCTII
//-------------------------------------------------
public:
	// constructor fara parametri
	SpaceShip();
	SpaceShip(GLfloat x, GLfloat y, GLfloat z, GLfloat size);
	//pozitie nava + dimensiune
	float x, y,z,boxSize;
	//shield
	float shield;
	float shieldsize;
	//valoare culoare verde
	float green;
	//sateliti invinsi
	int defeated_satellites;
	//sateliti cu care au avut loc coliziuni
	vector<int > satellites;
	//lumini nava
	Light *light_s,*light_s2;
	GLfloat spotdirection[4], light1[4], light2[4];
	std::vector<Point3d> points;
	//shield ++
	void powerup();
	std::vector<int> topology;
	//shield --
	void damage();


	// functia custmDraw care va fi apelata de Object3D pentru a desena planul
	void customDraw();
	
};

#endif
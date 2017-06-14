#pragma once


//tipul de Satellite
#define NORMAL   0
#define SELECTED 1
#define POWERUP  2
#define DIST1 25.5
#define DIST2 10
#define FADE 0.02
#define ICOS 1
#define DECA 2
#include "Light.h"
#include "SpaceShip.h"
#include <algorithm>
class Satellite {
	//culoare satelit
	GLfloat color[4];	
	//marimea spatiului de joc
	GLuint box;			
	//marime pas deplasare nava
	GLfloat dx, dy, dz;
	//marime satelit
	GLfloat Size;	
	//timpul
	GLfloat time;			
	//viteza 
	GLfloat v0;				
	//unghi de spin in jurul axelor Ox si Oz
	GLfloat spin;			

	static int total_count;
	SpaceShip * spaceship;
	
public:
	//pozitie
	GLfloat posx, posy, posz;
	//id
	int id;
	//daca explodeaza sau nu
	bool explodes;
	//bonus 
	bool bonus;
	//tip icoshaedron sau dodecahedron
	GLint type ;
	//raza explozie
	int explosionradius;
	int counter;
	//lumina explozie
	Light * explosion;
	Satellite();
	void SetShip(SpaceShip * spaceship);
	void setParameters(GLuint);
	int drawSatellite();
	GLfloat getSize();

	void destroy();
	void explode();
	bool collides(SpaceShip *spaceship, int type);
	static int count();
};


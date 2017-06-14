// EGC
// Laborator 7
//-------------------------------------------------

#ifndef LIGHT_H
#define LIGHT_H


#include "Object3D.h"
#include "Camera.h"

// tipul de iluminare
enum IlluminationType
{
	Ideal, // lumina omnidirectionala
	Spot   // lumina directionala
};

/*
	Light
	Clasa pentru desenarea si activarea unei lumini
*/

// derivata din object3D
class Light 
{

// VARIABILE STATICE
//-------------------------------------------------
private:
	static int baseId;

// VARIABILE
//-------------------------------------------------
public:
	// tipul luminii - nu este folosit, inca
	IlluminationType LightType;
	GLfloat attenuation;
		Vector3D translation;	// pozitie
	Vector3D rotation;		// rotatie
	Vector3D scale;			// scalare
	Vector3D color;			// culoare

private:
	// id-ul asignat. pleaca din 0 si este folosit pentru GL_LIGHT0 + id
	int id;
	// lumina difuza
	Vector4D diffuse;
	// lumina ambientala
	Vector4D ambient;
	// lumina speculara
	Vector4D specular;
	Vector4D position;

	// pentru spot :

// FUNCTII
//-------------------------------------------------
public:
	// constructor fara parametri
	Light();
	void SetAttenuation(GLfloat a);
	Light(Vector4D ambient, Vector4D diffuse, Vector4D specular, Vector4D position, GLint _attenuation);
	// plaseaza lumina in scena si o activeaza
	void Render();
	void SetColor(Vector3D *_color);
	void SetPosition(Vector3D *_translation);
	// dezactiveaza lumina
	void Disable();
	GLfloat getAttenuation();
	// seteaza tipul de lumina
	void SetLightType(IlluminationType LightType);
	void Light::Renderexplight();
};

#endif
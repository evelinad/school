// EGC
// Laborator 7
//-------------------------------------------------

#include "Light.h"

// id-ul de baza
int Light::baseId = 3;

// constructor fara parametri, mosteneste constructorul de Sfera din Object3D
// deoarece dorim ca lumina sa fie ilustrata printr-o sfera
Light::Light() 
{
	// valori default
	diffuse = Vector4D(6,8,8,1);
	ambient = Vector4D(6,8,8,1);
	specular = Vector4D(6,8,8,1);
	color = Vector3D(1,1,1);
	scale = Vector3D(0.2,0.2,0.2);

	// id-ul este unic, id-ul de baza incrementat
	id = baseId++;
	std::cout<<id<<std::endl;
	// sfera plasata in locul luminii nu este wireframe
	//Wireframe = false;

	// default lumina este omnidirectionala
	LightType = IlluminationType::Ideal;
}
Light::Light(Vector4D _ambient, Vector4D _diffuse, Vector4D _specular, Vector4D _position, GLint _attenuation) 
{
	// valori default
	diffuse = _diffuse;
	ambient = _ambient;
	specular = _specular;
	color = Vector3D(1,1,0);
	scale = Vector3D(0.2,0.2,0.2);
	position=_position;
	// id-ul este unic, id-ul de baza incrementat
	id = 5;
	
	// sfera plasata in locul luminii nu este wireframe
	//Wireframe = false;

	// default lumina este omnidirectionala
	LightType = IlluminationType::Ideal;
	
	SetAttenuation(_attenuation);
}
void Light::SetColor(Vector3D *_color)
{
	color = *_color;
}

// seteaza pozitie
void Light::SetPosition(Vector3D *_translation)
{
	translation = *_translation;
}
void Light::SetAttenuation(GLfloat a)
{
	attenuation = a;
}
// seteaza tipul de lumina
void Light::SetLightType(IlluminationType _LightType)
{
	LightType  = _LightType;
}
GLfloat Light::getAttenuation()
{
	return attenuation;
}
// functie care realizeaza efectul de explozie pt lumina in scena
void Light::Renderexplight()
{
	int index;
	
	glLightfv(GL_LIGHT0 + id, GL_AMBIENT, ambient.Array());
	glLightfv(GL_LIGHT0 + id, GL_DIFFUSE, diffuse.Array());
	glLightfv(GL_LIGHT0 + id, GL_SPECULAR, specular.Array());
	glLightfv(GL_LIGHT0 + id, GL_POSITION,  position.Array());	
//	printf("%f %f %f %f\n", position[0],position[1],position[2],position[3]);
	glLightf(GL_LIGHT0 + id, GL_QUADRATIC_ATTENUATION, attenuation);
	glLightf(GL_LIGHT0 + id,GL_CONSTANT_ATTENUATION,attenuation);
	glLightf(GL_LIGHT0 + id,GL_LINEAR_ATTENUATION,attenuation);

	//glLightf(GL_LIGHT0 + id,GL_LINEAR_ATTENUATION,0.2f);
	glLightfv(GL_LIGHT0 + id, GL_POSITION, Vector4D(position.x,position.y,position.z,1).Array());
	glEnable(GL_LIGHT0 + id);
	
}
void Light::Render()
{
	// atenuari standard
	glLightf(GL_LIGHT0 + id,GL_CONSTANT_ATTENUATION,1);
	glLightf(GL_LIGHT0 + id,GL_LINEAR_ATTENUATION,0.2f);

	// culoarea luminii 
	glLightfv(GL_LIGHT0 + id, GL_DIFFUSE, Vector4D(diffuse.x, diffuse.y, diffuse.z, diffuse.a).Array());
	// culoarea ambientala 
	glLightfv(GL_LIGHT0 + id, GL_AMBIENT, ambient.Array());
	// culoarea speculara
	glLightfv(GL_LIGHT0 + id, GL_SPECULAR, specular.Array());
	// pozitia luminii
	glLightfv(GL_LIGHT0 + id, GL_POSITION, Vector4D(translation.x,translation.y,translation.z,1).Array());

	// daca este de tip spot , setam parametrii de spot ( se vor folosi valori default )
	if(LightType == IlluminationType::Spot)
	{
		// directia spotului va fi in jos
		glLightfv(GL_LIGHT0 + id , GL_SPOT_DIRECTION, (Vector3D(0.0,0.0,-1.0)).Array());      
		// deschidere de 45 de grade
		glLightf(GL_LIGHT0 + id , GL_SPOT_CUTOFF, 20.0);
		glLightf(GL_LIGHT0 + id , GL_SPOT_EXPONENT, 2);
	}

	// activam lumina
	glEnable(GL_LIGHT0 + id);
}

// functie care dezactiveaza lumina
void Light::Disable()
{
	glDisable(GL_LIGHT0 + id);
}

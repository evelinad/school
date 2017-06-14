
#ifndef CAMERA_H
#define CAMERA_H
		
#include "Vector3D.h"
//tip camere
#define SIDEVIEW 1
#define SHIPVIEW 2
#define SATELLITEVIEW 3
#define BACKVIEW 4
#define ENDVIEW 5
#define PI 3.1415926535897932384626433832795

/*
	Camera
	Clasa pentru initializarea si miscarea unui observator prin scena
*/

class Camera
{
	
// VARIABILE
//-------------------------------------------------

	Vector3D ForwardVector;
	Vector3D RightVector;	
	Vector3D UpVector;
	Vector3D Position;

// FUNCTII
//-------------------------------------------------
public:
	// constructor fara parametri
	Camera();			
	Vector3D shipposition;
	Camera(GLfloat size, int type);
	
	// plaseaza observatorul in scena
	void Render ( void );	

	// rotatie fata de axele de coordonate
	void RotateX ( GLfloat Angle );
	void RotateY ( GLfloat Angle );
	void RotateZ ( GLfloat Angle );

	// rotatie fata de un centru de rotatie
	void RotateXCenter ( GLfloat Angle );
	void RotateYCenter ( GLfloat Angle, float distance );
	void RotateZCenter ( GLfloat Angle );


	// miscare fata/spate
	void MoveForward ( GLfloat Distance );
	void MoveBackward ( GLfloat Distance );

	// miscare sus/jos
	void MoveUpward ( GLfloat Distance );
	void MoveDownward ( GLfloat Distance );

	// miscare stanga/dreapta
	void MoveRight ( GLfloat Distance );
	void MoveLeft ( GLfloat Distance );

	// setare Pozitie
	void SetPosition(Vector3D *value);

	// setare ForwardVector
	void SetForwardVector(Vector3D *value);

	// setare RightVector
	void SetRightVector(Vector3D *value);

	// setare UpVector
	void SetUpVector(Vector3D *value);
	int type;
	GLfloat box;	//marimea spatiului de joc
};

#endif

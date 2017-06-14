// EGC
// Laborator 7
//-------------------------------------------------

#include "camera.h"
#include "math.h"
#include<stdlib.h>
#include<stdio.h>

// constructor
Camera::Camera(GLfloat size, int _type){
	box = size;
	type = _type;
		ForwardVector = Vector3D(0,0,-1);
	UpVector = Vector3D(0,1,0);
	RightVector = Vector3D(1,0,0);
	if(_type == SIDEVIEW || type == ENDVIEW)
		{
				GLfloat half = box/2;
	printf("sideview\n");
	Position = Vector3D(-half,half+10,half*5.0/4.0);
	ForwardVector = Vector3D(0,0,-1);
	UpVector = Vector3D(0,1,0);
	RightVector = Vector3D(1,0,0);
	RotateY(PI/2.0);
	}
			
	if (_type == BACKVIEW) {
		GLfloat half = box/2;
		printf("sideview\n");
		Position = Vector3D(half,half+10,box+half/2.0 );
		ForwardVector = Vector3D(0,0,-1);
		UpVector = Vector3D(0,1,0);
		RightVector = Vector3D(1,0,0);
	
	}

}
Camera::Camera()
{
	//initializare cu valorile standard OpenGL
	Position = Vector3D(1,0,20);
	ForwardVector = Vector3D(0,0,-1);
	UpVector = Vector3D(0,1,0);
	RightVector = Vector3D(1,0,0);


}


void Camera::RotateX (GLfloat Angle)
{
}

void Camera::RotateY (GLfloat Angle)
{
	ForwardVector=ForwardVector *cos(Angle) +RightVector *sin(Angle);
	RightVector=ForwardVector.CrossProduct(UpVector);
}

void Camera::RotateZ (GLfloat Angle)
{
}

// rotire fata de centru, la o distanta generica 5
void Camera::RotateXCenter (GLfloat Angle)
{
}

// rotire fata de centru, la o distanta generica 5
void Camera::RotateYCenter (GLfloat Angle, float distance)
{
	//float distance = 5;
	MoveForward(distance);
	RotateY(Angle);
	MoveBackward(distance);
	printf("rotatie");
}

// rotire fata de centru, la o distanta generica 5
void Camera::RotateZCenter (GLfloat Angle)
{
	float distance = 5;
	MoveForward(distance);
	RotateZ(Angle);
	MoveBackward(distance);
}

// plasare observator in scena
void Camera::Render( void )
{
	glMatrixMode(GL_MODELVIEW);
	glLoadIdentity();

	// punctul catre care se uita camera
	Vector3D ViewPoint = Position + ForwardVector;
	if(type == SHIPVIEW)
		gluLookAt(	Position.x,Position.y,Position.z,
				ViewPoint.x,ViewPoint.y,ViewPoint.z,
				0,1,0);
	else
	if(type == SATELLITEVIEW)
	gluLookAt(	Position.x,Position.y,Position.z,
				
				shipposition.x,shipposition.y,shipposition.z,
				UpVector.x,UpVector.y,UpVector.z);

	else
		gluLookAt(	Position.x,Position.y,Position.z,
				ViewPoint.x,ViewPoint.y,ViewPoint.z,
				UpVector.x,UpVector.y,UpVector.z);
	// stim vectorul UpVector, folosim LookAt
	}

// miscari simple, pe toate axele

void Camera::MoveForward( GLfloat Distance )
{
	Position = Position + (ForwardVector * Distance);
}

void Camera::MoveBackward( GLfloat Distance )
{
	Position = Position + (ForwardVector * -Distance);
}

void Camera::MoveRight ( GLfloat Distance )
{
	Position = Position + ( RightVector * Distance);
}

void Camera::MoveLeft ( GLfloat Distance )
{
	Position = Position + ( RightVector * -Distance);
}

void Camera::MoveUpward( GLfloat Distance )
{
	Position = Position + ( UpVector * Distance );
}

void Camera::MoveDownward( GLfloat Distance )
{
	Position = Position + ( UpVector * -Distance );
}

// seteaza pozitie
void Camera::SetPosition(Vector3D *value)
{
	Position = *value;
}

// seteaza forward vector
void Camera::SetForwardVector(Vector3D *value)
{
	ForwardVector = *value;
}

// seteaza right vector
void Camera::SetRightVector(Vector3D *value)
{
	RightVector = *value;
}

// seteaza up vector
void Camera::SetUpVector(Vector3D *value)
{
	UpVector = *value;
}
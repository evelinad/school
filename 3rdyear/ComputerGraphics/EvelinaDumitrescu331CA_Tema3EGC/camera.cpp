#include "camera.h"
#include<stdio.h>
#include<stdlib.h>
using namespace std;
Camera::Camera(){
}
Camera::~Camera(){
}

/*
	initialize camera
*/
void Camera::init(Vector3D poz, Vector3D frwrd,Vector3D u,Vector3D r, int type){
	position = poz;
	forward = frwrd;	
	up = u;	
	type = type;	
	right = r;
	directionx=directiony=directionz=0;


}

/*
	move forward
*/
void Camera::translate_Forward(float dist){
	if(directionx==0 && directionz==0)
		position=position+forward*dist;

}
/*
	move up
*/
void Camera::translate_Up(float dist){
	if(directionx==0 && directionz==0)
		position=position+up*dist;
}
/*
	move right
*/
void Camera::translate_Right(float dist){
	if(directionx==0 && directionz==0)
		position=position+right*dist;

}



/*
	Oy rotation for FPS
*/
void Camera::rotateFPS_OY(float angle){

	if(directionx == 0 && directionz ==0)
	{
		forward=forward *cos(angle) +right *sin(angle);
		right=forward.CrossProduct(up);
		if(angle>0.0) directiony++;
		else directiony--;
		if(directiony==-1)directiony=24+directiony;
		if(directiony==24)directiony=directiony-24;
	}
}
/*
	Ox rotation for FPS
*/
void Camera::rotateFPS_OX(float angle){

	if( directionz==0.0 && directiony==0)
	{
		Vector3D FrN,UpN;

		FrN= forward *cos(angle) + up * sin(angle);
		UpN = up * cos(angle) - forward * sin(angle);
		forward=FrN;
		up=UpN;
		forward.Normalize();
		up.Normalize();
		if(angle>0.0) directionx++;
		else directionx--;
		if(directionx==-1)directionx=24+directionx;
		if(directionx==24)directionx=directionx-24;
	}


}
/*
	Oz rotation for FPS
*/
void Camera::rotateFPS_OZ(float angle){
	if( directionx==0 && directiony==0)
	{
		Vector3D UpN,RightN;
		UpN= up *cos(angle) + right * sin(angle);
		RightN = right * cos(angle) - up * sin(angle);
		up=UpN;
		right=RightN;
		up.Normalize();
		right.Normalize();
		if(angle>0.0) directionz++;
		else directionz--;
		if(directionz==-1)directionz=24+directionz;
		if(directionz==24)directionz=directionz-24;

	}
}
/*
	Oy rotation for TPS
*/
void Camera::rotateTPS_OY(float angle, float dist_to_interes){
	if(directionx == 0 && directionz ==0)
	{

		Vector3D oldforward =forward;
		forward=forward * cos(angle)+right*sin(angle);

		right=forward.CrossProduct( up);
		position=position +  oldforward * dist_to_interes  -  forward * dist_to_interes;
		if(angle>0.0) directiony++;
		else directiony--;
		if(directiony==-1)directiony=24+directiony;
		if(directiony==24)directiony=directiony-24;
	}

}
/*
	Ox rotation for TPS
*/
void Camera::rotateTPS_OX(float angle, float dist_to_interes){
	if(directionz==0 && directiony==0)
	{
		translate_Up(dist_to_interes);
		rotateFPS_OX(angle);
		translate_Up(-dist_to_interes);
		if(angle>0.0) directionx++;
		else directionx--;
		if(directionx==-1)directionx=24+directionx;
		if(directionx==24)directionx=directionx-24;
	}

}
/*
	Oz rotation for TPS
*/
void Camera::rotateTPS_OZ(float angle, float dist_to_interes){
	if(directionx==0 && directiony==0)
	{
		translate_Right(dist_to_interes);
		rotateFPS_OZ(angle);
		translate_Right(-dist_to_interes);
		if(angle>0.0) directionz++;
		else directionz--;
		if(directionz==-1)directionz=24+directionz;
		if(directionz==24)directionz=directionz-24;

	}
}

/*
	render camera
*/
void Camera::render(float lowdist){
	Vector3D center = position + forward;
	gluLookAt(	position.x, position.y, position.z, 
		center.x, center.y, center.z,
		up.x, up.y, up.z);
}
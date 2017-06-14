#include "Support3d.h"


//-------------------------------------------------------------------------------------
//	Point3D
//-------------------------------------------------------------------------------------
Point3d::Point3d(){
	x=y=z=0;
}
Point3d::Point3d(float x, float y,float z){
	this->x=x;
	this->y=y;
	this->z=z;
}
Point3d::~Point3d(){}

//set
void Point3d::set(float x, float y, float z){
	this->x=x;
	this->y=y;
	this->z=z;
}

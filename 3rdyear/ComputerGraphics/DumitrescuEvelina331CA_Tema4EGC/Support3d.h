#pragma once
#include <cmath>
#include <iostream>
#include <vector>


//forward declaration



//attach points to objects...
class Point3d{
	//-------------------------------------------------------------------------------------
//	Point3D
//-------------------------------------------------------------------------------------
		public:
Point3d(){
	x=y=z=0;
}
 Point3d(float x, float y,float z){
	this->x=x;
	this->y=y;
	this->z=z;
}
Point3d::~Point3d(){}

		float x,y,z;											// (x,y)
};


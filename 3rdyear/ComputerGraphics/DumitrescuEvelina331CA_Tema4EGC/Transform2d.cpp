#pragma once
#include "Transform2d.h"
#include "Support2d.h"


void Transform2d::translate(Point2d *pct, float tx, float ty){
	(*pct).set((*pct).x+tx, (*pct).y+ty);
}

void Transform2d::rotate(Point2d *pct, float angleInRadians){
	float newx;
	float newy;
	newx=(*pct).x * cos(angleInRadians)- pct->y * sin(angleInRadians);
	newy=pct->x * sin(angleInRadians)+ pct->y * cos(angleInRadians);
	(*pct).set(newx,newy);
}

void Transform2d::rotateRelativeToAnotherPoint(Point2d *pct, Point2d *ref, float angleInRadians){
	Transform2d::translate(pct, (-1)* ref->x,(-1)* ref->y);
	Transform2d::rotate(pct, angleInRadians);
	Transform2d::translate(pct,ref->x,ref->y);
}
void Transform2d::scale(Point2d *pct, float sx, float sy){
	(pct->x)*=sx;
	(pct->y)*=sy;

}
void Transform2d::scaleRelativeToAnotherPoint(Point2d *pct, Point2d *ref, float sx, float sy){
	Transform2d::translate(pct, (-1)*ref->x, (-1)*ref->y);
	Transform2d::scale(pct, sx, sy);
	Transform2d::translate(pct, ref->x, ref->y);
	
}


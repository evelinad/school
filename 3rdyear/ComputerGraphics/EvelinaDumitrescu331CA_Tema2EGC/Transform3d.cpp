#pragma once
#include "Transform3d.h"
#include "Support3d.h"

void Transform3d::translate(Point3d *pct, float tx, float ty, float tz){
	pct->x += tx;
	pct->y += ty;
	pct->z += tz;
}

void Transform3d::rotateX(Point3d *pct, float angleInRadians){
	float y1 = pct->y;
	float z1 = pct->z;

	pct->y = y1 * cos(angleInRadians) - z1 *sin(angleInRadians);
	pct->z = y1 * sin(angleInRadians) + z1 *cos(angleInRadians);
}
void Transform3d::rotateRelativeToAnotherPoint(Point3d *pct, Point3d *ref, float angleInRadians){
	Transform3d::translate(pct, (-1)* ref->x,(-1)* ref->y,-ref->z);
	Transform3d::rotateZ(pct, angleInRadians);
	Transform3d::translate(pct,ref->x,ref->y, ref->z);


}

void Transform3d::rotateY(Point3d *pct, float angleInRadians){
	float x1 = pct->x;
	float z1 = pct->z;

	pct->x = x1 * cos(angleInRadians) - z1 *sin(angleInRadians);
	pct->z = x1 * sin(angleInRadians) + z1 *cos(angleInRadians);
}

void Transform3d::rotateZ(Point3d *pct, float angleInRadians){
	float x1 = pct->x;
	float y1 = pct->y;

	pct->x = x1 * cos(angleInRadians) - y1 *sin(angleInRadians);
	pct->y = x1 * sin(angleInRadians) + y1 *cos(angleInRadians);
}

void Transform3d::rotateXRelativeToAnotherPoint(Point3d *pct, Point3d *ref, float angleInRadians){
	translate (pct , -ref->x, -ref->y , -ref->z);
	rotateX (pct , angleInRadians);
	translate (pct , ref->x, ref->y , ref->z);
}
void Transform3d::rotateYRelativeToAnotherPoint(Point3d *pct, Point3d *ref, float angleInRadians){
	translate (pct , -ref->x, -ref->y , -ref->z);
	rotateY (pct , angleInRadians);
	translate (pct , ref->x, ref->y , ref->z);
}
void Transform3d::rotateZRelativeToAnotherPoint(Point3d *pct, Point3d *ref, float angleInRadians){
	translate (pct , -ref->x, -ref->y , -ref->z);
	rotateZ (pct , angleInRadians);
	translate (pct , ref->x, ref->y , ref->z);
}
void Transform3d::scale(Point3d *pct, float sx, float sy, float sz){
	pct->x *= sx;
	pct->y *= sy;
	pct->z *= sz;
}
void Transform3d::scaleRelativeToAnotherPoint(Point3d *pct, Point3d *ref, float sx, float sy, float sz){
	translate(pct , -ref->x, -ref->y , -ref->z);
	scale(pct, sx, sy, sz);
	translate(pct , ref->x, ref->y , ref->z);
}
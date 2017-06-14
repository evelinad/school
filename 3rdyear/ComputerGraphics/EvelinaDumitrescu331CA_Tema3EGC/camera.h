//clasa camera
#pragma once
#include "Vector3D.h"
#include "Rotation.h"
#include<stdlib.h>
#include<vector>
using namespace std;
#define X 1
#define Y 2
#define Z 3
#define TOPDOWN 1
#define FPS 2
#define TPS 3
#define DIST_HERO_CAMERA 8.0
struct rotation
{
	float angle;
	float axis;
};

class Camera{
	public:
		Camera();

		~Camera();

		void translate_Forward(float dist);
		void translate_Up(float dist);
		void translate_Right(float dist);

		void rotateFPS_OY(float angle);
		void rotateFPS_OX(float angle);
		void rotateFPS_OZ(float angle);
		void rotateTPS_OY(float angle, float dist_to_interes);
		void rotateTPS_OX(float angle, float dist_to_interes);
		void rotateTPS_OZ(float angle, float dist_to_interes);

		void init();
		void init(Vector3D poz, Vector3D frwrd, Vector3D u, Vector3D r, int type);
		void render(float lowdist);
		void update();
	public:
		int type;
		float directionx;
	
		float directiony;
		float directionz;
		Vector3D forward;
		Vector3D up;
		Vector3D right;
		Vector3D forward2;
		
		Vector3D right2;
		vector<rotation> Rotations;
	//	std::vecto <rotation> Rotations;
		Vector3D position;
};
#pragma once
#include "HeadersAndDefines.h"
#include "Support3d.h"
#include "Support2d.h"
		

class WorldDrawer3d{

	public:
		
	
	public:
		//implemented in worldDrawer2d_gl .. not for lab1
		WorldDrawer3d(int argc, char **argv, int windowWidth, int windowHeight, int windowStartX, int windowStartY, std::string windowName);
		~WorldDrawer3d();
		static void run();	
		static void displayCallbackFunction();
		static void reshapeCallbackFunction(int width, int height);
		static void idleCallbackFunction();
		static void keyboardCallbackFunction(unsigned char key, int x, int y);
		static void keyboardSpecialCallbackFunction(int key, int x, int y);


		//implemented in worldDrawer2d_logic .. for lab1
		static void init();
		static void onIdle();
		static void onKey(unsigned char key);
		static void drawscore(int score);
		static void drawdigit(int digit1, int deplx, int deply);
		static Object3d* drawCube(float r, float g, float b,float size );
	public:

		static bool animation;
		static int score;
		static bool gamestart;
		static bool gameover;
		static CoordinateSystem3d cs_basis;
		static std::vector<CoordinateSystem3d*> cs_used;
		
};
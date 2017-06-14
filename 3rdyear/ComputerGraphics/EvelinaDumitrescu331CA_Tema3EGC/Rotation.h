#pragma once
class Rotation
{
#define X 1
#define Y 2
#define Z 3
public:
	Rotation(void);
	~Rotation(void);
	float angle;
	int axis;
};


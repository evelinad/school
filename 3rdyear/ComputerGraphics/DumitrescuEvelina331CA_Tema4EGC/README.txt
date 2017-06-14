Dumitrescu Evelina 
331CA 
Tema4 EGC

1.CERINTA

Implementarea unui joc 3D in openGl ce simuleaza o batalie spatiala. 
Nava trebuie sa navigheze prin centura de asteroizi incercand sa distruga cat mai multi.

2.UTILIZARE
 * w, a,s,d-> deplasare camere inainte /inapoi/stanga/dreapta
 * i, k, j, l-> deplasare nava inainte /inapoi/stanga/dreapta
 * m,n-> rotire camere sideview si backview
 * z,x,c,v -> selectare camere z = sideview, x= backview, c = shipview , v = satelliteview
 * mouse selectare asteroid + distrugere cu raza laser
 * camera asteroid functioneaza astfel : intare in modul SATELLITEVIEW (tasta v) + selectare asteroid
	module default este cel SIDEVIEW
	

3. IMPLEMENTARE
	Mediu dezvoltare : Visual Studio 2010 express edition
	Descriere clase:
	1.Spaceship
		* metode pt deplasare nava
		* powerup/damage scut
		- scutul este o sfera ; de fiecare data cand nava este lovita scade parametrul alpha al culorii scutului
		- load coordonate din fisier off
	2.Camera
		* initializare vectori forward, up, right+setare tip camera
		* translatii/rotatii
		* randare in functie de tipul camerei
	3.Satellite
		* setare parametri 
			- culoare + dimensiune  (alese random)
			- tip 
				* icoshaedron
				* decahedron
				* bonus (satelitii reprezentati prin sfere rosii + inele albastre)
				* desenare (translatie)
		* explozie 
		* destroy pt resetare parametri
		* collision pt detectare coliziuni 
				- atunci cand un asteroid intra prima data intr-o nava id-ul lui este memorat intr-un vector (pt a inregistra 1 sg data aceasta coliziune ) 
				  si este sters abia cand iese din spatiul de joc 
		- am folosit bounding spheres	
		- inelele satelitilor se invart in jurul axelor Ox si Oz
		
	4.Light
		* am adaugat in plus fata de laborator metoda Renderexplight pentru efectul de explozie al satelitilor
		* pt cei 2 sori(lumini omnidirectionale) + tunuti laser(spoturi)
	5. Lab7
		- fisierul principal al programului contine fctia main + dirijeaza workflowul jocului
	5. Alte clase din laborator
	
	
	- daca nava distruge un astroid de tip bonus / este lovita de unul dintre acestia rezistenta scutului creste
	- daca este lovita de 5 ori de asteroizi de tip icoshaedron sau decahedron nava este distrusa
	- daca nava distruge 10 asteroizi jucatorul castiga
	- la finalul jocului se afiseaza un mesaj de GAMEOVER (WIN/LOST)
	- pentru efectul de explozie: efect de fade in/out, modific atenuarea luminii + parametrul alpha
	setez valori specifice pentru lumina abientala, difuza si speulara)
	- in timpul exploziei un asteroid ramane pe loc				
4. TESTARE
	Programul a fost testat pe Windows 7.


5. CONTINUT ARHIVA
	- lab7.cpp - fisier principal;
	- Spaceship.cpp Spaceship.h
	- Satellite.cpp, Satellite.h
	- Camera.cpp, Camera.h
	- Light.cpp, Light.h
	-Transform3d.cpp, Transform3d.h
	-Transform2d.cpp, Transform2d.h
	- Vector3D.h
	- Vector4D.h
	- Suport3D.cpp,Suport3D.h
	- Suport2D.cpp,Suport2D.h
	- Object3D.h, Object3D.cpp
	- Readme 
	
7.BONUSURI
	- powerups
	- background zona de joc cu stele (functiile createStars + random) din lab7.cpp
	- explozie asteroizi (setare atenuare + parametru alpha, lumina abientala, difuza si speulara)
	- rotatie asteroizi in jurul axelor Ox si Oz
	- numar constant asteroizi in zona de joc

 

 
 
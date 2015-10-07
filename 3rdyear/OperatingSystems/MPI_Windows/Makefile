CPP=cl
CFLAGS = /nologo /I. /W3 /D_CRT_SECURE_NO_DEPRECATE /DWIN32 /EHsc -I.
LDFLAGS = /nologo

WIN_IPC_OBJ = win_queue.obj

all: build

build: mpirun.exe sompi.dll
tema2.obj:	tema2.c 
	$(CPP) $(CFLAGS) /c tema2.c
win_queue.obj: win_queue.c
	$(CPP) $(CFLAGS) /c win_queue.c
sompi.obj: sompi.c
	$(CPP) $(CFLAGS) /c sompi.c
sompi.dll:	sompi.obj win_queue.obj	
	$(CPP) /LD sompi.obj win_queue.obj
mpirun.exe: tema2.obj win_queue.obj
	$(CPP) $(CFLAGS) /Fe$@ tema2.obj  win_queue.obj
client.exe: client.obj hash.obj hashtable.obj $(WIN_IPC_OBJ)
	link $(LDFLAGS) /OUT:$@ $**
	
server.exe: server.obj hash.obj hashtable.obj $(WIN_IPC_OBJ)
	link $(LDFLAGS) /OUT:$@ $**

clean:
	rm -f *.obj *.exe *.exp  *.dll *.lib


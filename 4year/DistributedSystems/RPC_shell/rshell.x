/**
	Sisteme de programe pentru retele de calculatoare
	
	Copyright (C) 2008 Ciprian Dobre & Florin Pop
	Univerity Politehnica of Bucharest, Romania

	This program is free software; you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
 */
const BUFFERSIZE=1024;
typedef char words<>;
struct request
{
	words command;
	struct request* next;
};


struct response
{
	words output;
    words message;
	int retcode;
	struct response* next;
};
program LOAD_PROG {
	version LOAD_VERS {
		response GET_REMOTEDIR(void) = 1;
		response EXECUTE_SIMPLE_COMMAND(request) = 2;
		response EXECUTE_COMMAND(request) = 3;		
	} = 1;
} = 123456789;

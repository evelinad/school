/**
 * Operating Sytems 2013 - Assignment 1
 * DUMITRESCU EVELINA 331CA
 */

#include <stdio.h>
#include <stdlib.h>

#include "parser.h"
#include "utils.h"

#define PROMPT		"> "


/*
 * display parsing error message
 */
void parse_error(const char *str, const int where)
{
	fprintf(stderr, "Parse error near %d: %s\n", where, str);
}

/*
 * shell loop
 */
void start_shell()
{
	char *line;
	command_t *root;

	int ret;

	for(;;) {
		printf(PROMPT);
		fflush(stdout);
		ret = 0;

		root = NULL;
		line = read_line();

		if (line == NULL)
			return;
		parse_line(line, &root);

		if (root != NULL) {
			ret = parse_command(root, 0, NULL);
		}
		else{
		    fprintf(stderr, "ERROR: Command is empty.\n");
        }

		free_parse_memory();
		free(line);
		if (ret == SHELL_EXIT || ret == EXIT_FAILURE) {
			break;
		}
	}
}
/*
 * Main
 */
int main(void)
{
	start_shell();

	return EXIT_SUCCESS;
}

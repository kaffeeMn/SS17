#ifndef __4B_H__
#define __4B_H__

#include "4a.h"
#include <stdlib.h> /* size_t */

node_t searchForChunks(node_t node, unsigned int chunks);
void *buddy_alloc(char request_id, size_t size);
void allocateChildren(node_t node, char request_id);
void housekeeping2(node_t node);

#endif

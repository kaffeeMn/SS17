#include "4b.h"
#include <stdio.h> /* NULL */
#include <errno.h>
#include <stdlib.h>

// TODO:
// i)   finde unbelegten Knoten mit passender Blockgröße
// ii)  Knoten belegen
// iii) Invariante soll weiterhin gelten (keine Hilfsfunktion vorhanden)
// iv)  bei 0 Bytes oder nicht ausreichendem Speicher NULL zurück geben
// v)   startadresse des knoten zurueckgeben

// i)
// added to 4b.h
node_t searchForChunks(node_t node, unsigned int chunks){
    // if chunks match and the node is not reserved: return the node
    if(*node == NODE_FREE && node_width_chunks(node) == chunks){
        return node;
    }
    // tmp is either a fitting node or NULL
    node_t tmp;
    node_t left = bst_left(node);
    node_t right = bst_right(node);
    if(left != NULL){
        tmp = searchForChunks(right, chunks);
        if(tmp != NULL){
            return tmp;
        }
    }
    if(right != NULL){
        tmp = searchForChunks(right, chunks);
        if(tmp != NULL){
            return tmp;
        }
    }
    // else return NULL
    return NULL;
}

// iii)
// added to 4b.h
void allocateChildren(node_t node, char request_id){
    node_t left = bst_left(node);
    node_t right = bst_right(node);
    if(left != NULL){
        *left = request_id;
        allocateChildren(left, request_id);
    }
    if(right != NULL){
        *right = request_id;
        allocateChildren(right, request_id);
    }
}
void housekeeping2(node_t node){
    node_t left = bst_left(node);
	node_t right = bst_right(node);

	// Abbruchbedingung der Rekursion
	if (left == NULL)
		return;

	// Zuerst die Teilbäume der Kinder aufräumen...
	housekeeping2(left);
	housekeeping2(right);

    // only sets character to NODE_SPLIT if node was marked as NODE_FREE in the 
    // the first place
    // hence nemo and all his allocated children remain unsplit
	if ( ((*left != NODE_FREE) || (*right != NODE_FREE)) && (*node == NODE_FREE) ){
		*node = NODE_SPLIT;
    }
}

void *buddy_alloc(char request_id, size_t size)
{
	// Diese Funktion sollt ihr implementieren

    // iv)
    if(size == 0){
        return NULL;
    }else{
        unsigned int croppedSize = (unsigned int) size;
        unsigned int chunks = size_to_chunks(croppedSize);

        node_t nemo = searchForChunks(bst_root(), chunks);
        // iv)
        if(nemo == NULL){
            // Fehlerbehandlung
            perror("Kein passender Knoten/ Speicher gefunden");
            // no special exitcode defined
            return NULL;
        }

        // ii)
        // nemo can not be NULL at this point, no need to call set_node_content
        *nemo = request_id;

        // iii)
        allocateChildren(nemo, request_id);
        housekeeping2(bst_parent(nemo));

        // v)
        return node_start_addr(nemo);
    }
}

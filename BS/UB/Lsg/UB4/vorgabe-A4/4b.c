#include "4b.h"
#include "bst.c"
#include <stdio.h> /* NULL */
#include <errno.h>
#include <stdlib.h>

// TODO:
// i)   finde unbelegten Knoten mit passender Blockgröße
// ii)  Knoten belegen
// iii) Invariante soll weiterhin gelten (keine Hilfsfunktion vorhanden)
// iv)  bei 0 Bytes oder nicht ausreichendem Speicher NULL zurück geben

// i)
// added to 4b,h
node_t searchForChunks(node_t node, unsigned int chunks){
    // if chunks match return the node
    if(*node == NODE_FREE && node_width_chunks(node) == chunks){
        return node;
    }
    // tmp is either a fitting node or NULL
    node_t tmp;
    if(bst_left(node) != NULL){
        tmp = searchForChunks(bst_left(node), chunks);
        if(tmp != NULL){
            return tmp;
        }
    }
    if(bst_right(node) != NULL){
        tmp = searchForChunks(bst_right(node), chunks);
        if(tmp != NULL){
            return tmp;
        }
    }
    // else return NULL
    return NULL;
}

// iii)
void invariante(){}

void *buddy_alloc(char request_id, size_t size)
{
	// Diese Funktion sollt ihr implementieren

    // iv)
    if(size == 0){
        return NULL;
    }else{
        croppedSize = (unsigned int) size;
        unsigned int chunks = size_to_chunks(croppedSize);

        node_t nemo = searchForChunks(bst_root(), chunks);
        // iv)
        if(nemo == NULL){
            //Fehlerbehandlung
            perror("Kein passender Knoten/ Speicher gefunden");
            exit(1);
        }

        // ii)
        // nemo can not be NULL no need to call set_node_content
        *nemo = NODE_SPLIT;

        // iii)
        invariante();
    }
}

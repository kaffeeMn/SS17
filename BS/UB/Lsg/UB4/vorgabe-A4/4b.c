#include "4b.h"
#include <stdio.h> /* NULL */

node_t searchForChunks(node_t node, unsigned int chunks){
    if(*node == NODE_FREE && node_width_chunks(node) == chunks){
        return node;
    }
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
    return NULL;
}

void *buddy_alloc(char request_id, size_t size)
{
	// Diese Funktion sollt ihr implementieren
    // TODO:
    // (check) i)   finde unbelegten Knoten mit passender Blockgröße
    // (check) ii)  Knoten belegen
    // () iii) Invariante soll weiterhin gelten (keine Hilfsfunktion vorhanden)
    // (check) iv)  bei 0 Bytes oder nicht ausreichendem Speicher NULL zurück geben

    if(size == 0){
        return NULL;
    }else{
        croppedSize = (unsigned int) size;
        unsigned int chunks = size_to_chunks(croppedSize);

        node_t nemo = searchForChunks(bst_root(), chunks);
        if(nemo == NULL){
            //Fehlerbehandlung
        }

        *nemo = NODE_SPLIT;

        // invarainten checken
    }
}

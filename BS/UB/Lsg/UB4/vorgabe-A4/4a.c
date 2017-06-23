#include "4a.h"
#include <stdio.h> /* NULL */

// is added to 4a.h
void search(void *addr, node_t node){
    if(node_start_addr(node) == addr){
        return node;
    }
    node_t tmp;
    if(bst_left(node) != NULL){
        tmp = search(addr, bst_left(node));
        if(tmp != NULL){
            return tmp;
        }
    }
    if(bst_right(node) != NULL){
        tmp = search(addr, bst_right(node));
        if(tmp != NULL){
            return tmp;
        }
    }
    return NULL;
}

void buddy_free(void *addr)
{
	// Diese Funktion sollt ihr implementieren
    if(addr != NULL){
        // TODO: 
        // (check) i)   finde Knoten mit der Adresse addr
        // (check) ii)  addr entspricht keiner startaddr -> fehleremeldung return 255
        // (check) iii) makiere den Knoten als frei
        
        // findet nemo
        node_t nemo = search(addr, bst_root())
        if(nemo == NULL){
            //Fehlerbehandlung
            return 255;
        }
        // nemo can not be NULL, hence no need to call set_node_content(nemo)

        // nach einem langem Abenteuer und einer aufregenden Flucht
        // vor der Tochter eines lokalen Zahnarztes ist nemo endlich frei
        *nemo = NODE_FREE;

        bst_housekeeping(nemo);
    }
}

#include "4a.h"
//#include "bst.c"
#include <stdio.h> /* NULL */
#include <errno.h>
#include <stdlib.h>

// TODO: 
// i)   finde Knoten mit der Adresse addr
// ii)  addr entspricht keiner startaddr -> fehleremeldung return 255
// iii) makiere den Knoten als frei
// iv)  sicherstellen, dass die Invariante der Baumstruktur weiterhin gilt.

// i)
// is added to 4a.h
node_t search(void *addr, node_t node){
    // returns node if adresses match
    if(node_start_addr(node) == addr){
        return node;
    }
    // tmp node will either be NULL or a correct node
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
    // returns NULL otherwise
    return NULL;
}

void freeChildren(node_t node){
    node_t left = bst_left(node);
    node_t right = bst_right(node);
    if(left != NULL){
        *left = NODE_FREE;
        freeChildren(left);
    }
    if(right != NULL){
        *right = NODE_FREE;
        freeChildren(right);
    }
}

void buddy_free(void *addr)
{
	// Diese Funktion sollt ihr implementieren
    if(addr != NULL){
        
        // Finding Nemo, hehe
        node_t nemo = search(addr, bst_root());
        // ii)
        if(nemo == NULL){
            // Fehlerbehandlung
            perror("Ungültige Adresse, die z.B. nicht der Startadresse eines belegten Speicherbereichs entspricht");
            // Rueckgabewert 255
            exit(255);
        }
        // iii)
        // nemo can not be NULL at this point, hence no need to call set_node_content(nemo)
        freeChildren(nemo);

        // iv)
        // calling housekeeping as demanded
        //
        // nach einem langem Abenteuer und einer aufregenden Flucht
        // vor der Tochter eines lokalen Zahnarztes ist nemo endlich frei
        // *nemo = NODE_FREE;
        bst_housekeeping(bst_parent(nemo));
    }
}

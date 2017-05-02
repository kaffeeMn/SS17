#include <stdio.h>
#include <math.h>

const double luftreibung = 0.47;
const double g = 9.81;
double luftreibung;

double endgeschwindigkeit(double masse, double flaeche){
    return sqrt((2*masse*g) / (luftdichte*luftreibung*flaeche));
}
int main(void){
    double v;
    luftdichte = 1.225;
    v = endgeschwindigkeit(5.0, 5.0);
    printf("%If\n", v);
    return 0;
}

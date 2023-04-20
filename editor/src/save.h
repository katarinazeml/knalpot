#ifndef SAVE_H
#define SAVE_H

#include "raylib.h"

void writeDataToXML(int mat_w, int mat_h, int *matrixArray[], int *matrixArraySize, Rectangle **layers, int *sizeRect, char names[4][11], int tile_w, int tile_h);

#endif
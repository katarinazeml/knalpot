#ifndef SAVE_H
#define SAVE_H

#include "raylib.h"

void writeDataToXML(int mat_w, int mat_h, int *tilemap, Rectangle *rectangles, int *sizeRect, int tile_w, int tile_h);

#endif
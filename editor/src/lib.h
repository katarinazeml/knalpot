#ifndef LIB_H
#define LIB_H

// --- DEFINING CONSTANTS ---
#define SCREEN_WIDTH 1280
#define SCREEN_HEIGHT 720

#define MATRIX_HEIGHT 12
#define MATRIX_WIDTH 36
#define MATRIX_SIZE MATRIX_HEIGHT * MATRIX_WIDTH

#define TILE_WIDTH 16
#define TILE_HEIGHT 16
#define CELL_WIDTH TILE_WIDTH
#define CELL_HEIGHT TILE_HEIGHT

#define MAX_INPUT_CHARS 11
#define MAX_LAYERS 4

// --- DEFINING CONSTRUCTORS ---
typedef struct canvas {
    float x;
    float y;
    float *cam_relat_x;
    float *cam_relat_y;
    int width;
    int height;
} Canvas;

typedef struct cell {
    float x;
    float y;
    float *cam_relat_x;
    float *cam_relat_y;
} Cell;

typedef struct tile {
    float x;
    float y;
    float *cam_relat_x;
    float *cam_relat_y;
    float tileset_x;
    float tileset_y;
    int width;
    int height;
} Tile;

#endif
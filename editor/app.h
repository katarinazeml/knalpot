#ifndef EDITOR_MAP
#define EDITOR_MAP

// --- DEFINING CONSTANTS ---
#define SCREEN_WIDTH 640
#define SCREEN_HEIGHT 480

#define TILE_WIDTH 16
#define TILE_HEIGHT 16
#define CELL_WIDTH TILE_WIDTH
#define CELL_HEIGHT TILE_HEIGHT

#define MATRIX_HEIGHT 5
#define MATRIX_WIDTH 7
#define MATRIX_SIZE MATRIX_HEIGHT * MATRIX_WIDTH

// --- DEFINING CONSTRUCTORS ---
typedef struct canvas {
    float x;
    float y;
    float *cam_relat_x;
    float *cam_relat_y;
    int width;
    int height;
} Canvas;

typedef struct tile {
    float x;
    float y;
    float *cam_relat_x;
    float *cam_relat_y;
    float tileset_x;
    float tileset_y;
} Tile;

typedef struct cell {
    float x;
    float y;
    float *cam_relat_x;
    float *cam_relat_y;
} Cell;

#endif
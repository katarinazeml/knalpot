#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <raylib.h>
#include <raymath.h>

#include "lib.h"
#include "save.h"

/*
    Global State variable.
    0 - draw
    1 - move
    2 - put collision blocks.
*/
int toolsAmount = 3;
int state = 0;

// Ordinary matrix tilemap
int tilemap[MATRIX_HEIGHT][MATRIX_WIDTH] = { 0 };
int *tCell;

// Global scale value.
float scale = 1.0f;
float *pScale = &scale;

// Tileset init.
Image image;
Texture tileset;
int tileIndex;

// Tileset pointers.
Image *pImage = &image;
Texture *pTileset = &tileset;
int *pTileIndex = &tileIndex;

// Simple AABB Collisions
bool AABB(float *x, float *y, int *width, int *height) {
    if ((int) *x <= GetMouseX() && (int) *x + *width >= GetMouseX()
        && (int) *y <= GetMouseY() && (int) *y + *height >= GetMouseY()) {
        // printf("Collision X:Y %f:%f\n", *x, *y);
        // printf("Mouse X:Y %d:%d\n", GetMouseX(), GetMouseY());
        return true;
    }
    return false;
}

// Return the coordinate of cell cursor collides with.
int collidingCellCoordinates(Cell *cells) {
    int width = CELL_WIDTH * (*pScale);
    int height = CELL_HEIGHT * (*pScale);

    float x;
    float y;

    for (int i = 0; i < MATRIX_SIZE; i++) {
        x = (*cells[i].cam_relat_x + cells[i].x * (*pScale));
        y = (*cells[i].cam_relat_y + cells[i].y * (*pScale));

        if (AABB(&x, &y, &width, &height)) {
            // printf("Colliding X:Y %f:%f\n", x, y);
            return i;
        }
    }
    return 0;
}

// Canvas data.
void writeCellsCoordinates(Canvas *canvas, Cell *cells) {
    for (int i = 0; i < MATRIX_SIZE; i++) {
        int col = i % MATRIX_WIDTH;
        int row = i / MATRIX_WIDTH;
        
        Cell cell;
        cell.x = col * CELL_WIDTH;
        cell.y = row * CELL_HEIGHT;
        cell.cam_relat_x = canvas->cam_relat_x;
        cell.cam_relat_y = canvas->cam_relat_y;

        printf("%d : %f:%f / %d:%f\n", i, cell.x, cell.y, i % MATRIX_WIDTH, (float) i / MATRIX_WIDTH);

        cells[i] = cell;
    }
}

// Updates locally-generated matrix.
bool updateMatrix(int *cellIndex, int *tilesetIndex) {
    int col = *cellIndex % MATRIX_WIDTH;
    int row = *cellIndex / MATRIX_WIDTH;
    tCell = &tilemap[row][col];
    if (*tCell != *tilesetIndex) {
        *tCell = *tilesetIndex;
        return true;
    }
    return false;
}

// Saves tile into an array.
void saveTileOnClick(Canvas *canvas, Tile *tiles, int *index, float *x, float *y, float *tile_x, float *tile_y) {
    tiles[*index].x = *x;
    tiles[*index].y = *y;
    tiles[*index].cam_relat_x = canvas->cam_relat_x;
    tiles[*index].cam_relat_y = canvas->cam_relat_y;
    tiles[*index].tileset_x = *tile_x;
    tiles[*index].tileset_y = *tile_y;
}

// Draws all saved tiles
void drawSavedTiles(Tile *tiles, int *threshold) {
    for (int i = 0; i < *threshold; i++) {
        DrawTextureRec(*pTileset, 
            (Rectangle) { tiles[i].tileset_x, tiles[i].tileset_y, TILE_WIDTH, TILE_HEIGHT }, 
            (Vector2) { tiles[i].x, tiles[i].y }, WHITE);
    }
}

// Draws canvas
void drawCanvas(Canvas *canvas) {
    DrawRectangleLines(canvas->x, canvas->y, canvas->width, canvas->height, WHITE);
}

int run() {
    InitWindow(SCREEN_WIDTH, SCREEN_HEIGHT, "Homemade Food");
    SetTargetFPS(60);

    bool isW;
    bool isS;
    bool isA;
    bool isD;

    float moveX = 0;
    float moveY = 0;

    int collidingCell;
    int savedTilesIndex = 0;

    Vector2 globalPos = { 0 };

    // Drawing current tile.
    *pImage = LoadImage("../grass.png");
    *pTileset = LoadTextureFromImage(*pImage);
    UnloadImage(*pImage);
    Rectangle currentTile = { moveX, moveY, TILE_WIDTH, TILE_HEIGHT };

    // Allocating memory to arrays for cells and tiles.
    Cell *cells = malloc(sizeof *cells * MATRIX_SIZE);
    Tile *tiles = malloc(sizeof *tiles * MATRIX_SIZE);

    // Collisions deserve a separate stuff.
    Rectangle *collisions = malloc(sizeof *collisions * MATRIX_SIZE);
    int collisionsIndex = 0;
    int timesMousePressed = 0;
    int rectWidth;
    int rectHeight;
    float startX;
    float startY;

    Camera2D camera = { 0 };
    camera.zoom = 1.0f;

    Canvas canvas = { 0 };
    canvas.x = .0f;
    canvas.y = .0f;
    canvas.cam_relat_x = &globalPos.x;
    canvas.cam_relat_y = &globalPos.y;
    canvas.width = MATRIX_WIDTH * CELL_WIDTH;
    canvas.height = MATRIX_HEIGHT * CELL_HEIGHT;

    writeCellsCoordinates(&canvas, cells);

    while (!WindowShouldClose()) {
        isW = IsKeyPressed(KEY_W);
        isS = IsKeyPressed(KEY_S);
        isA = IsKeyPressed(KEY_A);
        isD = IsKeyPressed(KEY_D);

        if (isA) moveX += -1;
        if (isD) moveX += 1;
        if (isW) moveY += -1;
        if (isS) moveY += 1;

        currentTile.x = moveX * TILE_WIDTH;
        currentTile.y = moveY * TILE_HEIGHT;

        if (IsKeyPressed(KEY_TAB)) {
            printf("State before: %d\n", state);
            state = (state + 1) % toolsAmount;
            printf("State after: %d\n", state);
        }

        if (IsMouseButtonDown(MOUSE_BUTTON_LEFT) && state == 1) {
            Vector2 delta = GetMouseDelta();
            delta = Vector2Scale(delta, -1.0f / camera.zoom);

            camera.target = Vector2Add(camera.target, delta);
        }

        // Zoom based on mouse wheel
        float wheel = GetMouseWheelMove();
        if (wheel != 0) {
            // Zoom increment
            const float zoomIncrement = 0.125f;

            camera.zoom += (wheel*zoomIncrement);
            if (camera.zoom < zoomIncrement) camera.zoom = zoomIncrement;
            *pScale = camera.zoom;
        }

        globalPos = GetWorldToScreen2D((Vector2) { .0f, .0f }, camera);

        collidingCell = collidingCellCoordinates(cells);

        BeginDrawing();
        ClearBackground(BLACK);

        DrawTexture(*pTileset, 0, 0, WHITE);
        DrawRectangleLines(currentTile.x, currentTile.y, TILE_WIDTH, TILE_HEIGHT, GRAY);

        BeginMode2D(camera);

        drawSavedTiles(tiles, &savedTilesIndex);

        if (state == 0) {
            DrawTextureRec(*pTileset, currentTile,
                (Vector2) { cells[collidingCell].x, cells[collidingCell].y }, WHITE);
            if (IsMouseButtonDown(MOUSE_BUTTON_LEFT)) {
                int tileIndex = (currentTile.x / TILE_WIDTH) + (5 * (currentTile.y) / 16) + 1;
                bool notFilledCell = updateMatrix(&collidingCell, &tileIndex);
                printf("Is this cell empty? %s\n", notFilledCell ? "true" : "false");
                if (notFilledCell) {
                    saveTileOnClick(&canvas, tiles, &savedTilesIndex,
                        &cells[collidingCell].x, &cells[collidingCell].y, &currentTile.x, &currentTile.y);
                    savedTilesIndex++;
                }
            }
        }

        // This whole stuff must be rewritten later. It is absolutely monstrous and ugly.
        if (state == 2) {
            bool mousePressed = IsMouseButtonPressed(MOUSE_BUTTON_LEFT);
            
            // Starting point.
            if (mousePressed) {
                if (timesMousePressed % 3 == 0) {
                    startX = cells[collidingCell].x;
                    startY = cells[collidingCell].y;
                }
                timesMousePressed++;
            }

            if (timesMousePressed % 3 != 1)
                DrawRectangleLines(cells[collidingCell].x, cells[collidingCell].y, TILE_WIDTH, TILE_HEIGHT, BLUE);

            // Rendering current collision block.
            if (timesMousePressed % 3 == 1) {
                rectWidth = cells[collidingCell].x - startX + TILE_WIDTH;
                rectHeight = cells[collidingCell].y - startY + TILE_HEIGHT;
                printf("Width:Height %d:%d\n", rectWidth, rectHeight);
                DrawRectangleLines(startX, startY, rectWidth, rectHeight, BLUE);
            }

            // Saving collisions block.
            if (timesMousePressed % 3 == 2) {
                printf("index: %d\n", collisionsIndex);
                collisions[collisionsIndex].x = startX;
                collisions[collisionsIndex].y = startY;
                collisions[collisionsIndex].width = rectWidth;
                collisions[collisionsIndex].height = rectHeight;
                collisionsIndex++;
                timesMousePressed += 1;
            }
        }

        drawCanvas(&canvas);

        EndMode2D();
        EndDrawing();
    }

    UnloadTexture(*pTileset);

    /* Display the matrix */
    for (int i = 0; i < MATRIX_HEIGHT; i++) {
        for (int j = 0; j < MATRIX_WIDTH; j++) {
            printf ("%d ", tilemap[i][j]);
        }
        printf ("\n");
    }
    printf("\n");

    writeDataToXML(MATRIX_WIDTH, MATRIX_HEIGHT, (int*) tilemap, collisions, &collisionsIndex, TILE_WIDTH, TILE_HEIGHT);

    free(cells);
    free(tiles);
    free(collisions);

    CloseWindow();
    return 0;
}
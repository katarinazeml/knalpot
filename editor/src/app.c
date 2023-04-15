/*
    NB! THIS PROGRAM CODE IS ABSOLUTELY HORRIBLE.
    I CANNOT UNDERSTAND HOW I COULD HAVE WRITTEN IT (ALTHOUGH I DO
    UNDERSTAND WHAT EXACTLY IT DOES AND CAN EVEN EXPLAIN IT).

    THIS CODE MUST BE REFACTORED LATER BECAUSE IT HAS BECOME
    SPAGHETTI-LIKE MESS. PLEASE DON'T JUDGE ME BY IT.

    btw, program works almost perfectly lol
*/

#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <raylib.h>
#include <raymath.h>
#include <string.h>

#include "lib.h"
#include "save.h"

/*
    Global State variable.
    0 - draw
    1 - move
    2 - put collision blocks
    3 - eraser
*/
int toolsAmount = 4;
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

// Adds layer to the list of names (for displaying current layer name)
// and to the layers list. I hope it will work because I don't quite
// understand how it's saved in terms of memory addresses.
// It had worked previously, therefore I suppose address location
// is a constant which is later saved to the list and can be
// freely accessed.
void addLayer(char *name, char namesList[MAX_LAYERS][MAX_INPUT_CHARS], int index, Rectangle **layers) {
    printf("previous layer name: %s\n", namesList[index - 1]);
    printf("pointer to name variable: %p\n", name);
    strcpy(namesList[index], name);
    Rectangle *newLayer = malloc(sizeof *newLayer * MATRIX_SIZE);
    layers[index] = newLayer;
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

// Deletes tile from the matrix.
void deleteTile(Tile *tiles, int *tilesAmount,  float *x, float *y, int *cellIndex, int *tileIndex) {
    // Resetting matrix
    int col = *cellIndex % MATRIX_WIDTH;
    int row = *cellIndex / MATRIX_WIDTH;
    tCell = &tilemap[row][col];
    if (*tCell != 0) {
        *tCell = 0;
    }

    // Removing tile from tiles matrix
    for (int i = 0; i < *tilesAmount; i++) {
        if (tiles[i].x == *x && tiles[i].y == *y) {
            tiles[i] = tiles[*tilesAmount];
        }
    }
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

// Draws textbox
void drawTextbox(Rectangle *textbox, char *name) {
    DrawRectangleRec(*textbox, LIGHTGRAY);
    DrawRectangleLines((int) textbox->x, (int) textbox->y, (int) textbox->width, (int) textbox->height, DARKGRAY);
    DrawText(name, (int) textbox->x + 5, (int) textbox->y + 8, 40, MAROON);
}

int run() {
    InitWindow(SCREEN_WIDTH, SCREEN_HEIGHT, "Homemade Food");
    SetTargetFPS(60);

    bool isW;
    bool isS;
    bool isA;
    bool isD;

    bool isALTTAB;
    
    // Text field
    bool isCTRLN;
    bool isBackspace;
    bool isEnter;
    bool toAddLayer = false;

    char name[MAX_INPUT_CHARS + 1] = "\0";
    int letterCount = 0;

    Rectangle textBox = { SCREEN_WIDTH / 2.0f - 100, SCREEN_HEIGHT / 2.0f - 25, 225, 50 };

    // Movement
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
    int collisionsIndex = 0;
    int timesMousePressed = 0;
    int rectWidth;
    int rectHeight;
    float startX;
    float startY;

    // Temporary layer stuff.
    int maxLayers = 4;
    int currentLayer = 0;
    int savedLayers = 0;

    char names[MAX_LAYERS][MAX_INPUT_CHARS]; // saving names for each layer.
    strcpy(names[0], "collisions");
    strcpy(names[1], "");
    strcpy(names[2], "");
    strcpy(names[3], "");

    int collisionsIndexList[4];
    collisionsIndexList[0] = 0;
    collisionsIndexList[1] = 0;
    collisionsIndexList[2] = 0;
    collisionsIndexList[3] = 0;

    int objCountInLayer[5];
    objCountInLayer[0] = maxLayers; // max amount of layers

    Rectangle **layers = malloc(sizeof **layers * objCountInLayer[0]);
    Rectangle *collisions = malloc(sizeof *collisions * MATRIX_SIZE);

    layers[0] = collisions;
    layers[1] = NULL;
    layers[2] = NULL;
    layers[3] = NULL;

    // Global camera
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
        // Checking keyboard clicks.
        isW = IsKeyPressed(KEY_W);
        isS = IsKeyPressed(KEY_S);
        isA = IsKeyPressed(KEY_A);
        isD = IsKeyPressed(KEY_D);
        isALTTAB = IsKeyDown(KEY_LEFT_ALT) && IsKeyPressed(KEY_TAB);
        isCTRLN = IsKeyDown(KEY_LEFT_CONTROL) && IsKeyPressed(KEY_N);
        isBackspace = IsKeyPressed(KEY_BACKSPACE);
        isEnter = IsKeyPressed(KEY_ENTER);

        if (isA) moveX += -1;
        if (isD) moveX += 1;
        if (isW) moveY += -1;
        if (isS) moveY += 1;

        currentTile.x = moveX * TILE_WIDTH;
        currentTile.y = moveY * TILE_HEIGHT;

        if (isALTTAB) {
            printf("previous layer: %s\n", names[currentLayer]);
            currentLayer++;
            currentLayer = currentLayer % maxLayers;
            printf("current layer index: %d\n", currentLayer);
            printf("current layer: %s\n", names[currentLayer]);
        }

        if (IsKeyPressed(KEY_TAB)) {
            printf("State before: %d\n", state);
            state = (state + 1) % toolsAmount;
            printf("State after: %d\n", state);
        }

        if (isCTRLN) {
            printf("CTRL-N is pressed");
            toAddLayer = true;
        }

        if (toAddLayer) {
            int key = GetCharPressed();

            while (key > 0) {
                if ((key > 32) && (key < 125) && letterCount < MAX_INPUT_CHARS) {
                    name[letterCount] = (char) key;
                    name[letterCount + 1] = '\0';
                    letterCount++;
                }

                key = GetCharPressed();
            }

            if (isBackspace) {
                letterCount--;
                if (letterCount < 0) letterCount = 0;
                name[letterCount] = '\0';
            }

            if (isEnter) {
                if (savedLayers + 1 < MAX_LAYERS && name[0] != '\0') {
                    addLayer(name, names, savedLayers + 1, layers);
                    savedLayers++;
                }
                toAddLayer = false;
            }
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

        if (toAddLayer) {
            drawTextbox(&textBox, name);
        }

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
                collisionsIndex = collisionsIndexList[currentLayer];

                layers[currentLayer][collisionsIndex].x = startX;
                layers[currentLayer][collisionsIndex].y = startY;
                layers[currentLayer][collisionsIndex].width = rectWidth;
                layers[currentLayer][collisionsIndex].height = rectHeight;

                collisionsIndexList[currentLayer] += 1;
                timesMousePressed += 1;
            }
        }

        if (state == 3) {
            bool mousePressed = IsMouseButtonPressed(MOUSE_BUTTON_LEFT);

            DrawRectangleLines(cells[collidingCell].x, cells[collidingCell].y, TILE_WIDTH, TILE_HEIGHT, GRAY);

            if (mousePressed) {
                int tileIndex = (currentTile.x / TILE_WIDTH) + (5 * (currentTile.y) / 16) + 1;
                deleteTile(tiles, &savedTilesIndex, &cells[collidingCell].x, &cells[collidingCell].y, &collidingCell, &tileIndex);
            }
        }

        drawCanvas(&canvas);

        EndMode2D();
        EndDrawing();
    }

    objCountInLayer[1] = collisionsIndexList[0];
    objCountInLayer[2] = collisionsIndexList[1];
    objCountInLayer[3] = collisionsIndexList[2];
    objCountInLayer[4] = collisionsIndexList[3];

    UnloadTexture(*pTileset);

    /* Display the matrix */
    for (int i = 0; i < MATRIX_HEIGHT; i++) {
        for (int j = 0; j < MATRIX_WIDTH; j++) {
            printf ("%d ", tilemap[i][j]);
        }
        printf ("\n");
    }
    printf("\n");

    writeDataToXML(MATRIX_WIDTH, MATRIX_HEIGHT, (int*) tilemap, layers, objCountInLayer, names, TILE_WIDTH, TILE_HEIGHT);

    free(cells);
    free(tiles);
    free(layers);
    free(collisions);

    CloseWindow();
    return 0;
}
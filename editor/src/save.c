#include <stdio.h>
#include <raylib.h>

// Saving customized matrix to XML file.
void writeMatrixData(FILE *f, int *mat_w, int *mat_h, int *matrixArray[], int *matrixArraySize) {
    for (int i = 0; i < *matrixArraySize; i++) {
        fprintf(f, "<layer id=\"%d\" name=\"Tile Layer 1\" width=\"%d\" height=\"%d\">\n", i, *mat_w, *mat_h);
        fprintf(f, "<data encoding=\"csv\">\n");
        int *array = matrixArray[i];
        for (int n = 0; n < (*mat_w * *mat_h); n++) {    
            fprintf(f, "%d,", array[n]);
            if (n != 0 && (n + 1) % *mat_w == 0) {
                fprintf(f, "\n");
            }
        }
        fprintf(f, "</data>\n");
        fprintf(f, "</layer>\n");
    }
}

// Writing object data to the XML file (for collisions)
void writeObjectData(FILE *f, Rectangle **layers, int *size, char names[4][11]) {
    printf("Array with sizes.\n");
    printf("%d\n", size[0]);
    printf("%d\n", size[1]);

    for (int n = 0; n < size[0]; n++) {
        fprintf(f, "<objectgroup id=\"%d\" name=\"%s\">\n", n, names[n]);
        if (layers[n] == NULL) {
            fprintf(f, "</objectgroup>\n");
            continue;
        } else {
            for (int i = 0; i < size[n + 1]; i++) {
                fprintf(f, "<object id=\"%d\" x=\"%d\" y=\"%d\" width=\"%d\" height=\"%d\" />\n", 
                    i, (int) layers[n][i].x, (int) layers[n][i].y, (int) layers[n][i].width, (int) layers[n][i].height);
            }
        }
        fprintf(f, "</objectgroup>\n");
    }
}

// Writing tileset data (basically linking atlas.
void writeTilesetData(FILE *f, int *mat_w, int *tile_w, int *tile_h) {
    fprintf(f, "<tileset tilewidth=\"%d\" tileheight=\"%d\" tilecount=\"%d\" columns=\"%d\">\n", *tile_w, *tile_h, 10, *mat_w);
    fprintf(f, "<image source=\"%s\" width=\"%d\" height=\"%d\"/>\n", "atlas.png", 256, 256);
    fprintf(f, "</tileset>\n");
}

// General function for saving map data.
void writeMapData(FILE *f, int *mat_w, int *mat_h, int *matrixArray[], int *matrixArraySize, Rectangle **layers, int *sizeRect, char names[4][11], int *tile_w, int *tile_h) {
    fprintf(f, "<map width=\"%d\" height=\"%d\" tilewidth=\"%d\" tileheight=\"%d\">\n", *mat_w, *mat_h, *tile_w, *tile_h);
    writeTilesetData(f, mat_w, tile_w, tile_h);
    writeMatrixData(f, mat_w, mat_h, matrixArray, matrixArraySize);
    writeObjectData(f, layers, sizeRect, names);
    fprintf(f, "</map>\n");
}

// Saving stuff to the XML file.
void writeDataToXML(int mat_w, int mat_h, int *matrixArray[], int *matrixArraySize, Rectangle **layers, int *sizeRect, char names[4][11], int tile_w, int tile_h) {
    FILE *f;
    f = fopen("../tilemap.xml", "w");
    fprintf(f, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");

    writeMapData(f, &mat_w, &mat_h, matrixArray, matrixArraySize, layers, sizeRect, names, &tile_w, &tile_h);
    
    fclose(f);
}

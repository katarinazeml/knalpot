#include <stdio.h>
#include <raylib.h>

// Saving customized matrix to XML file.
void writeMatrixData(FILE *f, int *mat_w, int *mat_h, int *tilemap) {
    fprintf(f, "<layer id=\"%d\" name=\"Tile Layer 1\" width=\"%d\" height=\"%d\">\n", 1, *mat_w, *mat_h);
        fprintf(f, "<data encoding=\"csv\">\n");
        for (int i = 0; i < (*mat_w * *mat_h); i++) {    
            fprintf(f, "%d,", tilemap[i]);
            if (i != 0 && (i + 1) % *mat_w == 0) {
                fprintf(f, "\n");
            }
        }
        fprintf(f, "</data>\n");
    fprintf(f, "</layer>\n");
}

// Writing object data to the XML file (for collisions)
void writeObjectData(FILE *f, Rectangle *rectangles, int *size) {
// <objectgroup id="2" name="Object Layer 1">
//   <object id="1" x="0" y="32" width="96" height="2"/>
//   <object id="2" x="96" y="0" width="16" height="16"/>
//  </objectgroup>
    fprintf(f, "<objectgroup id=\"1\" name=\"%s\">\n", "collisions");
    for (int i = 0; i < *size; i++) {
        fprintf(f, "<object id=\"%d\" x=\"%d\" y=\"%d\" width=\"%d\" height=\"%d\" />\n", 
            i, (int) rectangles[i].x, (int) rectangles[i].y, (int) rectangles[i].width, (int) rectangles[i].height);
    }
    fprintf(f, "</objectgroup>\n");
}

// Writing tileset data (basically linking atlas.
void writeTilesetData(FILE *f, int *mat_w, int *tile_w, int *tile_h) {
    fprintf(f, "<tileset tilewidth=\"%d\" tileheight=\"%d\" tilecount=\"%d\" columns=\"%d\">\n", *tile_w, *tile_h, 10, *mat_w);
    fprintf(f, "<image source=\"%s\" width=\"%d\" height=\"%d\"/>\n", "grass.png", 80, 32);
    fprintf(f, "</tileset>\n");
}

// General function for saving map data.
void writeMapData(FILE *f, int *mat_w, int *mat_h, int *tilemap, Rectangle *rectangles, int *sizeRect, int *tile_w, int *tile_h) {
    fprintf(f, "<map width=\"%d\" height=\"%d\" tilewidth=\"%d\" tileheight=\"%d\">\n", *mat_w, *mat_h, *tile_w, *tile_h);
    writeTilesetData(f, mat_w, tile_w, tile_h);
    writeMatrixData(f, mat_w, mat_h, tilemap);
    writeObjectData(f, rectangles, sizeRect);
    fprintf(f, "</map>\n");
}

// Saving stuff to the XML file.
void writeDataToXML(int mat_w, int mat_h, int *tilemap, Rectangle *rectangles, int *sizeRect, int tile_w, int tile_h) {
    FILE *f;
    f = fopen("../tilemap.xml", "w");
    fprintf(f, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");

    writeMapData(f, &mat_w, &mat_h, tilemap, rectangles, sizeRect, &tile_w, &tile_h);
    
    fclose(f);
}
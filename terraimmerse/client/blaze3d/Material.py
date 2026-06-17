def has_material(materials, x, y, z):
    return (x, y, z) in materials

def getVertices(x, y, z, chunk):
    material_list = chunk.get().keys()
    TOP=[
        0 + x, 1 + y, 1 + z, 0, 1,
        1 + x, 1 + y, 0 + z, 1, 0,
        0 + x, 1 + y, 0 + z, 0, 0,
        1 + x, 1 + y, 1 + z, 1, 1,
        1 + x, 1 + y, 0 + z, 1, 0,
        0 + x, 1 + y, 1 + z, 0, 1
    ]
    BOTTOM=[
        0 + x, 0 + y, 0 + z, 0, 0,
        1 + x, 0 + y, 0 + z, 1, 0,
        0 + x, 0 + y, 1 + z, 0, 1,
        1 + x, 0 + y, 0 + z, 1, 0,
        1 + x, 0 + y, 1 + z, 1, 1,
        0 + x, 0 + y, 1 + z, 0, 1
    ]
    FRONT=[
        0 + x, 0 + y, 1 + z, 0, 1,
        1 + x, 0 + y, 1 + z, 1, 1,
        0 + x, 1 + y, 1 + z, 0, 0,
        1 + x, 0 + y, 1 + z, 1, 1,
        1 + x, 1 + y, 1 + z, 1, 0,
        0 + x, 1 + y, 1 + z, 0, 0
    ]
    BACK=[
        1 + x, 0 + y, 0 + z, 0, 1,
        0 + x, 0 + y, 0 + z, 1, 1,
        1 + x, 1 + y, 0 + z, 0, 0,
        0 + x, 0 + y, 0 + z, 1, 1,
        0 + x, 1 + y, 0 + z, 1, 0,
        1 + x, 1 + y, 0 + z, 0, 0
    ]
    LEFT=[
        0 + x, 0 + y, 0 + z, 0, 1,
        0 + x, 0 + y, 1 + z, 1, 1,
        0 + x, 1 + y, 0 + z, 0, 0,
        0 + x, 0 + y, 1 + z, 1, 1,
        0 + x, 1 + y, 1 + z, 1, 0,
        0 + x, 1 + y, 0 + z, 0, 0
    ]
    RIGHT=[
        1 + x, 0 + y, 1 + z, 0, 1,
        1 + x, 0 + y, 0 + z, 1, 1,
        1 + x, 1 + y, 1 + z, 0, 0,
        1 + x, 0 + y, 0 + z, 1, 1,
        1 + x, 1 + y, 0 + z, 1, 0,
        1 + x, 1 + y, 1 + z, 0, 0
    ]
    vertices = []
    neighbors = {(0,1,0):TOP, (0,-1,0):BOTTOM, (0,0,1):FRONT, (0,0,-1):BACK, (-1, 0, 0):LEFT, (1,0,0):RIGHT}
    for neighbor in list(neighbors.keys()):
        if (x+neighbor[0], y+neighbor[1],z+neighbor[2]) not in material_list:
            for i in neighbors[neighbor]:
                vertices.append(i)
    return vertices

# TOP (+Y)
# BOTTOM (-Y)
# FRONT (+Z)
# BACK (-Z)
# LEFT (-X)
# RIGHT (+X)
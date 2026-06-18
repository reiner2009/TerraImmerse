def has_material(materials, x, y, z):
    return (x, y, z) in materials

def getVertices(x, y, z, material, chunk, atlas_data, atlas_w, atlas_h, texture_data):
    material_list = chunk.get().keys()
    w=1/atlas_w
    h=1/atlas_h
    top_texture_tx=atlas_data[texture_data[material][0]][0]/atlas_w
    top_texture_ty=atlas_data[texture_data[material][0]][1]/atlas_h
    bottom_texture_tx=atlas_data[texture_data[material][1]][0]/atlas_w
    bottom_texture_ty=atlas_data[texture_data[material][1]][1]/atlas_h
    front_texture_tx=atlas_data[texture_data[material][2]][0]/atlas_w
    front_texture_ty=atlas_data[texture_data[material][2]][1]/atlas_h
    back_texture_tx=atlas_data[texture_data[material][3]][0]/atlas_w
    back_texture_ty=atlas_data[texture_data[material][3]][1]/atlas_h
    left_texture_tx=atlas_data[texture_data[material][4]][0]/atlas_w
    left_texture_ty=atlas_data[texture_data[material][4]][1]/atlas_h
    right_texture_tx=atlas_data[texture_data[material][5]][0]/atlas_w
    right_texture_ty=atlas_data[texture_data[material][5]][1]/atlas_h
    TOP=[
        0 + x, 1 + y, 1 + z, top_texture_tx, top_texture_ty+h,
        1 + x, 1 + y, 0 + z, top_texture_tx+w, top_texture_ty,
        0 + x, 1 + y, 0 + z, top_texture_tx, top_texture_ty,
        1 + x, 1 + y, 1 + z, top_texture_tx+w, top_texture_ty+h,
        1 + x, 1 + y, 0 + z, top_texture_tx+w, top_texture_ty,
        0 + x, 1 + y, 1 + z, top_texture_tx, top_texture_ty+h
    ]
    BOTTOM=[
        0 + x, 0 + y, 0 + z, bottom_texture_tx, bottom_texture_ty,
        1 + x, 0 + y, 0 + z, bottom_texture_tx+w, bottom_texture_ty,
        0 + x, 0 + y, 1 + z, bottom_texture_tx, bottom_texture_ty+h,
        1 + x, 0 + y, 0 + z, bottom_texture_tx+w, bottom_texture_ty,
        1 + x, 0 + y, 1 + z, bottom_texture_tx+w, bottom_texture_ty+h,
        0 + x, 0 + y, 1 + z, bottom_texture_tx, bottom_texture_ty+h
    ]
    FRONT = [
        0 + x, 0 + y, 1 + z, front_texture_tx, front_texture_ty + h,
        1 + x, 0 + y, 1 + z, front_texture_tx + w, front_texture_ty + h,
        0 + x, 1 + y, 1 + z, front_texture_tx, front_texture_ty,
        1 + x, 0 + y, 1 + z, front_texture_tx + w, front_texture_ty + h,
        1 + x, 1 + y, 1 + z, front_texture_tx + w, front_texture_ty,
        0 + x, 1 + y, 1 + z, front_texture_tx, front_texture_ty
    ]

    BACK = [
        1 + x, 0 + y, 0 + z, back_texture_tx, back_texture_ty + h,
        0 + x, 0 + y, 0 + z, back_texture_tx + w, back_texture_ty + h,
        1 + x, 1 + y, 0 + z, back_texture_tx, back_texture_ty,
        0 + x, 0 + y, 0 + z, back_texture_tx + w, back_texture_ty + h,
        0 + x, 1 + y, 0 + z, back_texture_tx + w, back_texture_ty,
        1 + x, 1 + y, 0 + z, back_texture_tx, back_texture_ty
    ]

    LEFT = [
        0 + x, 0 + y, 0 + z, left_texture_tx, left_texture_ty + h,
        0 + x, 0 + y, 1 + z, left_texture_tx + w, left_texture_ty + h,
        0 + x, 1 + y, 0 + z, left_texture_tx, left_texture_ty,
        0 + x, 0 + y, 1 + z, left_texture_tx + w, left_texture_ty + h,
        0 + x, 1 + y, 1 + z, left_texture_tx + w, left_texture_ty,
        0 + x, 1 + y, 0 + z, left_texture_tx, left_texture_ty
    ]

    RIGHT = [
        1 + x, 0 + y, 1 + z, right_texture_tx, right_texture_ty + h,
        1 + x, 0 + y, 0 + z, right_texture_tx + w, right_texture_ty + h,
        1 + x, 1 + y, 1 + z, right_texture_tx, right_texture_ty,
        1 + x, 0 + y, 0 + z, right_texture_tx + w, right_texture_ty + h,
        1 + x, 1 + y, 0 + z, right_texture_tx + w, right_texture_ty,
        1 + x, 1 + y, 1 + z, right_texture_tx, right_texture_ty
    ]
    vertices = []
    neighbors = {(0,1,0):TOP, (0,-1,0):BOTTOM, (0,0,1):FRONT, (0,0,-1):BACK, (-1, 0, 0):LEFT, (1,0,0):RIGHT}
    for neighbor in list(neighbors.keys()):
        if (x+neighbor[0], y+neighbor[1],z+neighbor[2]) not in material_list:
            for i in neighbors[neighbor]:
                vertices.append(i)
    return vertices
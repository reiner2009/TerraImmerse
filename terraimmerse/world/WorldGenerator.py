from opensimplex import noise2

def createWorld(chunk, size=100):
    for x in range(size):
        for z in range(size):
            value = noise2(x * 0.05, z * 0.05)*5+noise2(x * 0.1, z * 0.1)*2.5+noise2(x * 0.2, z * 0.2)*(2.5/2)+noise2(x * 0.4, z * 0.4)*(2.5/4)+10
            ay = round(value)
            for y in range(ay):
                chunk.setMaterial(x, y, z, "stone_cube")
    for pos, m in chunk.get().items():
        if (pos[0], pos[1]+2, pos[2]) not in chunk.get().keys() and (pos[0], pos[1]+1, pos[2]) in chunk.get().keys():
            chunk.setMaterial(*pos, "dirt_cube")
    for pos, m in chunk.get().items():
        if (pos[0], pos[1]+1, pos[2]) not in chunk.get().keys():
            chunk.setMaterial(*pos, "grass_cube")
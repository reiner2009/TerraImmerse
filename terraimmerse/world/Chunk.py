class Chunk:
    def __init__(self, x, z):
        self.x = x
        self.z = z
        self.material={}
    def setMaterial(self, x, y, z, id_name):
        if id_name != "air":
            self.material[(int(x)+self.x*16, int(y), int(z)+self.z*16)] = id_name
        if id_name == "air":
            try:
                self.material.pop((int(x)+self.x*16, int(y), int(z)+self.z*16), None)
            except KeyError:
                pass
    def getMaterial(self, x,y,z):
        return self.material[(x,y,z)]
    def get(self):
        return self.material
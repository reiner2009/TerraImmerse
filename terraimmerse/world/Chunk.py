class Chunk:
    def __init__(self, x, z):
        self.x = x
        self.z = z
        self.material={}
    def setMaterial(self, x, y, z, id_name):
        self.material[(int(x)+self.x*16, int(y), int(z)+self.z*16)] = id_name
    def getMaterial(self, x,y,z):
        return self.material[(x,y,z)]
    def get(self):
        return self.material
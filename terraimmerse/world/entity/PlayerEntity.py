import glm

class PlayerEntity:
    def __init__(self):
        self.pos = glm.vec3(0, 0, 0)
        self.yaw = 0
        self.pitch = 0
    def move(self, x, y, z):
        self.pos += glm.vec3(x, y, z)
    def setPos(self, x, y, z):
        self.pos = glm.vec3(x, y, z)
    def getPos(self):
        return self.pos
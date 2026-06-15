import glm

def create_projection(width, height, fov=70.0, near=0.1, far=2000.0):
    aspect = width / height
    return glm.perspective(
        glm.radians(fov),
        aspect,
        near,
        far
    )

class Camera:
    def __init__(self, position=None, target=None):
        self.position = position or glm.vec3(0, 0, 3)
        self.target = target or glm.vec3(0, 0, 0)
        self.up = glm.vec3(0, 1, 0)
    def get_view_matrix(self):
        return glm.lookAt(
            self.position,
            self.target,
            self.up
        )
    def move(self, direction: glm.vec3):
        self.position += direction
        self.target += direction

def create_model(position=None, rotation=None, scale=None):
    model = glm.mat4(1.0)
    if position is not None:
        model = glm.translate(model, position)
    if rotation is not None:
        angle, axis = rotation
        model = glm.rotate(model, glm.radians(angle), axis)
    if scale is not None:
        model = glm.scale(model, scale)
    return model

def rotate_y(time_seconds, speed=1.0):
    return glm.rotate(
        glm.mat4(1.0),
        time_seconds * speed,
        glm.vec3(0, 1, 0)
    )
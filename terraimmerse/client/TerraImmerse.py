from math import radians

from terraimmerse.client.blaze3d import*
import terraimmerse.client.blaze3d.world.Chunk as ClientChunk
import terraimmerse.client.blaze3d.Shader as Shader
import glm
import math

from terraimmerse.client.Textures import load_texture
from terraimmerse.world.entity.PlayerEntity import PlayerEntity


class TerraImmerse:
    def __init__(self):
        self.running=True
        self.glContext = glContext()
        self.glContext.init_gl()
        self.clientChunk = ClientChunk.ClientChunk(ClientChunk.chunk)
        self.clientChunk.build()
        pygame.mouse.set_visible(False)
        pygame.event.set_grab(True)
        self.player=PlayerEntity()
        self.clock=pygame.time.Clock()
        self.width, self.height = self.glContext.get_resolution()
        self.shaderCompiler= Shader.ShaderCompiler()
        self.shader=self.shaderCompiler.create_shader_program(self.shaderCompiler.src_vertex, self.shaderCompiler.src_fragment)
        self.loc_model = glGetUniformLocation(self.shader, "model")
        self.loc_view = glGetUniformLocation(self.shader, "view")
        self.loc_proj = glGetUniformLocation(self.shader, "projection")
        self.texture = load_texture("assets/textures/atlas.png")
        self.tex_loc = glGetUniformLocation(self.shader, "tex")
        self.model = glm.mat4(1.0)
        self.view = glm.lookAt(
            glm.vec3(0, 0, 3),
            glm.vec3(0, 0, 0),
            glm.vec3(0, 1, 0)
        )
        self.projection = glm.perspective(
            glm.radians(70.0),
            self.width / self.height,
            0.1,
            2000.0
        )
        self.camera_pos = glm.vec3(0, 5, 0)
        self.sensitivity = 0.002
        self.player.setPos(*glm.vec3(0, 5, 0))
        self.speed=0.1
        while self.running:
            for event in pygame.event.get():
                if event.type == pygame.QUIT:
                    self.stop()
                if event.type == pygame.KEYDOWN and event.key == pygame.K_r:
                    ClientChunk.rebuild()
            self.drawScene()
    def getInstance(self):
        return self
    def stop(self):
        self.running=False
    def drawScene(self):
        self.checkMovement()
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)
        glUseProgram(self.shader)
        glActiveTexture(GL_TEXTURE0)
        glBindTexture(GL_TEXTURE_2D, self.texture)
        glUniform1i(self.tex_loc, 0)
        glUniformMatrix4fv(self.loc_model, 1, GL_FALSE, glm.value_ptr(self.model))
        glUniformMatrix4fv(self.loc_view, 1, GL_FALSE, glm.value_ptr(self.view))
        glUniformMatrix4fv(self.loc_proj, 1, GL_FALSE, glm.value_ptr(self.projection))
        glBindVertexArray(self.clientChunk.getVao())
        glDrawArrays(GL_TRIANGLES, 0, int(len(self.clientChunk.getVertices())/5))
        glBindVertexArray(0)
        pygame.display.flip()
    def checkMovement(self):
        self.move_x = 0
        self.move_z = 0
        self.move_y = 0
        self.dx = math.sin(self.player.yaw)
        self.dz = math.cos(self.player.yaw)
        self.dx_side = math.sin(self.player.yaw-radians(90))
        self.dz_side = math.cos(self.player.yaw-radians(90))
        keys = pygame.key.get_pressed()
        if keys[K_w]:
            self.move_x += self.dx * self.speed
            self.move_z += self.dz * self.speed
        if keys[K_s]:
            self.move_x -= self.dx * self.speed
            self.move_z -= self.dz * self.speed
        if keys[K_a]:
            self.move_x -= self.dx_side * self.speed
            self.move_z -= self.dz_side * self.speed
        if keys[K_d]:
            self.move_x += self.dx_side * self.speed
            self.move_z += self.dz_side * self.speed
        if keys[K_LSHIFT]:
            self.move_y -= 0.1
        if keys[K_SPACE]:
            self.move_y += 0.1
        self.player.move(*glm.vec3(self.move_x, self.move_y, self.move_z))
        dx, dy = pygame.mouse.get_rel()
        self.player.yaw -= dx * self.sensitivity
        self.player.pitch -= dy * self.sensitivity
        self.player.pitch = max(-1.5, min(1.5, self.player.pitch))
        self.yaw = self.player.yaw
        self.pitch = self.player.pitch
        self.direction = glm.vec3(
            math.cos(self.pitch) * math.sin(self.yaw),
            math.sin(self.pitch),
            math.cos(self.pitch) * math.cos(self.yaw)
        )
        self.view = glm.lookAt(
            self.player.getPos(),
            self.player.getPos() + self.direction,
            glm.vec3(0, 1, 0)
        )

terraImmerse = TerraImmerse()
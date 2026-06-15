from math import radians

import terraimmerse.client.blaze3d.Context as context
import terraimmerse.client.blaze3d as b3d
import terraimmerse.client.blaze3d.Shader as Shader
import glm
import math

from terraimmerse.client.Textures import load_texture
from terraimmerse.world.entity.PlayerEntity import PlayerEntity


class TerraImmerse:
    def __init__(self):
        self.running=True
        context.init_gl()
        b3d.initialize()
        context.pygame.mouse.set_visible(False)
        context.pygame.event.set_grab(True)
        self.player=PlayerEntity()
        self.clock=context.pygame.time.Clock()
        self.width, self.height = context.get_resolution()
        self.shader=Shader.create_shader_program(Shader.src_vertex, Shader.src_fragment)
        self.loc_model = context.glGetUniformLocation(self.shader, "model")
        self.loc_view = context.glGetUniformLocation(self.shader, "view")
        self.loc_proj = context.glGetUniformLocation(self.shader, "projection")
        self.texture = load_texture("assets/bricks.png")
        self.tex_loc = context.glGetUniformLocation(self.shader, "tex")
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
        self.camera_pos = glm.vec3(0, 0, 3)
        self.sensitivity = 0.002
        self.player.setPos(*glm.vec3(0, 0, -3))
        self.speed=0.1
        while self.running:
            for event in context.pygame.event.get():
                if event.type == context.pygame.QUIT:
                    self.stop()
            self.drawScene()
    def getInstance(self):
        return self
    def stop(self):
        self.running=False

    def drawScene(self):
        self.checkMovement()
        context.glClear(context.GL_COLOR_BUFFER_BIT | context.GL_DEPTH_BUFFER_BIT)
        context.glUseProgram(self.shader)
        context.glActiveTexture(context.GL_TEXTURE0)
        context.glBindTexture(context.GL_TEXTURE_2D, self.texture)
        context.glUniform1i(self.tex_loc, 0)
        context.glUniformMatrix4fv(self.loc_model, 1, context.GL_FALSE, glm.value_ptr(self.model))
        context.glUniformMatrix4fv(self.loc_view, 1, context.GL_FALSE, glm.value_ptr(self.view))
        context.glUniformMatrix4fv(self.loc_proj, 1, context.GL_FALSE, glm.value_ptr(self.projection))
        context.glBindVertexArray(b3d.getVao())
        context.glDrawArrays(context.GL_TRIANGLES, 0, 3)
        context.glBindVertexArray(0)
        context.pygame.display.flip()
    def checkMovement(self):
        self.move_x = 0
        self.move_z = 0
        self.move_y = 0
        self.dx = math.sin(self.player.yaw)
        self.dz = math.cos(self.player.yaw)
        self.dx_side = math.sin(self.player.yaw-radians(90))
        self.dz_side = math.cos(self.player.yaw-radians(90))
        keys = context.pygame.key.get_pressed()
        if keys[context.K_w]:
            self.move_x += self.dx * self.speed
            self.move_z += self.dz * self.speed
        if keys[context.K_s]:
            self.move_x -= self.dx * self.speed
            self.move_z -= self.dz * self.speed
        if keys[context.K_a]:
            self.move_x -= self.dx_side * self.speed
            self.move_z -= self.dz_side * self.speed
        if keys[context.K_d]:
            self.move_x += self.dx_side * self.speed
            self.move_z += self.dz_side * self.speed
        if keys[context.K_LSHIFT]:
            self.move_y -= 0.1
        if keys[context.K_SPACE]:
            self.move_y += 0.1
        self.player.move(*glm.vec3(self.move_x, self.move_y, self.move_z))
        dx, dy = context.pygame.mouse.get_rel()
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
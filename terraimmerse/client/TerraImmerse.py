import terraimmerse.client.blaze3d.Context as context
import terraimmerse.client.blaze3d as b3d
import terraimmerse.client.blaze3d.Shader as Shader
import glm

class TerraImmerse:
    def __init__(self):
        self.running=True
        context.init_gl()
        b3d.initialize()
        self.clock=context.pygame.time.Clock()
        self.width, self.height = context.get_resolution()
        self.shader=Shader.create_shader_program(Shader.src_vertex, Shader.src_fragment)
        self.loc_model = context.glGetUniformLocation(self.shader, "model")
        self.loc_view = context.glGetUniformLocation(self.shader, "view")
        self.loc_proj = context.glGetUniformLocation(self.shader, "projection")
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
        while self.running:
            for event in context.pygame.event.get():
                if event.type == context.pygame.QUIT:
                    self.stop()
            self.drawScene()
            context.pygame.display.flip()
            self.clock.tick(60)
    def getInstance(self):
        return self
    def stop(self):
        self.running=False
    def drawScene(self):
        self.model = glm.rotate(
            self.model,
            glm.radians(1.0),
            glm.vec3(0, 1, 0)
        )
        self.view = glm.lookAt(
            self.camera_pos,
            self.camera_pos + glm.vec3(0, 0, -1),
            glm.vec3(0, 1, 0)
        )
        self.camera_pos += glm.vec3(0, 0.001, 0)
        context.glClear(context.GL_COLOR_BUFFER_BIT | context.GL_DEPTH_BUFFER_BIT)
        context.glUseProgram(self.shader)
        context.glUniformMatrix4fv(self.loc_model, 1, context.GL_FALSE, glm.value_ptr(self.model))
        context.glUniformMatrix4fv(self.loc_view, 1, context.GL_FALSE, glm.value_ptr(self.view))
        context.glUniformMatrix4fv(self.loc_proj, 1, context.GL_FALSE, glm.value_ptr(self.projection))
        context.glBindVertexArray(b3d.getVao())
        context.glDrawArrays(context.GL_TRIANGLES, 0, 3)
        context.glBindVertexArray(0)

terraImmerse = TerraImmerse()
from math import radians

from terraimmerse.client.blaze3d import*
import terraimmerse.client.blaze3d.world.Chunk as ClientChunk
import terraimmerse.client.blaze3d.Shader as Shader
import glm
import math
import time

from terraimmerse.client.Textures import load_texture
from terraimmerse.world.entity.PlayerEntity import PlayerEntity
from terraimmerse.world.Chunk import Chunk
from terraimmerse.world.WorldGenerator import createWorld
from terraimmerse.client.blaze3d.sky.Sun import*

class TerraImmerse:
    def __init__(self):
        self.chunk=Chunk(0,0)
        createWorld(self.chunk)
        self.running=True
        self.glContext = glContext()
        self.glContext.init_gl()
        self.clientChunk = ClientChunk.ClientChunk(self.chunk)
        self.clientChunk.build()
        pygame.mouse.set_visible(False)
        pygame.event.set_grab(True)
        self.player=PlayerEntity()
        self.clock=pygame.time.Clock()
        self.width, self.height = self.glContext.get_resolution()
        self.shaderCompiler= Shader.ShaderCompiler("assets/shader/vertex.glsl", "assets/shader/fragment.glsl")
        self.sunShaderCompiler=Shader.ShaderCompiler("assets/shader/sun.vert", "assets/shader/sun.frag")
        self.shader=self.shaderCompiler.create_shader_program(self.shaderCompiler.src_vertex, self.shaderCompiler.src_fragment)
        self.sunShader=self.sunShaderCompiler.create_shader_program(self.sunShaderCompiler.src_vertex, self.sunShaderCompiler.src_fragment)
        self.loc_model = glGetUniformLocation(self.shader, "model")
        self.loc_view = glGetUniformLocation(self.shader, "view")
        self.loc_proj = glGetUniformLocation(self.shader, "projection")
        self.sunVBO=SunVBO()
        self.texture = load_texture("assets/textures/atlas.png")
        self.tex_loc = glGetUniformLocation(self.shader, "tex")
        self.model = glm.mat4(1.0)
        self.sunAngle=0.0
        self.view = glm.lookAt(glm.vec3(0, 0, 3),glm.vec3(0, 0, 0),glm.vec3(0, 1, 0))
        self.projection = glm.perspective(glm.radians(70.0),self.width / self.height,0.1,2000.0)
        self.camera_pos = glm.vec3(0, 5, 0)
        self.sensitivity = 0.002
        self.player.setPos(*glm.vec3(0, 5, 0))
        self.speed=0.1
        while self.running:
            for event in pygame.event.get():
                if event.type == pygame.QUIT or (event.type==KEYDOWN and event.key==K_ESCAPE):
                    self.stop()
                if event.type == pygame.KEYDOWN and event.key == pygame.K_r:
                    ClientChunk.rebuild()
                if event.type == pygame.KEYDOWN and event.key == pygame.K_F2:
                    self.takeScreenshot()
            self.drawScene()
    def stop(self):
        self.running=False
    def tickLight(self):
        if self.sunAngle >= 360:
            self.sunAngle=0
        self.sunAngle+=0.1
    def drawScene(self):
        self.tickLight()
        self.checkMovement()
        self.brightness = self.calculateLight(self.sunAngle)
        glClearColor(0.2*self.brightness, 0.4*self.brightness, 0.8*self.brightness, 1.0)
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)
        glDisable(GL_DEPTH_TEST)
        glDepthMask(GL_FALSE)
        self.sunView=glm.mat4(glm.mat3(self.view))
        self.calculateSunPos()
        glEnable(GL_BLEND)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
        self.renderSun(self.projection, self.sunView, self.sunModel)
        glDisable(GL_BLEND)
        glEnable(GL_DEPTH_TEST)
        glDepthMask(GL_TRUE)
        glUseProgram(self.shader)
        glActiveTexture(GL_TEXTURE0)
        glBindTexture(GL_TEXTURE_2D, self.texture)
        glUniform1i(self.tex_loc, 0)
        glUniformMatrix4fv(self.loc_model, 1, GL_FALSE, glm.value_ptr(self.model))
        glUniformMatrix4fv(self.loc_view, 1, GL_FALSE, glm.value_ptr(self.view))
        glUniformMatrix4fv(self.loc_proj, 1, GL_FALSE, glm.value_ptr(self.projection))
        glBindVertexArray(self.clientChunk.getVao())
        glDrawArrays(GL_TRIANGLES, 0, int(len(self.clientChunk.getVertices())/8))
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
        self.direction = glm.vec3(math.cos(self.pitch) * math.sin(self.yaw),math.sin(self.pitch),math.cos(self.pitch) * math.cos(self.yaw))
        self.view = glm.lookAt(self.player.getPos(),self.player.getPos() + self.direction,glm.vec3(0, 1, 0))
    def takeScreenshot(self):
        z = time.localtime()
        filename = f"{z.tm_year}-{z.tm_mon}-{z.tm_mday}_{z.tm_hour}-{z.tm_min}-{z.tm_sec}.png"
        data = glReadPixels(0, 0, self.width, self.height, GL_RGB, GL_UNSIGNED_BYTE)
        image = pygame.image.fromstring(data, (self.width, self.height), "RGB")
        image = pygame.transform.flip(image, False, True)
        os.makedirs("screenshots", exist_ok=True)
        pygame.image.save(image, "screenshots/"+filename+".png")
    def renderSun(self, projection, view, model):
        glUseProgram(self.sunShader)
        glUniformMatrix4fv(glGetUniformLocation(self.sunShader,"projection"),1,GL_FALSE,glm.value_ptr(projection))
        glUniformMatrix4fv(glGetUniformLocation(self.sunShader,"view"),1,GL_FALSE,glm.value_ptr(view))
        glUniformMatrix4fv(glGetUniformLocation(self.sunShader,"model"),1,GL_FALSE,glm.value_ptr(model))
        glUniform3f(glGetUniformLocation(self.sunShader,"sunColor"),1.0,0.9,0.5)
        glUniform1f(glGetUniformLocation(self.sunShader,"brightness"),2.0)
        glActiveTexture(GL_TEXTURE0)
        glBindTexture(GL_TEXTURE_2D, self.sunVBO.getSunTexture())
        glUniform1i(glGetUniformLocation(self.sunShader,"sunTexture"),0)
        glBindVertexArray(self.sunVBO.getSunVAO())
        glDrawArrays(GL_TRIANGLES,0,6)
        glBindVertexArray(0)
    def calculateSunPos(self):
        self.lightAngle = self.sunAngle * (math.pi / 180.0)
        radius = 10
        self.sunModel = glm.mat4(1.0)
        self.sunModel = glm.rotate(self.sunModel,self.lightAngle,glm.vec3(1,0,0))
        self.sunModel = glm.translate(self.sunModel,glm.vec3(0,0,-radius))
    def calculateLight(self, w):
        self.dark = 0.1
        self.bright = 1.0
        if w >= 370:
            t = (w - 370) / (390 - 370)
            return self.dark + t * (self.bright - self.dark)
        if 0 <= w <= 10:
            t = w / 10
            return self.dark + t * (self.bright - self.dark)
        if 10 < w < 150:
            return self.bright
        if 150 <= w <= 170:
            t = (w - 150) / (170 - 150)
            return self.bright - t * (self.bright - self.dark)
        return self.dark
import os
import contextlib
with open(os.devnull, 'w') as f, contextlib.redirect_stdout(f):
    import pygame
    from pygame.locals import *
from OpenGL.GL import *
from OpenGL.GLU import *
from terraimmerse.util import get_resource_path
from screeninfo import get_monitors

class glContext:
    def __init__(self):
        for m in get_monitors():
            self.width = m.width
            self.height = m.height
    def get_resolution(self):
        return self.width, self.height
    def init_gl(self):
        pygame.init()
        pygame.display.gl_set_attribute(pygame.GL_CONTEXT_MAJOR_VERSION, 3)
        pygame.display.gl_set_attribute(pygame.GL_CONTEXT_MINOR_VERSION, 3)
        pygame.display.gl_set_attribute(
            pygame.GL_CONTEXT_PROFILE_MASK,
            pygame.GL_CONTEXT_PROFILE_CORE
        )
        pygame.display.set_mode((self.width, self.height), DOUBLEBUF | OPENGL | FULLSCREEN, vsync=1)
        pygame.display.set_caption("TerraImmerse")
        pygame.display.set_icon(
            pygame.image.load(get_resource_path("assets/icon.png"))
        )
        glEnable(GL_DEPTH_TEST)
        glEnable(GL_CULL_FACE)
        glCullFace(GL_BACK)
        glFrontFace(GL_CCW)
        glViewport(0, 0, self.width, self.height)
        glClearColor(0.2, 0.4, 0.8, 1.0)